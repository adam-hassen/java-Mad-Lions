package Recyclage.controllers;

import Recyclage.entities.EcoDepot;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.tests.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AjouterEcodepot implements Initializable {
    @FXML
    private TextField adresseTF;
    @FXML
    private ComboBox<String> ComboBoxTF;

    @FXML
    private TextField capaciteStockageTF;

    @FXML
    private TextField nomTF;

    @FXML
    private ComboBox<String> ComboBoxTF1;


    @FXML
    void AfficherEcodepot(ActionEvent event)  {
        Stage stage = new Stage();
        // Charger la nouvelle vue ou créer une nouvelle fenêtre
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/EcoDepot/AfficherEcoDepot.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            // Fermer la fenêtre actuelle si nécessaire
            ((Stage) nomTF.getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void AjouterEcodepot(ActionEvent event) {
        // Vérification de la saisie des champs obligatoires
        if (nomTF.getText().isEmpty() || adresseTF.getText().isEmpty() || ComboBoxTF.getValue() == null ||
                capaciteStockageTF.getText().isEmpty() || ComboBoxTF1.getValue() == null) {
            afficherAlerteErreur("Tous les champs doivent être remplis");
            return;
        }
        // Vérification si le nom contient uniquement des lettres et des espaces
        if (!nomTF.getText().matches("[a-zA-ZÀ-ÿ\\s]+")) {
            afficherAlerteErreur("Le nom ne doit contenir que des lettres et des espaces");
            return;
        }

        // Vérifier si la capacité de stockage est un entier valide
        int capaciteStockage;
        try {
            capaciteStockage = Integer.parseInt(capaciteStockageTF.getText());
            if (capaciteStockage <= 0) {
                afficherAlerteErreur("La capacité de stockage doit être un nombre entier positif non nul");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerteErreur("La capacité de stockage doit être un nombre entier valide");
            return;
        }

        // Créer un éco-dépôt avec les données saisies
        EcoDepot ecoDepotToAdd = new EcoDepot();
        ecoDepotToAdd.setNom(nomTF.getText());
        ecoDepotToAdd.setAdresse(adresseTF.getText());
        ecoDepotToAdd.setType(ComboBoxTF.getValue());
        ecoDepotToAdd.setCapacite_stockage(Integer.parseInt(capaciteStockageTF.getText()));
        ecoDepotToAdd.setStatut_point_collecte(ComboBoxTF1.getValue());

        // Vérifier si l'éco-dépôt existe déjà
        EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();
        EcoDepot existingEcoDepot = ecoDepotMethodes.getEcoDepotByAttributes(ecoDepotToAdd);
        if (existingEcoDepot != null) {
            afficherAlerteErreur("Un éco-dépôt avec ces attributs existe déjà");
            return;
        }
        // Confirmation de l'ajout de l'éco-dépôt
        if (confirmerAjoutEcoDepot()) {
            // Ajout de l'éco-dépôt
            ecoDepotMethodes.ajouterEcodepot(ecoDepotToAdd);
        }
    }

    // Fonction pour confirmer l'ajout de l'éco-dépôt
    private boolean confirmerAjoutEcoDepot() {
        // Vous pouvez personnaliser cette boîte de dialogue selon vos besoins
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation d'ajout");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir ajouter cet éco-dépôt ?");
        confirmationDialog.setContentText("Cliquez sur OK pour confirmer.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Fonction pour afficher une alerte d'information
    private void afficherAlerteInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] items={"Centre de tri des déchets","Centre de recyclage","Centres de valorisation énergétique","Stations de réutilisation"};
        String [] items2={"Ouvert","Fermer","Préouverture"};
        ComboBoxTF.getItems().addAll(items);
        ComboBoxTF1.getItems().addAll(items2);

    }
    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
