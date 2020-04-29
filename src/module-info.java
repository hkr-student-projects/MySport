module MySport {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires json.simple;
    requires java.sql;
    requires mysql.connector.java;

    opens controller;
    opens model;
    opens model.Logging;
    opens model.Localization;
    opens view;
    opens view.css;
    opens view.img;
}