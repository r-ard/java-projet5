package net.safety.alerts.safetynet.repositories;

import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import net.safety.alerts.safetynet.utils.JsonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository extends JsonRepository<FireStationEntity> {
    public FireStationRepository() throws NullDatabaseException, org.json.JSONException {
        super();
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("firestations");
    }

    protected FireStationEntity toEntityInstance(JSONObject object) {
        return FireStationEntity.fromJsonObject(object);
    }

    public FireStationEntity getByAddress(String address) {
        List<FireStationEntity> stations = this.getEntities();

        for(FireStationEntity station : stations)
            if(station.getAddress().equals(address)) return station;

        return null;
    }

    public FireStationEntity[] getByStation(String stationNumber) {
        List<FireStationEntity> stations = this.getEntities();

        List<FireStationEntity> outStations = new ArrayList<>();

        for(FireStationEntity station : stations)
            if(station.getStation().equals(stationNumber)) outStations.add(station);

        return outStations.toArray(new FireStationEntity[0]);
    }
}
