package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class About extends Menu implements Initializable {

    @FXML
    public Button buttonReturn;


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { bindTab(this); }

    public void buttonReturnClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Settings"));
    }
}
