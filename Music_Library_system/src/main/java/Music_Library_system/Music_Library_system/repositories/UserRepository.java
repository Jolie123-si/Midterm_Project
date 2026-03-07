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
     * REQUIREMENT #8: Retrieve all users from a given PROVINCE using province code OR province name
     * 
     * Query Logic:
     * 1. Joins User with Location (user's direct location - could be any level)
     * 2. Recursively finds the province by traversing parent hierarchy
     * 3. Matches province by code OR name
     * 4. Returns all users whose location belongs to that province
     * 
     * @param code Province code (e.g., "KIG" for Kigali)
     * @param name Province name (e.g., "Kigali")
     * @return List of users from the specified province
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.location l " +
           "WHERE l.code = :code OR l.name = :name " +
           "OR l.parent.code = :code OR l.parent.name = :name " +
           "OR l.parent.parent.code = :code OR l.parent.parent.name = :name " +
           "OR l.parent.parent.parent.code = :code OR l.parent.parent.parent.name = :name")
    List<User> findByProvinceCodeOrProvinceName(@Param("code") String code, @Param("name") String name);
    
    /**
     * Alternative: Find users by province code only
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.location l " +
           "WHERE l.code = :code OR l.parent.code = :code " +
           "OR l.parent.parent.code = :code OR l.parent.parent.parent.code = :code")
    List<User> findByProvinceCode(@Param("code") String code);
    
    /**
     * Alternative: Find users by province name only
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.location l " +
           "WHERE l.name = :name OR l.parent.name = :name " +
           "OR l.parent.parent.name = :name OR l.parent.parent.parent.name = :name")
    List<User> findByProvinceName(@Param("name") String name);
    
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
