package model.Client.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Client.models.User;


public class UserRowData {
    private StringProperty username;
    private User user;

    public UserRowData(User user) {
        this.username = new SimpleStringProperty(user.getName());
        this.user = user;
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
