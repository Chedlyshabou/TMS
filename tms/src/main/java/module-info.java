module tms.tms {

    requires javafx.fxml;
    requires java.sql;
    requires javafx.swing;
    requires com.google.api.client;
    requires com.google.api.services.gmail;
    requires com.google.api.client.json.gson;
    requires com.jfoenix;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires org.controlsfx.controls;
    requires jdk.httpserver;
    requires mail;
    requires google.api.client;
    requires org.fxmisc.richtext;
    opens tms.tms to javafx.fxml;

    exports tms.tms;
}