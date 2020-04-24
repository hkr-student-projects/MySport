package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Main;
import model.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML
    private Button home, account, mail, forum, calendar, settings, logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        home.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Home.fxml")));
        account.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Account.fxml")));
        mail.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Mail.fxml")));
        forum.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Forum.fxml")));
        calendar.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Calendar.fxml")));
        settings.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Settings.fxml")));
        logout.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Login.fxml")));
    }
}
