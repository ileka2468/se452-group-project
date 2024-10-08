package edu.depaul.cdm.se452.rfa.profileManagement.controller;
import edu.depaul.cdm.se452.rfa.profileManagement.entity.Profile;
import edu.depaul.cdm.se452.rfa.profileManagement.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Create or update a profile
    @PostMapping("/create")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        Profile savedProfile = profileService.saveProfile(profile);
        return ResponseEntity.ok(savedProfile);
    }

    // Get a profile by profile ID
    @GetMapping("/{profileId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Integer profileId) {
        Optional<Profile> profile = profileService.getProfileById(profileId);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get a profile by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(@PathVariable Integer userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Integer profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

}
