package net.safety.alerts.safetynet.entities;

import net.safety.alerts.safetynet.exceptions.utils.EntityUpdateException;
import net.safety.alerts.safetynet.utils.IEntity;
import net.safety.alerts.safetynet.utils.JsonUtils;
import org.json.JSONObject;

public class MedicalRecordEntity implements IEntity<MedicalRecordEntity> {
    private String firstName;

    private String lastName;

    private String birthdate;

    private String[] medications;

    private String[] allergies;

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

    public String[] getMedications() {
        return medications;
    }

    public void setMedications(String[] medications) {
        this.medications = medications;
    }

    public String[] getAllergies() {
        return medications;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public MedicalRecordEntity() {
        this.firstName = null;
        this.lastName = null;
        this.birthdate = null;
        this.medications = null;
        this.allergies = null;
    }

    public static MedicalRecordEntity fromJsonObject(JSONObject object) {
        MedicalRecordEntity medicalRecord = new MedicalRecordEntity();

        medicalRecord.setFirstName( object.getString("firstName") );
        medicalRecord.setLastName( object.getString("lastName") );
        medicalRecord.setBirthdate( object.getString("birthdate") );

        medicalRecord.setMedications(
                JsonUtils.getJsonArrayStrings( object.getJSONArray("medications") )
        );

        medicalRecord.setAllergies(
                JsonUtils.getJsonArrayStrings( object.getJSONArray("allergies") )
        );

        return medicalRecord;
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
