package model;

import java.util.Date;

public class Member extends User {

    public Member(String name, String middleName, String surname, String ssn, Date birthDay, String password, String eMail) {
        super(name, middleName, surname, ssn, birthDay, password, eMail);
    }
}
