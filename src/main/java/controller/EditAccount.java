package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.App;
import model.Logging.Logger;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAccount extends Menu implements Initializable {

    @FXML
    private Button buttonNameSave, buttonPasswordSave, buttonReturn, buttonPhoneNumberSave;
    @FXML
    private TextField textFieldName, textFieldMiddleName, textFieldSurname, textFieldPhoneNumber;
    @FXML
    private PasswordField passwordFieldPassword, passwordFieldConfirmPassword, passwordFieldCurrentPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { bindTab(this); }

    @FXML
    public void buttonNameSaveClick() {
        try {
            if (!textFieldName.getText().isEmpty()) {
                if (!textFieldName.getText().matches("[A-Za-zöÖäÄåÅ]+")) {
                    alert("Error! Numbers detected!", "Re-enter new name without numbers.");
                } else {
                    String name = textFieldName.getText();
                    App.mySqlManager.updateName(App.instance.getSession().getId(), name);
                    confirm("Name saved!", "New name: " + name + " has been saved!");
                    textFieldName.clear();
                }
            } else if (!textFieldMiddleName.getText().isEmpty()) {
                if (!textFieldMiddleName.getText().matches("[A-Za-zöÖäÄåÅ]+")) {
                    alert("Error! Numbers detected!", "Re-enter new middle name without numbers.");
                } else {
                    String middleName = textFieldMiddleName.getText();
                    App.mySqlManager.updateMiddleName(App.instance.getSession().getId(), middleName);
                    confirm("Middle-name saved!", "New Middle-name: " + middleName +" has been saved!" );
                    textFieldMiddleName.clear();
                }
            } else if (!textFieldSurname.getText().isEmpty()) {
                if (!textFieldSurname.getText().matches("[A-Za-zöÖäÄåÅ]+")) {
                    alert("Error! Numbers detected!", "Re-enter new surname without numbers.");
                } else {
                    String surname = textFieldSurname.getText();
                    App.mySqlManager.updateSurname(App.instance.getSession().getId(), surname);
                    confirm("Surname saved!", "New Surname: " + surname + " has been saved!");
                    textFieldSurname.clear();
                }
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }

    @FXML
    public void buttonPasswordSaveClick() {
        try {
            if (!passwordFieldPassword.getText().isEmpty() && passwordFieldPassword.getText().length() <=5) {
                String password = passwordFieldPassword.getText();
                String oldPassword = passwordFieldCurrentPassword.getText();
                if (password.equals(passwordFieldConfirmPassword.getText())){
                    if (!App.mySqlManager.updatePassword(App.instance.getSession().getId(), oldPassword, password)) {
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
            if (!textFieldPhoneNumber.getText().isEmpty()){
                if (!textFieldPhoneNumber.getText().matches("[0-9]+")){
                    alert("Error! Phone number not detected", "Please enter a valid phone number.");
                } else {
                    String phoneNumber = textFieldPhoneNumber.getText();
                    App.mySqlManager.updatePhoneNumber(App.instance.getSession().getId(), phoneNumber);
                    confirm("Phone number saved!", "New phone number: " + phoneNumber + " has been saved!");
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

    private void confirm(String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
