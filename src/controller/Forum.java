package controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Forum extends Menu implements Initializable {
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
    protected void onAppClose() {

    }
}
