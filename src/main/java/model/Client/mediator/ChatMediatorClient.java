package model.Client.mediator;


import model.Client.models.Conversation;
import model.Client.models.Message;
import model.Client.models.User;
import model.observable.listeners.LocalListener;
import model.observable.listeners.RemoteListener;
import model.observable.subjects.LocalSubject;
import model.observable.subjects.RemoteSubject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatMediatorClient extends UnicastRemoteObject implements RemoteListener, LocalSubject {

    public static ChatMediatorClient currentClient;
    List<LocalListener> listeners;
    User currentUser;
    public static final String HOST = "localhost";
    private String host;
    private RemoteSubject remoteModel;


    public ChatMediatorClient(User user, String host)
        throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println("In ChatMediatorClient constructor");
        listeners = new ArrayList<>();
        this.currentUser = user;
        System.out.println(user.getName() + ": " + user.getMobile());
        this.host = host;
        this.remoteModel = (RemoteSubject) Naming
                .lookup("rmi://" + host + ":1099/ChatServer");
        UnicastRemoteObject.unexportObject(this, true);
        UnicastRemoteObject.exportObject(this, 0);
        this.remoteModel.addListener(user.getMobile(), this);
        currentClient = this;
    }

    public ChatMediatorClient(User user)
            throws RemoteException, NotBoundException, MalformedURLException {
        this(user, HOST);
    }

    public void sendMessage(String msg, int conversationID) {
        try {
            Message message = new Message();
            message.setFromMobile(currentUser.getMobile());
            message.setFromName(currentUser.getName());
            message.setToMobile("");
            message.setMessage(msg);
            message.setTimestamp(LocalDateTime.now());
            remoteModel.sendMessage(message, conversationID, currentUser.getMobile());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void newConversation(String msg, String toID){
        try {
            Message message = new Message();
            message.setFromMobile(currentUser.getMobile());
            message.setFromName(currentUser.getName());
            message.setToMobile(toID);
            message.setMessage(msg);
            message.setTimestamp(LocalDateTime.now());
            remoteModel.newConversation(message, currentUser.getMobile(), toID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<Conversation> loadConversations(){
        try {
            return remoteModel.loadConversations(currentUser.getMobile());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> loadTeamMembersContacts(){
        try {
            return remoteModel.loadTeamMembersContacts(currentUser.getMobile());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void receiveUsers(List<User> users) throws RemoteException{
        System.out.println("In receive users");
        listeners.forEach(i -> {
            i.receiveUsers(users);
        });
    }

    @Override
    public void receiveMessage(Message message, int conversationID) throws RemoteException {
        listeners.forEach(i -> {
            i.receiveMessage(message, conversationID);
        });
    }

    @Override
    public void receiveConversations(List<Conversation> conversations) throws RemoteException {
        listeners.forEach(i -> {
            i.receiveConversations(conversations);
        });
    }

    @Override
    public boolean addListener(LocalListener var) {
        listeners.add(var);
        return true;
    }

    @Override
    public boolean removeListener(LocalListener var) {
        listeners.remove(var);
        return true;
    }

    public void removeRemoteListener() throws RemoteException{
        remoteModel.removeListener(this.currentUser.getMobile(), this);
    }

}