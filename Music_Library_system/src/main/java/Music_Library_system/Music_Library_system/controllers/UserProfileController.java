package Music_Library_system.Music_Library_system.controllers;

import Music_Library_system.Music_Library_system.models.UserProfile;
import Music_Library_system.Music_Library_system.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserProfile Controller
 * Demonstrates One-to-One relationship between User and UserProfile
 */
@RestController
@RequestMapping("/api/user-profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        return ResponseEntity.ok(userProfileService.saveUserProfile(userProfile));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfile> getUserProfileByUserId(@PathVariable Long userId) {
        return userProfileService.getUserProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.existsByUserId(userId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.noContent().build();
    }
}
