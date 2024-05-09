package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.animation.*;
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
import javafx.scene.layout.Pane;
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
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class GestionnerConsoController {
    @FXML
    public HBox containerView;
    @FXML
    public Button Home;

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
        //modifierButton.setOnAction(this::handleModifierButton);
        showAction(containerView);
        //frame animation
            double totalWidth = containerView.getChildren().stream()
                    .mapToDouble(node -> node.getBoundsInLocal().getWidth())
                    .sum();
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(scrollPane.hvalueProperty(), 0)),
                    new KeyFrame(Duration.seconds(30), new KeyValue(scrollPane.hvalueProperty(), 1)),
                    new KeyFrame(Duration.seconds(40), new KeyValue(scrollPane.hvalueProperty(), 0))
            );
        scrollPane.setOnMouseClicked(event -> {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                double currentHvalue = scrollPane.getHvalue();
                timeline.pause();
                PauseTransition pause = new PauseTransition(Duration.seconds(6));
                pause.setOnFinished(e -> {
                    scrollPane.setHvalue(currentHvalue);
                    timeline.play();
                });
                pause.play();
            } else {
                timeline.play();
            }
        });

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        //sidebar animation
    }

    @FXML
    public void naviguerVersHome(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    public void AjouterAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/ActionForm.fxml"));
            //loader.setController(new ValiderFormAction());
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
            showAction(containerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleModifierButton(ActionEvent event) {
        Node selectedNode = containerView.getChildren().get(containerView.getChildren().size() - 1);
        if (selectedNode instanceof HBox) {
            HBox selectedRow = (HBox) selectedNode;
            // Extract the data from the selected row
            String typeName = ((TextField) selectedRow.getChildren().get(0)).getText();
            String danger = ((TextField) selectedRow.getChildren().get(1)).getText();
            String date = ((TextField) selectedRow.getChildren().get(2)).getText();
            String description = ((TextField) selectedRow.getChildren().get(3)).getText();
            String quantiteTime = ((TextField) selectedRow.getChildren().get(4)).getText();
            TypeName tpn = query2.cherchertypename(typeName);
            LocalDate actionDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                actionDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                actionDate = LocalDate.now();
            }
            Action selectedAction = new Action(tpn, 0.0, actionDate, danger, description);
            selectedAction.setQuantite_time(quantiteTime);
            System.out.println("sssssssssss" + selectedAction);
           // Modification(selectedAction);
        }
    }
    public void Modification(Action selectedAction) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* @FXML
    public void handleSupprimerButton(ActionEvent event) {
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
                int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                int selectedId = selectedRow.getId();

                query.supprimerAction(selectedId);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action supprimée avec succès");
                successAlert.showAndWait();

                ObservableList<Action> items = tableView.getItems();
                items.remove(selectedIndex);
                tableView.refresh();
            }
        }
    }*/
   public void showAction(Pane containerView) {
       try {
           //containerView.getChildren().clear();
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/showAction.fxml"));
           Parent root = loader.load();
           showActions showactioncontroller = loader.getController();
           containerView.getChildren().clear();
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
    public void handleHome(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
