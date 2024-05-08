package Recyclage.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class AfficherEcoDepot {
    @FXML
    private BarChart<String, Integer> BarChart;

    @FXML
    private PieChart PieChart;

    @FXML
    private TableColumn<EcoDepot, String> adresseCol;

    @FXML
    private TableColumn<EcoDepot, Integer> capaciteStockageCol;

    @FXML
    private TableColumn<EcoDepot, String> nomCol;

    @FXML
    private TableColumn<EcoDepot, String> statutPointCollecteCol;

    @FXML
    private TableView<EcoDepot> tableView;

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
        List<EcoDepot> list = ecoDepotMethodes.listeDesEcodepots();
        ObservableList<EcoDepot> ob = FXCollections.observableList(list);
        tableView.setItems(ob);

        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutPointCollecteCol.setCellValueFactory(new PropertyValueFactory<>("statut_point_collecte"));
        capaciteStockageCol.setCellValueFactory(new PropertyValueFactory<>("capacite_stockage"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        initializePieChart(list);
        initializeBarChart(list);
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
                modifierController.initData(ecoDepot, this);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void rafraichirTableView() {
        EcoDepotMethodes ecoDepotMethodes = new EcoDepotMethodes();
        List<EcoDepot> list = ecoDepotMethodes.listeDesEcodepots();
        ObservableList<EcoDepot> ob = FXCollections.observableList(list);
        tableView.setItems(ob);
    }

    private void initializePieChart(List<EcoDepot> list) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> capaciteParType = new HashMap<>();
        for (EcoDepot ecoDepot : list) {
            String type = ecoDepot.getType();
            int capacite = ecoDepot.getCapacite_stockage();
            capaciteParType.put(type, capaciteParType.getOrDefault(type, 0) + capacite);
        }
        for (Map.Entry<String, Integer> entry : capaciteParType.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " : " + entry.getValue(), entry.getValue()));
        }
        PieChart.setData(pieChartData);

        // Afficher les noms des tranches directement sur la PieChart
        for (final PieChart.Data data : PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
                Tooltip.install(data.getNode(), new Tooltip(data.getName()));
            });
        }
    }



    private void initializeBarChart(List<EcoDepot> list) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        Map<String, Integer> capaciteParType = new HashMap<>();
        for (EcoDepot ecoDepot : list) {
            String type = ecoDepot.getType();
            int capacite = ecoDepot.getCapacite_stockage();
            capaciteParType.put(type, capaciteParType.getOrDefault(type, 0) + capacite);
        }
        for (Map.Entry<String, Integer> entry : capaciteParType.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        BarChart.getData().add(series);

        // Ajouter les étiquettes
        for (final XYChart.Data<String, Integer> data : series.getData()) {
            Label label = new Label();
            label.setText(String.valueOf(data.getYValue()));
            Node node = data.getNode();
            node.setOnMouseEntered(e -> {
                Tooltip tooltip = new Tooltip(String.valueOf(data.getYValue()));
                Tooltip.install(node, tooltip);
            });
        }

    }
}


