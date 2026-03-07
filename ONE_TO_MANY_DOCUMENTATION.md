# One-to-Many Relationship Implementation Documentation
## REQUIREMENT 5: One-to-Many Relationship ✅

### **Implementation Overview:**
Your project implements **THREE One-to-Many relationships**:
1. **Artist → Album** (One Artist has Many Albums)
2. **Album → Song** (One Album has Many Songs)
3. **User → Location** (Many Users belong to One Location)

---

## **1. ARTIST → ALBUM RELATIONSHIP**

### **Business Logic:**
- **One Artist** can create **Multiple Albums** (e.g., "The Beatles" created "Abbey Road", "Let It Be", etc.)
- **Each Album** belongs to **One Artist** only

### **Entity Mapping:**

#### **A. Artist Entity (One Side):**
```java
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 500)
    private String biography;
    
    // One-to-Many: One Artist has Many Albums
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Album> albums;
}
```

#### **B. Album Entity (Many Side):**
```java
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(name = "release_year")
    private Integer releaseYear;
    
    // Many-to-One: Many Albums belong to One Artist
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;
}
```

### **Database Schema:**
```sql
-- Artists Table (One Side)
CREATE TABLE artists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    biography VARCHAR(500)
);

-- Albums Table (Many Side)
CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    release_year INT,
    artist_id BIGINT NOT NULL,  -- Foreign Key
    FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);
```

---

## **2. ALBUM → SONG RELATIONSHIP**

### **Business Logic:**
- **One Album** contains **Multiple Songs** (e.g., "Abbey Road" has "Come Together", "Something", etc.)
- **Each Song** belongs to **One Album** only

### **Entity Mapping:**

#### **A. Album Entity (One Side):**
```java
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    // One-to-Many: One Album has Many Songs
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Song> songs;
}
```

#### **B. Song Entity (Many Side):**
```java
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false)
    private Duration duration;
    
    // Many-to-One: Many Songs belong to One Album
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
}
```

### **Database Schema:**
```sql
-- Albums Table (One Side)
CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    release_year INT,
    artist_id BIGINT NOT NULL
);

-- Songs Table (Many Side)
CREATE TABLE songs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    duration TIME NOT NULL,
    album_id BIGINT NOT NULL,  -- Foreign Key
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE
);
```

---

## **3. RELATIONSHIP MAPPING EXPLANATION**

### **A. One Side (@OneToMany):**
```java
@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Album> albums;
```

**Components:**
- **`@OneToMany`**: Declares One-to-Many relationship
- **`mappedBy = "artist"`**: References the field name in the Many side (Album.artist)
- **`cascade = CascadeType.ALL`**: Operations on Artist cascade to Albums (delete, persist, etc.)
- **`fetch = FetchType.LAZY`**: Albums loaded only when accessed (performance optimization)
- **`List<Album>`**: Collection of related entities

### **B. Many Side (@ManyToOne):**
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "artist_id", nullable = false)
private Artist artist;
```

**Components:**
- **`@ManyToOne`**: Declares Many-to-One relationship (inverse of One-to-Many)
- **`@JoinColumn(name = "artist_id")`**: Specifies foreign key column name
- **`nullable = false`**: Foreign key cannot be null (enforces relationship)
- **`fetch = FetchType.LAZY`**: Artist loaded only when accessed
- **`Artist artist`**: Reference to parent entity

---

## **4. FOREIGN KEY USAGE**

### **Foreign Key Definition:**
```java
@JoinColumn(name = "artist_id", nullable = false)
```

### **What @JoinColumn Does:**
- **Creates Foreign Key Column**: Adds `artist_id` column in `albums` table
- **References Primary Key**: Points to `artists.id`
- **Enforces Referential Integrity**: Cannot insert invalid artist_id
- **Nullable Constraint**: `nullable = false` requires valid artist reference

### **Database Constraint:**
```sql
FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
```

### **Foreign Key Benefits:**
1. **Data Integrity**: Prevents orphaned records
2. **Referential Integrity**: Ensures valid relationships
3. **Cascade Operations**: Automatic cleanup when parent deleted
4. **Query Optimization**: Database can use indexes on foreign keys

---

## **5. BIDIRECTIONAL RELATIONSHIP**

### **Navigation:**
```java
// From Artist to Albums (One to Many)
Artist artist = artistRepository.findById(1L).orElseThrow();
List<Album> albums = artist.getAlbums();  // Get all albums by this artist

// From Album to Artist (Many to One)
Album album = albumRepository.findById(1L).orElseThrow();
Artist artist = album.getArtist();  // Get the artist of this album
```

### **Bidirectional Mapping:**
- **One Side**: Uses `mappedBy` to reference Many side field
- **Many Side**: Uses `@JoinColumn` to define foreign key
- **Synchronization**: Changes on Many side automatically reflect on One side

---

## **6. CASCADE OPERATIONS**

### **CascadeType.ALL Explanation:**
```java
@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
private List<Album> albums;
```

### **Cascade Types:**
- **PERSIST**: When Artist saved, Albums also saved
- **MERGE**: When Artist updated, Albums also updated
- **REMOVE**: When Artist deleted, Albums also deleted
- **REFRESH**: When Artist refreshed, Albums also refreshed
- **DETACH**: When Artist detached, Albums also detached
- **ALL**: All of the above operations cascade

### **Example:**
```java
// Create artist with albums
Artist artist = new Artist();
artist.setName("The Beatles");

