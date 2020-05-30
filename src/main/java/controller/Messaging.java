package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import model.Client.viewModel.ChatClientViewModel;
import model.Client.viewModel.ConversationRowData;
import model.Client.viewModel.UserRowData;

import java.net.URL;
import java.util.ResourceBundle;



public class Messaging extends Menu implements Initializable { // extends Menu

    @FXML
    public ListView convListView,msgListView ;

    @FXML
    private Pane pane;

    //  @FXML
    //  private Button home;
//
    //  @FXML
    //  private Button account;
//
    //  @FXML
    //  private Button mail;
//
    //  @FXML
    //  private Button forum;
//
    //  @FXML
    //  private Button calendar;
//
    //  @FXML
    //  private Button settings;
//
    //  @FXML
    //  private Button logout;

    @FXML
    private AnchorPane rootPane,titleBar ,detailPane ;

    @FXML
    private AnchorPane chatPane;

    @FXML
    private TextArea txtMsg;

    @FXML
    private VBox chatBox, clientListBox, box;

    @FXML
    private Button btnSend, btnEmoji, btnClose;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextFlow emojiList;

    @FXML
    private JFXDrawer drawerPane;

    @FXML
    private ScrollPane clientListScroll;



    @FXML
    private JFXButton inbox, sent , bin;

    @FXML
    private ComboBox namesList;

    private Tooltip homeTip, accountTip, mailTip, forumTip, calendarTip, settingTip, logOutTip, inboxTip, sentTip, binTip;



    private ChatClientViewModel model;

    public void init(ChatClientViewModel viewModel)
    {
        System.out.println("In Controller init");
        model = viewModel;
        viewModel.setController(this);
        this.namesList.getItems().addAll(model.getUsers());

        convListView.setItems(model.getConversationRowData());
        convListView.setCellFactory(conversationListView -> new ConversationListViewCell());

        msgListView.setItems(model.getConversationMessages());
        msgListView.setCellFactory(messageListView -> new MessageListViewCell());

        selectLastitemInMessagesList();

        namesList.setItems(model.getUsers());

        namesList.setCellFactory(new Callback<ListView<UserRowData>,ListCell<UserRowData>>(){

            @Override
            public ListCell<UserRowData> call(ListView<UserRowData> p) {

                final ListCell<UserRowData> cell = new ListCell<UserRowData>(){

                    @Override
                    protected void updateItem(UserRowData t, boolean bln) {
                        super.updateItem(t, bln);

                        if(t != null){
                            setText(t.getUsername());
                        }else{
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });
        namesList.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (! isShowing) {
                //System.out.println("Combo box popup hidden");
                newUserConversation(null);
            }
        });
        /*
        nameField.textProperty().bindBidirectional(viewModel.getNameProperty());
        numberField.textProperty().bindBidirectional(viewModel.getNumberProperty());
        resultLabel.textProperty().bind(viewModel.getResultProperty());

        numberColumn.setCellValueFactory(d -> d.getValue().getNumberProperty());
        nameColumn.setCellValueFactory(d -> d.getValue().getNameProperty());

        studentTable.setItems(viewModel.getAllStudents());

         */
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                txtMsg.setText(txtMsg.getText()+" "+((Text)text).getText());
                emojiList.setVisible(false);
            });
        }

        bindTab(this);
        System.out.println("In initialize Messaging");
        //  homeTip = new Tooltip("Home");
        //  home.setTooltip(homeTip);
        //  homeTip.setStyle("-fx-background-color: #763DEE");
//
        //  accountTip = new Tooltip("Account");
        //  account.setTooltip(accountTip);
        //  accountTip.setStyle("-fx-background-color: #763DEE");
//
        //  mailTip = new Tooltip("Mail");
        //  mail.setTooltip(mailTip);
        //  mailTip.setStyle("-fx-background-color: #763DEE");
//
        //  forumTip = new Tooltip("Forum");
        //  forum.setTooltip(forumTip);
        //  forumTip.setStyle("-fx-background-color: #763DEE");
//
        //  calendarTip = new Tooltip("Calendar");
        //  calendar.setTooltip(calendarTip);
        //  calendarTip.setStyle("-fx-background-color: #763DEE");
//
        //  settingTip = new Tooltip("Settings");
        //  setting.setTooltip(settingTip);
        //  settingTip.setStyle("-fx-background-color: #763DEE");
//
        //  logOutTip = new Tooltip("Log Out");
        //  logout.setTooltip(logOutTip);
        //  logOutTip.setStyle("-fx-background-color: #763DEE");
//
        inboxTip = new Tooltip("Inbox");
        inbox.setTooltip(inboxTip);
        inboxTip.setStyle("-fx-background-color: #763DEE");

        sentTip = new Tooltip("Sent");
        sent.setTooltip(sentTip);
        sentTip.setStyle("-fx-background-color: #763DEE");

        binTip = new Tooltip("Bin");
        bin.setTooltip(binTip);
        binTip.setStyle("-fx-background-color: #763DEE");



        drawerPane.setSidePane(pane);
        System.out.println("Drawer finished");
      ////  HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(burger);
        //System.out.println("transition finished");
        //transition.setRate(-1);
        //burger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
        //    transition.setRate(transition.getRate() * -1);
        //    transition.play();

       //     if (drawerPane.isOpened()) {
       //         drawerPane.close();
       //     } else {
       //         drawerPane.open();
       //     }
       // });
       // System.out.println("hamburger finished");
    }

    public ChatClientViewModel getModel() {
        return model;
    }

    public void setModel(ChatClientViewModel model) {
        this.model = model;
    }

    public void selectLastitemInMessagesList(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int size = model.getConversationMessages().size();
                msgListView.scrollTo(size);
                msgListView.getSelectionModel().select(size);
            }
        });
    }

    @FXML
    void emojiAction(ActionEvent event) {
        if (emojiList.isVisible()) {
            emojiList.setVisible(false);
        } else {
            emojiList.setVisible(true);
        }
    }

    @FXML
    public void clickTextField(ActionEvent event){
        System.out.println("Text field clicked");
        txtMsg.requestFocus();
    }

    @FXML
    public void clickConversation(ActionEvent event){
        // get object clicked in ListView
    }

    @FXML
    public void handleConversationClick(MouseEvent event) {
        ConversationRowData conversationRowData = (ConversationRowData) convListView.getSelectionModel().getSelectedItem();
        if(conversationRowData != null) {
            System.out.println("clicked on " + conversationRowData.getName());
            model.loadMessages(conversationRowData);
        }
        selectLastitemInMessagesList();
        txtMsg.requestFocus();
    }

    @FXML
    void sendAction(ActionEvent event) {
        if (txtMsg.getText().trim().equals("")) return;
        String message = txtMsg.getText().trim();
        model.sendMessage(message);
        //controller.notifyAllClients(username,txtMsg.getText().trim());
        txtMsg.setText("");
        selectLastitemInMessagesList();
        txtMsg.requestFocus();
    }

    @FXML
    public void newUserConversation(ActionEvent event) {
        UserRowData userRowData = (UserRowData)namesList.getSelectionModel().getSelectedItem();
        model.initiateConversation(userRowData.getUser());
        selectLastitemInMessagesList();
        txtMsg.requestFocus();
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

    //  @Override
    //  protected void onBurgerOpen() {
//
    //  }
//
    //  @Override
    //  protected void onBurgerClose() {
//
    //  }
//
    //  @Override
    //  protected void onBeforeSceneSwitch() {
//
    //  }
//
    //  @Override
    //  protected void onBeforeLogout() {
    //       }

}
