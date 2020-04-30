package model.People;

import java.util.Date;

public class Member extends User {

    public Member(String name, String middleName, String surname, String ssn, Date birthDay, String eMail, String password, String phoneNumber) {
        super(name, middleName, surname, ssn, birthDay, eMail, password, phoneNumber);
    }
}
