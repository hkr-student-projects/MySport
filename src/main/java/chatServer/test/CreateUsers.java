package chatServer.test;



import chatServer.clientModels.User;
import chatServer.repository.ChatDAO;
import java.time.LocalDate;

public class CreateUsers {

    public static void main(String[] args) {
        ChatDAO chatDAO = new ChatDAO();

        //User(int id, String name, String middleName, String surname, String ssn, String phoneNumber, LocalDate birthDay)
        User user = new User(4, "Anna", "E.", "Patrik", "950702-0834", "+076-240-80-22",
                LocalDate.of(1995,07,02));
        int member_id = chatDAO.insertMember(user);
        System.out.println("Member ID is " + member_id);
        chatDAO.insertAccount(member_id, "anna@yahoo.com", "anna123");



    }
}
