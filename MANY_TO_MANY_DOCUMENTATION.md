# Many-to-Many Relationship Implementation Documentation
## REQUIREMENT 4: Many-to-Many Relationship ✅

### **Implementation Overview:**
Your project implements a **Many-to-Many relationship** between `Song` and `Genre` entities using JPA annotations and a join table.

---

## **1. MANY-TO-MANY RELATIONSHIP MAPPING**

### **Business Logic:**
- **One Song** can belong to **Multiple Genres** (e.g., "Bohemian Rhapsody" can be Rock, Opera, Progressive Rock)
- **One Genre** can contain **Multiple Songs** (e.g., "Rock" genre contains thousands of songs)
- This creates a **Many-to-Many** relationship requiring a join table

### **Entity Mapping:**

#### **A. Song Entity (Owning Side):**
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
    
    // Many-to-Many relationship with Genre
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "song_genres",                    // Join table name
        joinColumns = @JoinColumn(name = "song_id"),      // Foreign key to Song
        inverseJoinColumns = @JoinColumn(name = "genre_id") // Foreign key to Genre
    )
    private List<Genre> genres;
}
```

#### **B. Genre Entity (Inverse Side):**
```java
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50, unique = true)
    private String name;
    
    @Column(length = 200)
    private String description;
    
    // Many-to-Many relationship with Song (inverse side)
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Song> songs;
}
```

---

## **2. JOIN TABLE EXPLANATION**

### **Join Table Structure:**
```sql
CREATE TABLE song_genres (
    song_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (song_id, genre_id),
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);
```

### **Join Table Components:**

#### **A. Table Name:**
- **`name = "song_genres"`**: Custom join table name
- **Convention**: `{entity1}_{entity2}` (song_genres)
- **Alternative**: JPA would auto-generate as `song_genre` without explicit naming

#### **B. Join Columns:**
- **`joinColumns = @JoinColumn(name = "song_id")`**: 
  - Column in join table referencing the owning entity (Song)
  - Foreign key pointing to `songs.id`
- **`inverseJoinColumns = @JoinColumn(name = "genre_id")`**:
  - Column in join table referencing the inverse entity (Genre)  
  - Foreign key pointing to `genres.id`

#### **C. Primary Key:**
- **Composite Primary Key**: `(song_id, genre_id)`
- **Prevents Duplicates**: Same song-genre combination cannot exist twice
- **Unique Constraint**: Ensures data integrity

---

## **3. RELATIONSHIP MAPPING DETAILS**

### **Owning vs Inverse Side:**

#### **A. Owning Side (Song Entity):**
```java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(...)
private List<Genre> genres;
```
- **Controls the relationship**: Defines join table structure
- **Responsible for persistence**: Changes to this side update the join table
- **Contains @JoinTable**: Specifies join table configuration

#### **B. Inverse Side (Genre Entity):**
```java
@ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
private List<Song> songs;
```
- **`mappedBy = "genres"`**: References the field name in the owning entity
- **Read-only by default**: Changes here don't automatically persist
- **No @JoinTable**: Relies on owning side configuration

### **Fetch Strategy:**
- **`FetchType.LAZY`**: Related entities loaded only when accessed
- **Performance Benefit**: Avoids loading unnecessary data
- **N+1 Problem Prevention**: Use JOIN FETCH queries when needed

---

## **4. DATABASE SCHEMA**

### **Complete Schema:**
```sql
-- Songs Table
CREATE TABLE songs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    duration TIME NOT NULL,
    album_id BIGINT NOT NULL,
    FOREIGN KEY (album_id) REFERENCES albums(id)
);

