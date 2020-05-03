package model;

import controller.Calendar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database.DatabaseManager;
import model.Logging.Logger;
import model.People.User;
import model.Tools.Block;
import model.Tools.Config;
import model.Tools.SceneSwitcher;

public class App extends Application {

    public static Config config;
    public static DatabaseManager databaseManager;
    public static App instance;
    private Stage stage;
    private User session;

    static {
        config = new Config();
        databaseManager = new DatabaseManager();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("view/img/jarIcon.png"));
        primaryStage.setScene(SceneSwitcher.instance.getScene("Login"));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
        instance = this;
    }

    public void setScene(Scene scene){
        this.stage.setScene(scene);
    }

    public User getSession() {
        return session;
    }

    public void setSession(User session) {
        this.session = session;
    }
}
