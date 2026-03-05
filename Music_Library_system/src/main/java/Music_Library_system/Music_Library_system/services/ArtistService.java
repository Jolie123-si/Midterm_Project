package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.Artist;
import Music_Library_system.Music_Library_system.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }
    
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
    
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }
    
    public boolean existsByName(String name) {
        return artistRepository.existsByName(name);
    }
    
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }
}
