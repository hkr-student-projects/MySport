package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private AnchorPane anchorPane;
    @FXML private MediaView mediaView;
    @FXML private JFXButton loginButton;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordTestField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Media media = new Media(getClass().getProtectionDomain().getCodeSource().getLocation() + "/view/resources/MySPORT_video.mp4");
        MediaPlayer player=new MediaPlayer(media);
        mediaView.setMediaPlayer(player);
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(anchorPane.widthProperty());
        mediaView.fitHeightProperty().bind(anchorPane.heightProperty());

        Rectangle2D viewportRect = new Rectangle2D(500, 200, 799, 462);
        mediaView.setViewport(viewportRect);

        player.setVolume(0);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();



        RequiredFieldValidator validator = new RequiredFieldValidator();
        usernameTextField.getValidators().add(validator);
        validator.setMessage("No input given");

        usernameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    usernameTextField.validate();
                }
            }
        });

    }

    @FXML
    void handleLoginButton(ActionEvent event) {

    }
}
