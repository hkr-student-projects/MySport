package model.Database;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDAO {

    /*
    create table userlogon (
    -> userid integer not null auto_increment,
    -> email varchar(255),
    -> password blob not null,
    -> primary key (userid)
    -> );
     */


    private static final String url = "jdbc:mysql://127.0.0.1:3306/employee?user=root&password=root&serverTimezone=Europe/Stockholm&autoReconnect=true&useSSL=false";
    private static final String user = "root";
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
    public User login(String email, String password){
        User user = null;
        String sql = "SELECT * FROM userlogon WHERE email = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                String passwordLocal = rs.getString("password");
                if(checkPass(password, passwordLocal)) {
                    int ssn = rs.getInt("userid");
                    String emailLocal = rs.getString("email");

                    user = new User();
                    user.setId(ssn);
                    user.setEmail(emailLocal);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public boolean emailExists(String email){
        String sql = "SELECT * FROM userlogon WHERE email = ?";
        boolean exists = false;
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                exists = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    public void save(User user){
        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = " insert into userlogon (email, password)"
                    + " values (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, user.getEmail());
            preparedStmt.setString (2, hashPassword(user.getPassword()));

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

    public void saveToken(String email, String token, Date futureDate){
        try
        {
            Connection conn = this.connect();

            // the mysql insert statement
            String query = " insert into resetpwtoken (email, token, futuredate3)"
                    + " values (?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, email);
            preparedStmt.setString (2, token);
            Timestamp toSave = new Timestamp(futureDate.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
            System.out.println("To be saved " + simpleDateFormat.format(toSave));
            preparedStmt.setTimestamp (3, toSave);
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

    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)){
            System.out.println("The password matches.");
            return true;
        }
        else{
            System.out.println("The password does not match.");
            return false;
        }
    }


    // check DB for email, code and time matching for the reset code
    public String checkResetToken(String token){
        String sql = "SELECT * FROM resetpwtoken WHERE token = ?";
        String email = null;
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();
            if ( rs.next() ){
                Timestamp future = rs.getTimestamp("futuredate3");
                Date current = new java.sql.Date(new Date().getTime());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                System.out.println(simpleDateFormat.format(current));
                System.out.println(simpleDateFormat.format(future));
                if(current.before(future)){
                    email = rs.getString("email");
                }
                else {
                    System.out.println("Reset token is late");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return email;
    }
    //update password for user email
    public void updateUserPassword(String email, String password){
        String sql = "UPDATE userlogon SET password = ? where email = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hashPassword(password));
            pstmt.setString(2, email);
            int rs = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
