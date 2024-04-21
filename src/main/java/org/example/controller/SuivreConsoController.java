package org.example.controller;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.entity.Action;
import org.example.service.ActionService;

import java.io.IOException;
import java.util.List;

public class SuivreConsoController {

    @FXML
    private PieChart Chart_danger;
    @FXML
    public TableView tableView;
   @FXML
    public TableColumn<Action,Integer> ColId;
    @FXML
    public TableColumn<Action,Integer> ColTypeName;
    @FXML
    public TableColumn<Action,Integer> ColDanger;
    @FXML
    public TableColumn<Action,Integer> ColDate;
    @FXML
    public TableColumn<Action,Integer> ColDescription;
    @FXML
    private Button home;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        home.setOnAction(this::naviguerVersHome);
        showAction();
        VBox sidebar = vboxside;
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Label) {
                Label label = (Label) node;
                addHoverAnimation(label);
            }
        });
    }

    public void showAction() {

        ActionService query = new ActionService();
        List<Action> actionList = query.afficherActions();
        ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
        ColTypeName.setCellValueFactory(new PropertyValueFactory<Action,Integer>("type_id"));
        ColDanger.setCellValueFactory(new PropertyValueFactory<Action,Integer>("niveau_danger"));
        ColDate.setCellValueFactory(new PropertyValueFactory<Action,Integer>("Date"));
        ColDescription.setCellValueFactory(new PropertyValueFactory<Action,Integer>("Description"));
        tableView.setItems(observableList);
        // graph danger
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Action action : actionList) {
            pieChartData.add(new PieChart.Data(action.getDate().toString(), action.getNiveau_danger()));
        }
        Chart_danger.setData(pieChartData);
    }
    @FXML
    public void naviguerVersHome(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/HomeGestionAction.fxml"));
            home.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
    public void addHoverAnimation(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        label.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        label.setOnMouseExited(event -> {
            scaleTransition.stop();
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }
}