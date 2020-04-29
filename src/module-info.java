module MySport {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires mysql.connector.java;
    requires javafx.media;

    opens controller;
    opens view;
    opens model;
}