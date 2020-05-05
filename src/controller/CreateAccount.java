package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.App;
import model.Database.DatabaseManager;
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
    @FXML
    TextField password;
    @FXML
    TextField passwordRepeat;

    String accFirstName;
    String accMiddleName;
    String accLastName;
    String accSsn;
    String accEmail;
    String accPhoneNumber;
    String accPassword;


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
        bindTab(this);

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

//        try {
//
//            if (validateInput()) {
//                App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccountNo2"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccountNo2"));

    }

    @FXML
    public Boolean validateInput() {

        if (!firstName.getText().isBlank()) {
            if (!isNumber(firstName.getText())) {

                if (firstName.getText().matches("[a-zA-Z ]*")) {

                    accFirstName = firstName.getText();
                    System.out.println("Successful first name saving");
                    return true;


                } else {

                }

            } else {
                Alert alertBox = new Alert(Alert.AlertType.WARNING);
                alertBox.setContentText("First name can't contain numbers!");
                alertBox.setHeaderText("Warning!");
                alertBox.showAndWait();

            }


        } else {

        }
        return false;

    }

    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        try {
            int number = Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }


    @FXML
    public void buttonBackClick() {
        try {
            App.instance.setScene(SceneSwitcher.instance.getScene("CreateAccount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
