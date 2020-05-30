package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetCode extends Menu implements Initializable {
    @FXML
    private JFXTextField resetCodeButton;

    @FXML
    private JFXButton resetButton;

    //UserDAO dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dao = new UserDAO();
    }

    @FXML
    void handleResetCode(ActionEvent event) {
//        String resetcode = resetCodeButton.getText();
//        String email = dao.checkResetToken(resetcode);
//        if(email != null){
//            Utilities.resetEmail = email;
        App.instance.setScene(SceneSwitcher.instance.getScene("NewPassword"));
//        }
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
}
