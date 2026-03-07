package Music_Library_system.Music_Library_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * UserProfile Entity - Represents detailed profile information for a user
 * Relationship: One-to-One with User (One User has One UserProfile)
 * 
 * REQUIREMENT 6: One-to-One Relationship Implementation
 */
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
