package Recyclage.controllers;

import Recyclage.entities.EcoDepot;
import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.services.ProduitRecyclableMethodes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ModifierProduitRecyclable implements Initializable {

    @FXML
    private ComboBox<String> ComboBoxTF1PM;

    @FXML
    private ComboBox<String> ComboBoxTFPM;

    @FXML
    private TextField DiscriptionTFPM;

    @FXML
    private DatePicker datePM;

    @FXML
    private TextField nomTFPM;

    @FXML
    private TextField quantiteTFPM;
    @FXML
    private TextField idFxM;
    private EcoDepotMethodes ecoDepotMethodes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String [] items={"verre","Metal","Plastique","Matiere Organique"};
        ComboBoxTFPM.getItems().addAll(items);
        ecoDepotMethodes= new EcoDepotMethodes();
        ecoDepotMethodes.chargerNomsEcoDepots(ComboBoxTF1PM);

    }
    @FXML
    void ModifierProduitRecyclable(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Modification");
        alert.setHeaderText("Confirmation de Modification");
        alert.setContentText("Êtes-vous sûr de vouloir Modifier cet éco-dépôt ?");
        ButtonType buttonType = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (nomTFPM.getText().isEmpty() || DiscriptionTFPM.getText().isEmpty() || ComboBoxTFPM.getValue() == null ||
                datePM.getValue() == null || ComboBoxTF1PM.getValue() == null || quantiteTFPM.getText().isEmpty()) {
            afficherAlerteErreur("Tous les champs doivent être remplis");
            return;
        }

        // Vérifier si la capacité de stockage est un entier valide
        int quantite;
        try {
            quantite = Integer.parseInt(quantiteTFPM.getText());
            if (quantite <= 0) {
                afficherAlerteErreur("La quantité doit être un nombre entier positif");
                return;
            }
        } catch (NumberFormatException e) {
            afficherAlerteErreur("La quantité doit être un nombre entier valide");
            return;
        }
        if (buttonType == ButtonType.OK) {
            ProduitRecyclableMethodes ProduitRecyclableMethodes = new ProduitRecyclableMethodes();
            ProduitRecyclable ProduitRecyclable = new ProduitRecyclable();
            ProduitRecyclable.setNom(nomTFPM.getText());
            ProduitRecyclable.setDescription(DiscriptionTFPM.getText());
            ProduitRecyclable.setType(ComboBoxTFPM.getValue());
            ProduitRecyclable.setQuantite(Integer.parseInt(quantiteTFPM.getText()));
            ProduitRecyclable.setDateDepot(java.sql.Date.valueOf(datePM.getValue()));
            ProduitRecyclable.setId(Integer.parseInt(idFxM.getText()));
            String selectedEcoDepotNom = ComboBoxTF1PM.getValue();

            // Obtenir l'ID de l'éco-dépôt correspondant au nom sélectionné
            long selectedEcoDepotId = ecoDepotMethodes.getIdEcoDepotByNom(selectedEcoDepotNom);

            // Créer un objet EcoDepot correspondant au nom sélectionné
            EcoDepot selectedEcoDepot = new EcoDepot();
            selectedEcoDepot.setId((int) selectedEcoDepotId);

            // Définir l'éco-dépôt sélectionné pour le produit recyclable
            ProduitRecyclable.setEcoDepot(selectedEcoDepot);
            ProduitRecyclableMethodes.modifierProduit(ProduitRecyclable, ProduitRecyclable.getId());
            boolean modificationReussie = ProduitRecyclableMethodes.modifierProduit(ProduitRecyclable, ProduitRecyclable.getId());

            // Afficher un message après la modification
            if (modificationReussie) {
                afficherMessage("Le produit a été modifié avec succès.", Alert.AlertType.INFORMATION);

            } else {
                afficherMessage("La modification du produit a échoué.", Alert.AlertType.ERROR);
            }

            Stage stage = (Stage) nomTFPM.getScene().getWindow();
            stage.close();
        }
    }

    public void initData1(ProduitRecyclable ProduitRecyclable) {
        // Mettez à jour les champs de texte avec les données de Produit sélectionné
        nomTFPM.setText(ProduitRecyclable.getNom());
        DiscriptionTFPM.setText(ProduitRecyclable.getDescription());
        quantiteTFPM.setText(String.valueOf(ProduitRecyclable.getQuantite()));
        ComboBoxTFPM.setValue(ProduitRecyclable.getType());
        // Convertir java.sql.Date en java.util.Date
        java.util.Date utilDate = new java.util.Date(ProduitRecyclable.getDateDepot().getTime());

        // Convertir java.util.Date en LocalDate
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Définir la valeur du DatePicker avec la LocalDate convertie
        datePM.setValue(localDate);

        idFxM.setText(String.valueOf(ProduitRecyclable.getId()));

        if (ecoDepotMethodes != null) {
            // Obtenez le nom de l'éco-dépôt associé à l'ProduitRecyclable
            String ecoDepotNom = ProduitRecyclable.getEcoDepot().getNom();
            // Sélectionnez le nom de l'éco-dépôt dans le ComboBox
            ComboBoxTF1PM .setValue(ecoDepotNom);
        }


    }

    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void afficherMessage(String s, Alert.AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

}
