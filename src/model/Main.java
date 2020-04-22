package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(SceneSwitcher.getScene("Home.fxml"));
        this.stage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.show();
        instance = this;
    }

    public void setScene(Scene scene){
        this.stage.setScene(scene);
    }
}
