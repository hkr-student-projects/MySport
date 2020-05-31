package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.App;
import model.Mailer.Mailer;
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
    final String username = "mysport.hkr@gmail.com";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);

        send.setOnAction(event -> {

            Mailer.sendMail(email.getText(), username ,message.getText(), name.getText());
            clearFields();

        });

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
