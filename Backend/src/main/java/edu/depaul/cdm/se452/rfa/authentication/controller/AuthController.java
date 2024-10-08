package edu.depaul.cdm.se452.rfa.authentication.controller;

import edu.depaul.cdm.se452.rfa.authentication.entity.Role;
import edu.depaul.cdm.se452.rfa.authentication.entity.User;
import edu.depaul.cdm.se452.rfa.authentication.entity.UserRole;
import edu.depaul.cdm.se452.rfa.authentication.entity.UserRoleId;
import edu.depaul.cdm.se452.rfa.authentication.payload.AuthDataResponse;
import edu.depaul.cdm.se452.rfa.authentication.service.AuthResponse;
import edu.depaul.cdm.se452.rfa.authentication.payload.LoginRequest;
import edu.depaul.cdm.se452.rfa.authentication.payload.RegisterRequest;
import edu.depaul.cdm.se452.rfa.authentication.payload.TokenType;
import edu.depaul.cdm.se452.rfa.authentication.repository.RoleRepository;
import edu.depaul.cdm.se452.rfa.authentication.repository.UserRepository;
import edu.depaul.cdm.se452.rfa.authentication.repository.UserRoleRepository;
import edu.depaul.cdm.se452.rfa.authentication.security.JwtTokenProvider;
import edu.depaul.cdm.se452.rfa.authentication.service.CustomUserDetailsService;
import edu.depaul.cdm.se452.rfa.authentication.util.UserPrincipal;
import edu.depaul.cdm.se452.rfa.invalidatedtokens.service.InvalidateTokenService;
import edu.depaul.cdm.se452.rfa.profileManagement.entity.Profile;
import edu.depaul.cdm.se452.rfa.profileManagement.service.InvalidCharacteristicException;
import edu.depaul.cdm.se452.rfa.profileManagement.service.ProfileService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    InvalidateTokenService invalidateTokenService;

    @Autowired
    ProfileService profileService;

    @Value("${app.refreshtokenExpirationInMs}")
    private int refreshtokenExpirationInMs;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEnabled(true);

        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByUsername(user.getUsername());
        if (savedUser.isPresent()) {
            user = savedUser.get();
        } else {
            throw new IllegalStateException("User was not saved successfully.");
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setRoleName("ROLE_USER");
            roleRepository.save(userRole);
            userRole = roleRepository.findByRoleName("ROLE_USER");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to assign role to user.");
            }
        }

        UserRole userRoleLink = new UserRole();
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(user.getId());
        userRoleId.setRoleId(userRole.getId());
        userRoleLink.setId(userRoleId);
        userRoleLink.setUser(user);
        userRoleLink.setRole(userRole);
        userRoleRepository.save(userRoleLink);

        try {
            Profile profile = profileService.createProfile(user);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            System.out.println(userDetails.getAuthorities());

            String accessToken = tokenProvider.generateToken(userDetails.getUsername(), (List<? extends GrantedAuthority>) userDetails.getAuthorities(), TokenType.JWT);
            String refreshToken = tokenProvider.generateToken(userDetails.getUsername(), (List<? extends GrantedAuthority>) userDetails.getAuthorities(), TokenType.REFRESH_TOKEN);

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/api/auth/");
            refreshTokenCookie.setMaxAge(refreshtokenExpirationInMs / 1000);

            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new AuthDataResponse(user.getUsername(), user.getFirstName(), user.getLastName(), profile.getPfpImage()));
        } catch (InvalidCharacteristicException e) {
            log.error("Profile creation failed for user {}: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )

            );

            AuthResponse authResponse = tokenProvider.getTokens(authentication);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getAccessToken());
            response.addCookie(authResponse.getRefreshCookie());
            User user = customUserDetailsService.getUserByUsername(loginRequest.getUsername());
            Profile profile = profileService.loadProfileByUsername(loginRequest.getUsername());
            return ResponseEntity.ok(new AuthDataResponse(user.getUsername(), user.getFirstName(), user.getLastName(), profile.getPfpImage()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletResponse response, HttpServletRequest request) {
        if (request.getCookies() == null || request.getCookies().length == 0) {
            return ResponseEntity.badRequest().body("Refresh token is empty");
        }

        String refreshToken = tokenProvider.getRefreshTokenFromCookies(request);

        if (refreshToken != null) {
            boolean isValid = tokenProvider.validateToken(refreshToken);
            if (isValid) {
                try {
                    String usernameFromJWT = tokenProvider.getUsernameFromJWT(refreshToken);
                    UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(usernameFromJWT);
                    String newAccessToken = tokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities(), TokenType.JWT);
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
                    return ResponseEntity.ok().build();
                } catch (AuthenticationException e) {
                    log.error("e: ", e);
                }
            } else {
                log.warn("Invalid refresh token: {}", refreshToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String refreshToken = tokenProvider.getRefreshTokenFromCookies(request);
        String accessToken = tokenProvider.getJwtFromRequest(request);

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Access token not provided.");
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token not provided.");
        }

        boolean accessTokenValid = tokenProvider.validateToken(accessToken);
        boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

        if (!accessTokenValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
        }
        if (!refreshTokenValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        boolean tokensDeactivated = invalidateTokenService.invalidateTokens(accessToken, refreshToken);
        if (tokensDeactivated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAuthedUserData(HttpServletRequest request) {
        String accessToken = tokenProvider.getJwtFromRequest(request);
        String username = tokenProvider.getUsernameFromJWT(accessToken);
        User user = customUserDetailsService.getUserByUsername(username);
        Profile profile = profileService.loadProfileByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to retrieve user.");
        }

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to retrieve profile.");
        }
        return ResponseEntity.ok(new AuthDataResponse(user.getUsername(), user.getFirstName(), user.getLastName(), profile.getPfpImage()));
    }
}
