package Music_Library_system.Music_Library_system.controllers;

import Music_Library_system.Music_Library_system.models.Location;
import Music_Library_system.Music_Library_system.models.ELocationType;
import Music_Library_system.Music_Library_system.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    /**
     * Create a new location
     * IMPLEMENTATION OF SAVING LOCATION (Requirement #2)
     */
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.saveLocation(location));
    }
    
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Location> getLocationByCode(@PathVariable String code) {
        return locationService.getLocationByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> getLocationsByType(@PathVariable ELocationType type) {
        return ResponseEntity.ok(locationService.getLocationsByType(type));
    }
    
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Location>> getChildrenByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(locationService.getChildrenByParentId(parentId));
    }
    
    @GetMapping("/exists-by-code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(locationService.existsByCode(code));
    }
    
    @GetMapping("/exists-by-name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(locationService.existsByName(name));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
