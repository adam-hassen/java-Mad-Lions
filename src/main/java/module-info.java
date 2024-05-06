module evenements1 {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires layout;
    requires io;
    requires kernel;
    requires java.desktop;
    requires json.simple;
    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens edu.evenements.entities;
    opens edu.evenements.controllers;
    opens edu.evenements.tests;
}