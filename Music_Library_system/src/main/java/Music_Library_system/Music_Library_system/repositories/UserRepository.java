package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Check if a user exists by email
     * Implementation of existBy() method requirement
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if a user exists by username
     * Implementation of existBy() method requirement
     */
    boolean existsByUsername(String username);
    
    /**
     * Find all users from a given location using location code OR location name
     * This fulfills requirement #8: Retrieve all users from a given location
     * 
     * @param code Location code
     * @param name Location name
     * @return List of users matching the location criteria
     */
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.code = :code OR l.name = :name")
    List<User> findByLocationCodeOrLocationName(@Param("code") String code, @Param("name") String name);
    
    /**
     * Find all users from a location with pagination
     */
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.code = :code")
    Page<User> findByLocationCode(@Param("code") String code, Pageable pageable);
    
    /**
     * Find user by email with case-insensitive search
     */
    Optional<User> findByEmailIgnoreCase(String email);
}
