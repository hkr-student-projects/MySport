package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import model.Tools.EventType;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Forum extends Menu implements Initializable {

    @FXML
    private Button newCategory;

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
}
