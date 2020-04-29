package model.People;

import java.util.Date;

public class Member extends User {

    public Member(String name, String middleName, String surname, String ssn, Date birthDay) {
        super(name, middleName, surname, ssn, birthDay);
    }
}
