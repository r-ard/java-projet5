package net.safety.alerts.safetynet.repositories;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository extends JsonRepository<MedicalRecordEntity> {
    public MedicalRecordRepository() throws NullDatabaseException, org.json.JSONException {
        super(MedicalRecordEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("medicalrecords");
    }

    public MedicalRecordEntity getPersonMedicalRecord(
            String firstName,
            String lastName
    ) {
        List<MedicalRecordEntity> medicalRecords = this.getEntities();

        for(MedicalRecordEntity e : medicalRecords) {
            if(e.getFirstName().equals(firstName) && e.getLastName().equals(lastName)) return e;
        }

        return null;
    }
}
