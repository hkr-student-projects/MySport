package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML private TextField textFieldNewPassword, textFieldReNewPassword, textFieldNewName, textFieldNewMiddleName, textFieldNewSurname;
    @FXML private Button buttonSaveNewPwd, buttonSaveName, buttonDeleteAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
