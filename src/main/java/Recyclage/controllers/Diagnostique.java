package Recyclage.controllers;

import Recyclage.entities.ProduitRecyclable;
import Recyclage.services.ProduitRecyclableMethodes;
import Recyclage.tests.HelloApplication;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Diagnostique {
    @FXML
    private AreaChart<String, Number> areaChart;
    @FXML
    void Affichage(ActionEvent event) {
        Stage stage = new Stage();
        // Charger la nouvelle vue ou créer une nouvelle fenêtre
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ProduitRecyclable/AfficherProduitRecyclable.fxml"));
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
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            fadeTransition.setOnFinished(e -> currentStage.close());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void initialize() {
        // Obtenir la liste des produits recyclables
        ProduitRecyclableMethodes produitRecyclableMethodes = new ProduitRecyclableMethodes();
        List<ProduitRecyclable> listeProduits = produitRecyclableMethodes.listeDesProduits();

        // Collecter les données pour la statistique
        Map<String, Integer> data = collectData(listeProduits);

        // Afficher les données dans le AreaChart
        displayData(data);
    }

    private Map<String, Integer> collectData(List<ProduitRecyclable> produits) {
        Map<String, Integer> data = new HashMap<>();

        // Parcourir la liste des produits pour collecter les données
        for (ProduitRecyclable produit : produits) {
            String type = produit.getType();
            int quantite = produit.getQuantite();

            // Ajouter ou mettre à jour la quantité dans la Map
            data.put(type, data.getOrDefault(type, 0) + quantite);
        }

        return data;
    }

    private void displayData(Map<String, Integer> data) {
        // Créer les axes du AreaChart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Type de Produit");
        yAxis.setLabel("Quantité");

        // Créer la série de données pour le AreaChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> dataList = FXCollections.observableArrayList();

        // Ajouter les données à la série
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataList.add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série au AreaChart
        series.setData(dataList);
        areaChart.getData().add(series);
    }

}