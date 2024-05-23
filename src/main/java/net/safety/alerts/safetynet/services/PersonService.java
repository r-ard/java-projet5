package net.safety.alerts.safetynet.services;

import net.safety.alerts.safetynet.dtos.childalert.ChildPersonDto;
import net.safety.alerts.safetynet.dtos.childalert.FamilyPersonDto;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import net.safety.alerts.safetynet.utils.AbstractService;
import net.safety.alerts.safetynet.utils.DateUtils;
import net.safety.alerts.safetynet.utils.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService extends AbstractService<PersonEntity> {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PersonRepository personRepository;

    @Override
    protected IRepository<PersonEntity> getRepository() {
        return this.personRepository;
    }

    public PersonService() {
        super();
    }

    @Override
    public boolean insert(PersonEntity entity) {
        if(PersonService.checkPersonData(entity) != null)
            return false;

        return super.insert(entity);
    }

    public List<String> getEmailsOfCityResidents(String city) {
        List<PersonEntity> persons = this.getByCity(city);

        List<String> outList = new ArrayList<>();

        for(PersonEntity e : persons)
            outList.add(e.getEmail());

        return outList;
    }

    public PersonEntity getByName(String firstName, String lastName) {
        List<PersonEntity> persons = this.getAll();

        for(PersonEntity e : persons) {
            if(e.getFirstName().toLowerCase().equals(firstName) && e.getLastName().toLowerCase().equals(lastName))
                return e;
        }

        return null;
    }

    public List<PersonEntity> getByCity(String city) {
        List<PersonEntity> persons = this.getAll();

        List<PersonEntity> outPersons = new ArrayList<>();

        for(PersonEntity e : persons) {
            if(e.getCity().equals(city)) outPersons.add(e);
        }

        return outPersons;
    }

    public List<PersonEntity> getByAddress(String address) {
        List<PersonEntity> persons = this.getAll();

        List<PersonEntity> outPersons = new ArrayList<>();

        for(PersonEntity e : persons) {
            if(e.getAddress().equals(address)) outPersons.add(e);
        }

        return outPersons;
    }

    public List<ChildPersonDto> getAddressChilds(String address) {
        List<PersonEntity> persons = this.getByAddress(address);

        List<ChildPersonDto> childs = new ArrayList<>();

        for(PersonEntity person : persons) {
            MedicalRecordEntity medicalRecord = medicalRecordService.getByName(person.getFirstName(), person.getLastName());

            if(medicalRecord == null) continue;

            int personAge = DateUtils.getDateYearsDiff(
                    DateUtils.convertStrDateToDate(medicalRecord.getBirthdate())
            );

            if(personAge < 18) {
                childs.add(new ChildPersonDto(
                        person.getFirstName(),
                        person.getLastName(),
                        personAge
                ));
            }
        }

        return childs;
    }

    public List<FamilyPersonDto> getAddressAdults(String address)  {
        List<PersonEntity> persons = this.getByAddress(address);

        List<FamilyPersonDto> adults = new ArrayList<>();

        for(PersonEntity person : persons) {
            MedicalRecordEntity medicalRecord = medicalRecordService.getByName(person.getFirstName(), person.getLastName());

            if(medicalRecord == null) continue;

            int personAge = DateUtils.getDateYearsDiff(
                    DateUtils.convertStrDateToDate(medicalRecord.getBirthdate())
            );

            if(personAge >= 18) {
                adults.add(new FamilyPersonDto(
                        person.getFirstName(),
                        person.getLastName()
                ));
            }
        }

        return adults;
    }

    public static String checkPersonData(PersonEntity person) {
        if(person.getFirstName() == null) return "firstName";
        else if(person.getLastName() == null) return "lastName";
        else if(person.getAddress() == null) return "address";
        else if(person.getCity() == null) return "city";
        else if(person.getEmail() == null) return "email";
        else if(person.getPhone() == null) return "phone";
        else if(person.getZip() == null) return "zip";

        return null;
    }
}
