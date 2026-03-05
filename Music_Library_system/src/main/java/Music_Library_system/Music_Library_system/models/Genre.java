package Music_Library_system.Music_Library_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Genre Entity - Represents a music genre
 * Relationship: Many-to-Many with Song (Many Songs can have Many Genres)
 */
@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50, unique = true)
    private String name;
    
    @Column(length = 200)
    private String description;
    
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Song> songs;
}
