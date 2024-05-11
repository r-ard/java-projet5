package net.safety.alerts.safetynet.responses.floodstation;

import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;

import java.util.List;

public class FloodStationItem {
    private final String address;

    private final List<PersonInfoResponse> residents;

    public FloodStationItem() {
        this.address = null;
        this.residents = null;
    }

    public FloodStationItem(
            String address,
            List<PersonInfoResponse> residents
    ) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public List<PersonInfoResponse> getResidents() {
        return residents;
    }
}
