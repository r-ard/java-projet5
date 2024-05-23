package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.exceptions.entities.*;
import net.safety.alerts.safetynet.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalService;

    @GetMapping("/medicalRecords")
    public List<MedicalRecordEntity> getFireStations() { return this.medicalService.getAll(); }

    @GetMapping("/medicalRecord")
    public MedicalRecordEntity getMedicalRecord(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        MedicalRecordEntity medicalRecord = medicalService.getByName(firstName, lastName);

        if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        return medicalRecord;
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity deleteMedicalRecord(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        MedicalRecordEntity medicalRecord = medicalService.getByName(firstName, lastName);

        if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        boolean removed = medicalService.remove(medicalRecord);

        if(!removed) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/medicalRecord")
    public MedicalRecordEntity updateMedicalRecord(
            @RequestBody MedicalRecordEntity medicalRecord
    ) throws EntityNotFoundException, EntityUpdateException, EntityMissingFieldException {
        String firstName = medicalRecord.getFirstName();
        String lastName = medicalRecord.getLastName();

        if(firstName == null) throw new EntityMissingFieldException(MedicalRecordEntity.class.getName(), "firstName");
        if(lastName == null) throw new EntityMissingFieldException(MedicalRecordEntity.class.getName(), "lastName");

        MedicalRecordEntity medicalRecordEntity = medicalService.getByName(firstName, lastName);

        if(medicalRecordEntity == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        medicalRecordEntity.update(medicalRecord);

        return medicalRecordEntity;
    }

    @PostMapping("/medicalRecord")
    public MedicalRecordEntity createMedicalRecord(
            @RequestBody MedicalRecordEntity medicalRecord
    ) throws EntityUpdateException, EntityAlreadyExistsException, EntityInsertException {
        String errorField = this.checkMedicalRecord(medicalRecord);

        if(errorField != null) throw new EntityUpdateException(MedicalRecordEntity.class.getName(), errorField);

        if(medicalService.getByName(medicalRecord.getFirstName(), medicalRecord.getLastName()) != null)
            throw new EntityAlreadyExistsException(MedicalRecordEntity.class.getName());

        boolean inserted = medicalService.insert(medicalRecord);
        if(!inserted) throw new EntityInsertException(MedicalRecordEntity.class.getName());

        return medicalRecord;
    }

    private String checkMedicalRecord(MedicalRecordEntity medicalRecord) {
        if(medicalRecord.getFirstName() == null) return "firstName";
        if(medicalRecord.getLastName() == null) return "lastName";
        if(medicalRecord.getBirthdate() == null) return "birthDate";
        if(medicalRecord.getMedications() == null) return "medications";
        if(medicalRecord.getAllergies() == null) return "allergies";

        return null;
    }
}
