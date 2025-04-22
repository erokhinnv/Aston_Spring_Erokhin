package universities.repositories;

import universities.entities.University;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import universities.utils.DatabaseSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

class UniversityRepositoryTest {
    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    public Connection openConnection() throws SQLException {
        Connection connection;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USERNAME, DatabaseSettings.PASSWORD);
        return connection;
    }

    @BeforeEach
    void setUp() {
        try {
            Connection connection;

            DatabaseSettings.URL = postgres.getJdbcUrl();
            DatabaseSettings.USERNAME = postgres.getUsername();
            DatabaseSettings.PASSWORD = postgres.getPassword();
            connection = openConnection();
            repository = new UniversityRepository(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void cleanUp() throws SQLException {
        Connection connection;
        Statement statement;

        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        statement = connection.createStatement();
        statement.execute("DELETE FROM universities");
    }

    @Test
    void testGet() throws SQLException {
        University first;
        University second;
        Collection<University> universities;

        first = new University();
        first.setName("ITMO");
        first.setCity("Saint-Petersburg");
        second = new University();
        second.setName("PGSHA");
        second.setCity("Perm");
        repository.add(first);
        repository.add(second);
        universities = repository.get();
        Assertions.assertEquals(2, universities.size());
    }

    @Test
    void testGetById() throws SQLException {
        University university, dbUniversity;
        int id;

        university = new University();
        university.setName("PSTU");
        university.setCity("Perm");
        repository.add(university);
        id = university.getId();
        dbUniversity = repository.getById(id);
        Assertions.assertEquals(university.getId(), dbUniversity.getId());
        Assertions.assertEquals(university.getName(), dbUniversity.getName());
        Assertions.assertEquals(university.getCity(), dbUniversity.getCity());
    }

    @Test
    void testAdd() throws SQLException {
        University university;
        Collection<University> universities;

        university = new University();
        university.setName("ITMO");
        university.setCity("Saint-Petersburg");
        repository.add(university);
        universities = repository.get();
        Assertions.assertFalse(universities.isEmpty());
    }

    @Test
    void testUpdate() throws SQLException {
        University university;
        boolean updated;

        university = new University();
        university.setName("ITMO");
        university.setCity("Saint-Petersburg");
        repository.add(university);
        university.setName("SpbSU");
        updated = repository.update(university);
        Assertions.assertTrue(updated);
    }

    @Test
    void testDelete() throws SQLException {
        University university;
        boolean deleted;

        university = new University();
        university.setName("ITMO");
        university.setCity("Saint-Petersburg");
        repository.add(university);
        deleted = repository.delete(university.getId());
        Assertions.assertTrue(deleted);
    }

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );
    UniversityRepository repository;
}
