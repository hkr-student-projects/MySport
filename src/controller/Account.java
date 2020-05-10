package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Account extends Menu implements Initializable {

    @FXML
    Button buttonEdit, buttonCreate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
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

    @FXML
    public void buttonEditClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("EditProfile"));
    }

    @FXML
    public void buttonCreateClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccount"));
    }
}
