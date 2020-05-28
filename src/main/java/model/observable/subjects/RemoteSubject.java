package model.observable.subjects;


import model.Client.models.Conversation;
import model.Client.models.Message;
import model.Client.models.User;
import model.observable.listeners.RemoteListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteSubject extends Remote {
    boolean addListener(String userID, RemoteListener var1) throws RemoteException;

    boolean removeListener(String userID, RemoteListener var1) throws RemoteException;

    void sendMessage(Message message, int conversationID, String fromID) throws RemoteException;

    void newConversation(Message message, String fromID, String toID) throws RemoteException;

    List<Conversation> loadConversations(String userID) throws RemoteException;

    List<User> loadTeamMembersContacts(String userID) throws RemoteException;

}
