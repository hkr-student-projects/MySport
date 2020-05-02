package model.Database;

import model.App;
import model.Logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseManager {

    private final String dbName = "sql7336945";
    private final String member = "`" + dbName + "`.`member`";
    private final String leader = "`" + dbName + "`.`leader`";
    private final String account = "`" + dbName + "`.`account`";
    private final String schedule = "`" + dbName + "`.`schedule`";
    private final String leader_has_activity = "`" + dbName + "`.`leader_has_activity`";
    private final String activity = "`" + dbName + "`.`activity`";

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

    public void addAccount(String name, String middleName, String surname, String ssn, String eMail, String password, String phoneNumber) {
        executeQuery(QueryType.UPDATE, "INSERT INTO member (ssn, name, middlename, surname, mobile) " +
                "VALUES ('" + ssn + "'," +
                " '" + name + "'," +
                " '" + middleName + "'," +
                " '" + surname + "'," +
                " '" + phoneNumber + "');" +
                "INSERT INTO account (email, password) " +
                "VALUES ('" + eMail + "', SHA2('" + password + "', 256)));"//AES better with secret key
        );
    }

    public void removeAccount(String ssn) {
        executeQuery(QueryType.UPDATE, "DELETE FROM member WHERE ssn = '" + ssn + "';");
    }

    public void updateName(String name, String ssn) {
        executeQuery(QueryType.UPDATE, "UPDATE member SET name = '" + name + "'" +
                " WHERE ssn = '" + ssn + "';");
    }

    public void updateMiddleName(String middleName, String ssn) {
        executeQuery(QueryType.UPDATE, "UPDATE member SET middlename = '" + middleName + "'" +
                " WHERE ssn = '" + ssn + "';");
    }

    public void updateSurname(String surname, String ssn) {
        executeQuery(QueryType.UPDATE, "UPDATE member SET surname = '" + surname + "'" +
                " WHERE ssn = '" + ssn + "';");
    }

    public void updatePhoneNumber(String phoneNumber, String ssn) {
        executeQuery(QueryType.UPDATE, "UPDATE member SET mobile = '" + phoneNumber + "'" +
                " WHERE ssn = '" + ssn + "';");
    }

    public void updatePassword(String password, String ssn) {//SHA ecnryption needed SHA1()
        executeQuery(QueryType.UPDATE, "UPDATE account SET password = '" + password + "'" +
                "WHERE member_id = SELECT id FROM member WHERE ssn = '" + ssn + "';");
    }

    public void updateEMail(String eMail, String ssn) {
        executeQuery(QueryType.UPDATE, "UPDATE account SET email = '" + eMail + "'" +
                "WHERE member_id = SELECT id FROM member WHERE ssn = '" + ssn + "';");
    }

    public void saveTables(byte[][] weeks){
        String query = "TRUNCATE "+ schedule +"; INSERT INTO " + schedule + " (week) VALUES ";
        for(byte[] week : weeks){

        }
        query += ";";
    }
//    "CREATE TABLE IF NOT EXISTS " + member + " (\n" +
//            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//            "  `ssn` CHAR(13) NOT NULL,\n" +
//            "  `name` VARCHAR(45) NOT NULL,\n" +
//            "  `middlename` VARCHAR(45) NULL,\n" +
//            "  `surname` VARCHAR(45) NOT NULL,\n" +
//            "  `mobile` VARCHAR(45) NOT NULL,\n" +
//            "  PRIMARY KEY (`id`));\n" +
//            "CREATE TABLE IF NOT EXISTS " + account + " (\n" +
//            "  `member_id` INT NOT NULL,\n" +
//            "  `email` VARCHAR(128) NOT NULL,\n" +
//            "  `password` VARCHAR(128) NOT NULL,\n" +
//            "  INDEX `fk_account_member_idx` (`member_id` ASC),\n" +
//            "  PRIMARY KEY (`member_id`),\n" +
//            "  CONSTRAINT `fk_account_member`\n" +
//            "    FOREIGN KEY (`member_id`)\n" +
//            "    REFERENCES " + member + " (`id`)\n" +
//            "    ON DELETE CASCADE\n" +
//            "    ON UPDATE CASCADE);\n" +
//            "CREATE TABLE IF NOT EXISTS " + leader + " (\n" +
//            "  `member_id` INT NOT NULL,\n" +
//            "  `keyNumber` VARCHAR(25) NOT NULL,\n" +
//            "  `boardPosition` VARCHAR(45) NOT NULL,\n" +
//            "  INDEX `fk_leader_member_idx` (`member_id` ASC),\n" +
//            "  PRIMARY KEY (`member_id`),\n" +
//            "  CONSTRAINT `fk_leader_member`\n" +
//            "    FOREIGN KEY (`member_id`)\n" +
//            "    REFERENCES " + member + " (`id`)\n" +
//            "    ON DELETE CASCADE\n" +
//            "    ON UPDATE CASCADE);\n" +
//            "CREATE TABLE IF NOT EXISTS " + activity + " (\n" +
//            "  `name` VARCHAR(45) NOT NULL,\n" +
//            "  PRIMARY KEY (`name`));\n" +
//            "  CREATE TABLE IF NOT EXISTS " + schedule + " (\n" +
//            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//            "  `week` BLOB NOT NULL,\n" +
//            "  PRIMARY KEY (`id`));" +
//            "  CREATE TABLE IF NOT EXISTS " + leader_has_activity + " (\n" +
//            "  `leader_member_id` INT NOT NULL,\n" +
//            "  `activity_name` VARCHAR(45) NOT NULL,\n" +
//            "  PRIMARY KEY (`leader_member_id`, `activity_name`),\n" +
//            "  INDEX `fk_leader_has_activity_activity_idx` (`activity_name` ASC),\n" +
//            "  INDEX `fk_leader_has_activity_leader_idx` (`leader_member_id` ASC),\n" +
//            "  CONSTRAINT `fk_leader_has_activity_leader`\n" +
//            "    FOREIGN KEY (`leader_member_id`)\n" +
//            "    REFERENCES " + leader + " (`member_id`)\n" +
//            "    ON DELETE CASCADE\n" +
//            "    ON UPDATE CASCADE,\n" +
//            "  CONSTRAINT `fk_leader_has_activity_activity`\n" +
//            "    FOREIGN KEY (`activity_name`)\n" +
//            "    REFERENCES " + activity + " (`name`)\n" +
//            "    ON DELETE NO ACTION\n" +
//            "    ON UPDATE NO ACTION);"
    public void checkSchema()
    {
        try {
            executeQuery(QueryType.BOOL,
                    "CREATE TABLE IF NOT EXISTS " + member + " (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `ssn` CHAR(13) NOT NULL,\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  `middlename` VARCHAR(45) NULL,\n" +
                            "  `surname` VARCHAR(45) NOT NULL,\n" +
                            "  `mobile` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`));\n" +
                            "CREATE TABLE IF NOT EXISTS " + account + " (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `email` VARCHAR(128) NOT NULL,\n" +
                            "  `password` VARCHAR(128) NOT NULL,\n" +
                            "  INDEX `fk_account_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_account_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES " + member + " (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS " + leader + " (\n" +
                            "  `member_id` INT NOT NULL,\n" +
                            "  `keyNumber` VARCHAR(25) NOT NULL,\n" +
                            "  `boardPosition` VARCHAR(45) NOT NULL,\n" +
                            "  INDEX `fk_leader_member_idx` (`member_id` ASC),\n" +
                            "  PRIMARY KEY (`member_id`),\n" +
                            "  CONSTRAINT `fk_leader_member`\n" +
                            "    FOREIGN KEY (`member_id`)\n" +
                            "    REFERENCES " + member + " (`id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE);\n" +
                            "CREATE TABLE IF NOT EXISTS " + activity + " (\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`name`));\n" +
                            "  CREATE TABLE IF NOT EXISTS " + schedule + " (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `week` BLOB NOT NULL,\n" +
                            "  PRIMARY KEY (`id`));" +
                            "  CREATE TABLE IF NOT EXISTS " + leader_has_activity + " (\n" +
                            "  `leader_member_id` INT NOT NULL,\n" +
                            "  `activity_name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`leader_member_id`, `activity_name`),\n" +
                            "  INDEX `fk_leader_has_activity_activity_idx` (`activity_name` ASC),\n" +
                            "  INDEX `fk_leader_has_activity_leader_idx` (`leader_member_id` ASC),\n" +
                            "  CONSTRAINT `fk_leader_has_activity_leader`\n" +
                            "    FOREIGN KEY (`leader_member_id`)\n" +
                            "    REFERENCES " + leader + " (`member_id`)\n" +
                            "    ON DELETE CASCADE\n" +
                            "    ON UPDATE CASCADE,\n" +
                            "  CONSTRAINT `fk_leader_has_activity_activity`\n" +
                            "    FOREIGN KEY (`activity_name`)\n" +
                            "    REFERENCES " + activity + " (`name`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION);"
            );
        }
        catch (Exception ex){
            Logger.logException(ex, "Errors in query.");
        }
    }

    public Object executeQuery(QueryType type, String query)
    {
        // This method is to reduce the amount of copy paste that there was within this class.
        Object result = null;
        try(PreparedStatement command = createConnection().prepareStatement(query))
        {
            result = type == QueryType.READER ? command.executeQuery() : command.executeUpdate();
        }
        catch (Exception ex)
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

    public enum QueryType{
        UPDATE,//INSERT, UPDATE, DELETE, CREATE TABLE, DROP TABLE
        READER,//SELECT -> ResultSet
        BOOL//ALL -> SELECT ? True : False
    }
}