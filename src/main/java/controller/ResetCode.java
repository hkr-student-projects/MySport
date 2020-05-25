package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetCode implements Initializable {
    @FXML
    private JFXTextField resetCodeField;

    @FXML
    private JFXButton resetButton;

    //UserDAO dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //dao = new UserDAO();
        NumberValidator numValidator = new NumberValidator();
        resetCodeField.getValidators().add(numValidator);
        numValidator.setMessage("Numerical input only!");
        resetCodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    resetCodeField.validate();
                }
            }
        });
    }

    @FXML
    void handleResetCode(ActionEvent event) {
//        String resetcode = resetCodeButton.getText();
//        String email = dao.checkResetToken(resetcode);
//        if(email != null){
//            Utilities.resetEmail = email;
        App.instance.setScene(SceneSwitcher.instance.getScene("NewPassword"));
//        }
    }

}
