package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    @FXML
    public void changeToCalendar(ActionEvent event) throws IOException {

        Parent newParent = FXMLLoader.load(getClass().getResource("/View/Calendar.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene newScene = new Scene(newParent);
        window.setScene(newScene);

    }
}
