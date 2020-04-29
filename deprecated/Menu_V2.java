package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Main;
import model.SceneSwitcher;

public class Menu {
    @FXML
    public static Button home, account, mail, forum, calendar, settings, logout;

    public static void bindEvents(){
        home.setOnMouseClicked(e -> {
            System.out.println("home click");
            Main.instance.setScene(SceneSwitcher.instance.getScene("Home.fxml"));
        });
        account.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Account.fxml")));
        //mail.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Mail.fxml")));
        //forum.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Forum.fxml")));
        calendar.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Settings.fxml")));
        settings.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Calendar.fxml")));
        logout.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Login.fxml")));
    }
}
