module pidev.gestion.consommation {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.persistence;
    requires profanity.filter;
    requires java.desktop;
    requires twilio;
    requires bcrypt;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;
    requires javax.mail.api;
    requires javafx.web;
    requires okhttp3;
    requires jdk.jsobject;
    opens org.example.controller;
    opens org.example.entity;
    opens org.example.service;
    opens EDU.userjava1.controllers;
    opens EDU.userjava1.entities to javafx.base;
    exports org.example;
    exports org.example.entity;
    exports org.example.controller;
    exports org.example.service;

}