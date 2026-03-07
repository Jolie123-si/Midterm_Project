# One-to-One Relationship Implementation Documentation
## REQUIREMENT 6: One-to-One Relationship ✅

### **Implementation Overview:**
Your project now implements a **One-to-One relationship** between `User` and `UserProfile` entities.

---

## **1. ONE-TO-ONE RELATIONSHIP MAPPING**

### **Business Logic:**
- **One User** has **One UserProfile** (detailed profile information)
- **One UserProfile** belongs to **One User** only
- This creates a **One-to-One** relationship with a unique foreign key

### **Entity Mapping:**

#### **A. UserProfile Entity (Owning Side):**
```java
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 15)
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(length = 500)
    private String bio;
    
    @Column(length = 200)
    private String favoriteGenre;
    
    /**
     * One-to-One relationship with User
     * This is the owning side (contains the foreign key)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
}
```

#### **B. User Entity (Inverse Side):**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    /**
     * One-to-One relationship with UserProfile
     * This is the inverse side (does not contain the foreign key)
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserProfile userProfile;
}
```

---

## **2. HOW ENTITIES ARE CONNECTED**

### **Database Schema:**
```sql
-- Users Table (Inverse Side)
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    location_id BIGINT NOT NULL
);

-- User Profiles Table (Owning Side)
CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone_number VARCHAR(15),
    date_of_birth DATE,
    bio VARCHAR(500),
    favorite_genre VARCHAR(200),
    user_id BIGINT NOT NULL UNIQUE,  -- Foreign Key with UNIQUE constraint
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### **Key Connection Points:**

#### **1. Foreign Key (user_id):**
- Located in `user_profiles` table (owning side)
- References `users.id` (primary key)
- **UNIQUE constraint**: Ensures one profile per user
- **NOT NULL**: Every profile must belong to a user

#### **2. Owning Side (@OneToOne with @JoinColumn):**
```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", unique = true, nullable = false)
private User user;
```
- **`@JoinColumn(name = "user_id")`**: Creates foreign key column
- **`unique = true`**: Enforces One-to-One constraint at database level
- **`nullable = false`**: Profile must have a user

#### **3. Inverse Side (@OneToOne with mappedBy):**
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
private UserProfile userProfile;
```
- **`mappedBy = "user"`**: References field name in owning side
- **`cascade = CascadeType.ALL`**: Operations cascade to profile
- **`orphanRemoval = true`**: Deletes profile when removed from user

---

## **3. RELATIONSHIP MAPPING DETAILS**

### **Owning vs Inverse Side:**

#### **Owning Side (UserProfile):**
- **Contains the foreign key**: `user_id` column
- **Controls the relationship**: Changes here update the database
- **Uses @JoinColumn**: Defines foreign key configuration
- **Responsible for persistence**: Saves/updates the relationship

#### **Inverse Side (User):**
- **No foreign key**: Relies on owning side
- **Uses mappedBy**: References owning side field name
- **Read-only by default**: Changes don't directly update FK
- **Cascade operations**: Can propagate operations to owning side

### **Unique Constraint:**
```java
@JoinColumn(name = "user_id", unique = true)
```
- **Enforces One-to-One**: Prevents multiple profiles for same user
- **Database-level constraint**: Cannot be violated
- **Throws exception**: If duplicate user_id attempted

---

## **4. BIDIRECTIONAL NAVIGATION**

### **From User to UserProfile:**
```java
User user = userRepository.findById(1L).orElseThrow();
UserProfile profile = user.getUserProfile();  // Lazy loading triggers here
System.out.println(profile.getPhoneNumber());
```

### **From UserProfile to User:**
```java
UserProfile profile = userProfileRepository.findById(1L).orElseThrow();
User user = profile.getUser();  // Lazy loading triggers here
System.out.println(user.getUsername());
```

---

## **5. CASCADE OPERATIONS**

### **Cascade Types:**
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
```

#### **CascadeType.ALL:**
- **PERSIST**: Save user → profile also saved
- **MERGE**: Update user → profile also updated
- **REMOVE**: Delete user → profile also deleted
- **REFRESH**: Refresh user → profile also refreshed
- **DETACH**: Detach user → profile also detached

#### **orphanRemoval = true:**
- If profile removed from user, profile is deleted
- Ensures no orphaned profiles in database

### **Example:**
```java
// Create user with profile
User user = new User();
user.setUsername("john_doe");
user.setEmail("john@example.com");

UserProfile profile = new UserProfile();
profile.setPhoneNumber("+250788123456");
profile.setBio("Music enthusiast");
profile.setUser(user);

user.setUserProfile(profile);

// Save user - profile automatically saved due to cascade
userRepository.save(user);

// Delete user - profile automatically deleted due to cascade
userRepository.deleteById(user.getId());
```

---

## **6. CRUD OPERATIONS**

