package net.safety.alerts.safetynet.repositories;

import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository extends JsonRepository<PersonEntity> {
    public PersonRepository() throws NullDatabaseException, org.json.JSONException {
        super(PersonEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("persons");
    }

    public List<PersonEntity> getByCity(String city) {
        List<PersonEntity> persons = this.getEntities();

        List<PersonEntity> outPersons = new ArrayList<>();

        for(PersonEntity e : persons) {
            if(e.getCity().equals(city)) outPersons.add(e);
        }

        return outPersons;
    }

    public List<PersonEntity> getByAddress(String address) {
        List<PersonEntity> persons = this.getEntities();

        List<PersonEntity> outPersons = new ArrayList<>();

        for(PersonEntity e : persons) {
            if(e.getAddress().equals(address)) outPersons.add(e);
        }

        return outPersons;
    }

    public PersonEntity getByName(String firstName, String lastName) {
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();

        List<PersonEntity> persons = this.getEntities();

        for(PersonEntity e : persons) {
            if(e.getFirstName().toLowerCase().equals(firstName) && e.getLastName().toLowerCase().equals(lastName)) return e;
        }

        return null;
    }
}
