package net.safety.alerts.safetynet.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    ObjectMapper mapper;

    public FireStationRepository() throws NullDatabaseException, org.json.JSONException {
        super(FireStationEntity.class);
    }

    protected JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException {
        return object.getJSONArray("firestations");
    }

    public FireStationEntity getByAddress(String address) {
        List<FireStationEntity> stations = this.getEntities();

        for(FireStationEntity station : stations)
            if(station.getAddress().equals(address)) return station;

        return null;
    }

    public List<FireStationEntity> getByStation(String stationNumber) {
        List<FireStationEntity> stations = this.getEntities();

        List<FireStationEntity> outStations = new ArrayList<>();

        for(FireStationEntity station : stations)
            if(station.getStation().equals(stationNumber)) outStations.add(station);

        return outStations;
    }

    public List<String> getStationAddresses(String stationNumber) {
        List<String> outAddresses = new ArrayList<>();

        List<FireStationEntity> fireStations = this.getByStation(stationNumber);
        for(FireStationEntity fireStation : fireStations) {
            outAddresses.add(fireStation.getAddress());
        }

        return outAddresses;
    }
}
