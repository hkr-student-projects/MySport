package model.server.models;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {

    private int id;
    List<Message> messageList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
