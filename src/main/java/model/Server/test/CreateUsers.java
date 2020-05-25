package model.Server.test;



import model.Server.models.User;
import model.Server.repository.ChatDAO;

import java.time.LocalDate;

public class CreateUsers {

    public static void main(String[] args) {
        ChatDAO chatDAO = new ChatDAO();
        //User(int id, String name, String middleName, String surname, String ssn, String phoneNumber, LocalDate birthDay)
        User user = new User(1, "Brad", "", "Pitt", "123456-7890", "+123-123-12-12",
                LocalDate.of(1960,5,25));
        chatDAO.insertUser(user);
    }
}
