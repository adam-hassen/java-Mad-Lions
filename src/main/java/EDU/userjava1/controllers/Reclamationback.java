package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Reclamationback implements Initializable {

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

        // Ajout d'un EventHandler pour le DatePicker
        ;

    }
}
