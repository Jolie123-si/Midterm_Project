package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.Album;
import Music_Library_system.Music_Library_system.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
    
    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }
    
    public List<Album> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistId(artistId);
    }
    
    public boolean existsByTitle(String title) {
        return albumRepository.existsByTitle(title);
    }
    
    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}
