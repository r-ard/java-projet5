package net.safety.alerts.safetynet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.repositories.FireStationRepository;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import net.safety.alerts.safetynet.responses.fireresident.FireResidentPerson;
import net.safety.alerts.safetynet.responses.floodstation.FloodStationItem;
import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
public class FireStationControllerTest {
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

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
        fireStationRepository.resetEntities();
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
        entity.setCity("Test-City");
        entity.setAddress("1509 Test St");

        return entity;
    }

    private FireStationEntity generateTemplateFireStation() {
        FireStationEntity fireStation = new FireStationEntity();
        fireStation.setStation("99");
        fireStation.setAddress("1509 Test St");
        return fireStation;
    }

    private void insertTemplatePerson(PersonEntity personEntity) {
        if(personEntity == null) personEntity = this.generateTemplatePerson();

        personRepository.insert( personEntity );
    }

    private void insertTemplateMedicalRecord(MedicalRecordEntity medicalRecordEntity) {
        if(medicalRecordEntity == null) medicalRecordEntity = this.generateTemplateMedicalRecord();

        medicalRecordRepository.insert( medicalRecordEntity );
    }

    private void insertTemplateFireStation(FireStationEntity fireStationEntity) {
        if(fireStationEntity == null) fireStationEntity = this.generateTemplateFireStation();

        fireStationRepository.insert( fireStationEntity );
    }

    @Test
    public void testGetFireStations() throws Exception {
        this.insertTemplateFireStation(null);

        ResultActions result = mockMvc.perform(get("/firestations"));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity[] entities = objectMapper.readValue(response.getContentAsString(), FireStationEntity[].class);

        Assertions.assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetFireStationByAddress() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateEntity);

        ResultActions result = mockMvc.perform(get("/firestation?address=" + templateEntity.getAddress()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity entity = objectMapper.readValue(response.getContentAsString(), FireStationEntity.class);

        Assertions.assertEquals(entity.getAddress(), templateEntity.getAddress());
        Assertions.assertEquals(entity.getStation(), templateEntity.getStation());
    }

    @Test
    public void testGetFireStationByAddressNotFound() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();

        ResultActions result = mockMvc.perform(get("/firestation?address=" + templateEntity.getAddress()));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetFireStationsByStationNumber() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateEntity);

        ResultActions result = mockMvc.perform(get("/firestation?stationNumber=" + templateEntity.getStation()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity[] entities = objectMapper.readValue(response.getContentAsString(), FireStationEntity[].class);

        Assertions.assertNotEquals(0, entities.length);
    }

    @Test
    public void testDeleteFireStation() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateEntity);

        ResultActions result = mockMvc.perform(delete("/firestation?address=" + templateEntity.getAddress()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity repositoryEntity = fireStationRepository.getByAddress(templateEntity.getAddress());
        Assertions.assertEquals(repositoryEntity, null);
    }

    @Test
    public void testDeleteFireStationNotFound() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();

        ResultActions result = mockMvc.perform(delete("/firestation?address=" + templateEntity.getAddress()));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testCreateFireStation() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();

        ResultActions result = mockMvc.perform(
                post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity responseEntity = objectMapper.readValue(response.getContentAsString(), FireStationEntity.class);
        Assertions.assertEquals(responseEntity.getStation(), templateEntity.getStation());
        Assertions.assertEquals(responseEntity.getAddress(), templateEntity.getAddress());

        FireStationEntity repositoryEntity = fireStationRepository.getByAddress(templateEntity.getAddress());
        Assertions.assertNotEquals(repositoryEntity, null);
    }

    @Test
    public void testCreateFireStationAlreadyExists() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateEntity);

        ResultActions result = mockMvc.perform(
                post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        result.andExpect(status().isConflict());
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        this.insertTemplateFireStation(null); // Instantiate new entity in repo

        FireStationEntity templateEntity = this.generateTemplateFireStation();
        templateEntity.setStation("100");

        ResultActions result = mockMvc.perform(
                put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireStationEntity responseEntity = objectMapper.readValue(response.getContentAsString(), FireStationEntity.class);
        Assertions.assertEquals(responseEntity.getAddress(), templateEntity.getAddress());
        Assertions.assertEquals(responseEntity.getStation(), templateEntity.getStation());

        FireStationEntity repositoryEntity = fireStationRepository.getByAddress(templateEntity.getAddress());
        Assertions.assertEquals(repositoryEntity.getStation(), templateEntity.getStation());
    }

    @Test
    public void testUpdateFireStationNotFound() throws Exception {
        FireStationEntity templateEntity = this.generateTemplateFireStation();
        templateEntity.setStation("100");

        ResultActions result = mockMvc.perform(
                put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateEntity))
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testPhoneAlert() throws Exception {
        PersonEntity templatePerson = this.generateTemplatePerson();
        this.insertTemplatePerson(templatePerson);

        FireStationEntity templateFireStation = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateFireStation);

        ResultActions result = mockMvc.perform(get("/phoneAlert?firestation=" + templateFireStation.getStation()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        String[] phoneNumbers = objectMapper.readValue(response.getContentAsString(), String[].class);

        String matchingPhoneNumber = null;
        for(String phoneNumber : phoneNumbers) {
            if(!phoneNumber.equals(templatePerson.getPhone())) continue;

            matchingPhoneNumber = phoneNumber;
            break;
        }
        Assertions.assertNotEquals(null, matchingPhoneNumber);
    }

    @Test
    public void testPhoneAlertNotFound() throws Exception {
        ResultActions result = mockMvc.perform(get("/phoneAlert?firestation=" + "INVALID STATION"));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetFireResidents() throws Exception {
        PersonEntity templatePerson = this.generateTemplatePerson();
        this.insertTemplatePerson(templatePerson);

        MedicalRecordEntity templateMedicalRecord = this.generateTemplateMedicalRecord();
        this.insertTemplateMedicalRecord(templateMedicalRecord);

        FireStationEntity templateFireStation = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateFireStation);

        ResultActions result = mockMvc.perform(get("/fire?address=" + templateFireStation.getAddress()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        FireResidentPerson[] fireResidents = objectMapper.readValue(response.getContentAsString(), FireResidentPerson[].class);

        FireResidentPerson matchingFireResident = null;
        for(FireResidentPerson fireResident : fireResidents) {
            // Check if resident matchs with templateEntity
            if(!fireResident.getFirstName().equals(templatePerson.getFirstName()) || !fireResident.getLastName().equals(templatePerson.getLastName()))
                continue;

            // Check if resident matchs with templateMedicalRecord
            if(fireResident.getAllergies().size() != templateMedicalRecord.getAllergies().size())
                continue;

            matchingFireResident = fireResident;
            break;
        }
        Assertions.assertNotEquals(null, matchingFireResident);
    }

    @Test
    public void testGetFireResidentsAddressNotFound() throws Exception {
        ResultActions result = mockMvc.perform(get("/fire?address=" + "INVALID ADDRESS"));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetFireResidentsMedicalRecordNotFound() throws Exception {
        PersonEntity templatePerson = this.generateTemplatePerson();
        this.insertTemplatePerson(templatePerson);

        FireStationEntity templateFireStation = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateFireStation);

        ResultActions result = mockMvc.perform(get("/fire?address=" + templatePerson.getAddress()));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testFloodStation() throws Exception {
        PersonEntity templatePerson = this.generateTemplatePerson();
        this.insertTemplatePerson(templatePerson);

        MedicalRecordEntity templateMedicalRecord = this.generateTemplateMedicalRecord();
        this.insertTemplateMedicalRecord(templateMedicalRecord);

        FireStationEntity templateFireStation = this.generateTemplateFireStation();
        this.insertTemplateFireStation(templateFireStation);

        ResultActions result = mockMvc.perform(get("/flood/stations?stations=" + templateFireStation.getStation()));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn().getResponse();

        TypeReference<HashMap<String, List<FloodStationItem>>> typeRef
                = new TypeReference<HashMap<String, List<FloodStationItem>>>() {};

        HashMap<String, List<FloodStationItem>> floodedStations = objectMapper.readValue(response.getContentAsString(), typeRef);

        PersonInfoResponse matchingResident = null;
        if(floodedStations.containsKey(templateFireStation.getStation())) {
            List<FloodStationItem> stationItems = floodedStations.get(templateFireStation.getStation());

            for(FloodStationItem stationItem : stationItems) {
                if(!stationItem.getAddress().equals(templatePerson.getAddress())) continue;

                List<PersonInfoResponse> residents = stationItem.getResidents();
                for(PersonInfoResponse resident : residents) {
                    // Check resident matchs with templatePerson
                    if(!resident.getFirstName().equals(templatePerson.getFirstName())
                        || !resident.getLastName().equals(templatePerson.getLastName())) continue;

                    // Check resident matchs with templateMedicalRecord
                    if(resident.getAllergies().size() != templateMedicalRecord.getAllergies().size())
                        continue;

                    matchingResident = resident;
                    break;
                }
                break;
            }
        }
        Assertions.assertNotEquals(null, matchingResident);
    }
}
