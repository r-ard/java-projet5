package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.dtos.fireresident.FireResidentPersonDto;
import net.safety.alerts.safetynet.dtos.floodstation.FloodStationItemDto;
import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.entities.EntityAlreadyExistsException;
import net.safety.alerts.safetynet.exceptions.entities.EntityInsertException;
import net.safety.alerts.safetynet.exceptions.entities.EntityNotFoundException;
import net.safety.alerts.safetynet.exceptions.entities.EntityUpdateException;
import net.safety.alerts.safetynet.services.FireStationService;
import net.safety.alerts.safetynet.services.MedicalRecordService;
import net.safety.alerts.safetynet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/firestations")
    public List<FireStationEntity> getFireStations() { return this.fireStationService.getAll(); }

    @GetMapping("/firestation")
    public List<FireStationEntity> getFireStation(
            @RequestParam(name = "stationNumber", required = true) String stationNumber
    ) throws EntityNotFoundException {
        List<FireStationEntity> fireStations = fireStationService.getByStation(stationNumber);
        if(fireStations.isEmpty()) throw new EntityNotFoundException(FireStationEntity.class.getName());

        return fireStations;
    }

    @DeleteMapping("/firestation")
    public ResponseEntity deleteFireStation(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        FireStationEntity fireStation = fireStationService.getByAddress(address);

        if(fireStation == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

        boolean removed = fireStationService.remove(fireStation);

        if(!removed) throw new EntityNotFoundException(FireStationEntity.class.getName());

        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/firestation")
    public FireStationEntity updateFirestation(
            @RequestBody FireStationEntity fireStation
    ) throws EntityNotFoundException, EntityUpdateException {
        String address = fireStation.getAddress();

        if(address == null) throw new EntityUpdateException(PersonEntity.class.getName(), "address");

        FireStationEntity fireStationEntity = fireStationService.getByAddress(address);

        if(fireStationEntity == null) throw new EntityNotFoundException(FireStationEntity.class.getName());

        fireStationEntity.update(fireStation);

        return fireStationEntity;
    }

    @PostMapping("/firestation")
    public FireStationEntity createFirestation(
            @RequestBody FireStationEntity fireStation
    ) throws EntityUpdateException, EntityAlreadyExistsException, EntityInsertException {
        String errorField = FireStationService.checkFirestationData(fireStation);
        if(errorField != null) throw new EntityUpdateException(FireStationEntity.class.getName(), errorField);

        if(fireStationService.getByAddress(fireStation.getAddress()) != null)
            throw new EntityAlreadyExistsException(FireStationEntity.class.getName());

        boolean inserted = fireStationService.insert(fireStation);
        if(!inserted) throw new EntityInsertException(FireStationEntity.class.getName());

        return fireStation;
    }

    @GetMapping("/fire")
    public List<FireResidentPersonDto> getFireResidents(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        List<FireResidentPersonDto> fireResidents = fireStationService.getFireResidents(address);
        if(fireResidents.isEmpty()) throw new EntityNotFoundException(FireStationEntity.class.getName());

        return fireResidents;
    }

    @GetMapping("/flood/stations")
    public Map<String, List<FloodStationItemDto>> floodStations(
            @RequestParam(name="stations", required=true) List<String> stations
    ) {
        Map<String, List<FloodStationItemDto>> outData = new HashMap<>();

        // Loop stations query param
        for(String station : stations) {
            // List object that will be set in out map
            List<FloodStationItemDto> stationData = fireStationService.getFloodStation(station);

            outData.put(station, stationData);
        }

        return outData;
    }

    @GetMapping("/phoneAlert")
    public List<String> phoneAlert(
            @RequestParam(name="firestation", required = true) String firestation
    ) throws EntityNotFoundException {
        List<String> phoneNumbers = fireStationService.getStationPhoneNumbers(firestation);
        if(phoneNumbers.isEmpty()) throw new EntityNotFoundException(FireStationEntity.class.getName());

        return phoneNumbers;
    }
}
