package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Config config;
    public static DatabaseManager databaseManager;
    public static Main instance;
    private Stage stage;

    static {
        config = new Config();
//        databaseManager = new DatabaseManager();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(SceneSwitcher.instance.getScene("Calendar"));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
        instance = this;
    }

    public void setScene(Scene scene){
        this.stage.setScene(scene);
    }
}
