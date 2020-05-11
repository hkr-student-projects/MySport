package model.People;

import java.time.LocalDate;
import java.util.Date;

public abstract class User implements Cloneable {

    private String name;
    private String surname;
    private String middlename;
    private LocalDate birthday;
    private String ssn;
    private String mobile;
    private int id;

    protected User(int id, String name, String middleName, String surname, String ssn, String phoneNumber, LocalDate birthDay){

        this.id = id;
        this.name = name;
        this.middlename = middleName;
        this.surname = surname;
        this.ssn = ssn;
        this.birthday = birthDay;
        this.mobile = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename == null ? "" : " " + middlename;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        User newUser = (User) super.clone();
        newUser.birthday = LocalDate.of(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
        return newUser;
    }

    public String getMobile() {
        return mobile;
    }

    public int getId() {
        return id;
    }

}
