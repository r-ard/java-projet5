package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.entities.EntityAlreadyExistsException;
import net.safety.alerts.safetynet.exceptions.entities.EntityInsertException;
import net.safety.alerts.safetynet.exceptions.entities.EntityNotFoundException;
import net.safety.alerts.safetynet.exceptions.entities.EntityUpdateException;
import net.safety.alerts.safetynet.repositories.FireStationRepository;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import net.safety.alerts.safetynet.responses.fireresident.FireResidentPerson;
import net.safety.alerts.safetynet.responses.floodstation.FloodStationItem;
import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FireStationController {
    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping("/firestations")
    public List<FireStationEntity> getFireStations() { return this.fireStationRepository.getAll(); }

    @GetMapping("/firestation")
    public ResponseEntity getFireStation(
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "stationNumber", required = false) String stationNumber
    ) throws EntityNotFoundException {
        // Handle address query (returns 1 entity)
        if(address != null) {
            FireStationEntity firestation = fireStationRepository.getByAddress(address);
            if(firestation == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

            return ResponseEntity.ok().body(firestation);
        }
        // Handle station query (returns multiple entities)
        else if(stationNumber != null) {
            List<FireStationEntity> fireStations = fireStationRepository.getByStation(stationNumber);
            return ResponseEntity.ok().body(fireStations);
        }

        // Then throw a not found exception
        throw new EntityNotFoundException(FireStationEntity.class.getName());
    }

    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        FireStationEntity fireStation = fireStationRepository.getByAddress(address);

        if(fireStation == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

        boolean removed = fireStationRepository.remove(fireStation);

        if(!removed) throw new EntityNotFoundException(FireStationEntity.class.getName());

        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/firestation")
    public FireStationEntity updateFirestation(
            @RequestBody FireStationEntity fireStation
    ) throws EntityNotFoundException, EntityUpdateException {
        String address = fireStation.getAddress();

        if(address == null) throw new EntityUpdateException(PersonEntity.class.getName(), "address");

        FireStationEntity fireStationEntity = fireStationRepository.getByAddress(address);

        if(fireStationEntity == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

        fireStationEntity.update(fireStation);

        return fireStationEntity;
    }

    @PostMapping("/firestation")
    public FireStationEntity createFirestation(
            @RequestBody FireStationEntity fireStation
    ) throws EntityUpdateException, EntityAlreadyExistsException, EntityInsertException {
        String errorField = this.checkFirestationBody(fireStation);

        if(errorField != null) throw new EntityUpdateException(FireStationEntity.class.getName(), errorField);

        if(fireStationRepository.getByAddress(fireStation.getAddress()) != null)
            throw new EntityAlreadyExistsException(FireStationEntity.class.getName());

        boolean inserted = fireStationRepository.insert(fireStation);

        if(!inserted) throw new EntityInsertException(FireStationEntity.class.getName());

        return fireStation;
    }

    @GetMapping("/fire")
    public List<FireResidentPerson> getFireResidents(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        FireStationEntity fireStation = fireStationRepository.getByAddress(address);

        if(fireStation == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

        List<PersonEntity> residents = personRepository.getByAddress(address);

        List<FireResidentPerson> outResidents = new ArrayList<>();

        for(PersonEntity resident : residents) {
            MedicalRecordEntity medicalRecord = medicalRecordRepository.getPersonMedicalRecord(resident.getFirstName(), resident.getLastName());
            if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordRepository.class.getName());

            outResidents.add(new FireResidentPerson(
               fireStation.getStation(),
               resident,
               medicalRecord
            ));
        }

        return outResidents;
    }

    @GetMapping("/flood/stations")
    public Map<String, List<FloodStationItem>> floodStations(
            @RequestParam(name="stations", required=true) List<String> stations
    ) {
        Map<String, List<FloodStationItem>> outData = new HashMap<>();

        // Loop stations query param
        for(String station : stations) {
            // List object that will be set in out map
            List<FloodStationItem> stationData = new ArrayList<>();

            // Fetch station number's addresses
            List<String> stationAddresses = fireStationRepository.getStationAddresses(station);
            // Loop through station number's addresses
            for(String stationAddress : stationAddresses) {
                // List object of address residents
                List<PersonInfoResponse> addressData = new ArrayList<>();

                // Fetch address residents
                List<PersonEntity> addressResidents = personRepository.getByAddress(stationAddress);
                // Loop through address residents
                for(PersonEntity resident : addressResidents) {
                    // Fetch resident's medical record
                    MedicalRecordEntity medicalRecord = medicalRecordRepository.getPersonMedicalRecord(resident.getFirstName(), resident.getLastName());
                    if(medicalRecord == null) continue;

                    // Append resident info data to address data List
                    addressData.add(new PersonInfoResponse(
                            resident,
                            medicalRecord
                    ));
                }

                // Append address data to station data List
                stationData.add(new FloodStationItem(
                        stationAddress,
                        addressData
                ));
            }

            outData.put(station, stationData);
        }

        return outData;
    }

    @GetMapping("/phoneAlert")
    public List<String> phoneAlert(
            @RequestParam(name="firestation", required = true) String firestation
    ) throws EntityNotFoundException {
        List<String> phoneNumbers = new ArrayList<>();

        // Fetch fire stations of station number
        List<FireStationEntity> fireStations = fireStationRepository.getByStation(firestation);
        if(fireStations.isEmpty()) throw new EntityNotFoundException(FireStationEntity.class.getName());

        // Loop through fire stations
        for(FireStationEntity fireStation : fireStations) {

            // Fetch fire station's address residents
            List<PersonEntity> residents = personRepository.getByAddress(fireStation.getAddress());
            // Loop through fire station address residents
            for(PersonEntity resident : residents) {
                String residentPhone = resident.getPhone();
                if(phoneNumbers.contains(residentPhone)) continue;

                // Append resident's phone number
                phoneNumbers.add(residentPhone);
            }
        }

        return phoneNumbers;
    }

    private String checkFirestationBody(FireStationEntity fireStation) {
        if(fireStation.getAddress() == null) return "address";
        if(fireStation.getStation() == null) return "station";

        return null;
    }
}
