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
import model.Database.DatabaseManager;
import model.Logging.Logger;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login extends Menu implements Initializable {

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
}
