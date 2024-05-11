package net.safety.alerts.safetynet.responses.childalert;

public class ChildPerson extends FamilyPerson {
    private final int age;

    public ChildPerson() {
        super();
        this.age = 0;
    }

    public ChildPerson(
            String firstName,
            String lastName,
            int age
    ) {
        super(firstName, lastName);

        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
