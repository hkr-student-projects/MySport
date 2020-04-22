package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Main;
import model.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private Button login;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setOnMouseClicked(e -> {
            Main.instance.setScene(SceneSwitcher.getScene("Home.fxml"));
        });
    }
}
