package model.People;

import java.util.Date;

public class Leader extends Member {

    private String keyNumber;
    private String position;
    private String leaderOf;

    public Leader(String name, String middleName, String surname, String ssn, Date birthDay, String eMail, String password, String phoneNumber, String keyNumber, String position, String leaderOf) {
        super(name, middleName, surname, ssn, birthDay, eMail, password, phoneNumber);

        this.keyNumber = keyNumber;
        this.position = position;
        this.leaderOf = leaderOf;
    }

    public String getKeyNumber() {return keyNumber;}

    public String getPosition() {return position;}

    public String getLeaderOf() {return leaderOf;}
}
