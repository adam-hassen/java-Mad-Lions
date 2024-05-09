package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
import EDU.userjava1.services.reclamationService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class profile implements Initializable {

    @FXML
    private Button delete;
    @FXML
    private Button ajoutreclamation;
    @FXML
    private Label address;
    @FXML
    private Button edit;
    @FXML
    private Label email;
    @FXML
    private Label genre;
    @FXML
    private Label nom;
    @FXML
    private Label prenom;
    @FXML
    private Label num;
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private Pane pane_1111;
    @FXML
    private Pane pane_11111;
    @FXML
    private Pane pane_111111;
    @FXML
    private Pane pane_13;
    @FXML
    private Pane pane_131;
    @FXML
    private Pane pane_1311;
    @FXML
    private Pane pane_132;
    @FXML
    private HBox root;
    private List<Reclamation> fruits = new ArrayList<>();

    private MyListener1 myListener1;
    reclamationService gs = new reclamationService();

    private List<Reclamation> getData() {
        List<Reclamation> reclamations = new ArrayList<>();
        int userId = Login.v.getId(); // Récupérer l'ID de l'utilisateur actuel
        reclamations = gs.getReclamationsByUserId(userId); // Modifier la méthode pour récupérer les réclamations par ID utilisateur
        return reclamations;
    }
    private edituser editUserController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afficherInformationsUtilisateur();
        afficheReclamation();
    }

    private void afficheReclamation() {
        fruits.addAll(getData());
        if (fruits.size() > 0) {

        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/afficheReclamationFront.fxml"));


                AnchorPane anchorPane = fxmlLoader.load();

                AfficheReclamationFront controller = fxmlLoader.getController();

                controller.setData(fruits.get(i), myListener1);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void afficherInformationsUtilisateur() {
        nom.setText(Login.v.getName());
        email.setText(Login.v.getUsername());
        num.setText(String.valueOf(Login.v.getNumero()));
        prenom.setText(Login.v.getPrenom());
        genre.setText(Login.v.getGenre());
    }

    @FXML
    void delete(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/donnerpassword.fxml"));
        try {
            Parent root = fxmlLoader.load();
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void edit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edituser.fxml"));
        Parent root2 = loader.load();
        editUserController = loader.getController();
        editUserController.setProfileController(this); // Pass instance of profile controller
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }
    @FXML
    void ajoutreclamation(ActionEvent event)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reclamation.fxml"));
        try {
        Parent root = fxmlLoader.load();
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

        } catch (IOException e) {
        throw new RuntimeException(e);
         }
    }
    @FXML
    void home(ActionEvent event)throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/home111.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }
    @FXML
    void changer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/changerMdp.fxml"));
        try {
            Parent root = fxmlLoader.load();
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("deconnexion!");
        alert.showAndWait();
        Parent root2 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

    public void rafraichirInformationsUtilisateur(User1 utilisateur) {
        nom.setText(utilisateur.getName());
        email.setText(utilisateur.getUsername());
        num.setText(String.valueOf(utilisateur.getNumero()));
        prenom.setText(utilisateur.getPrenom());
        genre.setText(utilisateur.getGenre());
    }


}
