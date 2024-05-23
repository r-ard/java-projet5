package net.safety.alerts.safetynet.repositories;

import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository extends JsonRepository<PersonEntity> {
    public PersonRepository() throws NullDatabaseException, org.json.JSONException {
        super(PersonEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("persons");
    }
}
