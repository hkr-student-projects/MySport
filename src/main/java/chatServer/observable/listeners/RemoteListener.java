package chatServer.observable.listeners;




import chatServer.clientModels.Conversation;
import chatServer.clientModels.Message;
import chatServer.clientModels.User;

import java.util.List;

public interface RemoteListener {

    public void receiveUsers(List<User> users);

    public void receiveMessage(Message message, int conversationID);

    public void receiveConversations(List<Conversation> conversations);

}
