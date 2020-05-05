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
import model.Logging.Logger;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private TextField email;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                password.setText("");
                error.setText("");
            }
        });
        login.setOnMouseClicked(e -> {
            if(toggle.isSelected())
            {
                App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                return;
            }
            try {
                if(checkFormat()){
                    int id;
//                    Thread thread = new Thread(() -> {
//                        try {
//                            App.databaseManager.checkCredentials(email.getText(), password.getText());
//                        } catch (SQLException throwables) {
//                            throwables.printStackTrace();
//                        }
//                    });
//                    thread.start();
//                    thread.join();
                    if((id = App.databaseManager.checkCredentials(email.getText(), password.getText())) == -1){
                        error.setText("Incorrect email or password");
                        redLines();
                        return;
                    }
                    email.setText("");
                    password.setText("");
                    error.setText("");
                    App.instance.setSession(App.databaseManager.getUser(id));
                    App.instance.setScene(SceneSwitcher.instance.getScene("Home"));
                }
            } catch (SQLException ex) {
                Logger.logException(ex);
            }
        });
    }

    private boolean checkFormat() {
        if(email.getText().isBlank() || password.getText().isBlank() || !email.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            error.setText("Incorrect email format");
            return false;
        }
//        DatabaseManager.AccountType acc = App.databaseManager.checkCredentials(email.getText(), password.getText());
//        if(acc == DatabaseManager.AccountType.NONE){
//            error.setText("Incorrect email or password");
//            return false;
//        }

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
}
