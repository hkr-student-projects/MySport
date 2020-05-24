package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.App;
import model.Mailer.Mailer;
import model.Tools.SceneSwitcher;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ContactForm extends Menu implements Initializable {

    @FXML
    private Button clear, send, goBack;
    @FXML
    private TextField name, email;
    @FXML
    private TextArea message;

//    private String username; //without @gmail.com
//    private String password;
//    private String recipient;
//    private String subject = "Subject";
//    private String body;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTab(this);

        send.setOnAction(event -> {

            Mailer.sendMail(email.getText(), message.getText(), name.getText());

//            body = message.getText();
//            sendFromGMail(username, password, recipient, subject, body);
        });

    }

    @FXML
    public void clearFields() {
        name.setText(null);
        email.setText(null);
        message.setText(null);
    }

//    private static void sendFromGMail(String from, String pass, String to, String subject, String body) {
//        Properties props = System.getProperties();
//        String host = "smtp.gmail.com";
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.user", from);
//        props.put("mail.smtp.password", pass);
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//
//        Session session = Session.getDefaultInstance(props);
//        MimeMessage message = new MimeMessage(session);
//
//        try {
//            message.setFrom(new InternetAddress(from));
//            InternetAddress toAddress = new InternetAddress(to);
//
//
//            message.addRecipient(Message.RecipientType.TO, toAddress);
//
//
//            message.setSubject(subject);
//            message.setText(body);
//            Transport transport = session.getTransport("smtp");
//            transport.connect(host, from, pass);
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        } catch (AddressException ae) {
//            ae.printStackTrace();
//        } catch (MessagingException me) {
//            me.printStackTrace();
//        }
//    }

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
