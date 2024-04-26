package net.safety.alerts.safetynet;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
public class MedicalRecordControllerTest {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        try {
            JSONDatabase.initDatabase( new ClassPathResource("database.json").getPath() );
        }
        catch(Exception ex) {}
    }

    @AfterEach
    public void afterAll() {
        personRepository.resetEntities();
        medicalRecordRepository.resetEntities();
    }

    private MedicalRecordEntity generateTemplateMedicalRecord() {
        MedicalRecordEntity medicalRecord = new MedicalRecordEntity();
        medicalRecord.setFirstName("firstName");
        medicalRecord.setLastName("lastName");
        medicalRecord.setBirthdate("01/03/1987");
        medicalRecord.setMedications(new ArrayList<>());

        List<String> allergies = new ArrayList<String>();
        allergies.add("shellfish");
        medicalRecord.setAllergies(allergies);

        return medicalRecord;
    }

    private PersonEntity generateTemplatePerson() {
        PersonEntity entity = new PersonEntity();
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        entity.setPhone("000-000-000");
        entity.setEmail("test@email.com");
        entity.setZip("97451");
        entity.setCity("Culver");
        entity.setAddress("1509 Culver St");

        return entity;
    }

    private void insertTemplatePerson(PersonEntity personEntity) {
        if(personEntity == null) personEntity = this.generateTemplatePerson();

        personRepository.insert( personEntity );
    }

    private void insertTemplateMedicalRecord(MedicalRecordEntity medicalRecordEntity) {
        if(medicalRecordEntity == null) medicalRecordEntity = this.generateTemplateMedicalRecord();

        medicalRecordRepository.insert( medicalRecordEntity );
    }

    @Test
    public void testGetMedicalRecords() throws Exception {
        this.insertTemplateMedicalRecord(null);

        ResultActions result = mockMvc.perform(get("/medicalRecords"));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        MedicalRecordEntity[] entities = objectMapper.readValue(response.getContentAsString(), MedicalRecordEntity[].class);

        Assertions.assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetMedicalRecord() throws Exception {
        MedicalRecordEntity templateEntity = this.generateTemplateMedicalRecord();
        this.insertTemplateMedicalRecord(templateEntity);

        ResultActions result = mockMvc.perform(get("/medicalRecord?firstName=" + templateEntity.getFirstName() + "&lastName=" + templateEntity.getLastName()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        MedicalRecordEntity entity = objectMapper.readValue(response.getContentAsString(), MedicalRecordEntity.class);

        Assertions.assertEquals(entity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(entity.getLastName(), templateEntity.getLastName());
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        MedicalRecordEntity templateEntity = this.generateTemplateMedicalRecord();
        this.insertTemplateMedicalRecord(templateEntity);

        ResultActions result = mockMvc.perform(delete("/medicalRecord?firstName=" + templateEntity.getFirstName() + "&lastName=" + templateEntity.getLastName()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        MedicalRecordEntity repositoryEntity = medicalRecordRepository.getPersonMedicalRecord(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertEquals(repositoryEntity, null);
    }

    @Test
    public void testCreateMedicalRecord() throws Exception {
        MedicalRecordEntity templateEntity = this.generateTemplateMedicalRecord();

        ResultActions result = mockMvc.perform(
                post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        MedicalRecordEntity responseEntity = objectMapper.readValue(response.getContentAsString(), MedicalRecordEntity.class);
        Assertions.assertEquals(responseEntity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(responseEntity.getLastName(), templateEntity.getLastName());

        MedicalRecordEntity repositoryEntity = medicalRecordRepository.getPersonMedicalRecord(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertNotEquals(repositoryEntity, null);
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        this.insertTemplateMedicalRecord(null); // Instantiate new entity in repo

        MedicalRecordEntity templateEntity = this.generateTemplateMedicalRecord();
        templateEntity.setBirthdate("0/0/0000");

        ResultActions result = mockMvc.perform(
                put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        MedicalRecordEntity responseEntity = objectMapper.readValue(response.getContentAsString(), MedicalRecordEntity.class);
        Assertions.assertEquals(responseEntity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(responseEntity.getLastName(), templateEntity.getLastName());
        Assertions.assertEquals(responseEntity.getBirthdate(), templateEntity.getBirthdate());

        MedicalRecordEntity repositoryEntity = medicalRecordRepository.getPersonMedicalRecord(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertEquals(repositoryEntity.getBirthdate(), templateEntity.getBirthdate());
    }
}
