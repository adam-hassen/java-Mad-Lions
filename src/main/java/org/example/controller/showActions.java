package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.entity.Action;
import org.example.service.ActionService;
import org.example.service.TypeNameService;

import java.util.List;

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
        List<Action> actionList = query.afficherActions();
        ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
        containerView.getChildren().clear();
        for (Action action : observableList) {
            VBox itemBox = new VBox();
            itemBox.setId("itemBox");

            HBox typeNameBox = new HBox();
            Label typeNameLabel = new Label("Type Name:");
            TextField typeNameTextField = new TextField();
            typeNameTextField.setId("typeNameTextField");
            typeNameTextField.setText(action.getType_id().getType());
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

            itemBox.getChildren().addAll(typeNameBox, dangerBox, dateBox, descriptionBox);
            containerView.getChildren().add(itemBox);
        }
    }
}
