module userjava1 {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.persistence;
    requires profanity.filter;
    requires java.desktop;
    //requires twilio;
    requires bcrypt;
   // requires poi;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.mail;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires twilio;
    //  requires javax.mail;


    opens EDU.userjava1.controllers;
    opens EDU.userjava1.entities to javafx.base;

}