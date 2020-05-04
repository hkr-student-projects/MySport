package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccount extends Menu implements Initializable {

    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField middleName;
    @FXML
    TextField ssn;
    @FXML
    TextField email;
    @FXML
    TextField phoneNumber;


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void buttonAccountClick() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Account"));

    }

    @FXML
    public void buttonClearAllClick() {

        firstName.setText(null);
        middleName.setText(null);
        lastName.setText(null);
        ssn.setText(null);
        email.setText(null);
        phoneNumber.setText(null);


    }

    @FXML
    public void buttonNextClick() {

    }

    public void validateInput(){

    }
}
