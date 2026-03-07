package Music_Library_system.Music_Library_system.config;

import Music_Library_system.Music_Library_system.models.*;
import Music_Library_system.Music_Library_system.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;


@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private GenreRepository genreRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing Music Library Database...");
        
        // Clear existing data
        userRepository.deleteAll();
        songRepository.deleteAll();
        genreRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        locationRepository.deleteAll();
        
        // ==================== 1. CREATE HIERARCHICAL LOCATIONS (RWANDA) ====================
        System.out.println("Creating Hierarchical Locations...");
        
        // PROVINCES
        Location kigali = new Location(null, "KGL", "Kigali City", ELocationType.PROVINCE, null, null);
        Location eastern = new Location(null, "EST", "Eastern Province", ELocationType.PROVINCE, null, null);
        Location western = new Location(null, "WST", "Western Province", ELocationType.PROVINCE, null, null);
        Location northern = new Location(null, "NTH", "Northern Province", ELocationType.PROVINCE, null, null);
        Location southern = new Location(null, "STH", "Southern Province", ELocationType.PROVINCE, null, null);
        
        locationRepository.saveAll(Arrays.asList(kigali, eastern, western, northern, southern));
        
        // DISTRICTS under Kigali
        Location gasabo = new Location(null, "GAS", "Gasabo", ELocationType.DISTRICT, kigali, null);
        Location kicukiro = new Location(null, "KIC", "Kicukiro", ELocationType.DISTRICT, kigali, null);
        Location nyarugenge = new Location(null, "NYA", "Nyarugenge", ELocationType.DISTRICT, kigali, null);
        
        // DISTRICTS under Eastern Province
        Location rwamagana = new Location(null, "RWA", "Rwamagana", ELocationType.DISTRICT, eastern, null);
        Location kayonza = new Location(null, "KAY", "Kayonza", ELocationType.DISTRICT, eastern, null);
        
        locationRepository.saveAll(Arrays.asList(gasabo, kicukiro, nyarugenge, rwamagana, kayonza));
        
        // SECTORS under Gasabo District
        Location kimironko = new Location(null, "KIM", "Kimironko", ELocationType.SECTOR, gasabo, null);
        Location remera = new Location(null, "REM", "Remera", ELocationType.SECTOR, gasabo, null);
        
        // SECTORS under Kicukiro District
        Location gatenga = new Location(null, "GAT", "Gatenga", ELocationType.SECTOR, kicukiro, null);
        Location niboye = new Location(null, "NIB", "Niboye", ELocationType.SECTOR, kicukiro, null);
        
        locationRepository.saveAll(Arrays.asList(kimironko, remera, gatenga, niboye));
        
        // CELLS under Kimironko Sector
        Location kibagabaga = new Location(null, "KIB", "Kibagabaga", ELocationType.CELL, kimironko, null);
        Location biryogo = new Location(null, "BIR", "Biryogo", ELocationType.CELL, kimironko, null);
        
        locationRepository.saveAll(Arrays.asList(kibagabaga, biryogo));
        
        // ==================== 2. CREATE USERS ====================
        System.out.println("Creating Users...");
        User user1 = new User(null, "patrick_d", "patrick@example.com", null, kigali);
        User user2 = new User(null, "uwase_marie", "uwase@example.com", null, gasabo);
        User user3 = new User(null, "mugisha_jean", "mugisha@example.com", null, eastern);
        User user4 = new User(null, "imena_alice", "imena@example.com", null, kimironko);
        User user5 = new User(null, "niyonzima_eric", "niyonzima@example.com", null, kibagabaga);
        
        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));
        
        // ==================== 3. CREATE ARTISTS ====================
        System.out.println("Creating Artists...");
        Artist artist1 = new Artist(null, "The Weeknd", "Canadian singer, songwriter, and record producer", null);
        Artist artist2 = new Artist(null, "Drake", "Canadian rapper, singer, and actor", null);
        Artist artist3 = new Artist(null, "Celine Dion", "Canadian singer known for powerful vocals", null);
        Artist artist4 = new Artist(null, "Justin Bieber", "Canadian pop singer", null);
        
        artistRepository.saveAll(Arrays.asList(artist1, artist2, artist3, artist4));
        
        // ==================== 4. CREATE ALBUMS ====================
        System.out.println("Creating Albums...");
        Album album1 = new Album(null, "After Hours", 2020, artist1, null);
        Album album2 = new Album(null, "Starboy", 2016, artist1, null);
        Album album3 = new Album(null, "Certified Lover Boy", 2021, artist2, null);
        Album album4 = new Album(null, "Views", 2016, artist2, null);
        Album album5 = new Album(null, "Falling into You", 1996, artist3, null);
        Album album6 = new Album(null, "Purpose", 2015, artist4, null);
        
        albumRepository.saveAll(Arrays.asList(album1, album2, album3, album4, album5, album6));
        
        // ==================== 5. CREATE GENRES ====================
        System.out.println("Creating Genres...");
        Genre genre1 = new Genre(null, "Pop", "Popular music genre", null);
        Genre genre2 = new Genre(null, "R&B", "Rhythm and Blues", null);
        Genre genre3 = new Genre(null, "Hip Hop", "Hip hop music genre", null);
        Genre genre4 = new Genre(null, "Rock", "Rock music genre", null);
        Genre genre5 = new Genre(null, "Electronic", "Electronic dance music", null);
        
        genreRepository.saveAll(Arrays.asList(genre1, genre2, genre3, genre4, genre5));
        
        // ==================== 6. CREATE SONGS ====================
        System.out.println("Creating Songs...");
        Song song1 = new Song(null, "Blinding Lights", Duration.ofMinutes(3).plusSeconds(20), album1, null);
        Song song2 = new Song(null, "Save Your Tears", Duration.ofMinutes(3).plusSeconds(35), album1, null);
        Song song3 = new Song(null, "Starboy", Duration.ofMinutes(3).plusSeconds(50), album2, null);
        Song song4 = new Song(null, "One Dance", Duration.ofMinutes(2).plusSeconds(54), album4, null);
        Song song5 = new Song(null, "Hotline Bling", Duration.ofMinutes(4).plusSeconds(27), album4, null);
        Song song6 = new Song(null, "My Heart Will Go On", Duration.ofMinutes(4).plusSeconds(40), album5, null);
        Song song7 = new Song(null, "Sorry", Duration.ofMinutes(3).plusSeconds(20), album6, null);
        
        songRepository.saveAll(Arrays.asList(song1, song2, song3, song4, song5, song6, song7));
        
        // ==================== 7. SETUP MANY-TO-MANY: SONG <-> GENRE ====================
        System.out.println("Setting up Song-Genre Relationships...");
        
        song1.setGenres(Arrays.asList(genre1, genre2, genre5));
        song2.setGenres(Arrays.asList(genre1, genre2));
        song3.setGenres(Arrays.asList(genre2, genre3, genre5));
        song4.setGenres(Arrays.asList(genre3, genre2));
        song5.setGenres(Arrays.asList(genre3, genre2));
        song6.setGenres(Arrays.asList(genre1));
        song7.setGenres(Arrays.asList(genre1, genre5));
        
        songRepository.saveAll(Arrays.asList(song1, song2, song3, song4, song5, song6, song7));
        
        System.out.println("Database initialization completed successfully!");
        System.out.println("\nSummary:");
        System.out.println("- Locations: " + locationRepository.count());
        System.out.println("  * Provinces: " + locationRepository.findByType(ELocationType.PROVINCE).size());
        System.out.println("  * Districts: " + locationRepository.findByType(ELocationType.DISTRICT).size());
        System.out.println("  * Sectors: " + locationRepository.findByType(ELocationType.SECTOR).size());
        System.out.println("  * Cells: " + locationRepository.findByType(ELocationType.CELL).size());
        System.out.println("- Users: " + userRepository.count());
        System.out.println("- Artists: " + artistRepository.count());
        System.out.println("- Albums: " + albumRepository.count());
        System.out.println("- Genres: " + genreRepository.count());
        System.out.println("- Songs: " + songRepository.count());
    }
}
