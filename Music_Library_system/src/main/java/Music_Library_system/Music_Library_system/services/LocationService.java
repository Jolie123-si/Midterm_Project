package Music_Library_system.Music_Library_system.services;

import Music_Library_system.Music_Library_system.models.Location;
import Music_Library_system.Music_Library_system.models.ELocationType;
import Music_Library_system.Music_Library_system.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    /**
     * Save a new location
     * IMPLEMENTATION OF SAVING LOCATION (Requirement #2)
     * 
     * How Data is Stored:
     * - Location entity stores: id, code, name, type (enum), parent_id
     * - Hierarchical structure: PROVINCE → DISTRICT → SECTOR → CELL
     * - Self-referencing relationship using parent_id
     * 
     * How Relationships are Handled:
     * - @ManyToOne with parent: One location can have one parent
     * - @OneToMany with children: One location can have many children
     * - @Enumerated for type: Stores enum as STRING in database
     */
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
    }
    
    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }
    
    public boolean existsByName(String name) {
        return locationRepository.existsByName(name);
    }
    
    public List<Location> getLocationsByType(ELocationType type) {
        return locationRepository.findByType(type);
    }
    
    public List<Location> getChildrenByParentId(Long parentId) {
        return locationRepository.findByParentId(parentId);
    }
    
    public List<Location> getLocationsByTypeAndParent(ELocationType type, Long parentId) {
        return locationRepository.findByTypeAndParentId(type, parentId);
    }
    
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
