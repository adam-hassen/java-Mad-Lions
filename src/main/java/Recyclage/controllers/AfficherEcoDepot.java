package Recyclage.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Recyclage.entities.EcoDepot;
import Recyclage.services.EcoDepotMethodes;
import Recyclage.tests.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AfficherEcoDepot {
    @FXML
    private TableColumn<EcoDepot, String> adresseCol;

    @FXML
    private TableColumn<EcoDepot, Integer> capaciteStockageCol;

    @FXML
    private TableColumn<EcoDepot, String> nomCol;

    @FXML
    private TableColumn<EcoDepot, String> statutPointCollecteCol;

    @FXML
    private  TableView<EcoDepot> tableView;

    @FXML
    private TableColumn<EcoDepot, String> typeCol;
    @FXML
    private TableColumn<EcoDepot, Integer> idCol;
    @FXML
    void Ajouter(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EcoDepot/AjouterEcodepot.fxml"));
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
        EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();
        List<EcoDepot> list=ecoDepotMethodes.listeDesEcodepots();
        ObservableList<EcoDepot> ob= FXCollections.observableList(list);
        tableView.setItems(ob);

        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutPointCollecteCol.setCellValueFactory(new PropertyValueFactory<>("statut_point_collecte"));
        capaciteStockageCol.setCellValueFactory(new PropertyValueFactory<>("capacite_stockage"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    }
    @FXML
    void getData(MouseEvent event) {

        EcoDepot ecoDepot = tableView.getSelectionModel().getSelectedItem();
        if (ecoDepot != null) {
            // Charger la nouvelle fenêtre de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EcoDepot/ModifierEcoDepot.fxml"));
            Parent root;
            try {
                root = loader.load();
                ModifierEcodepot modifierController = loader.getController();
                // Transférer les données EcoDepot sélectionnées à la fenêtre de modification
                modifierController.initData(ecoDepot);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }

    }


