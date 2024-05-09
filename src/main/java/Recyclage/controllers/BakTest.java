package Recyclage.controllers;
import EDU.userjava1.controllers.Login;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

//import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BakTest  {
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
    private Label idUser;

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
    void initialize() throws IOException {
        idUser.setText(Login.v.getName());
        hboxButtons.setVisible(false);
        StackPane view= FXMLLoader.load((getClass().getResource("/com/example/demo/dashboard.fxml")));
        BorderPane.setCenter(view);
   
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
        AnchorPane view = FXMLLoader.load(getClass().getResource("/userListe.fxml"));
        BorderPane.setCenter(view);

        // Obtient le BorderPane parent du AnchorPane
        BorderPane borderPane = (BorderPane) Utilisateur.getScene().getRoot();

        // Définit AnchorPane pour qu'il s'étende sur toute la zone du BorderPane
        BorderPane.setMargin(view, new Insets(0,0,0,0));
        borderPane.setCenter(view);

        hboxButtons.setVisible(false);
    }
    @FXML
    void consommation(ActionEvent event) throws IOException {
        VBox view= FXMLLoader.load((getClass().getResource("/Admin/Gestion Consommation/AdminGerAction.fxml")));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(false);
    }

    @FXML
    void evenemment(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/Fxml/Evenements.fxml"));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(false);
    }

    @FXML
    void produit(ActionEvent event) throws IOException {
        StackPane view= FXMLLoader.load((getClass().getResource("/com/example/demo/inventory.fxml")));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(false);
    }

    @FXML
    void workshop(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("/Workshop/AfficherWorkshop.fxml"));
        BorderPane.setCenter(view);
        hboxButtons.setVisible(false);
        hboxButtons.setVisible(false);
    }
    @FXML
    void home(ActionEvent event) throws IOException {
        StackPane view= FXMLLoader.load((getClass().getResource("/com/example/demo/dashboard.fxml")));
        BorderPane.setCenter(view);

    }
    @FXML
    void logout(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
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
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            fadeTransition.setOnFinished(e -> currentStage.close());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
