package model.Database;

import model.App;
import model.Logging.Logger;
import model.People.Member;
import model.People.User;
import model.Tools.ArrayList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MySqlManager {

    private final String member = "`" + App.config.DatabaseName + "`.`member`";
    private final String leader = "`" + App.config.DatabaseName + "`.`leader`";
    private final String account = "`" + App.config.DatabaseName + "`.`account`";
    private final String schedule = "`" + App.config.DatabaseName + "`.`schedule`";
    private final String leader_activity = "`" + App.config.DatabaseName + "`.`leader_activity`";
    private final String activity = "`" + App.config.DatabaseName + "`.`activity`";
    private final String conversation = "`" + App.config.DatabaseName + "`.`conversation`";
    private final String conversationMessage = "`" + App.config.DatabaseName + "`.`conversation_message`";
    private final String message = "`" + App.config.DatabaseName + "`.`message`";
    private final String memberConversation = "`" + App.config.DatabaseName + "`.`member_conversation`";
    private final String resetpwtoken = "`" + App.config.DatabaseName + "`.`resetpwtoken`";

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch(Exception ex){
            Logger.logException(ex);
        }
    }

    public MySqlManager(){
        checkSchema();
    }

    public void addAccount(String name, String middlename, String surname, String ssn, String mobile, String email, String password, LocalDate birthday) {
        executeQuery(QueryType.UPDATE, "INSERT INTO "+member+" (name, middlename, surname, ssn, mobile, birthday) " +
                "VALUES (?, ?, ?, ?, ?, ?);" +
                "INSERT INTO "+account+" (email, password) " +
                "VALUES (?, SHA2(?, 256));",//AES better with secret key,
                new Object[] { name, middlename, surname, ssn, mobile, birthday, email, password },
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.VARCHAR, Types.VARCHAR }
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
    @SuppressWarnings("Because QueryType is Reader")
    public boolean updatePassword(int id, String oldPassword, String newPassword){
        boolean matches = false;
        Map<String, ArrayList<Object>> memberData = (Map<String, ArrayList<Object>>)executeQuery(QueryType.READER,
                "SELECT 1 FROM "+account+" WHERE member_id = ? AND password = SHA2(?, 256);",
                new Object[] { id, oldPassword },
                new int[] { Types.INTEGER, Types.VARCHAR }
        );
        if(memberData.size() != 0){
            executeQuery(QueryType.UPDATE, "UPDATE "+account+" SET password = SHA2(?, 256) WHERE member_id = ?;",
                    new Object[] { newPassword, id },
                    new int[] { Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }

    public boolean existsEmail(String email){
        return (boolean) executeQuery(QueryType.BOOL, "SELECT email FROM "+account+" WHERE email = ?",
            new String[] { email }, new int[] { Types.VARCHAR }
            );
    }

    @SuppressWarnings("Because QueryType is Reader")
    public boolean updateEmail(int id, String oldEmail, String newEmail) {
        boolean matches = false;
        Map<String, ArrayList<Object>> memberData = (Map<String, ArrayList<Object>> )executeQuery(QueryType.READER,
                "SELECT member_id FROM "+account+" WHERE member_id = ? AND email = ?;",
                new Object[] { id, oldEmail },
                new int[] { Types.INTEGER, Types.VARCHAR }
        );
        ArrayList<Object> entries = memberData.get("member_id");
        if(entries.size() != 0){
            executeQuery(QueryType.UPDATE, "UPDATE "+account+" SET email = ? WHERE id = ?;",
                    new Object[] { newEmail, id },
                    new int[] { Types.VARCHAR, Types.INTEGER }
            );
            matches = true;
        }

        return matches;
    }
    @SuppressWarnings("Because QueryType is Reader")
    public User getUser(int id) {
        Map<String, ArrayList<Object>> memberData = (Map<String, ArrayList<Object>>)executeQuery(QueryType.READER,
                "SELECT * FROM "+member+" WHERE id = ?;",
                new Object[] { id },
                new int[] { Types.INTEGER }
        );
        Member member = new Member((int)memberData.get("id").get(0), (String)memberData.get("name").get(0), (String)memberData.get("middlename").get(0),
                (String)memberData.get("surname").get(0), (String)memberData.get("ssn").get(0),
                (String)memberData.get("mobile").get(0), ((java.sql.Date)memberData.get("birthday").get(0)).toLocalDate()
        );
        Map<String, ArrayList<Object>> leaderData = (Map<String, ArrayList<Object>>)executeQuery(QueryType.READER,
                "SELECT * FROM "+leader+" WHERE member_id = ?;",
                new Object[] { id },
                new int[] { Types.INTEGER }
        );
        ArrayList<Object> entries = leaderData.get("member_id");
        if(entries.size() != 0){
            Map<String, ArrayList<Object>> activities = (Map<String, ArrayList<Object>>)executeQuery(QueryType.READER,
                    "SELECT name FROM "+activity+" JOIN "+ leader_activity +" ON leader_member_id = ? AND activity_id = id",
                    new Object[] { id },
                    new int[] { Types.INTEGER }
            );
            ArrayList<Object> sports = activities.get("name");
            String[] arr = new String[sports.size()];
            for(int i = 0; i < arr.length; i++)
                arr[i] = (String) sports.get(i);

            member = member.toLeader((String)leaderData.get("key_number").get(0), (String)leaderData.get("board_position").get(0), arr);
        }

        return member;
    }

    @SuppressWarnings("Because QueryType is Reader")
    public int checkCredentials(String email, String password) {
        int id = -1;

        Map<String, ArrayList<Object>> memberData = (Map<String, ArrayList<Object>>) executeQuery(QueryType.READER,
                "SELECT member_id FROM "+account+" WHERE email = ? AND password = SHA2(?, 256);",
                new Object[] { email, password },
                new int[] { Types.VARCHAR, Types.VARCHAR }
        );
        ArrayList<Object> entries = memberData.get("member_id");
        if(entries.size() != 0)
            id = (int) entries.get(0);

        return id;
    }

    public void saveWeeks(byte[][] weeks){//do not want to unite because of need to create new array of objects
        executeQuery(QueryType.UPDATE, "TRUNCATE " + schedule + ";");
        executeQuery(QueryType.UPDATE, "INSERT INTO " + schedule + " (week) VALUES (?)" + repeat(weeks.length - 1) + ";", weeks, Types.BINARY);
    }

    private String repeat(int len){
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < len; i++)
            output.append(",(?)");

        return output.toString();
    }

    @SuppressWarnings("Because QueryType is Reader")
    public byte[][] loadWeeks()  {//fix in future
        ArrayList<byte[]> bytes = new ArrayList<>(8);

        Map<String, ArrayList<Object>> results = (Map<String, ArrayList<Object>>) executeQuery(QueryType.READER, "SELECT week FROM " + schedule + ";");
        ArrayList<Object> entries = results.get("week");
        for(int i = 0; i < entries.size(); i++){
            Blob blob = (Blob) entries.get(i);
            byte[] week;
            try {
                week = blob.getBytes(1L, (int)blob.length());
                bytes.add(week);
                blob.free();
            } catch (SQLException e) {
                Logger.logException(e);
            }
        }
        byte[][] weeks = new byte[bytes.size()][];
        System.arraycopy(bytes.getContents(), 0, weeks, 0, bytes.size());

        return weeks;
    }

    private Object getObject(ResultSet set, int col, int type) throws SQLException {
        switch (type){
            case -7 :
            case -6 :
            case 5 :
            case 4 :
                return set.getInt(col);
            case -1 :
            case 1 :
            case 12 :
                return set.getString(col);
            case 91 :
                return set.getDate(col);
            case 2004 :
            case -2 :
            case -3 :
            case -4 :
                return set.getBlob(col);
            case 0:
                return null;
            default:
                return set.getObject(col);
        }
    }
    
    public void checkSchema()
    {
        try {
            executeQuery(QueryType.UDP, "CREATE TABLE IF NOT EXISTS "+member+" ("+
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "`middlename` VARCHAR(45) NOT NULL," +
                    "`surname` VARCHAR(45) NULL," +
                    "`ssn` CHAR(13) NOT NULL," +
                    "`mobile` VARCHAR(25) NOT NULL," +
                    "`birthday` DATE NOT NULL," +

                    "PRIMARY KEY (`id`));" +
                    "CREATE TABLE IF NOT EXISTS "+account+" (\n" +
                    "  `member_id` INT NOT NULL AUTO_INCREMENT,\n" +
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
                    "  `member_id` INT NOT NULL AUTO_INCREMENT,\n" +
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
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`));\n" +
                    "  CREATE TABLE IF NOT EXISTS "+schedule+" (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `week` BLOB NOT NULL,\n" +
                    "  PRIMARY KEY (`id`));" +
                    "  CREATE TABLE IF NOT EXISTS "+ leader_activity +" (\n" +
                    "  `leader_member_id` INT NOT NULL,\n" +
                    "  `activity_id` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`leader_member_id`, `activity_id`),\n" +
                    "  INDEX `fk_leader_has_activity_activity_idx` (`activity_id` ASC),\n" +
                    "  INDEX `fk_leader_has_activity_leader_idx` (`leader_member_id` ASC),\n" +
                    "  CONSTRAINT `fk_leader_has_activity_leader`\n" +
                    "    FOREIGN KEY (`leader_member_id`)\n" +
                    "    REFERENCES "+leader+" (`member_id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `fk_leader_has_activity_activity`\n" +
                    "    FOREIGN KEY (`activity_id`)\n" +
                    "    REFERENCES "+activity+" (`id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE);" +
                    "CREATE TABLE IF NOT EXISTS "+conversation+" (\n" +
                    "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  PRIMARY KEY (`id`));"+
                    "CREATE TABLE IF NOT EXISTS "+message+" (\n" +
                    "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `fromMobile` VARCHAR(45) NOT NULL,\n" +
                    "  `toMobile` VARCHAR(45) NOT NULL,\n" +
                    "  `message` VARCHAR(1000) NOT NULL,\n" +
                    "  `timestamp` TIMESTAMP NOT NULL,\n" +
                    "  PRIMARY KEY (`id`));" +
                    "CREATE TABLE IF NOT EXISTS "+conversationMessage+" (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `conversation_id` INT(11) NOT NULL,\n" +
                    "  `message_id` INT(11) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  INDEX `fk_conversation_has_message_message1_idx` (`message_id` ASC),\n" +
                    "  INDEX `fk_conversation_has_message_conversation1_idx` (`conversation_id` ASC),\n" +
                    "  CONSTRAINT `fk_conversation_has_message_conversation1`\n" +
                    "    FOREIGN KEY (`conversation_id`)\n" +
                    "    REFERENCES "+conversation+" (`id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `fk_conversation_has_message_message1`\n" +
                    "    FOREIGN KEY (`message_id`)\n" +
                    "    REFERENCES "+message+" (`id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE);" +
                    "CREATE TABLE IF NOT EXISTS "+memberConversation+" (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `member_id` INT(11) NOT NULL,\n" +
                    "  `conversation_id` INT(11) NOT NULL,\n" +
                    "  INDEX `fk_member_has_conversation_conversation1_idx` (`conversation_id` ASC),\n" +
                    "  INDEX `fk_member_has_conversation_member1_idx` (`member_id` ASC),\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  CONSTRAINT `fk_member_has_conversation_member1`\n" +
                    "    FOREIGN KEY (`member_id`)\n" +
                    "    REFERENCES "+member+" (`id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE,\n" +
                    "  CONSTRAINT `fk_member_has_conversation_conversation1`\n" +
                    "    FOREIGN KEY (`conversation_id`)\n" +
                    "    REFERENCES "+conversation+" (`id`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE);" +
                    "CREATE TABLE IF NOT EXISTS "+resetpwtoken+" (\n" +
                    "  `id` INT(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `email` VARCHAR(45) NULL DEFAULT NULL,\n" +
                    "  `token` VARCHAR(45) NULL DEFAULT NULL,\n" +
                    "  `futuredate` TIMESTAMP NULL DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`id`));"
            );
        }
        catch (Exception ex){
            Logger.logException(ex, "Errors in query.");
        }
    }

    public Object executeQuery(QueryType type, String query)
    {
        return executeQuery(type, query, null, null);
    }

    public Object executeQuery(QueryType type, String query, Object[] parameters, int commonType){//java.sql.Types
        int[] types = new int[parameters.length];
        Arrays.fill(types, commonType);

        return executeQuery(type, query, parameters, types);
    }
    //STRICT ORDER
    public Object executeQuery(QueryType type, String query, Object[] parameters, int[] types)//java.sql.Types
    {
        // This method is to reduce the amount of copy paste that there was within this class.
        if(parameters != null && parameters.length != types.length)
            throw new IllegalArgumentException("executeQuery(): parameters length is different to types length.");
        Object result = null;
        try(Connection connection = createConnection();
            PreparedStatement command = connection.prepareStatement(query)
        )
        {
            if(parameters != null)
                for(int i = 0; i < parameters.length; i++)
                    command.setObject(i + 1, parameters[i], types[i]);

            if(type == QueryType.READER){
                ResultSet set = command.executeQuery();
                ResultSetMetaData metaData = set.getMetaData();
                Map<String, ArrayList<Object>> results = new HashMap<>(10);
                int count = metaData.getColumnCount();
                for(int i = 1; i < count + 1; i++)
                    results.put(metaData.getColumnName(i), new ArrayList<>(30));
                while (set.next())
                    for(int i = 1; i < count + 1; i++)
                        results.get(metaData.getColumnName(i)).add(getObject(set, i, metaData.getColumnType(i)));

                result = results;
            }
            else if(type == QueryType.UPDATE){
                result = command.executeUpdate();
            }
            else if(type == QueryType.BOOL)
                result = command.executeQuery().next();//returns true if there is at least 1 selection result
            else
                command.execute();
        }
        catch (SQLException ex)
        {
            Logger.logException(ex);
        }

        return result;
    }

    private static Connection createConnection(){
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
        BOOL,//SELECT -> Exists selection or not
        UDP
    }

    public Map<String, ArrayList<String>> getEmails(){
        return (Map<String, ArrayList<String>>) executeQuery(QueryType.READER, "SELECT email FROM "+account);
    }
}
