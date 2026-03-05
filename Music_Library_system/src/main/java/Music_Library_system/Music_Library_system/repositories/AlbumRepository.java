package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    
    /**
     * Check if an album exists by title
     * Implementation of existBy() method requirement
     */
    boolean existsByTitle(String title);
    
    /**
     * Find albums by artist
     */
    List<Album> findByArtistId(Long artistId);
    
    /**
     * Find albums by release year
     */
    List<Album> findByReleaseYear(Integer releaseYear);
}
