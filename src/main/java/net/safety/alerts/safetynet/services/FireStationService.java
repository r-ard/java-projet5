package net.safety.alerts.safetynet.services;

import net.safety.alerts.safetynet.dtos.fireresident.FireResidentPersonDto;
import net.safety.alerts.safetynet.dtos.floodstation.FloodStationItemDto;
import net.safety.alerts.safetynet.dtos.personinfo.PersonInfoResponseDto;
import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.repositories.FireStationRepository;
import net.safety.alerts.safetynet.utils.AbstractService;
import net.safety.alerts.safetynet.utils.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService extends AbstractService<FireStationEntity> {
    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Override
    protected IRepository<FireStationEntity> getRepository() {
        return this.fireStationRepository;
    }

    public FireStationService() {
        super();
    }

    @Override
    public boolean insert(FireStationEntity entity) {
        if(FireStationService.checkFirestationData(entity) != null)
            return false;

        return super.insert(entity);
    }

    public FireStationEntity getByAddress(String address) {
        List<FireStationEntity> stations = this.getAll();

        for(FireStationEntity station : stations)
            if(station.getAddress().equals(address)) return station;

        return null;
    }

    public List<FireStationEntity> getByStation(String stationNumber) {
        List<FireStationEntity> stations = this.getAll();

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

    public List<FireResidentPersonDto> getFireResidents(String address) {
        List<FireResidentPersonDto> outResidents = new ArrayList<>();

        FireStationEntity fireStation = this.getByAddress(address);
        if(fireStation == null) return outResidents;

        List<PersonEntity> residents = personService.getByAddress(address);
        for(PersonEntity resident : residents) {
            MedicalRecordEntity medicalRecord = medicalRecordService.getByName(resident.getFirstName(), resident.getLastName());
            if(medicalRecord == null) continue;

            outResidents.add(new FireResidentPersonDto(
                    fireStation.getStation(),
                    resident,
                    medicalRecord
            ));
        }

        return outResidents;
    }

    public List<String> getStationPhoneNumbers(String station) {
        List<String> phoneNumbers = new ArrayList<>();

        // Fetch fire stations of station number
        List<FireStationEntity> fireStations = this.getByStation(station);
        if(fireStations.isEmpty()) return phoneNumbers;

        // Loop through fire stations
        for(FireStationEntity fireStation : fireStations) {

            // Fetch fire station's address residents
            List<PersonEntity> residents = personService.getByAddress(fireStation.getAddress());
            // Loop through fire station address residents
            for(PersonEntity resident : residents) {
                String residentPhone = resident.getPhone();
                if(phoneNumbers.contains(residentPhone)) continue;

                // Append resident's phone number
                phoneNumbers.add(residentPhone);
            }
        }

        return phoneNumbers;
    }

    public List<FloodStationItemDto> getFloodStation(String station) {
        List<FloodStationItemDto> outData = new ArrayList<>();

        {
            // Fetch station number's addresses
            List<String> stationAddresses = this.getStationAddresses(station);
            // Loop through station number's addresses
            for(String stationAddress : stationAddresses) {
                // List object of address residents
                List<PersonInfoResponseDto> addressData = new ArrayList<>();

                // Fetch address residents
                List<PersonEntity> addressResidents = personService.getByAddress(stationAddress);
                // Loop through address residents
                for(PersonEntity resident : addressResidents) {
                    // Fetch resident's medical record
                    MedicalRecordEntity medicalRecord = medicalRecordService.getByName(resident.getFirstName(), resident.getLastName());
                    if(medicalRecord == null) continue;

                    // Append resident info data to address data List
                    addressData.add(new PersonInfoResponseDto(
                            resident,
                            medicalRecord
                    ));
                }

                // Append address data to station data List
                outData.add(new FloodStationItemDto(
                        stationAddress,
                        addressData
                ));
            }
        }

        return outData;
    }

    public static String checkFirestationData(FireStationEntity fireStation) {
        if(fireStation.getAddress() == null) return "address";
        if(fireStation.getStation() == null) return "station";

        return null;
    }
}
