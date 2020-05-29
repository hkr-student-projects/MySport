package chatServer.clientModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {

    private int id;
    List<Message> messageList;
    List<String> participants;
    private static final long serialVersionUID = 6529685328267757620L;

    public Conversation() {
        messageList = new ArrayList<>();
        participants = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public String conversationName(String currentUser){
        StringBuilder result = new StringBuilder();
        for(String user : participants){
            if(!user.equals(currentUser)){
                if(result.length() > 0) {
                    result.append(user + ", ");
                }
                else {
                    result.append(user);
                }
            }
        }
        return result.toString();
    }
}
