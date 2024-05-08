package Recyclage.controllers;

import Recyclage.entities.EcoDepot;
import Recyclage.services.EcoDepotMethodes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierEcodepot implements Initializable {

    @FXML
    private ComboBox<String> ComboBoxTF1M;

    @FXML
    private ComboBox<String> ComboBoxTFM;

    @FXML
    private TextField adresseTFM;

    @FXML
    private TextField capaciteStockageTFM;

    @FXML
    private TextField nomTFM;
    @FXML
    private TextField idFx;
    private AfficherEcoDepot afficherEcoDepotController;
    @FXML
    void Supprimer(ActionEvent event) {
        // Créer une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Confirmation de suppression");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet éco-dépôt ?");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        ButtonType buttonType = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (buttonType == ButtonType.OK){
        // Récupérer l'ID de l'EcoDepot à supprimer
        EcoDepot ecoDepot = new EcoDepot();
        int ecoDepotId = Integer.parseInt(idFx.getText());
         ecoDepot.setId(ecoDepotId);
        // Appeler la méthode de service pour supprimer l'EcoDepot
        EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();
        ecoDepotMethodes.supprimerEcodepot(ecoDepot);

        // Fermer la fenêtre de modification
        Stage stage = (Stage) nomTFM.getScene().getWindow();
        stage.close();
        afficherEcoDepotController.rafraichirTableView();
        }
    }

    public void initData(EcoDepot ecoDepot,AfficherEcoDepot afficherEcoDepotController) {
        // Mettez à jour les champs de texte avec les données de l'EcoDepot sélectionné
        nomTFM.setText(ecoDepot.getNom());
        adresseTFM.setText(ecoDepot.getAdresse());
        capaciteStockageTFM.setText(String.valueOf(ecoDepot.getCapacite_stockage()));
        idFx.setText(String.valueOf(ecoDepot.getId()));
        ComboBoxTFM.setValue(ecoDepot.getType());
        ComboBoxTF1M.setValue(ecoDepot.getStatut_point_collecte());
        this.afficherEcoDepotController = afficherEcoDepotController;

        // Assurez-vous de mettre à jour d'autres champs de texte de manière similaire
    }

    @FXML
    void Modifier(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Modification");
        alert.setHeaderText("Confirmation de Modification");
        alert.setContentText("Êtes-vous sûr de vouloir Modifier cet éco-dépôt ?");
        ButtonType buttonType = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (nomTFM.getText().isEmpty() || adresseTFM.getText().isEmpty() || ComboBoxTFM.getValue() == null ||
                capaciteStockageTFM.getText().isEmpty() || ComboBoxTF1M.getValue() == null) {
            afficherAlerteErreur("Tous les champs doivent être remplis");
            return;
        }

        // Vérifier si la capacité de stockage est un entier valide
        int capaciteStockage;
        try {
            capaciteStockage = Integer.parseInt(capaciteStockageTFM.getText());
            if (capaciteStockage <= 0) {
                afficherAlerteErreur("La capacité de stockage doit être un nombre entier positif non nul");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerteErreur("La capacité de stockage doit être un nombre entier valide");
            return;
        }
        if (buttonType == ButtonType.OK) {
            EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();
            EcoDepot ecoDepot = new EcoDepot();
            ecoDepot.setNom(nomTFM.getText());
            ecoDepot.setAdresse(adresseTFM.getText());
            ecoDepot.setType(ComboBoxTFM.getValue());
            ecoDepot.setCapacite_stockage(Integer.parseInt(capaciteStockageTFM.getText()));
            ecoDepot.setStatut_point_collecte(ComboBoxTF1M.getValue());
            ecoDepot.setId(Integer.parseInt(idFx.getText()));
            ecoDepotMethodes.modifierEcodepot(ecoDepot, ecoDepot.getId());
            afficherAlerteInformation("L'éco-dépôt a été modifié avec succès");
            // Fermer la fenêtre de modification

            Stage stage = (Stage) nomTFM.getScene().getWindow();
            stage.close();
            afficherEcoDepotController.rafraichirTableView();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] items={"Centre de tri des déchets","Centre de recyclage","Centres de valorisation énergétique","Stations de réutilisation"};
        String [] items2={"Ouvert","Fermer","Préouverture"};
        ComboBoxTFM.getItems().addAll(items);
        ComboBoxTF1M.getItems().addAll(items2);
    }
    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void afficherAlerteInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
