package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.entities.*;
import net.safety.alerts.safetynet.repositories.MedicalRecordRepository;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import net.safety.alerts.safetynet.responses.childalert.ChildAlertResponse;
import net.safety.alerts.safetynet.responses.personinfo.PersonInfoResponse;
import net.safety.alerts.safetynet.responses.childalert.ChildPerson;
import net.safety.alerts.safetynet.responses.childalert.FamilyPerson;
import net.safety.alerts.safetynet.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @GetMapping("/persons")
    public List<PersonEntity> getPersons() {
        return personRepository.getAll();
    }

    @GetMapping("/communityEmail")
    public List<String> communityEmail(
            @RequestParam(name = "city", required = true) String city
    ) {
        List<PersonEntity> persons = personRepository.getByCity(city);

        List<String> emails = new ArrayList<>();
        for(PersonEntity e : persons)
            emails.add(e.getEmail());

        return emails;
    }

    @GetMapping("/person")
    public PersonEntity getPerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personRepository.getByName(firstName, lastName);

        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        return person;
    }

    @DeleteMapping("/person")
    public ResponseEntity deletePerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personRepository.getByName(firstName, lastName);

        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        boolean removed = personRepository.remove(person);

        if(!removed) throw new EntityNotFoundException(PersonEntity.class.getName());

        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/person")
    public PersonEntity updatePerson(
            @RequestBody PersonEntity personBody
    ) throws EntityNotFoundException, EntityUpdateException {
        String firstName = personBody.getFirstName();
        String lastName = personBody.getLastName();

        if(firstName == null) throw new EntityUpdateException(PersonEntity.class.getName(), "firstName");
        else if(lastName == null) throw new EntityUpdateException(PersonEntity.class.getName(), "lastName");

        PersonEntity personEntity = personRepository.getByName(firstName, lastName);

        if(personEntity == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        personEntity.update(personBody);

        return personEntity;
    }

    @PostMapping("/person")
    public PersonEntity createPerson(
            @RequestBody PersonEntity person
    ) throws EntityUpdateException, EntityAlreadyExistsException, EntityInsertException {
        String errorField = this.checkPersonBody(person);

        if(errorField != null) throw new EntityUpdateException(FireStationEntity.class.getName(), errorField);

        if(personRepository.getByName(person.getFirstName(), person.getLastName()) != null)
            throw new EntityAlreadyExistsException(PersonEntity.class.getName());

        boolean inserted = personRepository.insert(person);

        if(!inserted) throw new EntityInsertException(PersonEntity.class.getName());

        return person;
    }

    @GetMapping("/personInfo")
    public PersonInfoResponse getPersonInfo(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personRepository.getByName(firstName, lastName);
        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        MedicalRecordEntity medicalRecord = medicalRecordRepository.getPersonMedicalRecord(firstName, lastName);
        if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        return new PersonInfoResponse(person, medicalRecord);
    }

    @GetMapping("/childAlert")
    public ChildAlertResponse getChilds(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        List<PersonEntity> persons = personRepository.getByAddress(address);

        if(persons.isEmpty()) throw new EntityNotFoundException(PersonEntity.class.getName());

        List<ChildPerson> childs = new ArrayList<>();
        List<FamilyPerson> familyMembers = new ArrayList<>();

        for(PersonEntity person : persons) {
            MedicalRecordEntity medicalRecord = medicalRecordRepository.getPersonMedicalRecord(person.getFirstName(), person.getLastName());

            if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

            int personAge = DateUtils.getDateYearsDiff(
                    DateUtils.convertStrDateToDate(medicalRecord.getBirthdate())
            );

            if(personAge < 18) {
                childs.add(new ChildPerson(
                        person.getFirstName(),
                        person.getLastName(),
                        personAge
                ));
            }
            else {
                familyMembers.add(new FamilyPerson(
                    person.getFirstName(),
                    person.getLastName()
                ));
            }
        }

        return new ChildAlertResponse(childs, familyMembers);
    }

    private String checkPersonBody(PersonEntity person) {
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
