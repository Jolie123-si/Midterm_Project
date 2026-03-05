package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.Song;
import Music_Library_system.Music_Library_system.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;
    
    public Song saveSong(Song song) {
        return songRepository.save(song);
    }
    
    /**
     * Get all songs with pagination and sorting
     * Demonstrates Spring Data JPA Pageable and Sort functionality
     */
    public Page<Song> getAllSongs(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return songRepository.findAll(PageRequest.of(page, size, sort));
    }
    
    public List<Song> getAllSongsUnpaginated() {
        return songRepository.findAll();
    }
    
    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }
    
    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }
    
    public boolean existsByTitle(String title) {
        return songRepository.existsByTitle(title);
    }
    
    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }
}