-- Genres Table
CREATE TABLE genres (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Join Table (Many-to-Many)
CREATE TABLE song_genres (
    song_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (song_id, genre_id),
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);
```

### **Sample Data:**
```sql
-- Sample Songs
INSERT INTO songs (id, title, duration, album_id) VALUES 
(1, 'Bohemian Rhapsody', '00:05:55', 1),
(2, 'Hotel California', '00:06:30', 2);

-- Sample Genres  
INSERT INTO genres (id, name, description) VALUES
(1, 'Rock', 'Rock music genre'),
(2, 'Progressive Rock', 'Progressive rock subgenre'),
(3, 'Opera', 'Operatic elements in music');

-- Many-to-Many Relationships
INSERT INTO song_genres (song_id, genre_id) VALUES
(1, 1), -- Bohemian Rhapsody -> Rock
(1, 2), -- Bohemian Rhapsody -> Progressive Rock  
(1, 3), -- Bohemian Rhapsody -> Opera
(2, 1); -- Hotel California -> Rock
```

---

## **5. REPOSITORY IMPLEMENTATION**

### **Query with Join Table:**
```java
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    /**
     * Find songs by genre using join table
     * Demonstrates Many-to-Many query capability
     */
    @Query("SELECT s FROM Song s JOIN s.genres g WHERE g.id = :genreId")
    Page<Song> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);
    
    /**
     * Find songs by genre name
     */
    @Query("SELECT s FROM Song s JOIN s.genres g WHERE g.name = :genreName")
    List<Song> findByGenreName(@Param("genreName") String genreName);
}
```

### **Generated SQL:**
```sql
-- Query: findByGenreId(1L)
SELECT s.* FROM songs s 
JOIN song_genres sg ON s.id = sg.song_id 
JOIN genres g ON sg.genre_id = g.id 
WHERE g.id = 1;
```

---

## **6. CRUD OPERATIONS**

### **Creating Relationships:**
```java
// Service method to add genre to song
public Song addGenreToSong(Long songId, Long genreId) {
    Song song = songRepository.findById(songId).orElseThrow();
    Genre genre = genreRepository.findById(genreId).orElseThrow();
    
    song.getGenres().add(genre);  // Add to owning side
    return songRepository.save(song);  // Persists to join table
}
```

### **Removing Relationships:**
```java
// Service method to remove genre from song
public Song removeGenreFromSong(Long songId, Long genreId) {
    Song song = songRepository.findById(songId).orElseThrow();
    song.getGenres().removeIf(genre -> genre.getId().equals(genreId));
    return songRepository.save(song);  // Updates join table
}
```

### **Querying Relationships:**
```java
// Get all genres for a song
public List<Genre> getGenresForSong(Long songId) {
    Song song = songRepository.findById(songId).orElseThrow();
    return song.getGenres();  // Lazy loading triggers here
}

// Get all songs for a genre
public List<Song> getSongsForGenre(Long genreId) {
    Genre genre = genreRepository.findById(genreId).orElseThrow();
    return genre.getSongs();  // Lazy loading triggers here
}
```

---

## **7. PERFORMANCE CONSIDERATIONS**

### **Lazy Loading:**
- **Default Behavior**: Related entities loaded on first access
- **Benefit**: Reduces initial query time and memory usage
- **Drawback**: Can cause N+1 queries if not handled properly

### **Eager Loading (when needed):**
```java
@Query("SELECT s FROM Song s JOIN FETCH s.genres WHERE s.id = :songId")
Optional<Song> findByIdWithGenres(@Param("songId") Long songId);
```

### **Batch Fetching:**
```java
@ManyToMany(fetch = FetchType.LAZY)
@BatchSize(size = 10)  // Load genres in batches of 10
private List<Genre> genres;
```

---

## **8. ADVANTAGES OF THIS IMPLEMENTATION**

### **Data Integrity:**
- **No Duplicate Relationships**: Composite primary key prevents duplicates
- **Referential Integrity**: Foreign key constraints ensure valid references
- **Cascade Operations**: Automatic cleanup when entities are deleted

### **Query Flexibility:**
- **Bidirectional Navigation**: Can query from Song to Genre or Genre to Song
- **Complex Queries**: Support for JOIN operations and filtering
- **Pagination Support**: Works with Spring Data JPA pagination

### **Maintainability:**
- **Clear Separation**: Join table isolates relationship data
- **Scalable**: Can handle millions of song-genre relationships
- **Standard JPA**: Uses industry-standard annotations and patterns

---

## **CONCLUSION:**
Your project successfully implements a **Many-to-Many relationship** with:
- ✅ **Proper Join Table**: `song_genres` with composite primary key
- ✅ **Correct Mapping**: Owning side (Song) and inverse side (Genre)
- ✅ **Performance Optimization**: Lazy loading and batch fetching
- ✅ **Query Support**: Complex queries with JOIN operations
- ✅ **Data Integrity**: Foreign key constraints and cascade operations

This implementation follows JPA best practices and provides a solid foundation for managing complex relationships in your music library system.