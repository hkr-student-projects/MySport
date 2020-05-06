package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Database.UserDAO;
import model.Tools.SceneSwitcher;
import model.Util.Utilities;
import java.net.URL;
import java.util.ResourceBundle;

public class NewPassword implements Initializable {

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField confirmPassword;

    @FXML
    private JFXButton updatePasswordButton;

    UserDAO dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new UserDAO();
    }
    @FXML
    void handleUpdatePassword(ActionEvent event) {

        if(newPassword.getText().equals(confirmPassword.getText())){
            dao.updateUserPassword(Utilities.resetEmail, confirmPassword.getText());
            SceneSwitcher.instance.getScene("/view/Login.fxml");
        }

    }
}
