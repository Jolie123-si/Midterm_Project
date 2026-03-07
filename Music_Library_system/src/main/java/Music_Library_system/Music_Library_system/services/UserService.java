package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.User;
import Music_Library_system.Music_Library_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Save a new user
     * @param user User object to save
     * @return Saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Get all users with pagination and sorting
     * 
     * IMPLEMENTATION OF SORTING AND PAGINATION (Requirement #3)
     * 
     * How Sorting Works:
     * - Spring Data JPA provides Sort class for defining sort order
     * - Sort.by("fieldName") sorts by field in ascending order
     * - Sort.by(Sort.Direction.DESC, "fieldName") sorts in descending order
     * - Multiple sorts can be chained: Sort.by("field1").and(Sort.by("field2"))
     * 
     * How Pagination Works:
     * - PageRequest creates a Pageable object with page number and size
     * - Page 0 is the first page, page 1 is the second, etc.
     * - Page size determines how many records per page
     * - Returns a Page object containing content and metadata
     * 
     * Performance Benefits:
     * 1. Reduces memory usage by loading only required records
     * 2. Improves query response time for large datasets
     * 3. Prevents database overload with LIMIT/OFFSET queries
     * 4. Better user experience with progressive data loading
     * 
     * @param page Page number (0-based)
     * @param size Number of records per page
     * @param sortBy Field to sort by
     * @param ascending Sort direction
     * @return Page of users
     */
    public Page<User> getAllUsers(int page, int size, String sortBy, boolean ascending) {
        // Create Sort object based on direction
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        // Create Pageable with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Execute paginated query
        return userRepository.findAll(pageable);
    }
    
    /**
     * REQUIREMENT #8: Get all users from a given PROVINCE using province code OR province name
     * 
     * Query Logic Explanation:
     * 1. Users are linked to locations at any level (Province, District, Sector, Cell)
     * 2. To find users by province, we traverse the location hierarchy upward
     * 3. The query checks if the user's location OR any of its parents match the province
     * 4. Uses OR condition to match either code OR name
     * 
     * Example:
     * - User in "Kimisagara" (Sector) → Parent: Nyarugenge (District) → Parent: Kigali (Province)
     * - Query finds this user when searching for province "Kigali" or code "KIG"
     * 
     * @param provinceCode Province code (e.g., "KIG")
     * @param provinceName Province name (e.g., "Kigali")
     * @return List of all users from that province
     */
    public List<User> getUsersByProvinceCodeOrName(String provinceCode, String provinceName) {
        return userRepository.findByProvinceCodeOrProvinceName(provinceCode, provinceName);
    }
    
    /**
     * Get users by province code only
     */
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findByProvinceCode(provinceCode);
    }
    
    /**
     * Get users by province name only
     */
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findByProvinceName(provinceName);
    }
    
    /**
     * Get users by location with pagination and sorting
     */
    public Page<User> getUsersByLocation(String locationCode, int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findByLocationCode(locationCode, pageable);
    }
    
    /**
     * Check if user exists by email
     * Implementation of existBy() functionality
     * @param email User email
     * @return true if exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Check if user exists by username
     * Implementation of existBy() functionality
     * @param username Username
     * @return true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Get user by ID
     * @param id User ID
     * @return Optional containing user if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Delete user by ID
     * @param id User ID
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
