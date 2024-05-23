package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.dtos.personinfo.PersonInfoResponseDto;
import net.safety.alerts.safetynet.entities.FireStationEntity;
import net.safety.alerts.safetynet.entities.MedicalRecordEntity;
import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.entities.*;
import net.safety.alerts.safetynet.dtos.childalert.ChildAlertDto;
import net.safety.alerts.safetynet.dtos.childalert.ChildPersonDto;
import net.safety.alerts.safetynet.dtos.childalert.FamilyPersonDto;
import net.safety.alerts.safetynet.services.MedicalRecordService;
import net.safety.alerts.safetynet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private MedicalRecordService medicalService;

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<PersonEntity> getPersons() {
        return personService.getAll();
    }

    @GetMapping("/communityEmail")
    public List<String> communityEmail(
            @RequestParam(name = "city", required = true) String city
    ) throws EntityNotFoundException {
        List<String> emails = personService.getEmailsOfCityResidents(city);

        if(emails.isEmpty())
            throw new EntityNotFoundException(PersonEntity.class.getName());

        return emails;
    }

    @GetMapping("/person")
    public PersonEntity getPerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personService.getByName(firstName, lastName);

        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        return person;
    }

    @DeleteMapping("/person")
    public ResponseEntity deletePerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personService.getByName(firstName, lastName);

        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        boolean removed = personService.remove(person);

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

        PersonEntity personEntity = personService.getByName(firstName, lastName);
        if(personEntity == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        personEntity.update(personBody);

        return personEntity;
    }

    @PostMapping("/person")
    public PersonEntity createPerson(
            @RequestBody PersonEntity person
    ) throws EntityUpdateException, EntityAlreadyExistsException, EntityInsertException {
        String errorField = PersonService.checkPersonData(person);
        if(errorField != null) throw new EntityUpdateException(FireStationEntity.class.getName(), errorField);

        if(personService.getByName(person.getFirstName(), person.getLastName()) != null)
            throw new EntityAlreadyExistsException(PersonEntity.class.getName());

        boolean inserted = personService.insert(person);
        if(!inserted) throw new EntityInsertException(PersonEntity.class.getName());

        return person;
    }

    @GetMapping("/personInfo")
    public PersonInfoResponseDto getPersonInfo(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) throws EntityNotFoundException {
        PersonEntity person = personService.getByName(firstName, lastName);
        if(person == null) throw new EntityNotFoundException(PersonEntity.class.getName());

        MedicalRecordEntity medicalRecord = medicalService.getByName(firstName, lastName);
        if(medicalRecord == null) throw new EntityNotFoundException(MedicalRecordEntity.class.getName());

        return new PersonInfoResponseDto(person, medicalRecord);
    }

    @GetMapping("/childAlert")
    public ChildAlertDto getChilds(
            @RequestParam(name = "address", required = true) String address
    ) throws EntityNotFoundException {
        List<ChildPersonDto> childs = personService.getAddressChilds(address);
        List<FamilyPersonDto> adults = personService.getAddressAdults(address);

        if(childs.isEmpty() && adults.isEmpty()) throw new EntityNotFoundException(PersonEntity.class.getName());

        return new ChildAlertDto(childs, adults);
    }
}
