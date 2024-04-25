package Recyclage.controllers;

import Recyclage.entities.EcoDepot;
import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.services.ProduitRecyclableMethodes;
import Recyclage.tests.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AjouterProduitRecyclable implements Initializable {

    @FXML
    private ComboBox<String> ComboBoxTF1P;

    @FXML
    private ComboBox<String> ComboBoxTFP;

    @FXML
    private TextField DiscriptionTFP;

    @FXML
    private DatePicker dateP;

    @FXML
    private TextField nomTFP;

    @FXML
    private TextField quantiteTFP;
    private EcoDepotMethodes ecoDepotMethodes;

    @FXML
    void AfficherLesProduitRecyclable(ActionEvent event) {
        Stage stage = new Stage();
        // Charger la nouvelle vue ou créer une nouvelle fenêtre
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ProduitRecyclable/AfficherProduitRecyclable.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            // Fermer la fenêtre actuelle si nécessaire
            ((Stage) nomTFP.getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void AjouterProduitRecyclable(ActionEvent event) {
        if (nomTFP.getText().isEmpty() || DiscriptionTFP.getText().isEmpty() || ComboBoxTFP.getValue() == null ||
                dateP.getValue() == null || ComboBoxTF1P.getValue() == null || quantiteTFP.getText().isEmpty()) {
            afficherAlerteErreur("Tous les champs doivent être remplis");
            return;
        }
        if (!nomTFP.getText().matches("[a-zA-ZÀ-ÿ\\s]+")) {
            afficherAlerteErreur("Le nom ne doit contenir que des lettres et des espaces");
            return;
        }
        // Vérifier si la capacité de stockage est un entier valide
        int quantite;
        try {
            quantite = Integer.parseInt(quantiteTFP.getText());
            if (quantite <= 0) {
                afficherAlerteErreur("La quantité doit être un nombre entier positif");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerteErreur("La quantité doit être un nombre entier valide");
            return;
        }
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        ProduitRecyclable produitRecyclable = new ProduitRecyclable();
        produitRecyclable.setNom(nomTFP.getText());
        produitRecyclable.setDescription(DiscriptionTFP.getText());
        produitRecyclable.setType(ComboBoxTFP.getValue());
        produitRecyclable.setDateDepot(java.sql.Date.valueOf(dateP.getValue()));
        produitRecyclable.setQuantite(Integer.parseInt(quantiteTFP.getText()));
        String selectedEcoDepotNom = ComboBoxTF1P.getValue();
        // Obtenir l'ID de l'éco-dépôt correspondant au nom sélectionné
        long selectedEcoDepotId = ecoDepotMethodes.getIdEcoDepotByNom(selectedEcoDepotNom);
        // Créer un objet EcoDepot correspondant au nom sélectionné
        EcoDepot selectedEcoDepot = new EcoDepot();
        selectedEcoDepot.setId((int) selectedEcoDepotId);
        // Associer l'éco-dépôt à l'objet ProduitRecyclable
        produitRecyclable.setEcoDepot(selectedEcoDepot);

        ProduitRecyclable existingProduct = produitRecyclableMethodes.getProduitRecyclableByAttributes(produitRecyclable);
        if (existingProduct != null) {
            if (confirmerAjoutProduit()) {
                // Le produit existe déjà, afficher une alerte
                afficherAlerteErreur("Ce produit existe déjà !");
                return;
            }
        }

        // Vérifier si un produit avec les mêmes données mais une quantité différente existe déjà
        ProduitRecyclable existingProductWithDifferentQuantity = produitRecyclableMethodes.getProduitRecyclableByAttributesButDifferentQuantity(produitRecyclable);
        if (existingProductWithDifferentQuantity != null) {
            if (confirmerAjoutProduit()) {
                // Mettre à jour la quantité du produit existant
                afficherAlerteErreur("Ce produit existe déjà et la quantite a eter modifier !");
                existingProductWithDifferentQuantity.setQuantite(existingProductWithDifferentQuantity.getQuantite() + quantite);
                produitRecyclableMethodes.updateProduit(existingProductWithDifferentQuantity);
                afficherPageProduitsRecyclables();
                return;

            }
        }

        // Ajouter le produit s'il n'existe pas déjà
        if (confirmerAjoutProduit()) {
        produitRecyclableMethodes.ajouterProduit(produitRecyclable);
        afficherPageProduitsRecyclables();
        }

    }

    // Méthode pour afficher une alerte d'erreur
    private void afficherAlerteErreur2(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] items = {"verre", "Metal", "Plastique", "Matiere Organique"};
        ComboBoxTFP.getItems().addAll(items);
        ecoDepotMethodes = new EcoDepotMethodes();
        ecoDepotMethodes.chargerNomsEcoDepots(ComboBoxTF1P);
        dateP.setValue(LocalDate.now());
    }

    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void réinitialiser(ActionEvent event) {
// Réinitialisation des champs texte
        DiscriptionTFP.setText("");
        nomTFP.setText("");
        quantiteTFP.setText("");
        // Réinitialisation du datepicker
        dateP.setValue(null);

    }

    private void afficherPageProduitsRecyclables() {
        Stage stage = new Stage();
        // Charger la nouvelle vue ou créer une nouvelle fenêtre
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ProduitRecyclable/AfficherProduitRecyclable.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            // Fermer la fenêtre actuelle si nécessaire
            ((Stage) nomTFP.getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean confirmerAjoutProduit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'ajout");
        alert.setHeaderText("Confirmer l'ajout du produit recyclable");
        alert.setContentText("Êtes-vous sûr de vouloir ajouter ce produit recyclable ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}


