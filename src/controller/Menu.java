package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import model.Main;
import model.SceneSwitcher;

public abstract class Menu {
    @FXML
    protected HBox tab;
    @FXML
    protected Button burger, home, account, mail, forum, calendar, settings, logout;
    private boolean flag = false;

    protected abstract void burgerOpenAction();

    protected abstract void burgerCloseAction();

    protected void toggleTab(){
        tab.setDisable(flag);
        tab.setVisible(!flag);
    }

    protected void bindTab(Menu caller){
        burger.setOnMouseClicked(e -> {
            burger.setRotate(360 - burger.getRotate() + 90);
            if(flag = !flag)
                burgerOpenAction();
            else
                burgerCloseAction();
        });
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
