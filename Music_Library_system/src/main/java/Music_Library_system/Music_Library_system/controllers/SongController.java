package Music_Library_system.Music_Library_system.controllers;

import Music_Library_system.Music_Library_system.models.Song;
import Music_Library_system.Music_Library_system.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*")
public class SongController {
    
    @Autowired
    private SongService songService;
    
    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        return ResponseEntity.ok(songService.saveSong(song));
    }
    
    /**
     * Get all songs with pagination and sorting
     * URL Example: /api/songs/paginated?page=0&size=5&sortBy=title&ascending=true
     */
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllSongsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {
        
        Page<Song> songPage = songService.getAllSongs(page, size, sortBy, ascending);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", songPage.getContent());
        response.put("totalPages", songPage.getTotalPages());
        response.put("totalElements", songPage.getTotalElements());
        response.put("numberOfElements", songPage.getNumberOfElements());
        response.put("first", songPage.isFirst());
        response.put("last", songPage.isLast());
        response.put("empty", songPage.isEmpty());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongsUnpaginated());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return songService.getSongById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-album/{albumId}")
    public ResponseEntity<List<Song>> getSongsByAlbum(@PathVariable Long albumId) {
        return ResponseEntity.ok(songService.getSongsByAlbum(albumId));
    }
    
    @GetMapping("/exists-by-title/{title}")
    public ResponseEntity<Boolean> existsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(songService.existsByTitle(title));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
