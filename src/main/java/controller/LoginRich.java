package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.App;
import model.Tools.SceneSwitcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.restfb.logging.RestFBLogger.CLIENT_LOGGER;

public class LoginRich implements Initializable {


    @FXML
    private MediaView mediaView;
    @FXML
    private JFXButton loginButton, forgotPasswordButton, fbLoginButton, clearButton, createButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TabPane tabPaneLogin;
    @FXML
    private Tab tabSignIn, tabSignUp;
    @FXML
    private JFXPasswordField passwordTextField1, passwordTextField2, confirmPassword;
    @FXML
    private Label labelClickSignUp;
    @FXML
    private JFXTextField firstNameFiled, lastNameField, usernameTextField, emailTextField, ssnTextField, mobileNbrField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Pane slidingPane;
    @FXML
    private Label labelSignUp, labelSignIn, labelSign;

    private static final String SUCCESS_URL = "https://www.facebook.com/connect/login_success.html";
    private String appId = "2864933576924620";
    private String appSecret = "5b11cb850b5100f411224c64f188360f";

    private Stage stage;

    private Tooltip signInTip, signUpTip, usernameTip, passwordTip, forgotTip, loginTip, fbLoginTip;

    private static void consumeAccessToken(FacebookClient.AccessToken accessToken) {
        CLIENT_LOGGER.debug("Access token: " + accessToken.getAccessToken());
        CLIENT_LOGGER.debug("Expires: " + accessToken.getExpires());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media media = new Media(getClass().getProtectionDomain().getCodeSource().getLocation() + "/view/vid/MySPORT_video.mp4");
        MediaPlayer player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(anchorPane.widthProperty());
        mediaView.fitHeightProperty().bind(anchorPane.heightProperty());

        Rectangle2D viewportRect = new Rectangle2D(500, 200, 799, 462);
        mediaView.setViewport(viewportRect);

        player.setVolume(0);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();

        RegexValidator emailRegex = new RegexValidator();
        usernameTextField.getValidators().add(emailRegex);
        if (!usernameTextField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailRegex.setMessage("Incorrect email format");
        }

        RequiredFieldValidator emailValidator = new RequiredFieldValidator();
        RequiredFieldValidator passwordValidator = new RequiredFieldValidator();
        usernameTextField.getValidators().add(emailValidator);
        // passwordTextField1.getValidators().add(passwordValidator);
        emailValidator.setMessage("No input given");
        passwordValidator.setMessage("No input given");


        usernameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    usernameTextField.validate();
                }
            }
        });
        //     passwordTextField1.focusedProperty().addListener(new ChangeListener<Boolean>() {
        //         @Override
        //         public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        //             if (!newValue) {
        //                 passwordTextField1.validate();
        //             }
        //         }
        //     });

        try {
            Image icon = new Image(new FileInputStream("src/sample/img/disapprove.png"));
            emailValidator.setIcon(new ImageView(icon));
            passwordValidator.setIcon(new ImageView(icon));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        signInTip = new Tooltip("Press SIGN IN to login into MySPORT");
        labelSignIn.setTooltip(signInTip);
        signInTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        signUpTip = new Tooltip("Press SIGN UP to create MySPORT account");
        labelSignUp.setTooltip(signUpTip);
        signUpTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        usernameTip = new Tooltip("Enter your email address here");
        usernameTextField.setTooltip(usernameTip);
        usernameTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        passwordTip = new Tooltip("Password should be at least 6 characters in length");
        //   passwordTextField1.setTooltip(passwordTip);
        passwordTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        forgotTip = new Tooltip("Press FORGOT PASSWORD to recovery your password");
        forgotPasswordButton.setTooltip(forgotTip);
        forgotTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        loginTip = new Tooltip("Login with your MySPORT credentials");
        loginButton.setTooltip(loginTip);
        loginTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");

        fbLoginTip = new Tooltip("Login with your Facebook credentials");
        fbLoginButton.setTooltip(fbLoginTip);
        fbLoginTip.setStyle("-fx-background-color: #FFFF8D; -fx-text-fill: #8f3942;");
    }

    public void handleLoginButton(ActionEvent actionEvent) {
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
        User user = null;
        try {
            user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email"));

        } catch (FacebookException ignored) {
        }
        if (stage != null) {
            App.instance.setScene(SceneSwitcher.instance.getScene("Account"));
            //URL url = new File("sample/Account.fxml").toURI().toURL();
            //  File myFile = new File("word.txt");
            //  System.out.println("Attempting to read from file in: "+myFile.getCanonicalPath());

            //  Scanner input = new Scanner(myFile);
            //  String in = "";
            //  in = input.nextLine();

            //Parent root = FXMLLoader.load(url);
            //Text lblData = (Text) root.lookup("#welcomeLabel");
            //if (user != null && lblData != null) {
            //    lblData.setText("Welcome " + user.getName());
            //}

            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            //stage.show();
        }
    }
    @FXML
    public void handleForgotPasswordAction(ActionEvent actionEvent) {
        //show a screen to receive the user's email address
        App.instance.setScene(SceneSwitcher.instance.getScene("ResetCode"));

    }


    @FXML
    void openSignInTab(MouseEvent event) {
        TranslateTransition leftTransition = new TranslateTransition(new Duration(500), labelSign);
        leftTransition.setToX(slidingPane.getTranslateX());
        leftTransition.play();
        leftTransition.setOnFinished(event1 ->
                labelSign.setText("SIGN IN"));

        tabPaneLogin.getSelectionModel().select(tabSignIn);
    }


    @FXML
    void openSignUpTab(MouseEvent event) {
        TranslateTransition rightTransition = new TranslateTransition(new Duration(500), labelSign);
        rightTransition.setToX(slidingPane.getTranslateX() + (slidingPane.getPrefWidth() - labelSign.getPrefWidth()));
        rightTransition.play();
        rightTransition.setOnFinished(event1 ->
                labelSign.setText("SIGN UP"));

        tabPaneLogin.getSelectionModel().select(tabSignUp);
    }


}




