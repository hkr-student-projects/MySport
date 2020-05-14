package model.People;

import java.time.LocalDate;
import java.util.Date;

public class Leader extends Member {

    private String keyNumber;
    private String position;
    private String[] leaderOf;

    public Leader(int id, String name, String middleName, String surname, String ssn, String mobile, LocalDate birthDay, String keyNumber, String boardPosition, String... leaderOf) {
        super(id, name, middleName, surname, ssn, mobile, birthDay);

        this.keyNumber = keyNumber;
        this.position = boardPosition;
        this.leaderOf = leaderOf;
    }

    public Leader(Member member, String keyNumber, String boardPosition, String... leaderOf){
        this(member.getId(), member.getName(), member.getMiddlename(), member.getSurname(), member.getSsn(), member.getMobile(), LocalDate.of(member.getBirthday().getYear(), member.getBirthday().getMonth(), member.getBirthday().getDayOfMonth()), keyNumber, boardPosition, leaderOf);
    }

    public Member toMember(){
        return new Member(this);
    }

    public String getKeyNumber() { return keyNumber; }

    public String getPosition() { return position; }

    public String[] getLeaderOf() { return leaderOf; }
}
