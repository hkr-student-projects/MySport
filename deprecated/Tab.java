package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.Main;
import model.SceneSwitcher;

public class Tab {
    private EventHandler<MouseEvent> home, account, mail, forum, calendar, settings, logout;

    public static Tab instance = new Tab();

    private Tab(){
        home = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Home.fxml"));
        account = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Account.fxml"));
        mail = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Mail.fxml"));
        forum = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Forum.fxml"));
        settings = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Settings.fxml"));
        calendar = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Calendar.fxml"));
        logout = mouseEvent -> Main.instance.setScene(SceneSwitcher.instance.getScene("Login.fxml"));
    }

    public EventHandler<MouseEvent> getHome() {
        return home;
    }

    public EventHandler<MouseEvent> getAccount() {
        return account;
    }

    public EventHandler<MouseEvent> getMail() {
        return mail;
    }

    public EventHandler<MouseEvent> getForum() {
        return forum;
    }

    public EventHandler<MouseEvent> getCalendar() {
        return calendar;
    }

    public EventHandler<MouseEvent> getSettings() {
        return settings;
    }

    public EventHandler<MouseEvent> getLogout() {
        return logout;
    }
}
