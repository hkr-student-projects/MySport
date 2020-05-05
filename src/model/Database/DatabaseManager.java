package model.Database;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import model.App;
import model.Logging.Logger;
import model.People.Member;
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
        executeQuery(QueryType.UPDATE, "INSERT INTO "+member+" (ssn, name, middlename, surname, mobile) " +
                "VALUES (?, ?, ?, ?, ?);" +
                "INSERT INTO "+account+" (email, password) " +
                "VALUES (?, SHA2(?, 256)));",//AES better with secret key,
                new String[] { ssn, name, middleName, surname, mobile, email, password },
                Types.VARCHAR
        );
    }

    public void removeAccount(String ssn) {
        executeQuery(QueryType.UPDATE, "DELETE FROM "+member+" WHERE ssn = ?;",
                new String[] { ssn }, Types.VARCHAR);
    }

    public void updateName(int id, String name) {
        executeQuery(QueryType.UPDATE, "UPDATE "+member+" SET name = ? WHERE id = ?;",
                new Object[] { name, id },
                new int[] { Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updateMiddleName(int id, String middleName) {
        executeQuery(QueryType.UPDATE, "UPDATE "+member+" SET middleName = ? WHERE id = ?;",
                new Object[] { middleName, id },
                new int[] { Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updateSurname(int id, String surname) {
        executeQuery(QueryType.UPDATE, "UPDATE "+member+" SET surname = ? WHERE id = ?;",
                new Object[] { surname, id },
                new int[] { Types.VARCHAR, Types.INTEGER }
        );
    }

    public void updatePhoneNumber(int id, String mobile) {
        executeQuery(QueryType.UPDATE, "UPDATE "+member+" SET mobile = ? WHERE id = ?;",
                new Object[] { mobile, id },
                new int[] { Types.VARCHAR, Types.INTEGER }
        );
    }

    public boolean updatePassword(int id, String oldPassword, String newPassword) throws SQLException {
        boolean matches = false;
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT 1 FROM "+account+" WHERE member_id = ? AND password = SHA2(?, 256);",
                new Object[] { id, oldPassword },
                new int[] { Types.INTEGER, Types.VARCHAR }
        );
        if(result.next()){
            executeQuery(QueryType.UPDATE, "UPDATE "+account+" SET password = SHA2(?, 256) WHERE member_id = ?;",
                    new Object[] { newPassword, id },
                    new int[] { Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }

    public boolean updateEmail(int id, String oldEmail, String newEmail) throws SQLException {
        boolean matches = false;
        ResultSet result = (ResultSet)executeQuery(QueryType.READER,
                "SELECT 1 FROM "+member+" WHERE id = ? AND email = ?;",
                new Object[] { id, oldEmail },
                new int[] { Types.INTEGER, Types.VARCHAR }
        );
        if(result.next()){
            executeQuery(QueryType.UPDATE, "UPDATE "+member+" SET email = ? WHERE id = ?;",
                    new Object[] { newEmail, id },
                    new int[] { Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }

    public User getUser(int id) throws SQLException {
        ResultSet memberData = (ResultSet)executeQuery(QueryType.READER,
                "SELECT * FROM "+member+" WHERE id = ?;",
                new Object[] { id },
                new int[] { Types.INTEGER }
        );
        Member user = new Member(memberData.getString("name"), memberData.getString("middlename"),
                memberData.getString("surname"), memberData.getString("ssn"),
                memberData.getDate("birthday"), memberData.getString("mobile")
        );
        ResultSet leaderData = (ResultSet)executeQuery(QueryType.READER,
                "SELECT * FROM "+leader+" WHERE member_id = ?;",
                new Object[] { id },
                new int[] { Types.INTEGER }
        );
        if(leaderData.next()){
            ResultSet activities = (ResultSet)executeQuery(QueryType.READER,
                    "SELECT activity_name FROM "+leader_has_activity+" WHERE leader_member_id = ?",
                    new Object[] { id },
                    new int[] { Types.INTEGER }
            );
            ArrayList<String> sports = new ArrayList<>(5);
            while(activities.next())
                sports.add(activities.getString(1));

            user = user.toLeader(leaderData.getString("key_number"), leaderData.getString("board_position"), sports.toArray());
        }


        return user;
    }

    public int checkCredentials(String email, String password) throws SQLException {
        int id = -1;
        PreparedStatement pst = (PreparedStatement)executeQuery(QueryType.READER,
                "SELECT member_id FROM "+account+" WHERE email = ? AND password = SHA2(?, 256);",
                new Object[] { email, password },
                new int[] { Types.VARCHAR, Types.VARCHAR }
        );
        ResultSet result = pst.getResultSet();
        if(result.next())
            id = result.getInt(1);
        pst.close();
        return id;
    }

    public void saveWeeks(byte[][] weeks){//do not want to unite because of need to create new array of objects
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
                    "CREATE TABLE IF NOT EXISTS "+member+" ("+
                            "`id` INT NOT NULL AUTO_INCREMENT," +
                            "`ssn` CHAR(13) NOT NULL," +
                            "`name` VARCHAR(45) NOT NULL," +
                            "`middlename` VARCHAR(45) NULL," +
                            "`surname` VARCHAR(45) NOT NULL," +
                            "`birthday` DATE NOT NULL," +
                            "`mobile` VARCHAR(45) NOT NULL," +
                            "PRIMARY KEY (`id`));" +
                            "CREATE TABLE IF NOT EXISTS "+account+" (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `email` VARCHAR(128) NOT NULL,\n" +
                            "  `password` VARCHAR(128) NOT NULL,\n" +
                            "  INDEX `fk_account_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_account_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES "+member+" (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS "+leader+" (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `key_number` VARCHAR(25) NOT NULL,\n" +
                            "  `board_position` VARCHAR(45) NOT NULL,\n" +
                            "  INDEX `fk_leader_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_leader_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES "+member+" (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS "+activity+" (\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`name`));\n" +
                            "  CREATE TABLE IF NOT EXISTS "+schedule+" (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `week` BLOB NOT NULL,\n" +
                            "  PRIMARY KEY (`id`));" +
                            "  CREATE TABLE IF NOT EXISTS "+leader_has_activity+" (\n" +
                            "  `leader_member_id` INT NOT NULL,\n" +
                            "  `activity_name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`leader_member_id`, `activity_name`),\n" +
                            "  INDEX `fk_leader_has_activity_activity_idx` (`activity_name` ASC),\n" +
                            "  INDEX `fk_leader_has_activity_leader_idx` (`leader_member_id` ASC),\n" +
                            "  CONSTRAINT `fk_leader_has_activity_leader`\n" +
                            "    FOREIGN KEY (`leader_member_id`)\n" +
                            "    REFERENCES "+leader+" (`member_id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE,\n" +
                            "  CONSTRAINT `fk_leader_has_activity_activity`\n" +
                            "    FOREIGN KEY (`activity_name`)\n" +
                            "    REFERENCES "+activity+" (`name`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION);"
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
        try
        {
            PreparedStatement command = createConnection().prepareStatement(query);
            for(int i = 0; i < parameters.length; i++)
                command.setObject(i + 1, parameters[i], types[i]);
            //result = type == QueryType.READER ? command.executeQuery() : type == QueryType.STATEMENT ? command : command.execute();// ?? open connection
            if(type == QueryType.READER){
                command.executeQuery();
                return command;
            }
            else if(type == QueryType.UPDATE){
                return command.executeUpdate();
            }
            else
                return command.execute();
        }
        catch (SQLException ex)
        {
            Logger.logException(ex);
        }

        return result;
    }

    public Object executeQuery(QueryType type, String query)//java.sql.Types
    {
        // This method is to reduce the amount of copy paste that there was within this class.
        Object result = null;
        try(PreparedStatement command = createConnection().prepareStatement(query))
        {
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