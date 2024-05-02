module userjava1 {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.persistence;
    requires profanity.filter;
    requires java.desktop;
    opens EDU.userjava1.controllers;
    opens EDU.userjava1.entities to javafx.base;

}