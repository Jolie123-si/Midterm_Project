package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.Genre;
import Music_Library_system.Music_Library_system.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    
    @Autowired
    private GenreRepository genreRepository;
    
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }
    
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
    
    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }
    
    public boolean existsByName(String name) {
        return genreRepository.existsByName(name);
    }
    
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
