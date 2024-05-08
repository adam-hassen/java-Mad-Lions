package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.services.UserServices;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.List;
public class userliste implements Initializable {
    @FXML
    private TextField recherche;
    @FXML
    private Label adresse;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label email;

    @FXML
    private GridPane grid;

    @FXML
    private Label id;

    @FXML
    private Label jeton;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Button profile;

    @FXML
    private Label role;

    @FXML
    private ScrollPane scroll;

    private List<User1> fruits = new ArrayList<>();
    private MyListener myListener;
    private UserServices gs = new UserServices();

    private void loadFruits() {
        fruits.addAll(gs.afficheruser());
    }

    private void setChosenFruit(User1 fruit) {
        role.setText(fruit.getRoles());
        nom.setText(fruit.getName());
        prenom.setText(fruit.getPrenom());
        email.setText(fruit.getUsername());
        jeton.setText(String.valueOf(fruit.getNumero()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFruits();
        if (fruits.size() > 0) {
            setChosenFruit(fruits.get(0));
            myListener = u -> setChosenFruit(u);
        }
        displayFruits(fruits);

        // Ajout du gestionnaire d'événements pour la recherche
        recherche.setOnKeyReleased(event -> {
            String searchText = recherche.getText();
            List<User1> searchResults = gs.recherche_user(searchText);
            displayFruits(searchResults);
        });
    }

    private void displayFruits(List<User1> fruits) {
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (User1 fruit : fruits) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/items.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(fruit, myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                // Mettre en forme la grille...
                GridPane.setMargin(anchorPane, new Insets(10));
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profile(ActionEvent event) throws IOException{

        Parent root2 = FXMLLoader.load(getClass().getResource("/profilAdmin.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();

    }

    @FXML
    void reclamationbutton(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamationback.fxml"));
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



}
