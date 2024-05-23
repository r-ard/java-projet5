package net.safety.alerts.safetynet.dtos.childalert;

public class FamilyPersonDto {
    private final String firstName;
    private final String lastName;

    public FamilyPersonDto() {
        this.firstName = null;
        this.lastName = null;
    }

    public FamilyPersonDto(
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
