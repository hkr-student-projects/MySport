package controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import model.App;
import model.Client.mediator.ChatMediatorClient;
import model.Client.viewModel.ChatClientViewModel;
import model.People.User;
import model.Tools.ArrayList;
import model.Tools.Colorable;
import model.Tools.SceneSwitcher;
import model.Tools.ThreadResult;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.Map;
import java.util.ResourceBundle;

public class Login implements Initializable, Colorable {

    @FXML
    private AnchorPane anchorPane;
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
    private Boolean emailFormat = false;
    private Map<String, model.Tools.ArrayList<String>> emailsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread getEmails = new Thread(() -> emailsList = App.mySqlManager.getEmails());
        getEmails.start();
        password.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                password.setText("");
                error.setText("");
            }
        });
        email.setOnKeyTyped(this::emailChecking);
        signUp.setOnMouseClicked(e -> App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccount")));
        login.setOnMouseClicked(e -> {
            if (toggle.isSelected()) {
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                return;
            }
            if (!emailFormat || !passwordFormat())
                return;
            Thread verification = new Thread(() -> {
                int id;
                if ((id = App.mySqlManager.checkCredentials(
                        email.getText(),
                        password.getText())) == -1) {
                    redLines();
                    error.setText("Incorrect credentials!");

                    return;
                }
                User user = App.mySqlManager.getUser(id);
                App.instance.setSession(user);
                ((Calendar) SceneSwitcher.instance.getController("Calendar")).loadUser();
                SceneSwitcher.controllers.forEach((n, c) -> {
                    if (c instanceof Menu) {
                        ((Menu) c).buildSessionName();
                    }
                });
                email.setText("");
                password.setText("");
                error.setText("");
                resetLines();
                setupMessaging(user);
                Platform.runLater(() -> App.instance.setScene(SceneSwitcher.instance.getScene("Home")));
            });
            verification.start();
        });
        try {
            getEmails.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Boolean existsEmail(String email) {
        ArrayList<String> entries = emailsList.get("email");

        for (int i = 0; i < entries.size(); i++)
            if (entries.get(i).equals(email))
                return true;
        return false;
    }

//    private boolean checkEmail() {
//        if (email.getText().isEmpty()) {
//            error.setText("Email is empty");
//            return false;
//        } else if (!email.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//            error.setText("Incorrect email format");
//            return false;
//        }
//        return true;
//    }

    private boolean passwordFormat() {
        if (password.getText().isEmpty()) {
            error.setText("Password is empty");
            return false;
        } else if (password.getText().length() < 5) {
            error.setText("Incorrect password length");
            return false;
        }
        return true;
    }

    private void redLines() {
        //?change CSS class
        line0.setStroke(Paint.valueOf("#ff4c4c"));
        line0.setStrokeWidth(2);
        line1.setStroke(Paint.valueOf("#ff4c4c"));
        line1.setStrokeWidth(2);
        email.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                line0.setStroke(Paint.valueOf("#000000"));
                line0.setStrokeWidth(1);
                line1.setStroke(Paint.valueOf("#000000"));
                line1.setStrokeWidth(1);
                error.setText("");
            }
        });
    }

    private void resetLines() {
        line0.setStroke(Paint.valueOf("#000000"));
        line0.setStrokeWidth(1);
        line1.setStroke(Paint.valueOf("#000000"));
        line1.setStrokeWidth(1);
    }

    @Override
    public void changeColor(String background, double opacity) {
        this.anchorPane.setStyle(background);
        this.anchorPane.setOpacity(opacity);
    }


    private void emailChecking(KeyEvent e) {

//        if (e.getCode() == KeyCode.BACK_SPACE && email.getText().isEmpty())
//            emailStr = "";
//        else if (e.getCode() == KeyCode.BACK_SPACE && email.getText().length() > 0)
//            emailStr = emailStr.substring(0, emailStr.length() - 2);
//        else
//            emailStr += e.getText();

        if (email.getText().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9]{2,10}\\.)+[A-Za-z]{2,8}$")) {
            ThreadResult<String, Boolean> emailCheck = new ThreadResult<>(this::existsEmail, email.getText());
            Thread thread = new Thread(emailCheck);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            //System.out.println(emailCheck.getValue());
            if (!emailCheck.getValue()) {
                emailFormat = false;
                error.setText("Email doesn't exist!");
            } else {
                emailFormat = true;
                error.setText("");
            }
        }
        else {
            emailFormat = false;
            resetLines();
        }
    }

    public static void setupMessaging(User user){
        Messaging messagingController;
        //System.out.println("In messaging initializer");
        try {
            //messagingRoot = loader.load();
            messagingController = (Messaging) SceneSwitcher.controllers.get("Messaging");
            model.Client.models.User localUser= new model.Client.models.User();
            localUser.setId(user.getId());
            localUser.setMobile(user.getMobile());
            localUser.setName(user.getName());
            //System.out.println("Messaging initialiser " + localUser.getName() + " : " + localUser.getMobile());
            ChatMediatorClient chatMediatorClient = new ChatMediatorClient(localUser);
            ChatClientViewModel chatClientViewModel = new ChatClientViewModel(user.getName(), chatMediatorClient);

            messagingController.init(chatClientViewModel);

            SceneSwitcher.controllers.put("Messaging", messagingController);
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}





