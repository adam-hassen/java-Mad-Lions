package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.entity.Action;
import org.example.service.ActionService;
import org.example.service.TypeNameService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class showActions {
    @FXML
    public TextField typeNameTextField;
    @FXML
    public TextField dangerTextField;
    @FXML
    public TextField dateTextField;
    @FXML
    public TextField descriptionTextField;
    @FXML
    private VBox itemBox;
    private ActionService query;
    private TypeNameService query2;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        query2 = new TypeNameService();
        query = new ActionService();
        //AjouterAction.setOnAction(this::AjouterAction);
        // modifierButton.setOnAction(this::handleModifierButton);
        //show tables of actions
        itemBox.getChildren().clear();
        itemBox.getChildren().addAll(
                typeNameTextField,
                dangerTextField,
                dateTextField,
                descriptionTextField
        );
        //showAction();
    }
    public void showAction(Pane containerView) {
        List<Action> actionList = query.afficherActions(Login.v.getId());
        ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
        containerView.getChildren().clear();
        for (Action action : observableList) {
            VBox itemBox = new VBox();
            itemBox.setId("itemBox");

            HBox typeNameBox = new HBox();
            Label typeNameLabel = new Label("Type Name:");
            TextField typeNameTextField = new TextField();
            typeNameTextField.setId("typeNameTextField");
            typeNameTextField.setText(action.getType_id().getNom());
            typeNameBox.getChildren().addAll(typeNameLabel, typeNameTextField);

            HBox dangerBox = new HBox();
            Label dangerLabel = new Label("Niveau de danger:");
            TextField dangerTextField = new TextField();
            dangerTextField.setId("dangerTextField");
            dangerTextField.setText(String.valueOf(action.getNiveau_danger()));
            dangerBox.getChildren().addAll(dangerLabel, dangerTextField);

            HBox dateBox = new HBox();
            Label dateLabel = new Label("Date:");
            TextField dateTextField = new TextField();
            dateTextField.setId("dateTextField");
            dateTextField.setText(String.valueOf(action.getDate()));
            dateBox.getChildren().addAll(dateLabel, dateTextField);

            HBox descriptionBox = new HBox();
            Label descriptionLabel = new Label("Description:");
            TextField descriptionTextField = new TextField();
            descriptionTextField.setId("descriptionTextField");
            descriptionTextField.setText(action.getDescription());
            descriptionBox.getChildren().addAll(descriptionLabel, descriptionTextField);

            HBox buttonBox = new HBox();
            Button supprimeButton = new Button("Supprimer");
            Button modifierButton = new Button("Modifier");
            supprimeButton.getStyleClass().add("button-liste");
            modifierButton.getStyleClass().add("button-liste");
            GestionnerConsoController gestionnerConsoController = new GestionnerConsoController();
            action.setQuantite_time(query.checrherAction(action.getId()).getQuantite_time());
            modifierButton.setOnAction(event -> {
                gestionnerConsoController.Modification(action);
                //containerView.getChildren().clear();
                gestionnerConsoController.showAction(containerView);

                //refreshUI(containerView,observableList);
                // gestionnerConsoController.handleSupprimerButton(action);
            });
            supprimeButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette action?");

                ButtonType buttonTypeYes = new ButtonType("Oui");
                ButtonType buttonTypeNo = new ButtonType("Non");

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {
                    query.supprimerAction(action.getId());
                    containerView.getChildren().remove(itemBox);
                    observableList.remove(action);
                }
            });
            buttonBox.getChildren().addAll(supprimeButton, modifierButton);
            itemBox.getChildren().addAll(typeNameBox, dangerBox, dateBox, descriptionBox, buttonBox);
            containerView.getChildren().add(itemBox);
        }
    }
}
