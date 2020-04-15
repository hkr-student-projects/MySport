package model;

import java.util.Date;

public class Admin extends User {

    public Admin(String name, String middleName, String surname, String ssn, Date birthDay, String password, String eMail) {
        super(name, middleName, surname, ssn, birthDay, password, eMail);
    }
}
