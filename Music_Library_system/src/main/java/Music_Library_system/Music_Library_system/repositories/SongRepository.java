package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    /**
     * Check if a song exists by title
     * Implementation of existBy() method requirement
     */
    boolean existsByTitle(String title);
    
    /**
     * Find songs by album
     */
    List<Song> findByAlbumId(Long albumId);
    
    /**
     * Find songs by title containing (case-insensitive)
     */
    List<Song> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find all songs with pagination and sorting
     * This demonstrates Spring Data JPA Pageable and Sort functionality
     * 
     * @param pageable Pagination and sorting information
     * @return Page of songs
     */
    Page<Song> findAll(Pageable pageable);
    
    /**
     * Find songs by genre with pagination
     */
    @Query("SELECT s FROM Song s JOIN s.genres g WHERE g.id = :genreId")
    Page<Song> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);
}
