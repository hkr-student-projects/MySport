module MySport {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires mysql.connector.java;

    opens controller;
    opens view;
    opens view.css;
    opens model;
}