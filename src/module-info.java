module MySport {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires json.simple;
    requires java.sql;
    requires mysql.connector.java;
    requires kotlin.stdlib;
    requires java.mail;
    requires javafx.web;
    requires restfb;
    requires jbcrypt;
    requires javafx.media;
    requires javafx.base;

    opens model;
    opens model.Logging;
    opens model.Localization;
    opens model.Database;
    opens model.People;
    opens model.Tools;
    opens model.Mailer;
    opens model.Util;
    opens view;
    opens view.css;
    opens view.img;
    opens view.vid;
    opens controller;
    opens resources;
}