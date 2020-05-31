package chatServer.repository;



import chatServer.clientModels.Conversation;
import chatServer.clientModels.Message;
import chatServer.clientModels.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final String url = "jdbc:mysql://sql3.freemysqlhosting.net/sql3344107?&allowMultiQueries=true&serverTimezone=Europe/Stockholm&useSSL=true";
    private static final String user = "sql3344107";
    private static final String password = "xhLWE9ssu3";
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
            String query = " insert into `message` (fromMobile, toMobile, message, timestamp)"
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


    public void insertAccount(int member_id, String email, String password){
        //insert new conversation_message into DB
        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = "insert into `account` (member_id, email, password)"
                    + " values (?, ?, SHA2(?, 256))";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, member_id);
            preparedStmt.setString (2, email);
            preparedStmt.setString (3, password);

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

    public int insertMember(User user){
        //insert message into DB and get its ID
        int generatedKey = 0;
        try
        {
            Connection conn = this.connect();
            // the mysql insert statement
            String query = "insert into `member`(name, middlename, surname, ssn, mobile, birthday)"
                    + " values(?, ?, ?, ?, ?, ?)";
            //PreparedStatement pstmt = conn.prepareStatement(query);
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
            e.printStackTrace();
        }
        return generatedKey;
    }

    // a java preparedstatement example
    public void updateMemberDetails(int member_id, String email, String password) throws SQLException
    {
        try
        {
            Connection conn = this.connect();
            // create our java preparedstatement using a sql update query
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `account` SET email = ?, password = SHA2(?, 256) WHERE member_id = ?");

            // set the preparedstatement parameters
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setInt(3, member_id);

            // call executeUpdate to execute our sql update statement
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException se)
        {
            // log the exception
            throw se;
        }
    }


    public int insertUser(User user){
        //insert message into DB and get its ID
        int generatedKey = 0;
        try
        {
            Connection conn = this.connect();
            // the mysql insert statement
            String query = " insert into `user` (name, middlename, surname, ssn, mobile, birthday)"
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
            String query = "insert into `conversation_message` (conversation_id, message_id)"
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
        String sql = "SELECT * FROM `user_conversation` WHERE conversation_id = ?";
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
            String query = "insert into `conversation` ()"
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
        String sql = "SELECT * FROM `user` WHERE mobile = ?";
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
        String sql = "SELECT * FROM `user` WHERE id = ?";
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
            String query = "insert into `user_conversation` (user_id, conversation_id)"
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
        String sql = "SELECT * FROM `user_conversation` WHERE user_id = ?";
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
        String sql = "SELECT * FROM `conversation` WHERE id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                conversation = new Conversation();
                conversation.setId(id);
                List<Message> messages = getMessagesForConversation(id);
                Collections.sort(messages);
                if(messages.size() > 0) {
                    System.out.println("Top is " + messages.get(messages.size() - 1).getTimestamp());
                }
                conversation.setMessageList(messages);
                List<String> participants = fetchParticipants(conversation_id);
                conversation.setParticipants(participants);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conversation;
    }

    private List<String> fetchParticipants(int conversation_id) {
        List<String> participants = new ArrayList<>();
        String sql = "SELECT * FROM `user_conversation` WHERE conversation_id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                int user_id = rs.getInt("user_id");
                User user = getUser(user_id);
                if(user != null){
                    participants.add(user.getName());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return participants;
    }

    private List<Message> getMessagesForConversation(int conversation_id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM `conversation_message` WHERE conversation_id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, conversation_id);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                int message_id = rs.getInt("message_id");
                Message messageLocal = loadMessage(message_id);
                if(messageLocal != null) {
                    messages.add(messageLocal);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    private Message loadMessage(int message_id) {
        Message message = null;
        String sql = "SELECT * FROM `message` WHERE id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, message_id);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                int id = rs.getInt("id");
                String fromMobile = rs.getString("fromMobile");
                String toMobile = rs.getString("toMobile");
                String messageLocal = rs.getString("message");
                LocalDateTime localDate = rs.getTimestamp("timestamp").toLocalDateTime();

                message = new Message();
                message.setToMobile(toMobile);
                message.setTimestamp(localDate);
                message.setFromMobile(fromMobile);
                message.setMessage(messageLocal);
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
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

    public List<User> getAllUsersNot(String mobileNum){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");

                if(!mobile.equals(mobileNum)) {
                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    user.setMobile(mobile);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
}
