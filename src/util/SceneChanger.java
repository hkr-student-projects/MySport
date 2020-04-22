package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChanger {

    private SceneChanger() {}

    public static void switchScene(ActionEvent event, String path) {
        try {
            Node node = (Node) event.getSource();
            Scene scene = node.getScene();
            Stage stage = (Stage) scene.getWindow();
            FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource(path));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } catch (IOException e) {
            System.err.println("Failed loading scene");
            e.printStackTrace();
        }
    }


}


