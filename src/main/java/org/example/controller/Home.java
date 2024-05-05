package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Home  {

    @FXML
    private Button login;

    @FXML
    private Button signup;
    @FXML
    private Button wf;
    @FXML
    void login(ActionEvent event)throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void signup(ActionEvent event)throws IOException  {
        Parent root1 = FXMLLoader.load(getClass().getResource("/inscrire.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }
@FXML
    void wf (ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Workshop/Fworkshop.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

}