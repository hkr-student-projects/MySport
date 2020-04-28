package controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Home extends Menu implements Initializable {

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
}
