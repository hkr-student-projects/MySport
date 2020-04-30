package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database.DatabaseManager;
import model.Logging.Logger;
import model.Tools.Block;
import model.Tools.Config;
import model.Tools.SceneSwitcher;

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
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("view/img/jarIcon.png"));
        primaryStage.setScene(SceneSwitcher.instance.getScene("Calendar"));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
        instance = this;
        Logger.logUML(Block.class, true);
    }

    public void setScene(Scene scene){
        this.stage.setScene(scene);
    }
}
