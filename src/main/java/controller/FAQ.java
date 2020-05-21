package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class FAQ extends Menu implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);

    }

    @FXML
    public void returnButtonPress() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Support"));
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