Album album1 = new Album();
album1.setTitle("Abbey Road");
album1.setArtist(artist);

Album album2 = new Album();
album2.setTitle("Let It Be");
album2.setArtist(artist);

artist.setAlbums(Arrays.asList(album1, album2));

// Save artist - albums automatically saved due to CascadeType.ALL
artistRepository.save(artist);

// Delete artist - albums automatically deleted due to CascadeType.ALL
artistRepository.deleteById(artist.getId());
```

---

## **7. LAZY vs EAGER LOADING**

### **Lazy Loading (Default):**
```java
@OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
private List<Album> albums;
```

**Behavior:**
- Albums loaded only when `artist.getAlbums()` is called
- Initial query: `SELECT * FROM artists WHERE id = ?`
- Second query (when accessed): `SELECT * FROM albums WHERE artist_id = ?`

**Benefits:**
- Faster initial load time
- Reduced memory usage
- Better performance for large collections

### **Eager Loading (When Needed):**
```java
@OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
private List<Album> albums;
```

**Behavior:**
- Albums loaded immediately with Artist
- Single query with JOIN: `SELECT * FROM artists a LEFT JOIN albums al ON a.id = al.artist_id WHERE a.id = ?`

**Use Cases:**
- Small collections
- Always need related data
- Avoid N+1 query problem

---

## **8. REPOSITORY QUERIES**

### **Query by Foreign Key:**
```java
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    
    // Find all albums by artist (using foreign key)
    List<Album> findByArtistId(Long artistId);
    
    // Find albums by artist name
    List<Album> findByArtistName(String artistName);
    
    // Count albums by artist
    Long countByArtistId(Long artistId);
    
    // Check if artist has albums
    boolean existsByArtistId(Long artistId);
}
```

### **Generated SQL:**
```sql
-- findByArtistId(1L)
SELECT * FROM albums WHERE artist_id = 1;

-- findByArtistName("The Beatles")
SELECT a.* FROM albums a 
JOIN artists ar ON a.artist_id = ar.id 
WHERE ar.name = 'The Beatles';
```

---

## **9. SAMPLE DATA FLOW**

### **Creating Relationship:**
```java
// Step 1: Create Artist
Artist artist = new Artist();
artist.setName("The Beatles");
artist.setBiography("Legendary British rock band");
artistRepository.save(artist);

// Step 2: Create Album with Artist reference
Album album = new Album();
album.setTitle("Abbey Road");
album.setReleaseYear(1969);
album.setArtist(artist);  // Set foreign key relationship
albumRepository.save(album);

// Step 3: Create Song with Album reference
Song song = new Song();
song.setTitle("Come Together");
song.setDuration(Duration.ofMinutes(4).plusSeconds(20));
song.setAlbum(album);  // Set foreign key relationship
songRepository.save(song);
```

### **Querying Relationship:**
```java
// Get all albums by artist
List<Album> albums = albumRepository.findByArtistId(1L);

// Get all songs in album
List<Song> songs = songRepository.findByAlbumId(1L);

// Navigate relationships
Artist artist = artistRepository.findById(1L).orElseThrow();
List<Album> albums = artist.getAlbums();  // Lazy loading triggers here
Album firstAlbum = albums.get(0);
List<Song> songs = firstAlbum.getSongs();  // Lazy loading triggers here
```

---

## **10. PERFORMANCE CONSIDERATIONS**

### **N+1 Query Problem:**
```java
// BAD: Causes N+1 queries
List<Artist> artists = artistRepository.findAll();  // 1 query
for (Artist artist : artists) {
    List<Album> albums = artist.getAlbums();  // N queries (one per artist)
}

// GOOD: Use JOIN FETCH
@Query("SELECT a FROM Artist a LEFT JOIN FETCH a.albums")
List<Artist> findAllWithAlbums();  // Single query with JOIN
```

### **Batch Fetching:**
```java
@OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
@BatchSize(size = 10)  // Load albums in batches of 10
private List<Album> albums;
```

---

## **CONCLUSION:**
Your project successfully implements **One-to-Many relationships** with:
- ✅ **Proper Mapping**: @OneToMany and @ManyToOne annotations
- ✅ **Foreign Key Usage**: @JoinColumn defines foreign key columns
- ✅ **Bidirectional Navigation**: Can traverse from parent to children and vice versa
- ✅ **Cascade Operations**: Automatic persistence and deletion
- ✅ **Performance Optimization**: Lazy loading and batch fetching
- ✅ **Data Integrity**: Foreign key constraints ensure valid relationships

This implementation follows JPA best practices and provides a solid foundation for managing hierarchical data in your music library system.