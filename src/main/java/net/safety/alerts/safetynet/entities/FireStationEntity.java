package net.safety.alerts.safetynet.entities;

import net.safety.alerts.safetynet.exceptions.entities.EntityUpdateException;
import net.safety.alerts.safetynet.utils.IEntity;
import org.json.JSONObject;

public class FireStationEntity implements IEntity<FireStationEntity> {
    
    private String address;

    private String station;

    public FireStationEntity() {
        this.address = null;
        this.station = null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public static FireStationEntity fromJsonObject(JSONObject object) {
        FireStationEntity fireStation = new FireStationEntity();

        fireStation.setAddress( object.getString("address") );
        fireStation.setStation( object.getString("station") );

        return fireStation;
    }

    @Override
    public void update(FireStationEntity data) throws EntityUpdateException {
        if(data.getStation() != null) this.setStation(data.getStation());
    }
}
