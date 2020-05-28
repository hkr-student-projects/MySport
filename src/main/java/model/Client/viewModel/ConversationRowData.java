package model.Client.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ConversationRowData {

    private int conversationID;
    private StringProperty name;
    private StringProperty mostRecentMessage;

    public ConversationRowData(int conversationID, String name, String mostRecentMessage) {
        this.conversationID = conversationID;
        this.name = new SimpleStringProperty(name);
        this.mostRecentMessage = new SimpleStringProperty(mostRecentMessage);
    }

    public int getConversationID() {
        return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getMostRecentMessage() {
        return mostRecentMessage.get();
    }

    public StringProperty mostRecentMessageProperty() {
        return mostRecentMessage;
    }

    public void setMostRecentMessage(String mostRecentMessage) {
        this.mostRecentMessage.set(mostRecentMessage);
    }
}
