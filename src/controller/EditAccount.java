package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.App;
import model.Database.DatabaseManager;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAccount extends Menu implements Initializable {

    DatabaseManager dbM = new DatabaseManager();
    @FXML Button buttonNameSave, buttonPasswordSave, buttonReturn, buttonPhoneNumberSave;
    @FXML TextField textFieldName, textFieldMiddleName, textFieldSurname, textFieldPassword, textFieldConfirmPassword, textFieldPhoneNumber;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
    }

    @FXML
    public void buttonNameSaveClick() {
        try {
            if (!textFieldName.getText().isBlank()) {
                if (textFieldName.getText().matches("[0-9]")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error! Numbers detected!");
                    alert.setContentText("Re-enter new name without numbers.");
                    alert.showAndWait();
                    return;
                } else {
                    String name = textFieldName.getText();
//                    dbM.updateName(name, Use log in to extract current users ssn );
                }
            } else if (!textFieldMiddleName.getText().isBlank()) {
                if (textFieldMiddleName.getText().matches("[0-9]")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error! Numbers detected!");
                    alert.setContentText("Re-enter new middle name without numbers.");
                    alert.showAndWait();
                    return;
                } else {
                    String middleName = textFieldName.getText();
//                    dbM.updateName(middleName, Use log in to extract current users ssn );
                }
            } else if (!textFieldSurname.getText().isBlank()) {
                if (textFieldSurname.getText().matches("[0-9]")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Error! Numbers detected!");
                    alert.setContentText("Re-enter new surname without numbers.");
                    alert.showAndWait();
                    return;
                } else {
                    String surname = textFieldName.getText();
//                    dbM.updateName(surname, Use log in to extract current users ssn );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void buttonPasswordSaveClick() {
        try {
            if (!textFieldPassword.getText().isBlank()) {
                String password = textFieldPassword.getText();
                if (password.equals(textFieldConfirmPassword.getText())){
//                    dbM.updatePassword(password, use login to extract ssn from current user);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error! No password detected.");
                alert.setContentText("Please enter a new password and confirm it by re-entering the new password before saving changes.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void buttonPhoneNumberSaveClick() {
        try {
            if (!textFieldPhoneNumber.getText().isBlank()){
                if (!textFieldPhoneNumber.getText().matches("[0-9]+")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error! Phone number not detected.");
                    alert.setContentText("Please enter a valid phone number.");
                    alert.showAndWait();
                    return;
                } else {
                    String phoneNumber = textFieldPhoneNumber.getText();
//                    dbM.updatePhoneNumber(phoneNumber, use log in to get ssn of current user );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void buttonReturnClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Account"));
    }

    @Override
    protected void onBurgerOpen() {

    }

    @Override
    protected void onBurgerClose() {

    }

    @Override
    protected void onSceneSwitch() {

    }

    @Override
    protected void onAppClose() {

    }
}
