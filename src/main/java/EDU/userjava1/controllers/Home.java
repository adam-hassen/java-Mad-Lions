package EDU.userjava1.controllers;

import Recyclage.tests.HelloApplication;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Home  {

    @FXML
    private Button login;

    @FXML
    private Button signup;
    @FXML
    private Button gestConso;
    @FXML
    void Consommation(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }
    @FXML
    void Evenement(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void Recyclage(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }
    @FXML
    void produit(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }
    @FXML
    void workshop(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

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

}
