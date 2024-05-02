package Recyclage.controllers;

import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.ProduitRecyclableMethodes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BakTest {
    @FXML
    private Button home;
    @FXML
    private Button Affichage;

    @FXML
    private Button Ajouter;

    @FXML
    private BorderPane BorderPane;

    @FXML
    private Button Carte;

    @FXML
    private Button Recyclage;

    @FXML
    private Button Utilisateur;

    @FXML
    private Button consommation;

    @FXML
    private Button evenemment;

    @FXML
    private Button produit;

    @FXML
    private Button workshop;

    @FXML
    private HBox hboxButtons;

    @FXML
    void initialize() {
        hboxButtons.setVisible(false);
   
    }
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
    @FXML
    void Recyclage(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/EcoDepot/AjouterEcoDepot.fxml")));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(true);
    }

    @FXML
    void Utilisateur(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/EcoDepot/AfficherEcoDepot.fxml")));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(false);
    }
    @FXML
    void consommation(ActionEvent event) {
        hboxButtons.setVisible(false);
    }

    @FXML
    void evenemment(ActionEvent event) {
        hboxButtons.setVisible(false);
    }

    @FXML
    void produit(ActionEvent event) {
        hboxButtons.setVisible(false);
    }

    @FXML
    void workshop(ActionEvent event) {
        hboxButtons.setVisible(false);
    }
    @FXML
    void home(ActionEvent event) {

    }
}
