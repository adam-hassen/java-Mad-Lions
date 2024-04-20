package Recyclage.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.ProduitRecyclableMethodes;
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
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.util.List;

public class AfficherProduitRecyclable {

    @FXML
    private VBox carteContainer;
    private List<ProduitRecyclable> listeProduits;

    private final int ITEMS_PER_PAGE = 1; // Nombre d'éléments par page
    private Pagination pagination;

    @FXML
    private Button ajouterButton;
    @FXML
    private MediaView mediaView;

    @FXML
    void AjouterProduitRecyclable(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProduitRecyclable/AjouterProduitRecyclable.fxml"));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre d'affichage actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void initialize() {
     //   String videoPath = "src/main/resources/css/Images/VIDEO3.mp4";
       // Media = new Media(new File(videoPath).toURI().toString());
       // MediaPlayer mediaPlayer = new MediaPlayer(media);
        //mediaView.setMediaPlayer(mediaPlayer);
        //mediaPlayer.setVolume(0);
       // mediaView.toBack();
       // mediaPlayer.play();
      // mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));

        // Obtenir la liste des produits recyclables
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        listeProduits = produitRecyclableMethodes.listeDesProduits();

        // Créer et configurer la pagination
        pagination = new Pagination((int) Math.ceil(listeProduits.size() / (double) ITEMS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);

        // Ajouter la pagination à votre conteneur
        carteContainer.getChildren().add(pagination);
    }

    private VBox createPage(int pageIndex) {
        VBox vbox = new VBox(10);
        int startIndex = pageIndex * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, listeProduits.size());

        for (int i = startIndex; i < endIndex; i++) {
            AnchorPane carteProduit = createCarteProduit(listeProduits.get(i));
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

        // Créer un bouton "Supprimer"
        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setStyle("-fx-background-color: #ff6666; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt; " +
                "-fx-border-radius: 5px;");

        // Créer un bouton "Modifier"
        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #3399ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt; " +
                "-fx-border-radius: 5px;");

        // Gérer l'événement de clic sur le bouton "Modifier"
        modifierButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProduitRecyclable/ModifierProduitRecyclable.fxml"));
            Parent root;
            try {
                root = loader.load();
                ModifierProduitRecyclable controller = loader.getController();
                controller.initData1(produit); // Passer les informations du produit au contrôleur de la page de modification
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Gérer l'événement de clic sur le bouton "Supprimer"
        supprimerButton.setOnAction(event -> {
            // Créer une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit recyclable ?");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            ButtonType buttonType = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (buttonType == ButtonType.OK) {
                // Supprimer le produit recyclable
                ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
                boolean isDeleted = produitRecyclableMethodes.supprimerProduit(produit);

                // Afficher une confirmation dans l'interface
                if (isDeleted) {
                    afficherMessage("Le produit a été supprimer avec succès.", Alert.AlertType.INFORMATION);
                } else {
                    afficherMessage("La suppression du produit a échoué.", Alert.AlertType.ERROR);
                }

                // Actualiser la vue en supprimant la carte produit de la liste
                carteContainer.getChildren().remove(anchorPane);
            }
        });

        // Ajouter les boutons à la HBox
        buttonBox.getChildren().addAll(supprimerButton, modifierButton);
        if (produit.getEcoDepot().getNom() != null && !produit.getEcoDepot().getNom().isEmpty()) {
            // Créer le code QR avec l'adresse de l'éco-dépôt
            String adresseEcoDepot = produit.getEcoDepot().getNom();
            ByteArrayOutputStream out = QRCode.from(adresseEcoDepot).to(ImageType.PNG).stream();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            javafx.scene.image.Image qrImage = new javafx.scene.image.Image(in);

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
}
