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

    opens Recyclage.entities;
    opens Recyclage.controllers;
    opens Recyclage.services;
    opens Recyclage.interfaces;
    opens Recyclage.tests;
    opens Recyclage.tools;
}
