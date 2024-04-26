package net.safety.alerts.safetynet.entities;

import net.safety.alerts.safetynet.exceptions.entities.EntityUpdateException;
import net.safety.alerts.safetynet.utils.IEntity;

import java.util.List;

public class MedicalRecordEntity implements IEntity<MedicalRecordEntity> {
    private String firstName;

    private String lastName;

    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public MedicalRecordEntity() {
        this.firstName = null;
        this.lastName = null;
        this.birthdate = null;
        this.medications = null;
        this.allergies = null;
    }

    @Override
    public void update(MedicalRecordEntity data) throws EntityUpdateException {
        if(data.getFirstName() != null) this.setFirstName(data.getFirstName());

        if(data.getLastName() != null) this.setLastName(data.getLastName());

        if(data.getBirthdate() != null) this.setBirthdate(data.getBirthdate());

        if(data.getMedications() != null) this.setMedications(data.getMedications());

        if(data.getAllergies() != null) this.setAllergies(data.getAllergies());
    }
}
