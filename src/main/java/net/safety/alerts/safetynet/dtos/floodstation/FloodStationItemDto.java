package net.safety.alerts.safetynet.dtos.floodstation;

import net.safety.alerts.safetynet.dtos.personinfo.PersonInfoResponseDto;

import java.util.List;

public class FloodStationItemDto {
    private final String address;

    private final List<PersonInfoResponseDto> residents;

    public FloodStationItemDto() {
        this.address = null;
        this.residents = null;
    }

    public FloodStationItemDto(
            String address,
            List<PersonInfoResponseDto> residents
    ) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public List<PersonInfoResponseDto> getResidents() {
        return residents;
    }
}
