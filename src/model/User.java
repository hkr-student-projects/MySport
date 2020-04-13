package model;

import java.util.Date;

public abstract class User {

    private String name;
    private String surname;
    private String middleName;
    private Date birthDay;
    private String ssn;

    public User(String name, String middleName, String surname, String ssn, Date birthDay){

        this.name = name;
        this.middleName = middleName;
        this.surname = surname;
        this.ssn = ssn;
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getSsn() {
        return ssn;
    }
}
