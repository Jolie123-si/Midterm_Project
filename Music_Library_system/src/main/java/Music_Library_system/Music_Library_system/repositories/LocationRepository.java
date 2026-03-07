package Music_Library_system.Music_Library_system.repositories;

import Music_Library_system.Music_Library_system.models.Location;
import Music_Library_system.Music_Library_system.models.ELocationType;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * Find location by code
     */
    Optional<Location> findByCode(String code);
    
    /**
     * Check if location exists by code
     */
    boolean existsByCode(String code);
    
    /**
     * Check if location exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Find all locations by type (PROVINCE, DISTRICT, SECTOR, CELL)
     */
    List<Location> findByType(ELocationType type);
    
    /**
     * Find all children of a parent location
     */
    List<Location> findByParentId(Long parentId);
    
    /**
     * Find all locations by type and parent
     */
    List<Location> findByTypeAndParentId(ELocationType type, Long parentId);
}
