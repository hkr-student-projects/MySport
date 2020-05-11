package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.Logging.Logger;
import model.People.Leader;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAccount extends Menu implements Initializable {

    @FXML Button buttonNameSave, buttonPasswordSave, buttonReturn, buttonPhoneNumberSave;
    @FXML TextField textFieldName, textFieldMiddleName, textFieldSurname, textFieldPhoneNumber;
    @FXML PasswordField passwordFieldPassword, passwordFieldConfirmPassword, passwordFieldCurrentPassword;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
    }

    @FXML
    public void buttonNameSaveClick() {
        try {
            if (!textFieldName.getText().isBlank()) {
                if (textFieldName.getText().matches("[0-9]")) {
                    alert("Error! Numbers detected!", "Re-enter new name without numbers.");
                } else {
                    String name = textFieldName.getText();
                    App.databaseManager.updateName(App.instance.getSession().getId(), name);
                }
            } else if (!textFieldMiddleName.getText().isBlank()) {
                if (textFieldMiddleName.getText().matches("[0-9]")) {
                    alert("Error! Numbers detected!", "Re-enter new middle name without numbers.");
                } else {
                    String middleName = textFieldName.getText();
                    App.databaseManager.updateName(App.instance.getSession().getId(), middleName);
                }
            } else if (!textFieldSurname.getText().isBlank()) {
                if (textFieldSurname.getText().matches("[0-9]")) {
                    alert("Error! Numbers detected!", "Re-enter new surname without numbers.");
                } else {
                    String surname = textFieldName.getText();
                    App.databaseManager.updateName(App.instance.getSession().getId(), surname);
                }
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

    @FXML
    public void buttonPasswordSaveClick() {
        try {
            if (!passwordFieldPassword.getText().isBlank() && passwordFieldPassword.getText().length() <=5) {
                String password = passwordFieldPassword.getText();
                String oldPassword = passwordFieldCurrentPassword.getText();
                if (password.equals(passwordFieldConfirmPassword.getText())){
                    if (!App.databaseManager.updatePassword(App.instance.getSession().getId(), oldPassword, password)) {
                        alert("Error!","Some passwords may not match, please try again!");
                    }
                } else {
                    alert("Error!", "Passwords do not match, please try again.");
                }
            } else {
                alert("Error!", "Please enter a new password that is at least 5 symbols long and confirm it by re-entering the new password before saving changes.");
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

    @FXML
    public void buttonPhoneNumberSaveClick() {
        try {
            if (!textFieldPhoneNumber.getText().isBlank()){
                if (!textFieldPhoneNumber.getText().matches("[0-9]+")){
                    alert("Error! Phone number not detected", "Please enter a valid phone number.");
                } else {
                    String phoneNumber = textFieldPhoneNumber.getText();
                    App.databaseManager.updatePhoneNumber(App.instance.getSession().getId(), phoneNumber);
                }
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
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
    protected void onBeforeSceneSwitch() {

    }

    @Override
    protected void onBeforeLogout() {

    }

    private void alert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
