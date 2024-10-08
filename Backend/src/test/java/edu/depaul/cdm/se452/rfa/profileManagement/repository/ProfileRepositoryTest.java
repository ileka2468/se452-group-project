package edu.depaul.cdm.se452.rfa.profileManagement.repository;

import edu.depaul.cdm.se452.rfa.authentication.entity.User;
import edu.depaul.cdm.se452.rfa.authentication.repository.UserRepository;
import edu.depaul.cdm.se452.rfa.profileManagement.entity.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileRepositoryTest{
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndFindProfile() {
        // Step 1: Create and save a User entity (required for Profile)
        User user = new User();
        user.setUsername("jane_doe");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setEnabled(true);
        user = userRepository.save(user); // Save user to the database
        assertNotNull(user.getId()); // Verify that the user has an ID after saving

        // Step 2: Create a new Profile for the User
        Profile profile = new Profile();
        profile.setUser(user);  // Link the profile to the user
        profile.setIsActivelyLooking(true);
        profile.setBio("Looking for a friendly roommate.");
        profile.setPfpImage("https://ui-avatars.com/api/?background=0D8ABC&color=fff");

        // Set characteristics as a JSON-like Map
        Map<String, Object> characteristics = new HashMap<>();
        characteristics.put("cleanliness", "high");
        characteristics.put("smoker", false);
        profile.setCharacteristics(characteristics);

        // Save the profile
        Profile savedProfile = profileRepository.save(profile);
        assertNotNull(savedProfile.getId()); // Ensure profile ID is generated

        // Step 3: Retrieve the profile by ID
        Optional<Profile> fetchedProfile = profileRepository.findById(savedProfile.getId());
        assertTrue(fetchedProfile.isPresent());  // Check that the profile exists
        assertEquals("Looking for a friendly roommate.", fetchedProfile.get().getBio());

        // Step 4: Check if the characteristics are correctly saved and retrieved
//        Map<String, Object> retrievedCharacteristics = fetchedProfile.get().getCharacteristics();
//        assertNotNull(retrievedCharacteristics);
//        assertEquals("high", retrievedCharacteristics.get("cleanliness"));
//        assertEquals(false, retrievedCharacteristics.get("smoker"));
    }
}