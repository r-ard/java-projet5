package net.safety.alerts.safetynet.repositories;

import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class MedicalRecordRepository extends JsonRepository<MedicalRecordEntity> {
    public MedicalRecordRepository() throws NullDatabaseException, org.json.JSONException {
        super(MedicalRecordEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("medicalrecords");
    }
}
