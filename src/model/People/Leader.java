package model.People;

import java.util.Date;

public class Leader extends Member {

    private String keyNumber;
    private String position;
    private String[] leaderOf;

    public Leader(int id, String name, String middleName, String surname, String ssn, Date birthDay, String mobile, String keyNumber, String boardPosition, String... leaderOf) {
        super(id, name, middleName, surname, ssn, birthDay, mobile);

        this.keyNumber = keyNumber;
        this.position = boardPosition;
        this.leaderOf = leaderOf;
    }

    public Leader(Member member, String keyNumber, String boardPosition, String... leaderOf){
        this(member.getId(), member.getName(), member.getMiddlename(), member.getSurname(), member.getSsn(), (Date) member.getBirthday().clone(), member.getMobile(), keyNumber, boardPosition, leaderOf);
    }

    public Member toMember(){
        return new Member(this);
    }

    public String getKeyNumber() { return keyNumber; }

    public String getPosition() { return position; }

    public String[] getLeaderOf() { return leaderOf; }
}
