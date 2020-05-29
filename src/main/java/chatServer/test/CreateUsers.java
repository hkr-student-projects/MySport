package chatServer.test;



import chatServer.clientModels.User;
import chatServer.repository.ChatDAO;

import java.sql.SQLException;
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


     //   try {
     //       chatDAO.updateMemberDetails(4, "emma@yahoo.com", "emma123");
     //       chatDAO.updateMemberDetails(5, "marie@yahoo.com", "marie123");
     //       chatDAO.updateMemberDetails(6, "daniel@yahoo.com", "daniel123");
     //   } catch (SQLException e) {
     //       e.printStackTrace();
     //   }
        /*
        List<Conversation> users = chatDAO.getConversationsForUser(1);
        System.out.println(users.size());
        users.forEach(i -> {
            System.out.println(i.conversationName("Daniel"));
            List<Message> messages = i.getMessageList();
            messages.forEach(j -> System.out.println(j.getFromMobile() + ": " + j.getMessage()  + ": " + j.getTimestamp()));
        });

         */
    }
}
