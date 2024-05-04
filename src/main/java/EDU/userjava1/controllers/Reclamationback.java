package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Reclamationback implements Initializable {
    @FXML
    private Button envoyer;

    @FXML
    private TextField reponse;

    @FXML
    private TableColumn<Reclamation, Date> DATE;

    @FXML
    private TableColumn<Reclamation, String> EMAIL;

    @FXML
    private TableColumn<Reclamation, String> RECLAMATION;

    @FXML
    private TableColumn<Reclamation, String> REPONSE;

    @FXML
    private TableColumn<Reclamation, String> TYPE;

    @FXML
    private TableView<Reclamation> table; // Correction du type de TableView
    reclamationService gs = new reclamationService();

    private List<Reclamation> getAllReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        reclamations = gs.getAllReclamations(); // Modifier la méthode pour récupérer les réclamations par ID utilisateur
        return reclamations;
    }

    // Méthode pour récupérer et afficher les réclamations dans la TableView
    public void showReclamation() {
        List<Reclamation> reclamationsList = getAllReclamations();
        ObservableList<Reclamation> list = FXCollections.observableList(reclamationsList);
        table.setItems(list);
        RECLAMATION.setCellValueFactory(new PropertyValueFactory<>("message")); // Utilisation des noms de propriétés
        EMAIL.setCellValueFactory(new PropertyValueFactory<>("userName"));
        DATE.setCellValueFactory(new PropertyValueFactory<>("date"));
        REPONSE.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        TYPE.setCellValueFactory(new PropertyValueFactory<>("type")); // Utilisation des noms de propriétés

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Affichage des événements existants lors du chargement de la page
        showReclamation();

        // Ajout d'un EventHandler pour détecter les sélections d'éléments dans la TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Afficher les détails de la réclamation sélectionnée
                System.out.println("Réclamation sélectionnée : " + newSelection.getId());
            }
        });        ;

    }
    @FXML
    void envoyer(ActionEvent event) {
        // Récupérer la réclamation sélectionnée
        Reclamation selectedReclamation = table.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Répondre à la réclamation sélectionnée
            String response = reponse.getText(); // Récupérer la réponse à partir d'un champ de texte
            gs.repondreReclamation(selectedReclamation.getId(), response);

            // Mettre à jour la TableView pour afficher la nouvelle réponse
            selectedReclamation.setReponse(response);
            table.refresh();
            reponse.clear();// Rafraîchir la TableView pour afficher les changements
        } else {
            System.out.println("Aucune réclamation sélectionnée.");
        }
    }
    @FXML
    void back(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/userliste.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }
}
