package Music_Library_system.Music_Library_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Duration;
import java.util.List;

/**
 * Song Entity - Represents a song/track in the music library
 * Relationship: Many-to-One with Album (Many Songs belong to one Album)
 * Relationship: Many-to-Many with Genre (Many Songs can have Many Genres)
 */
@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false)
    private Duration duration;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "song_genres",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
