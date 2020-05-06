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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.App;
import model.Database.DatabaseManager;
import model.Logging.Logger;
import model.Tools.SceneSwitcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.restfb.logging.RestFBLogger.CLIENT_LOGGER;

public class Login extends Menu implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private Button forgotPassword;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Label error;
    @FXML
    private JFXToggleButton toggle;
    @FXML
    private Line line0, line1;

    private String appId = "2864933576924620";
    private String appSecret = "5b11cb850b5100f411224c64f188360f";
    private Stage stage;

    private static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setOnMouseClicked(e -> {
            if(toggle.isSelected())
            {
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                return;
            }
            if(email.getText().isEmpty() || password.getText().isEmpty()){
                redOutLines();
                return;
            }
            try {
                if(verifyFields()){
                    email.setText("");
                    password.setText("");
                }
            } catch (SQLException ex) {
                Logger.logException(ex);
            }
        });
    }

    private boolean verifyFields() throws SQLException {
        if(!email.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            error.setText("Incorrect email format");
            return false;
        }
        ResultSet rs1 = (ResultSet) App.databaseManager.executeQuery(DatabaseManager.QueryType.READER,
                "SELECT 1 FROM hotel.Account WHERE hotel.Account.email = '" + email.getText() + "' " +
                        "AND hotel.Account.id IN (SELECT `hotel`.`Employee`.`account_id` FROM `hotel`.`Employee`);");
        if(rs1.next()){
            error.setText("You are an employee!");
            return false;
        }

        ResultSet rs = (ResultSet) App.databaseManager.executeQuery(DatabaseManager.QueryType.READER,
                "SELECT 1 FROM hotel.Account WHERE hotel.Account.email = '" + email.getText() + "' " +
                        "AND hotel.Account.password = SHA2('" + password.getText() + "', 256);");
        if(!rs.next()){
            error.setText("Incorrect password or email");
        }
        else{
            error.setText("");
            return true;
        }

        return false;
    }

    private void redOutLines(){
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
            }
        });
    }

    @Override
    protected void onBurgerOpen() {

    }

    @Override
    protected void onBurgerClose() {

    }

    @Override
    protected void onSceneSwitch() {

    }

    @Override
    protected void onAppClose() {

    }
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
                            goToScreen2(accessToken.getAccessToken());
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

    public void goToScreen2(String accessToken) throws Exception{
        FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
        User user = null;
        try
        {
            user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email"));

        }
        catch (FacebookException ex)
        {
        }
        if(stage != null) {
            URL url = new File("src/view/Account.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Text lblData = (Text) root.lookup("#welcomeLabel");
            if(user != null && lblData != null) {
                lblData.setText("Welcome " + user.getName());
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }


    private static void consumeAccessToken(FacebookClient.AccessToken accessToken) {
        CLIENT_LOGGER.debug("Access token: " + accessToken.getAccessToken());
        CLIENT_LOGGER.debug("Expires: " + accessToken.getExpires());
    }

    public void handleForgotPasswordAction(ActionEvent actionEvent) {
        //show a screen to receive the user's email address
        if(stage != null) {
            URL url = null;
            try {
                url = new File("src/view/ResetCode.fxml").toURI().toURL();
                Parent root = FXMLLoader.load(url);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}




