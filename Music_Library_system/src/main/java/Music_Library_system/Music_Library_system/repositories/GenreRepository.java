package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    /**
     * Check if a genre exists by name
     * Implementation of existBy() method requirement
     */
    boolean existsByName(String name);
    
    /**
     * Find genre by name
     */
    Optional<Genre> findByName(String name);
}
