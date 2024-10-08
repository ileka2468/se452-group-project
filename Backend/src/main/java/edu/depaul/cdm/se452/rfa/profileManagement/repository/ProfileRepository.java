package edu.depaul.cdm.se452.rfa.profileManagement.repository;

import edu.depaul.cdm.se452.rfa.profileManagement.entity.Profile;
import edu.depaul.cdm.se452.rfa.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    public Optional<Profile> findProfileByUser(User user);
    Profile findByUserId(Integer userId);
}
