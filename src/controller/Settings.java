// unfinished, still needs work done.
package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;



public class Settings extends Menu implements Initializable {

    @FXML private TextField textFieldNewPassword, textFieldReNewPassword, textFieldNewName, textFieldNewMiddleName, textFieldNewSurname;
    @FXML private Button buttonSaveNewPwd, buttonSaveName, buttonDeleteAccount;
    // add database manager object

    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            bindTab(this);
        }


    @FXML public void buttonSaveNewPwdClick(){
        if (!textFieldNewPassword.getText().equals(textFieldReNewPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Passwords do not match!");
            alert.setContentText("Please make sure that the two passwords match.");
            alert.showAndWait();
            return;
        } else {
            // code for updating password
        }

    }

    @FXML public void buttonSaveNameClick() {
        if (textFieldNewName.getText().matches("[0-9!.,?]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Non-alphabetical characters detected in name!");
            alert.setContentText("Please make sure your name does not contain any symbols such as !.,?");
            alert.showAndWait();
            return;
        } else if (textFieldNewMiddleName.getText().matches("[0-9!.,?]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Non-alphabetical characters detected in middle-name!");
            alert.setContentText("Please make sure your middle-name does not contain any symbols such as !.,?");
            alert.showAndWait();
            return;
        } else if (textFieldNewSurname.getText().matches("[0-9!.,?]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Non-alphabetical characters detected in surname!");
            alert.setContentText("Please make sure your surname does not contain any symbols such as !.,?");
            alert.showAndWait();
            return;
        } else {
            if (!textFieldNewName.getText().isBlank()) {
                String name = textFieldNewName.getText();
                // code for updating name
            } else if (!textFieldNewMiddleName.getText().isBlank()) {
                String middleName = textFieldNewMiddleName.getText();
                // code for updating middle-name
            } else if (!textFieldNewSurname.getText().isBlank()) {
                String surname = textFieldNewSurname.getText();
                // code for updating surname
            }
        }
    }

    @FXML public void buttonDeleteAccountClick() {}


}
