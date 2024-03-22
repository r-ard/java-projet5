package net.safety.alerts.safetynet.controllers;

import net.safety.alerts.safetynet.entities.PersonEntity;
import net.safety.alerts.safetynet.exceptions.utils.EntityUpdateException;
import net.safety.alerts.safetynet.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/persons")
    public List<PersonEntity> getPersons() {
        return personRepository.getAll();
    }

    @GetMapping("/communityEmail")
    public List<String> communityEmail(
            @RequestParam(name = "city", required = true) String city
    ) {
        PersonEntity[] persons = personRepository.getByCity(city);

        List<String> emails = new ArrayList<>();
        for(PersonEntity e : persons)
            emails.add(e.getEmail());

        return emails;
    }

    @GetMapping("/person")
    public ResponseEntity getPerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) {
        PersonEntity person = personRepository.getByName(firstName, lastName);

        if(person == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(person);
    }

    @DeleteMapping("/person")
    public ResponseEntity deletePerson(
            @RequestParam(name = "firstName", required = true) String firstName,
            @RequestParam(name = "lastName", required = true) String lastName
    ) {
        PersonEntity person = personRepository.getByName(firstName, lastName);

        if(person == null) return ResponseEntity.notFound().build();

        boolean removed = personRepository.remove(person);

        if(!removed) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/person")
    public ResponseEntity updatePerson(
            @RequestBody PersonEntity personBody
    ) {
        String firstName = personBody.getFirstName();
        String lastName = personBody.getLastName();

        if(firstName == null) return ResponseEntity.badRequest().body("Missing firstName.");
        else if(lastName == null) return ResponseEntity.badRequest().body("Missing lastName.");

        PersonEntity personEntity = personRepository.getByName(firstName, lastName);

        if(personEntity == null) return ResponseEntity.notFound().build();

        try {
            personEntity.update(personBody);
        }
        catch(EntityUpdateException ex) {
            return ResponseEntity.internalServerError()
                    .body("Failed to update person.");
        }

        return ResponseEntity.ok()
                .body("Success");
    }

    @PostMapping("/person")
    public ResponseEntity createPerson(
            @RequestBody PersonEntity person
    ) {
        String errorField = this.checkPersonBody(person);

        if(errorField != null) {
            return ResponseEntity.badRequest()
                    .body("Missing '" + errorField + "' field.");
        }

        if(personRepository.getByName(person.getFirstName(), person.getLastName()) != null) {
            return ResponseEntity.badRequest()
                    .body("'" + person.getFirstName() + " " + person.getLastName() + "' already exists.");
        }

        boolean inserted = personRepository.insert(person);

        if(!inserted) {
            return ResponseEntity.internalServerError()
                    .body("Something went wrong while inserting the person.");
        }

        return ResponseEntity.ok()
                .body("Success");
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
