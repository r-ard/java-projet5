package net.safety.alerts.safetynet.responses.childalert;

public class FamilyPerson {
    private final String firstName;
    private final String lastName;

    public FamilyPerson() {
        this.firstName = null;
        this.lastName = null;
    }

    public FamilyPerson(
            String firstName,
            String lastName
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
