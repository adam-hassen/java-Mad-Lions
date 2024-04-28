package Recyclage.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Back {

    @FXML
    private Button Affichage;

    @FXML
    private Button Ajouter;

    @FXML
    private BorderPane BorderPane;

    @FXML
    private Button Carte;

    @FXML
    void Afficher(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/EcoDepot/AfficherEcoDepot.fxml")));
        BorderPane.setCenter(view);
    }

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/EcoDepot/AjouterEcoDepot.fxml")));
        BorderPane.setCenter(view);
    }

    @FXML
    void carte(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/EcoDepot/Carte.fxml")));
        BorderPane.setCenter(view);
    }

}
