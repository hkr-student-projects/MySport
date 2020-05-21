package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Home extends Menu implements Initializable {

    @FXML
    Button buttonAboutView, supportView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
    }

    @FXML
    public void buttonAboutViewClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("About"));
    }

    @FXML
    public void buttonSupportViewClick() {
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
