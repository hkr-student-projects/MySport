package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import static java.lang.System.out;

public class App extends Application {

    public static Config config;
    public static DatabaseManager databaseManager;
    public static App instance;
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
