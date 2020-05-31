package model.Client.viewModel;


import controller.Messaging;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Client.mediator.ChatMediatorClient;


import model.Client.models.Conversation;
import model.Client.models.Message;
import model.Client.models.User;
import model.observable.listeners.LocalListener;


import java.util.ArrayList;
import java.util.List;


public class ChatClientViewModel implements LocalListener<User, Message, Conversation> {

    private ObservableList<MessageRowData> conversationMessages;

    private ObservableList<UserRowData> users;

    private ObservableList<ConversationRowData> conversationRowData;

    private List<Conversation> userConversations;

    private int currentConversationID = 0;

    boolean found = false;

    private String currentUser;

    private ChatMediatorClient mediator;
    private User newUserToChatWith;
    private Messaging controller;
    int previousMessageID = -1;

    public ChatClientViewModel(String currentUser, ChatMediatorClient mediatorLocal) {
        System.out.println("In ChatClientViewModel constructor");
        conversationMessages = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        conversationRowData = FXCollections.observableArrayList();
        userConversations = new ArrayList<>();
        this.currentUser = currentUser;
        mediator = mediatorLocal;
        mediator.addListener(this);
        List<User> userList = mediator.loadTeamMembersContacts();
        System.out.println("Users list size " + userList.size());
        initializeUsers(userList);
        List<Conversation> conversationList = mediator.loadConversations();
        System.out.println("Conversations list size " + conversationList.size());
        receiveConversations(conversationList);
    }

    public void initializeUsers(List<User> usersList){
        users.clear();
        usersList.forEach(i -> {
            System.out.println(i.getName());
            users.add(new UserRowData(i));
        });
    }

    public void initializeConversation(List<Conversation> conversationList){
        conversationRowData.clear();
        conversationList.forEach(i -> {
            String msg = "";
            if(i.getMessageList().size() > 0){
                msg = i.getMessageList().get(i.getMessageList().size() - 1).getMessage();
            }
            conversationRowData.add(new ConversationRowData(i.getId(),
                    i.conversationName(currentUser), msg));
        });
    }


    public void initializeConversationMessages(List<Message> conversationMessagesList){
        System.out.println("Clearing old messages");
        Platform.runLater(() -> conversationMessages.clear());
        System.out.println("Putting in the new messages");

        conversationMessagesList.forEach(i -> {
            //if(i.getId() != 0) {
                //System.out.println("Message ID is " + i.getId());
                Platform.runLater(() -> conversationMessages.add(new MessageRowData(i)));
           // }
        });
        previousMessageID = -1;
    }

    public void setUserConversations(List<Conversation> userConversations) {
        this.userConversations = userConversations;
    }

    @Override
    public void receiveUsers(List<User> users) {
        initializeUsers(users);
    }


    @Override
    public void receiveMessage(Message message, int conversationID) {
        // search conversation list if id exist
        found = false;
        userConversations.forEach(i -> {
            if(i.getId() == conversationID){
                i.getMessageList().add(message);
                found = true;
            }
        });
        if (found == false) {
            Conversation conversation = new Conversation();
            conversation.setId(conversationID);
            conversation.getMessageList().add(message);
            List<String> participants = new ArrayList<>();
            if(currentUser.equals(message.getFromName())){
                participants.add(newUserToChatWith.getName());
                participants.add(currentUser);
            }
            else {
                participants.add(message.getFromName());
                participants.add(currentUser);
            }
            conversation.setParticipants(participants);
            userConversations.add(conversation);
            conversationRowData.add(new ConversationRowData(conversationID,
                    conversation.conversationName(currentUser), message.getMessage()));
        }
        if(currentConversationID == 0 || currentConversationID == -1){
            currentConversationID = conversationID;
        }

        if(currentConversationID == conversationID){
            Platform.runLater(() -> {
                if(message.getId() != 0){
                    conversationMessages.add(new MessageRowData(message));
                }
            });
        }

        ConversationRowData rowData = findConversationRowData(conversationID);
        if(rowData != null){
            Platform.runLater(() -> {
                rowData.setMostRecentMessage(message.getMessage());
                conversationRowData.set(findConversationRowDataIndex(conversationID), rowData);
            });
        }
    }

