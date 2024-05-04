module GestionRecyclage {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires qrgen;
    requires com.gluonhq.maps;
    requires javafx.web;
    requires jdk.jsobject;
    requires twilio; // Ajout de la bibliothèque net.glxn.qrgen
    requires com.google.gson;
    requires java.persistence;
    requires profanity.filter;
    requires java.desktop;
    requires bcrypt;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;
    requires javax.mail.api;
    requires okhttp3;
    opens Recyclage.entities;
    opens Recyclage.controllers;
    opens Recyclage.services;
    opens Recyclage.interfaces;
    opens Recyclage.tests;
    opens Recyclage.tools;
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
