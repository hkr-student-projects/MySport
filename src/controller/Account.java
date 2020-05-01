package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Account extends Menu implements Initializable {

    @FXML Button buttonEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);
    }

    @Override
    protected void burgerOpenAction() {

    }

    @Override
    protected void burgerCloseAction() {

    }

    @FXML
    public void buttonEditClick () {
        App.instance.setScene(SceneSwitcher.instance.getScene("EditAccount"));
    }
}
