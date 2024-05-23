package net.safety.alerts.safetynet.dtos.fireresident;

import net.safety.alerts.safetynet.dtos.personinfo.PersonInfoResponseDto;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;

public class FireResidentPersonDto extends PersonInfoResponseDto {
    private final String fireStation;

    public FireResidentPersonDto() {
        super();
        this.fireStation = null;
    }

    public FireResidentPersonDto(
            String fireStation,
            PersonEntity personEntity,
            MedicalRecordEntity medicalRecord
    ) {
        super(personEntity, medicalRecord);

        this.fireStation = fireStation;
    }

    public String getFireStation() {
        return fireStation;
    }
}
