# Entity Relationship Diagram (ERD) Documentation
## Music Library System - 6 Tables with Clear Relationships

### **REQUIREMENT 1: ERD with 5+ Tables ✅**

## **Tables Overview:**
1. **artists** - Music artists/performers
2. **albums** - Music albums 
3. **songs** - Individual tracks/songs
4. **genres** - Music genres (Rock, Pop, etc.)
5. **locations** - Hierarchical location data (Rwanda administrative structure)
6. **users** - System users
7. **song_genres** - Junction table (Many-to-Many relationship)

---

## **Entity Relationships Logic:**

### **1. Artist ↔ Album (One-to-Many)**
- **Logic**: One artist can create multiple albums, but each album belongs to one artist
- **Implementation**: 
  - `Artist.albums` → `@OneToMany(mappedBy = "artist")`
  - `Album.artist` → `@ManyToOne` with `artist_id` foreign key

### **2. Album ↔ Song (One-to-Many)**
- **Logic**: One album contains multiple songs, but each song belongs to one album
- **Implementation**:
  - `Album.songs` → `@OneToMany(mappedBy = "album")`
  - `Song.album` → `@ManyToOne` with `album_id` foreign key

### **3. Song ↔ Genre (Many-to-Many)**
- **Logic**: One song can have multiple genres, and one genre can be applied to multiple songs
- **Implementation**:
  - `Song.genres` → `@ManyToMany` with junction table `song_genres`
  - `Genre.songs` → `@ManyToMany(mappedBy = "genres")`
  - Junction table: `song_genres(song_id, genre_id)`

### **4. Location ↔ Location (Self-Referencing One-to-Many)**
- **Logic**: Hierarchical structure - PROVINCE → DISTRICT → SECTOR → CELL
- **Implementation**:
  - `Location.parent` → `@ManyToOne` with `parent_id` foreign key
  - `Location.children` → `@OneToMany(mappedBy = "parent")`

### **5. User ↔ Location (Many-to-One)**
- **Logic**: Multiple users can belong to the same location, but each user has one location
- **Implementation**:
  - `User.location` → `@ManyToOne` with `location_id` foreign key

---

## **Database Schema:**

```sql
-- Artists Table
CREATE TABLE artists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    biography VARCHAR(500)
);

-- Albums Table  
CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    release_year INT,
    artist_id BIGINT NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artists(id)
);

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

-- Song-Genre Junction Table (Many-to-Many)
CREATE TABLE song_genres (
    song_id BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (song_id, genre_id),
    FOREIGN KEY (song_id) REFERENCES songs(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Locations Table (Self-Referencing)
CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL, -- PROVINCE, DISTRICT, SECTOR, CELL
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES locations(id)
);

-- Users Table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);
```

---

## **Relationship Summary:**
- **Total Tables**: 7 (including junction table)
- **One-to-Many**: 3 relationships
- **Many-to-Many**: 1 relationship  
- **Self-Referencing**: 1 relationship
- **Foreign Keys**: 6 total

This ERD demonstrates proper normalization and clear business logic for a music library system with hierarchical location management.