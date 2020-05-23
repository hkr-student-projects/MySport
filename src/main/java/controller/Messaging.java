package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class Messaging implements Initializable {
    @FXML
    private Pane pane;

    @FXML
    private Button home;

    @FXML
    private Button account;

    @FXML
    private Button mail;

    @FXML
    private Button forum;

    @FXML
    private Button calendar;

    @FXML
    private Button settings;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane chatPane;

    @FXML
    private TextArea txtMsg;

    @FXML
    private VBox chatBox;

    @FXML
    private Button btnSend;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextFlow emojiList;

    @FXML
    private Button btnEmoji;
    @FXML
    private JFXDrawer drawerPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXButton setting;

    @FXML
    private JFXButton inbox;

    @FXML
    private JFXButton sent;

    @FXML
    private JFXButton bin;

    private Tooltip homeTip, accountTip, mailTip, forumTip, calendarTip, settingTip, logOutTip, inboxTip, sentTip, binTip;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeTip = new Tooltip("Home");
        home.setTooltip(homeTip);
        homeTip.setStyle("-fx-background-color: #763DEE");

        accountTip = new Tooltip("Account");
        account.setTooltip(accountTip);
        accountTip.setStyle("-fx-background-color: #763DEE");

        mailTip = new Tooltip("Mail");
        mail.setTooltip(mailTip);
        mailTip.setStyle("-fx-background-color: #763DEE");

        forumTip = new Tooltip("Forum");
        forum.setTooltip(forumTip);
        forumTip.setStyle("-fx-background-color: #763DEE");

        calendarTip = new Tooltip("Calendar");
        calendar.setTooltip(calendarTip);
        calendarTip.setStyle("-fx-background-color: #763DEE");

        settingTip = new Tooltip("Settings");
        setting.setTooltip(settingTip);
        settingTip.setStyle("-fx-background-color: #763DEE");

        logOutTip = new Tooltip("Log Out");
        logout.setTooltip(logOutTip);
        logOutTip.setStyle("-fx-background-color: #763DEE");

        inboxTip = new Tooltip("Inbox");
        inbox.setTooltip(inboxTip);
        inboxTip.setStyle("-fx-background-color: #763DEE");

        sentTip = new Tooltip("Sent");
        sent.setTooltip(sentTip);
        sentTip.setStyle("-fx-background-color: #763DEE");

        binTip = new Tooltip("Bin");
        bin.setTooltip(binTip);
        binTip.setStyle("-fx-background-color: #763DEE");



        drawer.setSidePane(pane);
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (drawer.isOpened()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }
   // System.out.println(username);
   //  for(Node text : emojiList.getChildren()){
   //      text.setOnMouseClicked(event -> {
   //          txtMsg.setText(txtMsg.getText()+" "+((Text)text).getText());
   //          emojiList.setVisible(false);
   //      });
   //  }



    @FXML
    void emojiAction(ActionEvent event) {
        if (emojiList.isVisible()) {

            emojiList.setVisible(false);
        } else {
            emojiList.setVisible(true);
        }
    }


    @FXML
    void sendAction(ActionEvent event) {
        if (txtMsg.getText().trim().equals("")) return;
        //controller.notifyAllClients(username,txtMsg.getText().trim());
        txtMsg.setText("");
        txtMsg.requestFocus();
    }



}
