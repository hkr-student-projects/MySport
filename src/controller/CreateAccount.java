package controller;

import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import model.App;
import model.Database.DatabaseManager;
import model.Tools.SceneSwitcher;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAccount implements Initializable {

    @FXML
    private TextField firstname, surname, middlename, ssn, email, mobile;
    @FXML
    private PasswordField password, repassword;
    @FXML
    private DatePicker birthday;
    @FXML
    private Line line0, line1, line2, line3, line4, line5, line6, line7;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addFocusStyle(new TextField[] { firstname, middlename, surname, ssn, mobile, email, password, repassword },
                line0, line1, line2, line3, line4, line5, line6, line7
        );
    }

    private void addFocusStyle(TextField[] fields, Line... lines){
        if(fields.length != lines.length)
            throw new IllegalArgumentException("TextField array and Line array have different lengths");
        for(int i = 0; i < fields.length; i++){
            int i2 = i;
            fields[i].focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused){
                    fields[i2].setText("");
                    lines[i2].setStroke(Paint.valueOf("#000000"));
                    lines[i2].setStrokeWidth(1);
                }
            });
        }
    }

    @FXML
    public void clearFields() {

        firstname.setText("");
        surname.setText("");
        middlename.setText("");
        ssn.setText("");
        email.setText("");
        mobile.setText("");
    }

    @FXML
    public void returnHome() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Login"));
    }

    private void redLine(Line line){
        line.setStrokeWidth(2);
        line.setStroke(Paint.valueOf("#ff4c4c"));
    }

    @FXML
    private void saveClick(){

        String naming = "[A-Z][a-z]*";

        if(!firstname.getText().matches(naming)){
            redLine(line0);
            return;
        }
        if((!middlename.getText().isBlank()) && !middlename.getText().matches(naming)){
            redLine(line1);
            return;
        }
        if(!surname.getText().matches(naming)){
            redLine(line0);
            return;
        }
//
//
//
//            // "Name", "Surname", "19890518-4376", "+073-751-06-21", "Storagatan 12A-1006"
//            boolean errors = false;
//            String name = null;
//            String sname = null;
//            String ssn = null;
//            String phone = null;
//            String addr = null;
//            String email = null;
//            String pass = passwordfield.getText();
//
//            if(passwordfield.getText().isEmpty() || passwordfield2.getText().isEmpty() || !Main.equals(passwordfield.getText().toCharArray(), passwordfield2.getText().toCharArray())){
//                redOutField(passwordfield);
//                redOutField(passwordfield2);
//                errors = true;
//            }
//            if(pass.isBlank())
//                for(TextField field : fields) {
//                    switch (field.getPromptText().toLowerCase()) {
//
//                        case "name":
//                            name = field.getText();
//                        case "surname":
//                            sname = field.getText();
//                            if (!field.getText().matches("[A-Z][a-z]*")) {
//                                redOutField(field);
//                                errors = true;
//                            }
//                            break;
//
//                        case "19890518-1234":
//                            ssn = field.getText();
//                            if (!field.getText().matches("\\d{8}\\-\\d{4}")) {
//                                redOutField(field);
//                                errors = true;
//                            }
//                            break;
//
//                        case "+073-751-06-21":
//                            phone = field.getText();
//                            if (!field.getText().matches("\\+\\d{3}\\-\\d{3}\\-\\d{2}\\-\\d{2}")) {
//                                redOutField(field);
//                                errors = true;
//                            }
//                            break;
//
//                        case "storagatan 12a-1006":
//                            addr = field.getText();
//                            if (!field.getText().matches("[A-Z][a-z]+\\s\\d+[A-Z]?\\-\\d+")) {
//                                redOutField(field);
//                                errors = true;
//                            }
//                            break;
//                        case "example@example.com" :
//                            email = field.getText();
//                            ResultSet rs = (ResultSet) Main.databaseManager.executeQuery(DatabaseManager.QueryType.READER,
//                                    "SELECT 1 FROM hotel.Account WHERE hotel.Account.email = '"+email+"';");
//                            try {
//                                if(rs.next() || !field.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
//                                    redOutField(field);
//                                    errors = true;
//                                }
//                            } catch (SQLException e) {
//                                Logger.logException(e);
//                            }
//
//                            break;
//                    }
//                }
//            if(!errors){
//                Main.databaseManager.createPerson(email, pass, ssn, name, sname, addr, phone);
//                String color = "ffb053";
//                if(homeSession != null){
//                    color = PersonalAreaEmp.colorCode;
//                    homeSession.customers = Main.databaseManager.getProfiles();
//                    homeSession.loadButtons();
//                }
//                new PopUP("You have created the account.", color).show();
//                this.close();
//            }
   }
}
