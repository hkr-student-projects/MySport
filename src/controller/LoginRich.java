package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.scope.ScopeBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.People.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.restfb.logging.RestFBLogger.CLIENT_LOGGER;

public class LoginRich implements Initializable {
    @FXML private MediaView mediaView;
    @FXML private JFXButton loginButton;
    @FXML private JFXPasswordField passwordTestField;
    @FXML private JFXTextField usernameTextField;
    @FXML private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media media = new Media(getClass().getProtectionDomain().getCodeSource().getLocation() + "/view/vid/MySPORT_video.mp4");
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
    public void handleFBLoginButton(ActionEvent event) {
        System.out.println("In handle button");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        System.out.println("In startLogin");
        /*
        // create the scene
        stage.setTitle("Facebook Login Example");
        // use quite a wide window to handle cookies popup nicely
        stage.setScene(new Scene(new VBox(webView), 1000, 600, Color.web("#666970")));
        stage.show();
        */

        // obtain Facebook access token by loading login page
//        stage = new Stage();
//        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
//        String loginDialogUrl = facebookClient.getLoginDialogUrl(appId, SUCCESS_URL, new ScopeBuilder());
//        System.out.println(loginDialogUrl);
//        webEngine.load(loginDialogUrl + "&display=popup&response_type=code");
//        webEngine.locationProperty().addListener((property, oldValue, newValue) -> {
//                    if (newValue.startsWith(SUCCESS_URL)) {
//                        // extract access token
//                        CLIENT_LOGGER.debug(newValue);
//                        int codeOffset = newValue.indexOf("code=");
//                        String code = newValue.substring(codeOffset + "code=".length());
//                        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(
//                                appId, appSecret, SUCCESS_URL, code);
//
//                        // trigger further code's execution
//                        consumeAccessToken(accessToken);
//
//                        // close the app as goal was reached
//                        stage.close();
//                        try {
//                            goToScreen2(accessToken.getAccessToken());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else if ("https://www.facebook.com/dialog/close".equals(newValue)) {
//                        throw new IllegalStateException("dialog closed");
//                    }
//                }
//        );
//
//        Scene scene = new Scene(webView);
//        stage.setScene(scene);
//        stage.show();
    }
//
//    public void goToScreen2(String accessToken) throws Exception{
//        FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
//        User user = null;
//        try
//        {
//            user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email"));
//
//        }
//        catch (FacebookException ignored)
//        {
//        }
//        if(stage != null) {
//            URL url = new File("src/view/Account.fxml").toURI().toURL();
//            Parent root = FXMLLoader.load(url);
//            Text lblData = (Text) root.lookup("#welcomeLabel");
//            if(user != null && lblData != null) {
//                lblData.setText("Welcome " + user.getName());
//            }
//
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        }
//    }
//
//
//    private static void consumeAccessToken(FacebookClient.AccessToken accessToken) {
//        CLIENT_LOGGER.debug("Access token: " + accessToken.getAccessToken());
//        CLIENT_LOGGER.debug("Expires: " + accessToken.getExpires());
//    }
//
//    public void handleForgotPasswordAction(ActionEvent actionEvent) {
//        //show a screen to receive the user's email address
//        if(stage != null) {
//            URL url = null;
//            try {
//                url = new File("src/view/ResetCode.fxml").toURI().toURL();
//                Parent root = FXMLLoader.load(url);
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
    @FXML
    public void handleLoginButton(ActionEvent actionEvent) {
    }
}


