package org.example.controller;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class ConsoController {
    @FXML
    private Button button;
    @FXML
    private Button button1;
    @FXML
    private TextField textField;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        button.setOnAction(this::naviguerVersGestion);
        button1.setOnAction(this::naviguerVersSuivre);
        VBox sidebar = vboxside;
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Label) {
                Label label = (Label) node;
                addHoverAnimation(label);
            }
        });
    }
    @FXML
    public void naviguerVersGestion(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/GestionnerConso.fxml"));
            button.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
    @FXML
    public void naviguerVersSuivre(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/SuivreConso.fxml"));
            button1.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
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
