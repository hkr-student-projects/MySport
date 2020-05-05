package model.People;

import java.util.Date;

public class Member extends User implements Cloneable {

    public Member(String name, String middleName, String surname, String ssn, Date birthDay, String mobile) {
        super(name, middleName, surname, ssn, birthDay, mobile);
    }

    public Member(Leader leader){
        this(leader.getName(), leader.getMiddlename(), leader.getSurname(), leader.getSsn(), (Date) leader.getBirthday().clone(), leader.getMobile());
    }

    @Override
    protected Member clone() throws CloneNotSupportedException {
        return (Member) super.clone();
    }

    public Leader toLeader(String keyNumber, String boardPosition, String... leaderOf){
        return new Leader(this, keyNumber, boardPosition, leaderOf);
    }
}
