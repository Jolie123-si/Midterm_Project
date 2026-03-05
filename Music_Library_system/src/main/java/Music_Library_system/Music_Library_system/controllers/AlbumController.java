package Music_Library_system.Music_Library_system.controllers;

import Music_Library_system.Music_Library_system.models.Album;
import Music_Library_system.Music_Library_system.services.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
@CrossOrigin(origins = "*")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        return ResponseEntity.ok(albumService.saveAlbum(album));
    }
    
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-artist/{artistId}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(albumService.getAlbumsByArtist(artistId));
    }
    
    @GetMapping("/exists-by-title/{title}")
    public ResponseEntity<Boolean> existsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(albumService.existsByTitle(title));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }
}
