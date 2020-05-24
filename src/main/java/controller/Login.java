package controller;

import com.jfoenix.controls.JFXToggleButton;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.App;
import model.Tools.Language;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

import static com.restfb.logging.RestFBLogger.CLIENT_LOGGER;

public class Login implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private Button forgotPassword;
    @FXML
    private PasswordField password;
    @FXML
    private Button login, signUp;
    @FXML
    private Label error;
    @FXML
    private JFXToggleButton toggle;
    @FXML
    private Line line0, line1;

    private Stage stage;
    private static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";
    private String appId = "2864933576924620";
    private String appSecret = "5b11cb850b5100f411224c64f188360f";
    private static void consumeAccessToken(FacebookClient.AccessToken accessToken) {
        CLIENT_LOGGER.debug("Access token: " + accessToken.getAccessToken());
        CLIENT_LOGGER.debug("Expires: " + accessToken.getExpires());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                password.setText("");
                error.setText("");
            }
        });
        signUp.setOnMouseClicked(e -> App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccount")));
        login.setOnMouseClicked(e -> {
            if(toggle.isSelected())
            {
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                return;
            }
            if(checkEmail() && checkPassword()){
                int id;
                if((id = App.mySqlManager.checkCredentials(email.getText(), password.getText())) == -1){
                    error.setText("Incorrect email or password");
                    redLines();
                    return;
                }
                email.setText("");
                password.setText("");
                error.setText("");
                redLines();
                App.instance.setSession(App.mySqlManager.getUser(id));
                new Thread(
                        () -> ((Calendar)SceneSwitcher.instance.getController("Calendar")).loadUser()
                ).start();
                SceneSwitcher.controllers.forEach((n, c) -> {
                    if(c instanceof Menu)
                        ((Menu)c).buildSessionName();
                });
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                resetLines();
            }
        });
    }

    private boolean checkEmail() {
        if(email.getText().isBlank()){
            error.setText("Email is empty");
            return false;
        }
        else if(!email.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            error.setText("Incorrect email format");
            return false;
        }
        return true;
    }

    private boolean checkPassword(){
        if(password.getText().isBlank()){
            error.setText("Password is empty");
            return false;
        }
        else if(password.getText().length() < 5){
            error.setText("Incorrect password length");
            return false;
        }
        return true;
    }

    private void redLines(){
        //?change CSS class
        line0.setStroke(Paint.valueOf("#ff4c4c"));
        line0.setStrokeWidth(2);
        line1.setStroke(Paint.valueOf("#ff4c4c"));
        line1.setStrokeWidth(2);
        email.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                line0.setStroke(Paint.valueOf("#000000"));
                line0.setStrokeWidth(1);
                line1.setStroke(Paint.valueOf("#000000"));
                line1.setStrokeWidth(1);
                error.setText("");
            }
        });
    }

    private void resetLines(){
        line0.setStroke(Paint.valueOf("#000000"));
        line0.setStrokeWidth(1);
        line1.setStroke(Paint.valueOf("#000000"));
        line1.setStrokeWidth(1);
    }
    @FXML
    public void handleFBLoginButton(ActionEvent event) {
        System.out.println("In handle button");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        System.out.println("In startLogin");


        // obtain Facebook access token by loading login page
        stage = new Stage();
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);
        String loginDialogUrl = facebookClient.getLoginDialogUrl(appId, SUCCESS_URL, new ScopeBuilder());
        System.out.println(loginDialogUrl);
        webEngine.load(loginDialogUrl + "&display=popup&response_type=code");
        webEngine.locationProperty().addListener((property, oldValue, newValue) -> {
                    if (newValue.startsWith(SUCCESS_URL)) {
                        // extract access token
                        CLIENT_LOGGER.debug(newValue);
                        int codeOffset = newValue.indexOf("code=");
                        String code = newValue.substring(codeOffset + "code=".length());
                        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(
                                appId, appSecret, SUCCESS_URL, code);

                        // trigger further code's execution
                        consumeAccessToken(accessToken);

                        // close the app as goal was reached
                        stage.close();
                        try {
                            goToNextScene(accessToken.getAccessToken());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if ("https://www.facebook.com/dialog/close".equals(newValue)) {
                        throw new IllegalStateException("dialog closed");
                    }
                }
        );

        Scene scene = new Scene(webView);
        stage.setScene(scene);
        stage.show();
    }

    public void goToNextScene(String accessToken) throws Exception {
        FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
        com.restfb.types.User user = null;
        try {
            user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email"));

        } catch (FacebookException ignored) {
        }
        if (stage != null) {
            App.instance.setScene(SceneSwitcher.instance.getScene("Account"));

        }
    }
    @FXML
    public void handleForgotPasswordAction(ActionEvent actionEvent) {
        //show a screen to receive the user's email address
        App.instance.setScene(SceneSwitcher.instance.getScene("ResetCode"));

    }

}




