package net.safety.alerts.safetynet.entities;

import net.safety.alerts.safetynet.exceptions.utils.EntityUpdateException;
import net.safety.alerts.safetynet.utils.IEntity;
import org.json.JSONObject;

public class PersonEntity implements IEntity<PersonEntity> {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public PersonEntity() {
        this.firstName = null;
        this.lastName = null;
        this.address = null;
        this.city = null;
        this.zip = null;
        this.phone = null;
        this.email = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static PersonEntity fromJsonObject(JSONObject object) {
        PersonEntity person = new PersonEntity();

        person.firstName = object.getString("firstName");
        person.lastName = object.getString("lastName");
        person.address = object.getString("address");
        person.city = object.getString("city");
        person.zip = object.getString("zip");
        person.phone = object.getString("phone");
        person.email = object.getString("email");

        return person;
    }

    @Override
    public void update(PersonEntity data) throws EntityUpdateException {
        if(data.getFirstName() != null) this.setFirstName(data.getFirstName());

        if(data.getLastName() != null) this.setLastName(data.getLastName());

        if(data.getEmail() != null) this.setEmail(data.getEmail());

        if(data.getCity() != null) this.setCity(data.getCity());

        if(data.getAddress() != null) this.setAddress(data.getAddress());

        if(data.getZip() != null) this.setZip(data.getZip());

        if(data.getPhone() != null) this.setPhone(data.getPhone());
    }
}
