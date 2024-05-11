package net.safety.alerts.safetynet.responses.fireresident;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;

public class FireResidentPerson extends PersonInfoResponse {
    private final String fireStation;

    public FireResidentPerson() {
        super();
        this.fireStation = null;
    }

    public FireResidentPerson(
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
