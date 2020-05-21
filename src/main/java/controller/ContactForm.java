package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.App;
import model.Tools.SceneSwitcher;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactForm extends Menu implements Initializable {

    @FXML
    private Button clear, send, goBack;
    @FXML
    private TextField name, email;
    @FXML
    private TextArea message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);

    }

    @FXML
    public void clearFields() {
        name.setText(null);
        email.setText(null);
        message.setText(null);
    }

    @FXML
    public void returnButtonPress() {
        App.instance.setScene(SceneSwitcher.instance.getScene("Support"));
    }

    @Override
    protected void onBurgerOpen() {

    }

    @Override
    protected void onBurgerClose() {

    }

    @Override
    protected void onBeforeSceneSwitch() {

    }

    @Override
    protected void onBeforeLogout() {

    }
}
