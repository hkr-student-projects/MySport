package model.client.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import models.Message;


public class MessageRowData {
    private StringProperty name;
    private StringProperty message;
    private StringProperty timestamp;

    public MessageRowData(Message message)
    {
        this.message = new SimpleStringProperty(message.getMessage());
        this.name = new SimpleStringProperty(message.getFromName());
        this.timestamp = new SimpleStringProperty(message.getTimestamp().getHour() + ":" + message.getTimestamp().getMinute());
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

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }
}
