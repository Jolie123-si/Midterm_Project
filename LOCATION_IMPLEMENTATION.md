# Location Implementation Documentation
## REQUIREMENT 2: Implementation of Saving Location ✅

### **How Location Data is Stored:**

#### **1. Database Structure:**
```sql
CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,        -- Unique identifier (e.g., "KIG", "NYAR")
    name VARCHAR(100) NOT NULL,              -- Human-readable name
    type VARCHAR(20) NOT NULL,               -- PROVINCE, DISTRICT, SECTOR, CELL
    parent_id BIGINT,                        -- Self-referencing foreign key
    FOREIGN KEY (parent_id) REFERENCES locations(id)
);
```

#### **2. Entity Mapping:**
- **@Entity** annotation maps to `locations` table
- **@Id @GeneratedValue** for auto-increment primary key
- **@Column** annotations define constraints (nullable, unique, length)
- **@Enumerated(EnumType.STRING)** stores enum as string in database

---

### **How Relationships are Handled:**

#### **1. Self-Referencing Relationship:**
```java
// Parent relationship (Many-to-One)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_id")
private Location parent;

// Children relationship (One-to-Many)
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Location> children;
```

#### **2. Hierarchical Structure Logic:**
- **PROVINCE** → `parent = null` (root level)
- **DISTRICT** → `parent = Province`
- **SECTOR** → `parent = District`  
- **CELL** → `parent = Sector`

#### **3. Cascade Operations:**
- **CascadeType.ALL**: When parent is deleted, all children are deleted
- **FetchType.LAZY**: Children loaded only when accessed (performance optimization)

---

### **Saving Location Implementation:**

#### **1. API Endpoint:**
```java
@PostMapping("/api/locations")
public ResponseEntity<Location> createLocation(@RequestBody Location location) {
    return ResponseEntity.ok(locationService.saveLocation(location));
}
```

#### **2. Service Layer:**
```java
public Location saveLocation(Location location) {
    return locationRepository.save(location);
}
```

#### **3. Repository Layer:**
```java
public interface LocationRepository extends JpaRepository<Location, Long> {
    // JpaRepository provides save() method automatically
}
```

---

### **Example Usage:**

#### **1. Creating a Province:**
```json
POST /api/locations
{
    "code": "KIG",
    "name": "Kigali",
    "type": "PROVINCE",
    "parent": null
}
```

#### **2. Creating a District:**
```json
POST /api/locations
{
    "code": "NYAR",
    "name": "Nyarugenge",
    "type": "DISTRICT",
    "parent": {
        "id": 1  // Kigali Province ID
    }
}
```

#### **3. Creating a Sector:**
```json
POST /api/locations
{
    "code": "NYAR_KIMIS",
    "name": "Kimisagara",
    "type": "SECTOR",
    "parent": {
        "id": 2  // Nyarugenge District ID
    }
}
```

---

### **Key Features:**

#### **1. Data Validation:**
- **Unique codes**: Prevents duplicate location codes
- **Required fields**: Name and type cannot be null
- **Enum validation**: Type must be valid ELocationType

#### **2. Relationship Management:**
- **Automatic foreign key handling**: JPA manages parent_id relationships
- **Bidirectional mapping**: Can navigate from parent to children and vice versa
- **Lazy loading**: Improves performance by loading related data only when needed

#### **3. Query Capabilities:**
- Find by type: `findByType(ELocationType.PROVINCE)`
- Find children: `findByParentId(Long parentId)`
- Find by code: `findByCode(String code)`
- Existence checks: `existsByCode()`, `existsByName()`

---

### **Business Logic:**
This implementation supports Rwanda's administrative structure:
- **5 Provinces** (Kigali, Northern, Southern, Eastern, Western)
- **30 Districts** under provinces
- **416 Sectors** under districts  
- **2,148 Cells** under sectors

The hierarchical relationship ensures data integrity and enables efficient querying of administrative boundaries.