package model.Client.viewModel;

//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import mediator.ChatMediatorClient;
//import models.Conversation;
//import models.Message;
//import models.User;
//import observable.listeners.LocalListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChatClientViewModel implements LocalListener<User, Message, Conversation> {
//
//    private ObservableList<MessageRowData> conversationMessages;
//
//    private ObservableList<UserRowData> users;
//
//    private ObservableList<ConversationRowData> conversationRowData;
//
//    private List<Conversation> userConversations;
//
//    private int currentConversationID = 0;
//
//    boolean found = false;
//
//    private String currentUser;
//
//    private ChatMediatorClient mediator;
//
//    public ChatClientViewModel(String currentUser, ChatMediatorClient mediator) {
//        conversationMessages = FXCollections.observableArrayList();
//        users = FXCollections.observableArrayList();
//        conversationRowData = FXCollections.observableArrayList();
//        userConversations = new ArrayList<>();
//        this.currentUser = currentUser;
//        this.mediator = mediator;
//        this.mediator.addListener(this);
//        this.mediator.loadTeamMembersContacts();
//        this.mediator.loadConversations();
//    }
//
//    public void initializeUsers(List<User> usersList){
//        users.clear();
//        usersList.forEach(i -> users.add(new UserRowData(i)));
//    }
//
//    public void initializeConversation(List<Conversation> conversationList){
//        conversationRowData.clear();
//        conversationList.forEach(i -> conversationRowData.add(new ConversationRowData(i.getId(),
//                i.conversationName(currentUser), i.getMessageList().get(0).getMessage())));
//    }
//
//
//    public void initializeConversationMessages(List<Message> conversationMessagesList){
//        conversationMessages.clear();
//        conversationMessagesList.forEach(i -> conversationMessages.add(new MessageRowData(i)));
//    }
//
//    public void setUserConversations(List<Conversation> userConversations) {
//        this.userConversations = userConversations;
//    }
//
//    @Override
//    public void receiveUsers(List<User> users) {
//        initializeUsers(users);
//    }
//
//
//    @Override
//    public void receiveMessage(Message message, int conversationID) {
//        // search conversation list if id exist
//        found = false;
//        userConversations.forEach(i -> {
//            if(i.getId() == conversationID){
//                i.getMessageList().add(message);
//                found = true;
//            }
//        });
//        if (found == false) {
//            Conversation conversation = new Conversation();
//            conversation.setId(conversationID);
//            conversation.getMessageList().add(message);
//        }
//        if(currentConversationID == 0){
//            currentConversationID = conversationID;
//        }
//
//        if(currentConversationID == conversationID){
//            Platform.runLater(() -> conversationMessages.add(new MessageRowData(message)));
//        }
//    }
//
//    @Override
//    public void receiveConversations(List<Conversation> conversations) {
//        setUserConversations(conversations);
//        initializeConversation(conversations);
//    }
//
//    public ObservableList<MessageRowData> getConversationMessages() {
//        return conversationMessages;
//    }
//
//
//    public ObservableList<UserRowData> getUsers() {
//        return users;
//    }
//
//    public ObservableList<ConversationRowData> getConversationRowData() {
//        return conversationRowData;
//    }
//
//    public void sendMessage(String msg) {
//        mediator.sendMessage(msg, currentConversationID);
//    }
//
//    public void newConversation(String message, String toID){
//        mediator.newConversation(message, toID);
//    }
//
//    public void loadMessages(ConversationRowData conversationRowData) {
//        // search Conversation where id is same
//        for(Conversation conversation : userConversations){
//            if(conversation.getId() == conversationRowData.getConversationID()){
//                // get Message list
//                List<Message> messages = conversation.getMessageList();
//                // initialize conv mesages list
//                initializeConversationMessages(messages);
//                currentConversationID = conversationRowData.getConversationID();
//                break;
//            }
//        }
//
//    }
//}
