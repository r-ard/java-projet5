package net.safety.alerts.safetynet.dtos.childalert;

public class ChildPersonDto extends FamilyPersonDto {
    private final int age;

    public ChildPersonDto() {
        super();
        this.age = 0;
    }

    public ChildPersonDto(
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
