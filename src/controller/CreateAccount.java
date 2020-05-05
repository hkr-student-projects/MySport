package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.Database.DatabaseManager;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccount implements Initializable {

    @FXML
    TextField firstName, lastName, middleName, ssn, email, phoneNumber, passwordRepeat;
    @FXML
    PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void buttonClearAllClick() {

        firstName.setText("");
        middleName.setText("");
        lastName.setText("");
        ssn.setText("");
        email.setText("");
        phoneNumber.setText("");
    }

    @FXML
    public void buttonNextClick() {

//        try {
//
//            if (validateInput()) {
//                App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccountNo2"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccount-V2"));

    }

    @FXML
    public Boolean validateInput() {

        if (!firstName.getText().isBlank()) {
            if (!isNumber(firstName.getText()))
                return firstName.getText().matches("[a-zA-Z ]*");
            else {
                Alert alertBox = new Alert(Alert.AlertType.WARNING);
                alertBox.setContentText("First name can't contain numbers!");
                alertBox.setHeaderText("Warning!");
                alertBox.showAndWait();
            }
        }
        return false;
    }

    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        try {
            int number = Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }


    @FXML
    public void buttonBackClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Login"));
    }

    @FXML
    public void saveClick(){
//        App.databaseManager.addAccount(firstName.getText(), middleName.getText(), lastName.getText(),
//            ssn.getText(), email.getText(), password.getText(), phoneNumber.getText()
//        );
    }
}
