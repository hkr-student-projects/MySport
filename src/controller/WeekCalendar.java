package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class WeekCalendar implements Initializable {

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPane.setOnMouseReleased(e -> {
            out.println("Drag over!");
        });
        gridPane.setOnMousePressed(e -> {
            out.println("drag detected");
        });
    }
}
