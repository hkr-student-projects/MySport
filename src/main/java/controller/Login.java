package controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import model.App;
import model.People.User;
import model.Tools.SceneSwitcher;
import model.Client.mediator.ChatMediatorClient;
import model.Client.viewModel.ChatClientViewModel;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.ResourceBundle;

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
                if( App.mySqlManager == null){
                    System.out.println("MySqlManager is null");
                }
                if((id = App.mySqlManager.checkCredentials(
                        email.getText(),
                        password.getText())) == -1) {
                    error.setText("Incorrect email or password");
                    redLines();
                    return;
                }
                email.setText("");
                password.setText("");
                error.setText("");
                User user = App.mySqlManager.getUser(id);
                App.instance.setSession(user);
                new Thread(
                        () -> ((Calendar)SceneSwitcher.instance.getController("Calendar")).loadUser()
                ).start();
                SceneSwitcher.controllers.forEach((n, c) -> {
                    if(c instanceof Menu)
                        ((Menu)c).buildSessionName();
                });
                setupMessaging(user);
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                resetLines();
            }
        });
    }

    private boolean checkEmail() {
        if(email.getText().isEmpty()){
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
        if(password.getText().isEmpty()){
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
    private void setupMessaging(User user){
        Messaging messagingController = null;
        System.out.println("In messaging initializer");
        try {
            //messagingRoot = loader.load();
            messagingController = (Messaging) SceneSwitcher.controllers.get("Messaging");
            model.Client.models.User localUser= new model.Client.models.User();
            localUser.setId(user.getId());
            localUser.setMobile(user.getMobile());
            localUser.setName(user.getName());
            System.out.println("Messaging initialiser " + localUser.getName() + " : " + localUser.getMobile());
            ChatMediatorClient chatMediatorClient = new ChatMediatorClient(localUser);
            ChatClientViewModel chatClientViewModel = new ChatClientViewModel(user.getName(), chatMediatorClient);

            messagingController.init(chatClientViewModel);

            SceneSwitcher.controllers.put("Messaging", messagingController);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}




