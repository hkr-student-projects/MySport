package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Database.MongoManager;
import model.Database.MySqlManager;
import model.People.User;
import model.Tools.Config;
import model.Tools.SceneSwitcher;

public class App extends Application {

    public static Config config;
    public static MySqlManager mySqlManager;
    public static MongoManager mongoManager;
    public static App instance;
    public static String login = "Login";
    private Stage stage;
    private User session;

    static {
        config = new Config();
        mySqlManager = new MySqlManager();
        mongoManager = new MongoManager();
    }

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("view/img/jarIcon.png"));
        primaryStage.setScene(SceneSwitcher.instance.getScene(login));
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
