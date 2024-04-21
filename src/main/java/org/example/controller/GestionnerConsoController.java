package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entity.Action;
import org.example.entity.TypeName;
import org.example.service.ActionService;
import org.example.service.TypeNameService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GestionnerConsoController {
    @FXML
    public HBox containerView;

    private ActionService query;
    private TypeNameService query2;
    @FXML
    private Button AjouterAction;
    @FXML
    private Button home;
    @FXML
    private Button modifierButton;
    @FXML
    private HBox itemBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        query2 = new TypeNameService();
        query = new ActionService();
        AjouterAction.setOnAction(this::AjouterAction);
       // modifierButton.setOnAction(this::handleModifierButton);
        //show tables of actions
        /*itemBox.getChildren().clear();
        itemBox.getChildren().addAll(
                typeNameTextField,
                dangerTextField,
                dateTextField,
                descriptionTextField
        );*/
        showAction();
        // sssssssssssssssssssssss
        double totalWidth = containerView.getChildren().stream()
                .mapToDouble(node -> node.getBoundsInLocal().getWidth())
                .sum();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(scrollPane.hvalueProperty(), 0)),
                new KeyFrame(Duration.seconds(10), new KeyValue(scrollPane.hvalueProperty(), 1)),
                new KeyFrame(Duration.seconds(20), new KeyValue(scrollPane.hvalueProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        //sidebar animation
        VBox sidebar = vboxside;
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Label) {
                Label label = (Label) node;
                addHoverAnimation(label);
            }
        });
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
    public void AjouterAction(ActionEvent event){
        try {
            // Load the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/ActionForm.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Create a new stage and set the new scene
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
            showAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* @FXML
    private void handleModifierButton(ActionEvent event) {
        // Get the selected item from the container
        Node selectedNode = containerView.getChildren().get(containerView.getChildren().size() - 1);
        if (selectedNode instanceof HBox) {
            HBox selectedRow = (HBox) selectedNode;
            // Extract the data from the selected row
            String typeName = ((TextField) selectedRow.getChildren().get(0)).getText();
            String danger = ((TextField) selectedRow.getChildren().get(1)).getText();
            String date = ((TextField) selectedRow.getChildren().get(2)).getText();
            String description = ((TextField) selectedRow.getChildren().get(3)).getText();
            TypeName tpn = query2.cherchertypename(typeName);
            LocalDate date;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                // Handle the error, e.g., set a default date or show an error message
                date = LocalDate.now();
            }
            // Create an Action object with the extracted data
            Action selectedAction = new Action(tpn,0.0, date, danger, description);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/ActionForm.fxml"));
                Parent root = loader.load();
                ValiderFormAction controller = loader.getController();
                controller.ModifierForm(selectedAction);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Action Details");
                stage.show();
                showAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }*/
  /*  @FXML
    private void handleSupprimerButton(ActionEvent event) {
        Action selectedRow = (Action) tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette action?");
            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                query.supprimerAction(selectedRow.getId());
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action supprimée avec succès");
                successAlert.showAndWait();
                ObservableList<Action> items = tableView.getItems();
                items.remove(selectedRow);
                tableView.refresh();
            }
        }
    }*/
   public void showAction() {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/showAction.fxml"));
           Parent root = loader.load();
           showActions showactioncontroller = loader.getController();
           showactioncontroller.showAction(containerView);
       } catch (IOException e) {
           e.printStackTrace();
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