    private boolean messageDoesNotExistInConversationMessages(Message message, int conversationID) {
        boolean doesNotExist = true;
        for(Conversation convo : userConversations) {
            if(convo.getId() == conversationID){
                for(Message msg :convo.getMessageList()){
                    if(msg.getId() == message.getId()){
                        System.out.println(("Found duplicate message ID" + message.getId()));
                        doesNotExist = false;
                        break;
                    }
                }
            }
        }
        return doesNotExist;
    }

    @Override
    public void receiveConversations(List<Conversation> conversations) {
        setUserConversations(conversations);
        initializeConversation(conversations);
    }

    public ObservableList<MessageRowData> getConversationMessages() {
        return conversationMessages;
    }


    public ObservableList<UserRowData> getUsers() {
        return users;
    }

    public ObservableList<ConversationRowData> getConversationRowData() {
        return conversationRowData;
    }

    public void sendMessage(String msg) {
        if(currentConversationID == -1){
            newConversation(msg, newUserToChatWith.getMobile());
        }
        mediator.sendMessage(msg, currentConversationID);
    }

    public void initiateConversation(User userToChatWith){
        // check if existing ConversationRowData with userToChatWith
        ConversationRowData conversationRowData = conversationExist(userToChatWith);
        if(conversationRowData != null){
            loadMessages(conversationRowData);
            currentConversationID = conversationRowData.getConversationID();
        }
        else {
            currentConversationID = -1;
            this.newUserToChatWith = userToChatWith;
            conversationMessages.clear();
        }
    }

    private ConversationRowData conversationExist(User userToChatWith) {
        for(Conversation convo : userConversations){
            List<String> participants = convo.getParticipants();
            System.out.println("Participant's size " + participants.size());
            for(String participant : participants){
                System.out.println("checking " + participant + " in conversation " + convo.getId());
                if(participant.equalsIgnoreCase(userToChatWith.getName())){
                    int convoID = convo.getId();
                    ConversationRowData conversationRowData = findConversationRowData(convoID);
                    return conversationRowData;
                }
            }
        }
        return null;
    }

    private int findConversationRowDataIndex(int conversationID) {
        for(int i = 0; i < conversationRowData.size(); i++){
            if(conversationRowData.get(i).getConversationID() == conversationID){
                return i;
            }
        }
        return -1;
    }

    private ConversationRowData findConversationRowData(int conversationID) {
        for(ConversationRowData rowData : conversationRowData){
            if(rowData.getConversationID() == conversationID){
                return rowData;
            }
        }
        return null;
    }

    public void newConversation(String message, String toID){
        mediator.newConversation(message, toID);
    }

    public void loadMessages(ConversationRowData conversationRowData) {
        // search Conversation where id is same
        System.out.println("In loadmessages " + userConversations.size());
        for(Conversation conversation : userConversations){
            System.out.println("Checking " + conversation.getId());
            if(conversation.getId() == conversationRowData.getConversationID()){
                // get Message list
                List<Message> messages = conversation.getMessageList();
                if(messages != null && messages.size() > 1) {
                    if (messages.get(0) != null && messages.get(1) != null) {
                        if (messages.get(0).getMessage().equals(messages.get(1).getMessage())) {
                            messages.remove(0);
                        }
                    }
                }
                System.out.println("Messages size is " + messages.size());
                // initialize conv mesages list
                initializeConversationMessages(messages);
                currentConversationID = conversationRowData.getConversationID();
                break;
            }
        }
    }

    public void setController(Messaging controller) {
        this.controller = controller;
    }

    class Tuple2<K, V> {

        private K first;
        private V second;

        public Tuple2(K first, V second){
            this.first = first;
            this.second = second;
        }

        // getters and setters
    }
}
