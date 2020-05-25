package model.Server.repository;



import model.Server.models.Conversation;
import model.Server.models.Message;
import model.Server.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ChatDAO {

     /*
    create table userlogon (
    -> userid integer not null auto_increment,
    -> email varchar(255),
    -> password blob not null,
    -> primary key (userid)
    -> );
    DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
    /*!40101 SET character_set_client = utf8 */;
  /*  CREATE TABLE `user` (
            `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `middlename` varchar(45) NOT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `ssn` char(13) NOT NULL,
  `mobile` varchar(25) NOT NULL,
  `birthday` date NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8; */
    /*!40101 SET character_set_client = @saved_cs_client */;
/*
    user
    user_conversation
    CREATE TABLE `user_conversation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `conversation_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
)
    conversation
    CREATE TABLE `conversation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
)
    conversation_message
  CREATE TABLE `conversation_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
)
    message
    CREATE TABLE `message` (
       `id` int(11) NOT NULL AUTO_INCREMENT,
       `fromMobile` varchar(45) NOT NULL,
       `toMobile` varchar(45) NOT NULL,
       `message` varchar(1000) NOT NULL,
       `timestamp` timestamp NOT NULL,
        PRIMARY KEY (`id`)
  }

  sendMessage
    insert message into DB and get its ID
    insert new conversation_message into DB
    return to Chatmediator
        Look for Users who are part of this conversation from DB
        For each User found, search the active listeners Map for Listeners mapped by mobileNumber
        call receiveMessage method on them
  addListener
    receive mobileNumber and Remotelistener
    put key and value into Map
  newConversation
    insert message into DB and get its ID
    create new conversation and get its ID
    insert new conversation_message into DB
    Look for User whose mobileNumber is fromID
    insert new user_conversation into DB
    Look for User whose mobileNumber is toID
    insert new user_conversation into DB
    return to Chatmediator
        Look for RemoteListener mapped by mobileNumber in fromID
        call receiveMessage method on it
        Look for RemoteListener mapped by mobileNumber in toID
        call receiveMessage method on it
  loadConversations
    Search for User whose mobileNumber is userID supplied
    Search user_conversation where userid = User.id
    For each result, load Conversation where id is same
    return conversations to ChatMediator
        Look for RemoteListener mapped by mobileNumber in userID
        call receiveConversations
  loadTeamMembersContacts
    load all users in the DB
    return conversations to ChatMediator
        Look for RemoteListener mapped by mobileNumber in userID
        call receiveUsers


     */

    // JDBC URL, username and password of MySQL server
    private static final String url = "";
    private static final String user = "";
    private static final String password = "";
    // JDBC variables for opening and managing connection
    private static Connection connect;

    public Connection connect(){
        Connection connect = null;
        // opening database connection to MySQL server
        try {
            connect = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }

    public ChatDAO(){
        connect();
    }

    public int insertMessage(Message message){
        //insert message into DB and get its ID
        int generatedKey = 0;
        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = " insert into message (fromMobile, toMobile, message, timestamp)"
                    + " values (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, message.getFromMobile());
            preparedStmt.setString (2, message.getToMobile());
            preparedStmt.setString (3, message.getMessage());
            LocalDateTime now = LocalDateTime.now();
            preparedStmt.setTimestamp (4, Timestamp.valueOf(now));

            // execute the preparedstatement
            preparedStmt.execute();

            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            System.out.println("Inserted record's ID: " + generatedKey);

            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return generatedKey;
    }


    public int insertUser(User user){
        //insert message into DB and get its ID
        int generatedKey = 0;
        try
        {
            Connection conn = this.connect();
            // the mysql insert statement
            String query = " insert into user (name, middlename, surname, ssn, mobile, birthday)"
                    + " values (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, user.getName());
            preparedStmt.setString (2, user.getMiddlename());
            preparedStmt.setString (3, user.getSurname());
            preparedStmt.setString (4, user.getSsn());
            preparedStmt.setString (5, user.getMobile());

            preparedStmt.setDate (6, Date.valueOf(user.getBirthday()));
            // execute the preparedstatement
            preparedStmt.execute();

            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            System.out.println("Inserted record's ID: " + generatedKey);

            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return generatedKey;
    }

    public void insertConversationMessage(int message_id, int conversation_id){
        //insert new conversation_message into DB

        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = "insert into conversation_message (conversation_id, message_id)"
                    + " values (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, conversation_id);
            preparedStmt.setInt (2, message_id);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public List<User> getUsersInConversation(int conversation_id){
        //Look for Users who are part of this conversation from DB

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user_conversation WHERE conversation_id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                int userid = rs.getInt("user_id");
                User user = getUser(userid);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public int createNewConversation(){
        //create new conversation and get its ID
        int generatedKey = 0;
        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = "insert into conversation ()"
                    + " values ()";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);

            // execute the preparedstatement
            preparedStmt.execute();

            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            System.out.println("Inserted record's ID: " + generatedKey);

            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return generatedKey;
    }

    public User getUser(String mobileNumber){
        //Look for User whose mobileNumber is mobileNumber
        User user = null;
        String sql = "SELECT * FROM user WHERE mobile = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mobileNumber);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                user = new User();
                user.setId(id);
                user.setName(name);
                user.setMobile(mobile);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public User getUser(int userid){
        //Look for User whose id is same
        User user = null;
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                user = new User();
                user.setId(id);
                user.setName(name);
                user.setMobile(mobile);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void insertUserConversation(int user_id, int conversation_id){
        //insert new user_conversation into DB

        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = "insert into user_conversation (user_id, conversation_id)"
                    + " values (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, user_id);
            preparedStmt.setInt (2, conversation_id);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

    public List<Conversation> getConversationsForUser(int user_id){
        //Search user_conversation where userid = User.id
        List<Conversation> conversations = new ArrayList<>();
        String sql = "SELECT * FROM user_conversation WHERE user_id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                int conversation_id = rs.getInt("conversation_id");
                Conversation conversation = getConversationByID(conversation_id);
                conversations.add(conversation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conversations;
    }

    private Conversation getConversationByID(int conversation_id){
        Conversation conversation = null;
        String sql = "SELECT * FROM conversation WHERE id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                conversation = new Conversation();
                conversation.setId(id);
                List<Message> messages = getMessagesForConversation(id);
                conversation.setMessageList(messages);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conversation;
    }

    private List<Message> getMessagesForConversation(int conversation_id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM conversation_message WHERE conversation_id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                String message = rs.getString("message");
                Message messageLocal = new Message();
                messageLocal.setMessage(message);

                messages.add(messageLocal);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setMobile(mobile);

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
}
