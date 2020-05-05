package model.People;

import java.util.Date;

public abstract class User implements Cloneable {

    private String name;
    private String surname;
    private String middlename;
    private Date birthday;
    private String ssn;
    private String mobile;
    private int id;

    protected User(int id, String name, String middleName, String surname, String ssn, Date birthDay, String phoneNumber){

        this.id = id;
        this.name = name;
        this.middlename = middleName;
        this.surname = surname;
        this.ssn = ssn;
        this.birthday = birthDay;
        this.mobile = phoneNumber;
    }

    protected String getName() {
        return name;
    }

    protected String getSurname() {
        return surname;
    }

    protected String getMiddlename() {
        return middlename;
    }

    protected Date getBirthday() {
        return birthday;
    }

    protected String getSsn() {
        return ssn;
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        User newUser = (User) super.clone();
        newUser.birthday = (Date) birthday.clone();
        return newUser;
    }

    public String getMobile() {
        return mobile;
    }

    public int getId() {
        return id;
    }
}
