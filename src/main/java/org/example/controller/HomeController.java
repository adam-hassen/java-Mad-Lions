package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
import java.io.IOException;

public class HomeController {
    @FXML
    private VBox vboxside;

    @FXML
    private Button test;
    @FXML
    private Button workshop;
    @FXML
    void test(ActionEvent event) throws IOException {
            Parent root1 = FXMLLoader.load(getClass().getResource("/Workshop/addtest.fxml"));
            Scene scene1 = new Scene(root1);
            Stage stage1;
            stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.setScene(scene1);
            stage1.show();
    }
    @FXML
    void workshop(ActionEvent event)throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Workshop/AjouterWorkshop.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();


    } @FXML
    public void initialize() {
        VBox sidebar = vboxside;
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Label) {
                Label label = (Label) node;
                addHoverAnimation(label);
            }
        });

    }

    public void addHoverAnimation(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        label.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        label.setOnMouseExited(event -> {
            scaleTransition.stop();
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }
}