package Recyclage.controllers;

import Recyclage.entities.EcoDepot;
import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.services.ProduitRecyclableMethodes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
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
        if (confirmerModifProduit()) {
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
            EcoDepot selectedEcoDepot = ecoDepotMethodes.getEcoDepotByNom(selectedEcoDepotNom);

            if (selectedEcoDepot != null) {
                // Associer l'éco-dépôt récupéré à l'objet ProduitRecyclable
                ProduitRecyclable.setEcoDepot(selectedEcoDepot);
            } else {
                // Gérer le cas où aucun éco-dépôt correspondant n'a été trouvé
                System.out.println("Aucun éco-dépôt correspondant trouvé pour le nom : " + selectedEcoDepotNom);
            }
            ProduitRecyclableMethodes.modifierProduit(ProduitRecyclable, ProduitRecyclable.getId());
            boolean modificationReussie = ProduitRecyclableMethodes.modifierProduit(ProduitRecyclable, ProduitRecyclable.getId());

            // Afficher un message après la modification
            if (modificationReussie) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("succès");
                alert.setHeaderText("succès");
                alert.setContentText("Produit recyclable modifié avec succès");

                // Charger l'image à partir du chemin spécifié
                Image image = new Image("/css/Images/aa.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100); // Ajuster la largeur de l'image si nécessaire
                imageView.setPreserveRatio(true);
                alert.setGraphic(imageView);

                // Appliquer un style CSS personnalisé à l'alerte
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/CSS/succes.css").toExternalForm());
                dialogPane.getStyleClass().add("custom-alert");

                alert.showAndWait();

            } else {
                afficherAlerteErreur("La modification du produit a échoué.");
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

        // Charger l'image à partir du chemin spécifié
        Image image = new Image("/css/Images/alerte2.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // Ajuster la largeur de l'image si nécessaire
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);

        // Appliquer un style CSS personnalisé à l'alerte
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/CSS/alerte.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
    private boolean confirmerModifProduit() {
        // Créer une nouvelle alerte de type CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Modification");
        alert.setHeaderText("Confirmer la Modification du produit recyclable");
        alert.setContentText("Êtes-vous sûr de vouloir modifier ce produit recyclable ?");

        // Obtenir le bouton de type OK
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        // Appliquer un style CSS personnalisé au bouton OK
        okButton.getStyleClass().add("custom-ok-button");

        // Obtenir le bouton de type Annuler
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        // Appliquer un style CSS personnalisé au bouton Annuler
        cancelButton.getStyleClass().add("custom-cancel-button");

        // Charger la feuille de style CSS pour l'alerte
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/CSS/confirmation.css").toExternalForm());

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


}
