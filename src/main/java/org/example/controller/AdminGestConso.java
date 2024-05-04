package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class AdminGestConso {
    @FXML
    private Button button;
    @FXML
    private Button button1;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        button.setOnAction(this::naviguerVersAction);
        button1.setOnAction(this::naviguerVersTypeAction);
    }

    @FXML
    public void naviguerVersAction(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Admin/Gestion Consommation/AdminGerAction.fxml"));
            button.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
    @FXML
    public void naviguerVersTypeAction(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Admin/Gestion Consommation/AdminGerTypeAction.fxml"));
            button.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
}
