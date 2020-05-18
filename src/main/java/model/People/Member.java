package model.People;

import java.time.LocalDate;
import java.util.Date;

public class Member extends User implements Cloneable {

    public Member(int id, String name, String middleName, String surname, String ssn, String mobile, LocalDate birthDay) {
        super(id, name, middleName, surname, ssn, mobile, birthDay);
    }

    public Member(Leader leader){
        this(leader.getId(), leader.getName(), leader.getMiddlename(), leader.getSurname(), leader.getSsn(), leader.getMobile(), LocalDate.of(leader.getBirthday().getYear(), leader.getBirthday().getMonth(), leader.getBirthday().getDayOfMonth()));
    }

    @Override
    protected Member clone() throws CloneNotSupportedException {
        return (Member) super.clone();
    }

    public Leader toLeader(String keyNumber, String boardPosition, String... leaderOf){
        return new Leader(this, keyNumber, boardPosition, leaderOf);
    }
}
