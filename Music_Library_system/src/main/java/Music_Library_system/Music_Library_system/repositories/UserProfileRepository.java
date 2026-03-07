package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    /**
     * Find user profile by user ID
     */
    Optional<UserProfile> findByUserId(Long userId);
    
    /**
     * Check if user profile exists for a user
     */
    boolean existsByUserId(Long userId);
}
