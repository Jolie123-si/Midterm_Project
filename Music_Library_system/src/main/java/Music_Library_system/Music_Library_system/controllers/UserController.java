package Music_Library_system.Music_Library_system.controllers;

import Music_Library_system.Music_Library_system.models.User;
import Music_Library_system.Music_Library_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
    
    /**
     * Get all users with pagination and sorting
     * 
     * URL Example: /api/users/paginated?page=0&size=10&sortBy=username&ascending=true
     * 
     * Query Parameters:
     * - page: Page number (0-based, default 0)
     * - size: Number of items per page (default 10)
     * - sortBy: Field to sort by (default: id)
     * - ascending: Sort direction (default: true)
     * 
     * Response includes:
     * - content: List of users on current page
     * - totalPages: Total number of pages
     * - totalElements: Total number of elements
     * - numberOfElements: Number of elements on current page
     * - first: Whether this is the first page
     * - last: Whether this is the last page
     * - empty: Whether the page is empty
     */
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        
        Page<User> userPage = userService.getAllUsers(page, size, sortBy, ascending);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", userPage.getContent());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalElements", userPage.getTotalElements());
        response.put("numberOfElements", userPage.getNumberOfElements());
        response.put("first", userPage.isFirst());
        response.put("last", userPage.isLast());
        response.put("empty", userPage.isEmpty());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * REQUIREMENT #8: Get all users from a given PROVINCE using province code OR province name
     * 
     * URL Examples:
     * - /api/users/by-province?code=KIG
     * - /api/users/by-province?name=Kigali
     * - /api/users/by-province?code=KIG&name=Kigali
     * 
     * Query Logic:
     * - Searches through location hierarchy to find users in specified province
     * - Accepts either province code OR province name OR both
     * - Returns all users whose location (at any level) belongs to that province
     * 
     * @param code Province code (e.g., "KIG" for Kigali)
     * @param name Province name (e.g., "Kigali")
     * @return List of users from the specified province
     */
    @GetMapping("/by-province")
    public ResponseEntity<List<User>> getUsersByProvince(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name) {
        
        if ((code == null || code.isEmpty()) && (name == null || name.isEmpty())) {
            return ResponseEntity.badRequest().build();
        }
        
        List<User> users;
        if (code != null && !code.isEmpty() && name != null && !name.isEmpty()) {
            users = userService.getUsersByProvinceCodeOrName(code, name);
        } else if (code != null && !code.isEmpty()) {
            users = userService.getUsersByProvinceCode(code);
        } else {
            users = userService.getUsersByProvinceName(name);
        }
        
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by location with pagination
     */
    @GetMapping("/by-location-paginated")
    public ResponseEntity<Map<String, Object>> getUsersByLocationPaginated(
            @RequestParam String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        
        Page<User> userPage = userService.getUsersByLocation(code, page, size, sortBy, ascending);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", userPage.getContent());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalElements", userPage.getTotalElements());
        response.put("numberOfElements", userPage.getNumberOfElements());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/exists-by-email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
    
    @GetMapping("/exists-by-username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
