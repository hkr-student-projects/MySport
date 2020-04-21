module MySport {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires mysql.connector.java;
    requires json.simple;
    requires java.sql;

    opens controller;
    opens view.css;
    opens view.img;
    opens view;
    opens model.Logging;
    opens model;
}