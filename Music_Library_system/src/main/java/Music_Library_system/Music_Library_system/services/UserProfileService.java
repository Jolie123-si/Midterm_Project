package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.UserProfile;
import Music_Library_system.Music_Library_system.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserProfileService {
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
    
    public Optional<UserProfile> getUserProfileById(Long id) {
        return userProfileRepository.findById(id);
    }
    
    public Optional<UserProfile> getUserProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }
    
    public boolean existsByUserId(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }
    
    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}
