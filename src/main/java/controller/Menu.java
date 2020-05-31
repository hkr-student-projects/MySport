package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.App;
import model.Logging.Logger;
import model.People.Leader;
import model.People.User;
import java.util.ArrayList;

import model.Tools.Colorable;
import model.Tools.SceneSwitcher;

public abstract class Menu implements Colorable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    protected Text sessionName;
    @FXML
    protected HBox tab;
    @FXML
    protected Button burger, home, account, mail, forum, calendar, settings, logout;
    private boolean flag = false;
    private static final ArrayList<Menu> instances;

    static {
        instances = new ArrayList<>(35);
    }

    protected abstract void onBurgerOpen();

    protected abstract void onBurgerClose();

    protected abstract void onBeforeSceneSwitch();

    protected abstract void onBeforeLogout();

    protected void buildSessionName(){
        User user = App.instance.getSession();
        if(sessionName == null)
            return;
        sessionName.setText(user.getName() + user.getMiddlename() + " " + user.getSurname() + (user.getClass() == Leader.class ? " (Leader)" : " (Member)"));
    }

    protected void toggleTab(){
        tab.setDisable(flag);
        tab.setVisible(!flag);
    }

    protected void bindTab(Menu caller){
        instances.add(caller);
        burger.setOnMouseClicked(e -> {
            burger.setRotate(360 - burger.getRotate() + 90);
            if(flag = !flag)
                onBurgerOpen();
            else
                onBurgerClose();
        });
        home.setOnMouseClicked(e -> {
            if(caller instanceof Home)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
        });
        account.setOnMouseClicked(e -> {
            if(caller instanceof Account)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Account"));
        });
        mail.setOnMouseClicked(e -> {
            if(caller instanceof Messaging)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Messaging"));
        });
        forum.setOnMouseClicked(e -> {
            if(caller instanceof Forum)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Forum"));
        });
        calendar.setOnMouseClicked(e -> {
            if(caller instanceof Calendar)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Calendar"));
        });
        settings.setOnMouseClicked(e -> {
            if(caller instanceof Settings)
                return;
            onBeforeSceneSwitch();
            App.instance.setScene(SceneSwitcher.instance.getScene("Settings"));
        });
        logout.setOnMouseClicked(e -> {
            Thread thread = new Thread(this::onBeforeLogout);// This method is executed <number of scenes> times unlike other methods which are executed once per scene
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.logException(ex);
            }
            App.instance.setScene(SceneSwitcher.instance.getScene("Login"));
        });
    }

    public static void changeThemeColor(Color color){
        String background = "-fx-background-color: " + String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255.0),
                (int)(color.getGreen() * 255.0),
                (int)(color.getBlue() * 255.0)
        ) + ";";
        for(Menu menu : instances){
            menu.changeColor(background,
                    color.getOpacity()
            );
        }
        CreateAccount ca = SceneSwitcher.instance.getController("CreateAccount");
        ca.changeColor(background, color.getOpacity());
    }

    @Override
    public void changeColor(String background, double opacity){
        this.anchorPane.setStyle(background);
        this.anchorPane.setOpacity(opacity);
    }
}