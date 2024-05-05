package Recyclage.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import EDU.userjava1.controllers.Login;
import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.services.ProduitRecyclableMethodes;
import Recyclage.tests.HelloApplication;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.util.Duration;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AfficherProduitRecyclable {

    @FXML
    private VBox carteContainer;
    private List<ProduitRecyclable> listeProduits;

    private final int ITEMS_PER_PAGE = 1; // Nombre d'éléments par page
    private Pagination pagination;


    @FXML
    private TextField BarreDeRecherche;

    EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();


    @FXML
    void AjouterProduitRecyclable(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProduitRecyclable/AjouterProduitRecyclable.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();

            // Créer une transition de fondu pour la nouvelle scène
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
            fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
            fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

            // Démarrer la transition de fondu
            fadeTransition.play();

            // Afficher la nouvelle scène dans une nouvelle fenêtre
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle après la transition
          //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //fadeTransition.setOnFinished(e -> currentStage.close());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void initialize() {
        // Obtenir la liste des produits recyclables
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        listeProduits = produitRecyclableMethodes.listeDesProduits(Login.v.getId());

        // Créer et configurer la pagination
        pagination = new Pagination((int) Math.ceil(listeProduits.size() / (double) ITEMS_PER_PAGE), 0);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex, listeProduits));

        // Ajouter la pagination à votre conteneur
        carteContainer.getChildren().add(pagination);
        BarreDeRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercheProduit(newValue);
        });
    }


    private void rechercheProduit(String terme) {
        // Créer une liste temporaire pour stocker les résultats de la recherche
        List<ProduitRecyclable> produitsTrouves = new ArrayList<>();

        // Parcourir la liste des produits et ajouter ceux correspondant au terme de recherche à la liste temporaire
        for (ProduitRecyclable produit : listeProduits) {
            if (produit.getNom().toLowerCase().contains(terme.toLowerCase())) {
                produitsTrouves.add(produit);
            }
        }

        // Mettre à jour les cartes affichées avec les résultats de la recherche
        carteContainer.getChildren().clear(); // Effacer les cartes précédentes
        pagination = new Pagination((int) Math.ceil(produitsTrouves.size() / (double) ITEMS_PER_PAGE), 0);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex, produitsTrouves));
        carteContainer.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex, List<ProduitRecyclable> produits) {
        VBox vbox = new VBox(10);
        int startIndex = pageIndex * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, produits.size());

        for (int i = startIndex; i < endIndex; i++) {
            AnchorPane carteProduit = createCarteProduit(produits.get(i));
            vbox.getChildren().add(carteProduit);
        }

        return vbox;
    }


        private AnchorPane createCarteProduit(ProduitRecyclable produit) {
        // Créer le conteneur principal de la carte produit
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(100, 100);

        // Définir le fond de la carte avec une certaine transparence et une forme arrondie
        anchorPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);" +
                "-fx-background-radius: 10px;");

        // Créer un VBox pour le contenu de la carte
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));
        contentBox.setAlignment(Pos.CENTER_LEFT);

        // Utilisez une image représentative en fonction du type de produit
        String type = produit.getType();
        ImageView imageView = new ImageView();
        switch (type) {
            case "Plastique":
                imageView.setImage(new Image("/css/Images/Plastique.png"));
                break;
            case "Metal":
                imageView.setImage(new Image("/css/Images/Metal.png"));
                break;
            case "verre":
                imageView.setImage(new Image("/css/Images/verre.png"));
                break;
            case "Matiere Organique":
                imageView.setImage(new Image("/css/Images/organique.png"));
                break;
            // Ajoutez d'autres cas pour d'autres types de produits si nécessaire
            default:
                // Par défaut, utilisez une image générique ou laissez vide
                break;
        }

        // Spécifiez la largeur et la hauteur de l'image
        imageView.setFitWidth(100); // Ajustez la largeur selon votre besoin
        imageView.setFitHeight(100); // Ajustez la hauteur selon votre besoin
        // Créer un label pour le nom du produit
        Label nomLabel = new Label("Nom: " + produit.getNom());
        nomLabel.setStyle("-fx-font-size: 18pt; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #333333;");

        // Créer un label pour la description du produit
        Label descriptionLabel = new Label("Description: " + produit.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14pt; " +
                "-fx-text-fill: #666666;");

        // Créer un label pour la quantité du produit
        Label quantiteLabel = new Label("Quantite: " + produit.getQuantite());
        quantiteLabel.setStyle("-fx-font-size: 14pt; " +
                "-fx-text-fill: #666666;");

        // Créer un label pour la date du produit
        Label dateLabel = new Label("Date: " + produit.getDateDepot().toString());
        dateLabel.setStyle("-fx-font-size: 14pt; " +
                "-fx-text-fill: #666666;");

        // Créer un label pour le type du produit
        Label typeLabel = new Label("Type: " + produit.getType());
        typeLabel.setStyle(
                "-fx-font-size: 14pt; " +
                        "-fx-text-fill: #666666; " +
                        "-fx-background-color:#7AA95C; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #666666; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-style: solid;");

        // Créer un label pour l'éco-dépôt du produit
        Label ecoDepotLabel = new Label("Ecodepot: " + produit.getEcoDepot().getNom());
        ecoDepotLabel.setStyle("-fx-font-size: 14pt; " +
                "-fx-text-fill: #666666;");

        // Ajouter les éléments au VBox
        contentBox.getChildren().addAll(imageView, nomLabel, descriptionLabel, dateLabel, quantiteLabel, typeLabel, ecoDepotLabel);

        // Créer un HBox pour les boutons "Supprimer" et "Modifier"
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));
        ImageView supprimerIcon = new ImageView(new Image("/css/Images/1214428.png"));
            supprimerIcon.setFitWidth(20); // Ajuster la largeur de l'icône
            supprimerIcon.setFitHeight(20); // Ajuster la hauteur de l'icône
        // Créer un bouton "Supprimer"
        Button supprimerButton = new Button("Supprimer", supprimerIcon);
        supprimerButton.setStyle("-fx-background-color: #ff6666; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt; " +
                "-fx-border-radius: 5px;");

        // Créer un bouton "Modifier"
            ImageView modifierIcon = new ImageView(new Image("/css/Images/860814.png"));
            modifierIcon.setFitWidth(20); // Ajuster la largeur de l'icône
            modifierIcon.setFitHeight(20); // Ajuster la hauteur de l'icône
        Button modifierButton = new Button("Modifier",modifierIcon);
        modifierButton.setStyle("-fx-background-color: #3399ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt; " +
                "-fx-border-radius: 5px;");

        // Gérer l'événement de clic sur le bouton "Modifier"
        modifierButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProduitRecyclable/ModifierProduitRecyclable.fxml"));
            try {
                Parent root = loader.load();
                ModifierProduitRecyclable controller = loader.getController();
                controller.initData1(produit); // Passer les informations du produit au contrôleur de la page de modification
                Stage stage = new Stage();

                // Créer une transition de fondu pour la nouvelle scène
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                // Démarrer la transition de fondu
                fadeTransition.play();

                stage.setScene(new Scene(root));
                stage.showAndWait();
                // Mettre à jour la liste des produits après la modification
                updateProduitsList();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        });

        // Gérer l'événement de clic sur le bouton "Supprimer"
        supprimerButton.setOnAction(event -> {
            // Créer une boîte de dialogue de confirmation
            if (confirmerAjoutProduit()) {
                int quantiteProduitSupprime = produit.getQuantite();

                // Récupérer la capacité de stockage de l'éco-dépôt associé au produit supprimé
                int capaciteStockageEcoDepot = produit.getEcoDepot().getCapacite_stockage();
                System.out.println(capaciteStockageEcoDepot);

                // Ajouter la quantité du produit supprimé à la capacité de stockage de l'éco-dépôt
                int nouvelleCapaciteStockage = capaciteStockageEcoDepot + quantiteProduitSupprime;

                // Mettre à jour la capacité de stockage de l'éco-dépôt dans la base de données
                ecoDepotMethodes.updateCapaciteStockage(produit.getEcoDepot().getNom(), nouvelleCapaciteStockage);
                ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
                boolean isDeleted = produitRecyclableMethodes.supprimerProduit(produit);

                // Afficher une confirmation dans l'interface
                if (isDeleted) {
                    updateProduitsList();
                    rafraichirAnchorPane(anchorPane);
                } else {
                    afficherMessage("La suppression du produit a échoué.", Alert.AlertType.ERROR);
                }

                // Actualiser la vue en supprimant la carte produit de la liste
                carteContainer.getChildren().remove(anchorPane);
            }
        });

        // Ajouter les boutons à la HBox
        buttonBox.getChildren().addAll(supprimerButton, modifierButton);
        if (produit.getEcoDepot().getAdresse() != null && !produit.getEcoDepot().getAdresse().isEmpty()) {
            // Créer le code QR avec l'adresse de l'éco-dépôt
            String adresseEcoDepot = produit.getEcoDepot().getAdresse();
            ByteArrayOutputStream out = QRCode.from(adresseEcoDepot).to(ImageType.PNG).stream();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            Image qrImage = new Image(in);

            // Créer un ImageView pour afficher le code QR
            ImageView qrImageView = new ImageView(qrImage);
            qrImageView.setFitWidth(200); // Ajuster la largeur du code QR selon votre besoin
            qrImageView.setFitHeight(200); // Ajuster la hauteur du code QR selon votre besoin
            qrImageView.setStyle("-fx-background-color: transparent;");
            // Ajouter les éléments à l'AnchorPane
            anchorPane.getChildren().addAll(contentBox, buttonBox, qrImageView);

            // Positionner les éléments
            AnchorPane.setTopAnchor(contentBox, 10.0);
            AnchorPane.setLeftAnchor(contentBox, 10.0);
            AnchorPane.setBottomAnchor(buttonBox, 10.0);
            AnchorPane.setRightAnchor(buttonBox, 10.0);
            AnchorPane.setTopAnchor(qrImageView, 10.0);
            AnchorPane.setRightAnchor(qrImageView, 10.0);
        } else {
            anchorPane.getChildren().addAll(contentBox, buttonBox);

            // Positionner les éléments
            AnchorPane.setTopAnchor(contentBox, 10.0);
            AnchorPane.setLeftAnchor(contentBox, 10.0);
            AnchorPane.setBottomAnchor(buttonBox, 10.0);
            AnchorPane.setRightAnchor(buttonBox, 10.0);

        }

        return anchorPane;
    }
    private void afficherMessage(String s, Alert.AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
    @FXML
    void Diagnostique(ActionEvent event) {
        Stage stage = new Stage();
        // Charger la nouvelle vue ou créer une nouvelle fenêtre
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ProduitRecyclable/Diagnosrique.fxml"));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));

            // Créer une transition de fondu pour la nouvelle scène
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
            fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
            fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

            // Démarrer la transition de fondu
            fadeTransition.play();

            stage.show();

            // Fermer la fenêtre actuelle après la transition
          //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           // fadeTransition.setOnFinished(e -> currentStage.close());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void rafraichirAnchorPane(AnchorPane anchorPane) {
        // Effacer le contenu actuel de l'AnchorPane
        anchorPane.getChildren().clear();

        // Réinitialiser la liste des produits recyclables
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        List<ProduitRecyclable> produits = produitRecyclableMethodes.listeDesProduits(Login.v.getId());

        // Calculer le nouveau nombre total de pages
        int totalPages = (int) Math.ceil(produits.size() / (double) ITEMS_PER_PAGE);

        // Mettre à jour la pagination avec le nouveau nombre total de pages
        pagination.setPageCount(totalPages);

        // Réinitialiser le numéro de page actuel à zéro
        pagination.setCurrentPageIndex(0);

        // Vérifier si la liste des produits est vide
        if (!produits.isEmpty()) {
            // Créer une liste pour stocker les produits à afficher sur la page courante
            List<ProduitRecyclable> produitsPageCourante = new ArrayList<>();

            // Calculer l'index de début et de fin des produits à afficher sur la page courante
            int startIndex = pagination.getCurrentPageIndex() * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, produits.size());

            // Ajouter les produits à afficher sur la page courante à la liste
            for (int i = startIndex; i < endIndex; i++) {
                produitsPageCourante.add(produits.get(i));
            }

            // Créer et configurer la pagination avec le nouveau nombre total de pages
            pagination = new Pagination(totalPages, 0);
            pagination.setPageFactory(pageIndex -> createPage(pageIndex, produitsPageCourante));

            // Ajouter la pagination à votre conteneur
            anchorPane.getChildren().add(pagination);
        } else {
            // Afficher un message si la liste est vide
            Label label = new Label("Aucun produit recyclable disponible.");
            anchorPane.getChildren().add(label);
            // Positionner le label au centre de l'AnchorPane
            AnchorPane.setTopAnchor(label, (anchorPane.getHeight() - label.getHeight()) / 2);
            AnchorPane.setLeftAnchor(label, (anchorPane.getWidth() - label.getWidth()) / 2);
        }
    }
    private void updateProduitsList() {
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        listeProduits = produitRecyclableMethodes.listeDesProduits(Login.v.getId());
    }
    private boolean confirmerAjoutProduit() {
        // Créer une nouvelle alerte de type CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Confirmer la suppression du produit recyclable");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit recyclable ?");

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
    @FXML
    void Home(ActionEvent event) {

    }

}
