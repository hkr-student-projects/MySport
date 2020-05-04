package model.Database;

import com.mysql.cj.xdevapi.Result;
import model.App;
import model.Logging.Logger;
import model.People.User;
import model.Tools.ArrayList;

import java.sql.*;
import java.util.Arrays;


public class DatabaseManager {

    private final String member = "`" + App.config.DatabaseName + "`.`member`";
    private final String leader = "`" + App.config.DatabaseName + "`.`leader`";
    private final String account = "`" + App.config.DatabaseName + "`.`account`";
    private final String schedule = "`" + App.config.DatabaseName + "`.`schedule`";
    private final String leader_has_activity = "`" + App.config.DatabaseName + "`.`leader_has_activity`";
    private final String activity = "`" + App.config.DatabaseName + "`.`activity`";

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch(Exception ex){
            Logger.logException(ex);
        }
    }

    public DatabaseManager(){
        checkSchema();
    }

    public void addAccount(String name, String middleName, String surname, String ssn, String email, String password, String mobile) {
        executeQuery(QueryType.UPDATE, "INSERT INTO ? (ssn, name, middlename, surname, mobile) " +
                "VALUES (?, ?, ?, ?, ?);" +
                "INSERT INTO ? (email, password) " +
                "VALUES (?, SHA2(?, 256)));",//AES better with secret key,
                new String[] { member , ssn, name, middleName, surname, mobile, account, email, password },
                Types.VARCHAR
        );
    }

    public void removeAccount(String ssn) {
        executeQuery(QueryType.UPDATE, "DELETE FROM ? WHERE ssn = ?;",
                new String[] { member, ssn }, Types.VARCHAR);
    }

    public void updateName(int id, String name) {
        executeQuery(QueryType.UPDATE, "UPDATE ? SET name = ? WHERE id = ?;",
                new Object[] { member, name, id },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updateMiddleName(int id, String middleName) {
        executeQuery(QueryType.UPDATE, "UPDATE ? SET middleName = ? WHERE id = ?;",
                new Object[] { member, middleName, id },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updateSurname(int id, String surname) {
        executeQuery(QueryType.UPDATE, "UPDATE ? SET surname = ? WHERE id = ?;",
                new Object[] { member, surname, id },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updatePhoneNumber(int id, String mobile) {
        executeQuery(QueryType.UPDATE, "UPDATE ? SET mobile = ? WHERE id = ?;",
                new Object[] { member, mobile, id },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
        );
    }

    public boolean updatePassword(int id, String oldPassword, String newPassword) throws SQLException {
        boolean matches = false;
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT 1 FROM ? WHERE id = ? AND password = SHA2(?, 256);",
                new Object[] { account, id, oldPassword },
                new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR }
        );
        if(result.next()){
            executeQuery(QueryType.UPDATE, "UPDATE ? SET password = SHA2(?, 256) WHERE id = ?;",
                    new Object[] { account, newPassword, id },
                    new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }

    public boolean updateEmail(int id, String oldEmail, String newEmail) throws SQLException {
        boolean matches = false;
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT 1 FROM ? WHERE id = ? AND email = ?;",
                new Object[] { member, id, oldEmail },
                new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR }
        );
        if(result.next()){
            executeQuery(QueryType.UPDATE, "UPDATE ? SET email = ? WHERE id = ?;",
                    new Object[] { member, newEmail, id },
                    new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }

    public User getUser(int id){
        User user = null;
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT 1 FROM ? WHERE ? IN (SELECT id FROM ?);",
                new Object[] { leader, id, leader },
                new int[] { Types.VARCHAR, Types.INTEGER }
        );
//        if(result.next())
//            us

        return user;
    }

    public boolean checkCredentials(String email, String password) throws SQLException {
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT id FROM ? WHERE email = ? AND password = SHA2(?, 256);",
                new Object[] { account, email, password },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR }
        );
        return result.next();
    }

    public void saveWeeks(byte[][] weeks){
        executeQuery(QueryType.UPDATE, "TRUNCATE ?;", new String[] { schedule }, new int[] { Types.VARCHAR });
        executeQuery(QueryType.UPDATE, "INSERT INTO " + schedule + " (week) VALUES'" + " (?)".repeat(weeks.length) + "';", weeks, Types.BINARY);
    }

    public byte[][] loadWeeks(){//fix in future
        ArrayList<byte[]> bytes = new ArrayList<>(8);
        try (Connection conn = createConnection()){
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT week FROM " + schedule + ";"
            );
            boolean isResult = pst.execute();
            do {
                try (ResultSet rs = pst.getResultSet()) {
                    while (rs.next()) {
                        Blob blob = rs.getBlob("week"); // creates the blob object from the result
                        byte[] week = blob.getBytes(1L, (int)blob.length());
                        bytes.add(week);
                        blob.free();
                    }
                    isResult = pst.getMoreResults();
                }
            }
            while (isResult);
        }
        catch(Exception ex){
            Logger.logException(ex);
        }
        byte[][] weeks = new byte[bytes.size()][];
        System.arraycopy(bytes.getContents(), 0, weeks, 0, bytes.size());

        return weeks;
    }
    
    public void checkSchema()
    {
        try {
            executeQuery(QueryType.BOOL,
                    "CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `ssn` CHAR(13) NOT NULL,\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  `middlename` VARCHAR(45) NULL,\n" +
                            "  `surname` VARCHAR(45) NOT NULL,\n" +
                            "  `mobile` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`));\n" +
                            "CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `email` VARCHAR(128) NOT NULL,\n" +
                            "  `password` VARCHAR(128) NOT NULL,\n" +
                            "  INDEX `fk_account_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_account_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES ? (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `keyNumber` VARCHAR(25) NOT NULL,\n" +
                            "  `boardPosition` VARCHAR(45) NOT NULL,\n" +
                            "  INDEX `fk_leader_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_leader_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES ? (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`name`));\n" +
                            "  CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `week` BLOB NOT NULL,\n" +
                            "  PRIMARY KEY (`id`));" +
                            "  CREATE TABLE IF NOT EXISTS ? (\n" +
                            "  `leader_member_id` INT NOT NULL,\n" +
                            "  `activity_name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`leader_member_id`, `activity_name`),\n" +
                            "  INDEX `fk_leader_has_activity_activity_idx` (`activity_name` ASC),\n" +
                            "  INDEX `fk_leader_has_activity_leader_idx` (`leader_member_id` ASC),\n" +
                            "  CONSTRAINT `fk_leader_has_activity_leader`\n" +
                            "    FOREIGN KEY (`leader_member_id`)\n" +
                            "    REFERENCES ? (`member_id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE,\n" +
                            "  CONSTRAINT `fk_leader_has_activity_activity`\n" +
                            "    FOREIGN KEY (`activity_name`)\n" +
                            "    REFERENCES ? (`name`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION);",
                    new String[] { member, account, member, leader, member, activity, schedule, leader_has_activity, leader, activity },
                    Types.VARCHAR
            );
        }
        catch (Exception ex){
            Logger.logException(ex, "Errors in query.");
        }
    }
    //STRICT ORDER
    public Object executeQuery(QueryType type, String query, Object[] parameters, int commonType){//java.sql.Types
        int[] types = new int[parameters.length];
        Arrays.fill(types, commonType);

        return executeQuery(type, query, parameters, types);
    }
    //STRICT ORDER
    public Object executeQuery(QueryType type, String query, Object[] parameters, int[] types)//java.sql.Types
    {
        // This method is to reduce the amount of copy paste that there was within this class.
        if(parameters.length != types.length)
            throw new IllegalArgumentException("executeQuery(): parameters length is different to types length.");
        Object result = null;
        try(PreparedStatement command = createConnection().prepareStatement(query))
        {
            for(int i = 0; i < parameters.length; i++)
                command.setObject(i + 1, parameters[i], types[i]);
            result = type == QueryType.READER ? command.executeQuery() : type == QueryType.STATEMENT ? command : command.execute();// ?? open connection
        }
        catch (SQLException ex)
        {
            Logger.logException(ex);
        }

        return result;
    }

    public static Connection createConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(App.config.DatabaseAddress, App.config.DatabaseUsername, App.config.DatabasePassword);
        } catch (SQLException ex) {
            Logger.logException(ex);
        }

        return conn;
    }

    public enum QueryType {
        UPDATE,//INSERT, UPDATE, DELETE, CREATE TABLE, DROP TABLE
        READER,//SELECT -> ResultSet
        STATEMENT,
        BOOL//ALL -> SELECT ? True : False
    }

//    public enum AccountType { //there might be more people of different occupations in DB
//
//        MEMBER,
//        LEADER,
//        ADMIN,
//        ACCOUNTANT,
//        NONE;
//
//        int id;
//        String name;
//        String middlename;
//        String surname;
//        String ssn;
//        String mobile;
//
//        AccountType() { }
//        AccountType(int id, String name, String middename, String surname, String ssn, String mobile){
//            this.id = id;
//            this.name = name;
//            this.middlename = middename;
//            this.surname = surname;
//            this.ssn = ssn;
//            this.mobile = mobile;
//        }
//    }
}