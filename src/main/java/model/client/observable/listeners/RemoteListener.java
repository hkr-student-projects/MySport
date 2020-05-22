package model.client.observable.listeners;

import models.Conversation;
import models.Message;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteListener extends Remote {

    public void receiveUsers(List<User> users) throws RemoteException;

    public void receiveMessage(Message message, int conversationID) throws RemoteException;

    public void receiveConversations(List<Conversation> conversations) throws RemoteException;

}
