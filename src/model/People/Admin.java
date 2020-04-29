package model.People;

import java.util.Date;

public class Admin extends User {

    public Admin(String name, String middleName, String surname, String ssn, Date birthDay) {
        super(name, middleName, surname, ssn, birthDay);
    }
}
