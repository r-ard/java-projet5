package net.safety.alerts.safetynet.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;


@Repository
public class FireStationRepository extends JsonRepository<FireStationEntity> {
    ObjectMapper mapper;

    public FireStationRepository() throws NullDatabaseException, org.json.JSONException {
        super(FireStationEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("firestations");
    }
}
