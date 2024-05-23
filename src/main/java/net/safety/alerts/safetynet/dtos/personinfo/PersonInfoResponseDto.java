package net.safety.alerts.safetynet.dtos.personinfo;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.utils.DateUtils;

import java.util.List;

public class PersonInfoResponseDto {
    private final String firstName;
    private final String lastName;
    private final String email;

    private final int age;

    private final List<String> medications;

    private final List<String> allergies;

    public PersonInfoResponseDto() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.age = 0;
        this.medications = null;
        this.allergies = null;
    }

    public PersonInfoResponseDto(
        PersonEntity personEntity,
        MedicalRecordEntity medicalRecord
    ) {
        this.firstName = personEntity.getFirstName();
        this.lastName = personEntity.getLastName();
        this.email = personEntity.getEmail();

        this.age = DateUtils.getDateYearsDiff(
                DateUtils.convertStrDateToDate(medicalRecord.getBirthdate())
        );

        this.medications = medicalRecord.getMedications();
        this.allergies = medicalRecord.getAllergies();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }
}
