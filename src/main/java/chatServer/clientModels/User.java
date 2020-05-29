package chatServer.clientModels;


import java.io.Serializable;
import java.time.LocalDate;

public class User implements Cloneable, Serializable {

    private String name;
    private String surname;
    private String middlename;
    private LocalDate birthday;
    private String ssn;
    private String mobile;
    private int id;
    private static final long serialVersionUID = 6529685098267757620L;

    public User(){

    }

    public User(int id, String name, String middleName, String surname, String ssn, String phoneNumber, LocalDate birthDay){

        this.id = id;
        this.name = name;
        this.middlename = middleName;
        this.surname = surname;
        this.ssn = ssn;
        this.birthday = birthDay;
        this.mobile = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename == null ? "" : " " + middlename;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        User newUser = (User) super.clone();
        newUser.birthday = LocalDate.of(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
        return newUser;
    }

    public String getMobile() {
        return mobile;
    }

    public int getId() {
        return id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}