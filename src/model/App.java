package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database.DatabaseManager;
import model.People.User;
import model.Tools.Config;
import model.Tools.SceneSwitcher;
import model.Tools.ThreadResult;

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

    public Stage getStage(){
        return this.stage;
    }

    public User getSession() {
        return this.session;
    }

    public void setSession(User session) {
        this.session = session;
    }
}
