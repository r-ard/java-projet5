package net.safety.alerts.safetynet;

import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.exceptions.database.DatabaseAlreadyInitException;
import net.safety.alerts.safetynet.exceptions.database.DatabaseReadFileException;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.repositories.FireStationRepository;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONDatabaseTest {
    @BeforeAll
    public static void beforeAll() {
        JSONDatabase.clearInstance();
    }

    @AfterEach
    public void afterEach() {
        JSONDatabase.clearInstance();
    }

    @Test
    public void testInitDatabaseAlreadyExist() {
        try {
            JSONDatabase.initDatabase( new ClassPathResource("database.json").getPath() );
        }
        catch(Exception ex) {}

        assertThrows(
                DatabaseAlreadyInitException.class,
                () -> JSONDatabase.initDatabase("")
        );
    }

    @Test
    public void testInitDatabaseWithInvalidFile() {
        assertThrows(
                DatabaseReadFileException.class,
                () -> JSONDatabase.initDatabase("")
        );
    }

    @Test
    public void testCreateFireStationRespositoryWithNullDatabase() {
        assertThrows(
                NullDatabaseException.class,
                () -> new FireStationRepository()
        );
    }

    @Test
    public void testCreateMedicalRecordRespositoryWithNullDatabase() {
        assertThrows(
                NullDatabaseException.class,
                () -> new MedicalRecordRepository()
        );
    }

    @Test
    public void testCreatePersonRespositoryWithNullDatabase() {
        assertThrows(
                NullDatabaseException.class,
                () -> new PersonRepository()
        );
    }
}
