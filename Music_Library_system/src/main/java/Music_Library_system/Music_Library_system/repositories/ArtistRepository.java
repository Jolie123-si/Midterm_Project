package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
    /**
     * Check if an artist exists by name
     * Implementation of existBy() method requirement
     */
    boolean existsByName(String name);
    
    /**
     * Find artists by name containing (case-insensitive)
     */
    List<Artist> findByNameContainingIgnoreCase(String name);
}
