package net.safety.alerts.safetynet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;
import net.safety.alerts.safetynet.responses.childalert.ChildAlertResponse;
import net.safety.alerts.safetynet.responses.childalert.ChildPerson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc()
public class PersonControllerTest {
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
    public void testGetPersons() throws Exception {
        this.insertTemplatePerson(null);

        ResultActions result = mockMvc.perform(get("/persons"));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonEntity[] entities = objectMapper.readValue(response.getContentAsString(), PersonEntity[].class);

        Assertions.assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetPerson() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        ResultActions result = mockMvc.perform(get("/person?firstName=" + templateEntity.getFirstName() + "&lastName=" + templateEntity.getLastName()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonEntity entity = objectMapper.readValue(response.getContentAsString(), PersonEntity.class);

        Assertions.assertEquals(entity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(entity.getLastName(), templateEntity.getLastName());
    }

    @Test
    public void testGetPersonNotFound() throws Exception {
        String invalidFirstName = "Invalid First Name";
        String invalidLastName = "Invalid Last Name";

        ResultActions result = mockMvc.perform(get("/person?firstName=" + invalidFirstName + "&lastName=" + invalidLastName));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testCommunityEmail() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        ResultActions result = mockMvc.perform(get("/communityEmail?city=" + templateEntity.getCity()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        String[] emails = objectMapper.readValue(response.getContentAsString(), String[].class);

        boolean emailDetected = false;
        for(String email : emails) {
            if(!email.equals(templateEntity.getEmail())) continue;

            emailDetected = true;
            break;
        }

        Assertions.assertTrue(emailDetected);
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        MedicalRecordEntity medicalRecord = this.generateTemplateMedicalRecord();
        this.insertTemplateMedicalRecord(medicalRecord);

        ResultActions result = mockMvc.perform(get("/personInfo?firstName=" + templateEntity.getFirstName() + "&lastName=" + templateEntity.getLastName()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonInfoResponse responseObject = objectMapper.readValue(response.getContentAsString(), PersonInfoResponse.class);

        Assertions.assertEquals(responseObject.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(responseObject.getLastName(), templateEntity.getLastName());
        Assertions.assertEquals(responseObject.getAllergies().size(), medicalRecord.getAllergies().size());
    }

    @Test
    public void testGetPersonInfoNotFound() throws Exception {
        String invalidFirstName = "Invalid First Name";
        String invalidLastName = "Invalid Last Name";

        ResultActions result = mockMvc.perform(get("/personInfo?firstName=" + invalidFirstName + "&lastName=" + invalidLastName));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testChildAlert() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        MedicalRecordEntity medicalRecord = this.generateTemplateMedicalRecord();

        // Set birthdate to today
        {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            medicalRecord.setBirthdate(today.format(formatter));
        }

        this.insertTemplateMedicalRecord(medicalRecord);

        ResultActions result = mockMvc.perform(get("/childAlert?address=" + templateEntity.getAddress()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        ChildAlertResponse responseObject = objectMapper.readValue(response.getContentAsString(), ChildAlertResponse.class);


        ChildPerson matchingPerson = null;
        for(ChildPerson childPerson : responseObject.getChilds()) {
            if(!childPerson.getFirstName().equals(templateEntity.getFirstName()) || !childPerson.getLastName().equals(templateEntity.getLastName()))
                continue;

            matchingPerson = childPerson;
            break;
        }

        Assertions.assertNotEquals(matchingPerson, null);
    }

    @Test
    public void testDeletePerson() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        ResultActions result = mockMvc.perform(delete("/person?firstName=" + templateEntity.getFirstName() + "&lastName=" + templateEntity.getLastName()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonEntity repositoryEntity = personRepository.getByName(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertEquals(repositoryEntity, null);
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        String invalidFirstName = "Invalid First Name";
        String invalidLastName = "Invalid Last Name";

        ResultActions result = mockMvc.perform(delete("/person?firstName=" + invalidFirstName + "&lastName=" + invalidLastName));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePerson() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();

        ResultActions result = mockMvc.perform(
                post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonEntity responseEntity = objectMapper.readValue(response.getContentAsString(), PersonEntity.class);
        Assertions.assertEquals(responseEntity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(responseEntity.getLastName(), templateEntity.getLastName());

        PersonEntity repositoryEntity = personRepository.getByName(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertNotEquals(repositoryEntity, null);
    }

    @Test
    public void testCreatePersonAlreadyExists() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        this.insertTemplatePerson(templateEntity);

        ResultActions result = mockMvc.perform(
                post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        result.andExpect(status().isConflict());
    }

    @Test
    public void testUpdatePerson() throws Exception {
        this.insertTemplatePerson(null); // Instantiate new entity in repo

        PersonEntity templateEntity = this.generateTemplatePerson();
        templateEntity.setEmail("test2@email.com");

        ResultActions result = mockMvc.perform(
                put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        PersonEntity responseEntity = objectMapper.readValue(response.getContentAsString(), PersonEntity.class);
        Assertions.assertEquals(responseEntity.getFirstName(), templateEntity.getFirstName());
        Assertions.assertEquals(responseEntity.getLastName(), templateEntity.getLastName());
        Assertions.assertEquals(responseEntity.getEmail(), templateEntity.getEmail());

        PersonEntity repositoryEntity = personRepository.getByName(templateEntity.getFirstName(), templateEntity.getLastName());
        Assertions.assertEquals(repositoryEntity.getEmail(), templateEntity.getEmail());
    }

    @Test
    public void testUpdatePersonNotFound() throws Exception {
        PersonEntity templateEntity = this.generateTemplatePerson();
        templateEntity.setEmail("test2@email.com");

        ResultActions result = mockMvc.perform(
                put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        result.andExpect(status().isNotFound());
    }
}
