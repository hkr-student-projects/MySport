package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Main;
import model.SceneSwitcher;

public abstract class Menu {
    @FXML
    protected Button burger, home, account, mail, forum, calendar, settings, logout;

    protected void bindTab(Menu caller){
        home.setOnMouseClicked(e -> {
            if(caller instanceof Home)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Home"));
        });
        account.setOnMouseClicked(e -> {
            if(caller instanceof Account)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Account"));
        });
        mail.setOnMouseClicked(e -> {
            if(caller instanceof Mail)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Mail"));
        });
        forum.setOnMouseClicked(e -> {
            if(caller instanceof Forum)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Forum"));
        });
        calendar.setOnMouseClicked(e -> {
            if(caller instanceof Calendar)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Calendar"));
        });
        settings.setOnMouseClicked(e -> {
            if(caller instanceof Settings)
                return;
            Main.instance.setScene(SceneSwitcher.instance.getScene("Settings"));
        });
        logout.setOnMouseClicked(e -> Main.instance.setScene(SceneSwitcher.instance.getScene("Login")));
    }
}
