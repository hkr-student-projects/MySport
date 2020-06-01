package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPassword extends Menu implements Initializable {
    @FXML private JFXTextField emailTextField;
    @FXML private JFXButton sendPasswordButton;
    @FXML private Text text;


    //UserDAO dao;
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void checkEmail(ActionEvent event) {


  App.instance.setScene(SceneSwitcher.instance.getScene("ResetCode"));

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
