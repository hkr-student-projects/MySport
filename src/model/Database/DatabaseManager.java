package model.Database;

import model.Logging.Logger;
import model.Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DatabaseManager {

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

    public void checkSchema()
    {
//        try {
//            executeQuery(QueryType.BOOL,
//                    "CREATE TABLE IF NOT EXISTS `hotel`.`Account` (\n" +
//                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                            "  `email` VARCHAR(45) NOT NULL,\n" +
//                            "  `password` VARCHAR(40) NOT NULL,\n" +
//                            "  PRIMARY KEY (`id`));" +
//                            "CREATE TABLE IF NOT EXISTS " + books + " (" +
//                            "`reference` INT NOT NULL AUTO_INCREMENT," +
//                            "`guests` TINYINT NOT NULL," +
//                            "`movein` DATE NOT NULL," +
//                            "`moveout` DATE NOT NULL," +
//                            "PRIMARY KEY (`reference`));" +
//                            "CREATE TABLE IF NOT EXISTS `hotel`.`Customer` (\n" +
//                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                            "  `account_id` INT NOT NULL,\n" +
//                            "  `ssn` VARCHAR(13) NOT NULL,\n" +
//                            "  `name` VARCHAR(45) NOT NULL,\n" +
//                            "  `surname` VARCHAR(45) NOT NULL,\n" +
//                            "  `phone` VARCHAR(15) NOT NULL,\n" +
//                            "  `address` VARCHAR(45) NOT NULL,\n" +
//                            "  PRIMARY KEY (`id`),\n" +
//                            "  INDEX `fk_Customer_Account1_idx` (`account_id` ASC),\n" +
//                            "  CONSTRAINT `fk_Customer_Account1`\n" +
//                            "    FOREIGN KEY (`account_id`)\n" +
//                            "    REFERENCES `hotel`.`Account` (`id`)\n" +
//                            "    ON DELETE CASCADE" +
//                            "    ON UPDATE CASCADE);" +
//                            "CREATE TABLE IF NOT EXISTS `hotel`.`Order` (\n" +
//                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                            "  `Customer_id` INT NOT NULL,\n" +
//                            "  `Booking_reference` INT NOT NULL,\n" +
//                            "  PRIMARY KEY (`id`),\n" +
//                            "  INDEX `fk_Order_Customer1_idx` (`Customer_id` ASC),\n" +
//                            "  INDEX `fk_Order_Booking1_idx` (`Booking_reference` ASC),\n" +
//                            "  CONSTRAINT `fk_Order_Customer1`\n" +
//                            "    FOREIGN KEY (`Customer_id`)\n" +
//                            "    REFERENCES `hotel`.`Customer` (`id`)\n" +
//                            "    ON DELETE NO ACTION\n" +
//                            "    ON UPDATE NO ACTION,\n" +
//                            "  CONSTRAINT `fk_Order_Booking1`\n" +
//                            "    FOREIGN KEY (`Booking_reference`)\n" +
//                            "    REFERENCES `hotel`.`Booking` (`reference`)\n" +
//                            "    ON DELETE CASCADE\n" +
//                            "    ON UPDATE CASCADE);" +
//                            "CREATE TABLE IF NOT EXISTS `hotel`.`Employee` (\n" +
//                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                            "  `account_id` INT NOT NULL,\n" +
//                            "  `position` ENUM('ADMIN', 'RECEPTIONIST', 'CLEANER') NOT NULL,\n" +
//                            "  `ssn` VARCHAR(13) NOT NULL,\n" +
//                            "  `name` VARCHAR(45) NOT NULL,\n" +
//                            "  `surname` VARCHAR(45) NOT NULL,\n" +
//                            "  `phone` VARCHAR(15) NOT NULL,\n" +
//                            "  `address` VARCHAR(45) NOT NULL,\n" +
//                            "  PRIMARY KEY (`id`),\n" +
//                            "  INDEX `fk_Customer_Account1_idx` (`account_id` ASC),\n" +
//                            "  CONSTRAINT `fk_Customer_Account10`\n" +
//                            "    FOREIGN KEY (`account_id`)\n" +
//                            "    REFERENCES `hotel`.`Account` (`id`)\n" +
//                            "    ON DELETE CASCADE\n" +
//                            "    ON UPDATE CASCADE);" +
//                            "CREATE TABLE IF NOT EXISTS " + rooms + " (" +
//                            "`number` VARCHAR(10) NOT NULL," +
//                            "`floor` SMALLINT(3) NOT NULL," +
//                            "`class` ENUM('ECONOMY', 'MIDDLE', 'LUXURY') NOT NULL," +
//                            "PRIMARY KEY (`number`));" +
//                            "CREATE TABLE IF NOT EXISTS " + booked + " (" +
//                            "`id` INT NOT NULL AUTO_INCREMENT," +
//                            "`Booking_reference` INT NOT NULL," +
//                            "`Room_number` VARCHAR(10) NOT NULL," +
//                            "PRIMARY KEY (`id`)," +
//                            "INDEX `fk_BookedRoom_Booking1_idx` (`Booking_reference` ASC)," +
//                            "INDEX `fk_BookedRoom_Room1_idx` (`Room_number` ASC)," +
//                            "CONSTRAINT `fk_BookedRoom_Booking1`" +
//                            "FOREIGN KEY (`Booking_reference`)" +
//                            "REFERENCES " + books + " (`reference`)" +
//                            "ON DELETE CASCADE " +
//                            "ON UPDATE CASCADE," +
//                            "CONSTRAINT `fk_BookedRoom_Room1`" +
//                            "FOREIGN KEY (`Room_number`)" +
//                            "REFERENCES " + rooms + " (`number`)" +
//                            "ON DELETE CASCADE " +
//                            "ON UPDATE CASCADE);"
//            );
//        }
//        catch (Exception ex){
//            Logger.logException(ex);
//        }
    }

    public Object executeQuery(QueryType type, String query)
    {
        // This method is to reduce the amount of copy paste that there was within this class.
        Object result = null;
        Connection connection = createConnection();
        try
        {
            PreparedStatement command = connection.prepareStatement(query);
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
            conn = DriverManager.getConnection(Main.config.DatabaseAddress, Main.config.DatabaseUsername, Main.config.DatabasePassword);
        } catch (SQLException ex) {
            Logger.logException(ex);
        }

        return conn;
    }

    public enum QueryType{
        UPDATE,//INSERT, UPDATE, DELETE, CREATE TABLE, DROP TABLE
        READER,//SELECT -> ResultSet
        BOOL//ALL -> SELECT ? true : false
    }
}