### **Creating Relationship:**
```java
// Method 1: Create from owning side
User user = userRepository.findById(1L).orElseThrow();

UserProfile profile = new UserProfile();
profile.setPhoneNumber("+250788123456");
profile.setDateOfBirth(LocalDate.of(1990, 5, 15));
profile.setBio("Love rock and jazz music");
profile.setFavoriteGenre("Rock");
profile.setUser(user);

userProfileRepository.save(profile);

// Method 2: Create from inverse side with cascade
User user = new User();
user.setUsername("jane_doe");
user.setEmail("jane@example.com");

UserProfile profile = new UserProfile();
profile.setPhoneNumber("+250788654321");
profile.setUser(user);

user.setUserProfile(profile);
userRepository.save(user);  // Profile saved automatically
```

### **Querying Relationship:**
```java
// Get profile by user ID
Optional<UserProfile> profile = userProfileRepository.findByUserId(1L);

// Check if user has profile
boolean hasProfile = userProfileRepository.existsByUserId(1L);

// Get user with profile (eager loading)
@Query("SELECT u FROM User u LEFT JOIN FETCH u.userProfile WHERE u.id = :userId")
Optional<User> findByIdWithProfile(@Param("userId") Long userId);
```

### **Updating Relationship:**
```java
UserProfile profile = userProfileRepository.findByUserId(1L).orElseThrow();
profile.setPhoneNumber("+250788999888");
profile.setBio("Updated bio");
userProfileRepository.save(profile);
```

### **Deleting Relationship:**
```java
// Delete profile only
userProfileRepository.deleteById(profileId);

// Delete user (profile deleted automatically due to cascade)
userRepository.deleteById(userId);
```

---

## **7. API ENDPOINTS**

### **Create User Profile:**
```
POST /api/user-profiles
Content-Type: application/json

{
    "phoneNumber": "+250788123456",
    "dateOfBirth": "1990-05-15",
    "bio": "Music lover and enthusiast",
    "favoriteGenre": "Rock",
    "user": {
        "id": 1
    }
}
```

### **Get Profile by User ID:**
```
GET /api/user-profiles/user/1
```

### **Check if User has Profile:**
```
GET /api/user-profiles/exists/user/1
```

### **Delete Profile:**
```
DELETE /api/user-profiles/1
```

---

## **8. PERFORMANCE CONSIDERATIONS**

### **Lazy Loading:**
```java
@OneToOne(fetch = FetchType.LAZY)
private User user;
```
- **Default behavior**: Related entity loaded only when accessed
- **Benefit**: Reduces initial query time
- **Use case**: When profile not always needed with user

### **Eager Loading (when needed):**
```java
@OneToOne(fetch = FetchType.EAGER)
private User user;
```
- **Immediate loading**: Related entity loaded with parent
- **Use case**: When profile always needed with user

### **JOIN FETCH Query:**
```java
@Query("SELECT u FROM User u LEFT JOIN FETCH u.userProfile WHERE u.id = :userId")
Optional<User> findByIdWithProfile(@Param("userId") Long userId);
```
- **Single query**: Loads both entities in one query
- **Avoids N+1**: No additional query for profile
- **Best practice**: Use when you know you need both entities

---

## **9. ADVANTAGES OF ONE-TO-ONE**

### **Data Organization:**
- **Separation of Concerns**: Core user data vs extended profile data
- **Optional Information**: Profile can be created later
- **Performance**: Can load user without profile details

### **Database Design:**
- **Normalization**: Separates frequently accessed from rarely accessed data
- **Flexibility**: Easy to add more profile fields without affecting users table
- **Scalability**: Can optimize queries based on data access patterns

### **Business Logic:**
- **Privacy**: Profile information can have different access controls
- **Modularity**: Profile can be managed independently
- **Extensibility**: Easy to add more One-to-One relationships

---

## **10. COMPARISON WITH OTHER RELATIONSHIPS**

### **One-to-One vs One-to-Many:**
- **One-to-One**: User has ONE profile (unique constraint)
- **One-to-Many**: Artist has MANY albums (no unique constraint)

### **One-to-One vs Many-to-Many:**
- **One-to-One**: Direct foreign key with unique constraint
- **Many-to-Many**: Junction table with composite key

### **When to Use One-to-One:**
- Optional extended information (user profile, settings)
- Large text fields that aren't always needed (bio, description)
- Sensitive information requiring separate access control
- Performance optimization (split frequently/rarely accessed data)

---

## **CONCLUSION:**
Your project now successfully implements a **One-to-One relationship** with:
- ✅ **Proper Mapping**: @OneToOne on both sides
- ✅ **Unique Foreign Key**: Enforces One-to-One constraint
- ✅ **Bidirectional Navigation**: Can traverse both directions
- ✅ **Cascade Operations**: Automatic persistence and deletion
- ✅ **Orphan Removal**: Automatic cleanup of orphaned profiles
- ✅ **Performance Optimization**: Lazy loading support

This implementation follows JPA best practices and demonstrates how two entities can be connected in a One-to-One relationship.