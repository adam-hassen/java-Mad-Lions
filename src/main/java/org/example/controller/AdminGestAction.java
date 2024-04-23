package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.entity.Action;
import org.example.entity.TypeName;
import org.example.service.ActionService;
import org.example.service.TypeNameService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class AdminGestAction {
    @FXML
    public ComboBox<TypeName> Type;
    @FXML
    public TextField Description;
    @FXML
    public DatePicker Date;
    @FXML
    public TextField Quantite;
    @FXML
    public Button ValiderAction;
    @FXML
    public ComboBox<Integer>  secondComboBox;
    @FXML
    public ComboBox<Integer> minuteComboBox;
    @FXML
    public ComboBox<Integer> hourComboBox;
    @FXML
    public ToggleGroup toggleGroup;
    @FXML
    public ToggleButton timeToggle;
    @FXML
    public ToggleButton quantiteToggle;
    @FXML
    private Label timeLabel;
    @FXML
    private Label quantiteLabel;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn<Action,Integer> ColTypeName;
    @FXML
    public TableColumn<Action,Integer> ColDanger;
    @FXML
    public TableColumn<Action,Integer> ColDate;
    @FXML
    public TableColumn<Action,Integer> ColDescription;
    private ActionService query;
    private TypeNameService query2;
    @FXML
    private Button réinitialisationButton;
    @FXML
    private Button home;
    @FXML
    private VBox vboxside;
    @FXML
    public void initialize() {
        home.setOnAction(this::naviguerVersHome);
        query2 = new TypeNameService();
        query = new ActionService();
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        hourComboBox.setItems(hours);
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            minutes.add(i);
        }
        minuteComboBox.setItems(minutes);
        ObservableList<Integer> seconds = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            seconds.add(i);
        }
        secondComboBox.setItems(seconds);
        ValiderAction.setOnAction(this::AjouterAction);
        showtypes();
        //toggle quantite
        toggleGroup = new ToggleGroup();
        timeToggle.setToggleGroup(toggleGroup);
        quantiteToggle.setToggleGroup(toggleGroup);
        // reset show quantity type
        timeLabel.setVisible(false);
        quantiteToggle.setVisible(false);
        timeToggle.setVisible(false);
        hourComboBox.setVisible(false);
        minuteComboBox.setVisible(false);
        secondComboBox.setVisible(false);
        quantiteLabel.setVisible(false);
        Quantite.setVisible(true);
        //show tables of actions
        showAction();
    }
    @FXML
    public void naviguerVersHome(ActionEvent event) {
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Admin/Gestion Consommation/AdminGestConso.fxml"));
            home.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleToggle() {
        ToggleButton selectedToggle = (ToggleButton) toggleGroup.getSelectedToggle();
        if (selectedToggle == timeToggle) {
            Quantite.setText("0");
            timeLabel.setVisible(true);
            hourComboBox.setVisible(true);
            minuteComboBox.setVisible(true);
            secondComboBox.setVisible(true);
            quantiteLabel.setVisible(false);
            Quantite.setVisible(false);
        } else if (selectedToggle == quantiteToggle) {
            hourComboBox.setValue(0);
            minuteComboBox.setValue(0);
            secondComboBox.setValue(0);
            timeLabel.setVisible(false);
            hourComboBox.setVisible(false);
            minuteComboBox.setVisible(false);
            secondComboBox.setVisible(false);
            quantiteLabel.setVisible(true);
            Quantite.setVisible(true);
        }
        //toggle metric type
        TypeName selectedType = Type.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            if (selectedType.getMateriel().equals("temps")) {
                timeLabel.setVisible(true);
                hourComboBox.setVisible(true);
                minuteComboBox.setVisible(true);
                secondComboBox.setVisible(true);
                quantiteLabel.setVisible(false);
                Quantite.setVisible(false);
            } else if (selectedType.getMateriel().equals("solid")) {
                timeLabel.setVisible(false);
                hourComboBox.setVisible(false);
                minuteComboBox.setVisible(false);
                secondComboBox.setVisible(false);
                quantiteLabel.setVisible(true);
                Quantite.setVisible(true);
            }
        }
    }
    public void AjouterAction(ActionEvent event){
        int hour = 0;
        if (hourComboBox.getValue() != null) {
            hour=hourComboBox.getValue();
        }
        int minute = 0;
        if (minuteComboBox.getValue() != null) {
            minute=minuteComboBox.getValue();
        }
        int second = 0;
        if (secondComboBox.getValue() != null) {
            second=secondComboBox.getValue();
        }
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        Double k = 0.0;
        if (!Quantite.getText().isEmpty()) {
            k = Double.parseDouble(Quantite.getText());
        }
        if ((!Quantite.getText().isEmpty() || time!=null) && (Type.getValue()!=null) && (Date.getValue()!=null)) {
            Action act = new Action(Type.getValue(), k, Date.getValue(), Description.getText(), time);
            act = query.calculerScoreEtDanger(act);
            act.setUser_id(1);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir ajouter cette action?");
            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                double quantite = 0.0;
                if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", Description.getText())) {
                    Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                    validationAlert.setTitle("Gestion De Consommation :");
                    validationAlert.setHeaderText(null);
                    validationAlert.setContentText("Description doit contenir que des lettes, des nombres et des escpaces!");
                    validationAlert.showAndWait();
                    return;
                }
                else if (!Quantite.getText().isEmpty()) {
                    try {
                        quantite = Double.parseDouble(Quantite.getText());
                        if (quantite <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                        validationAlert.setTitle("Gestion Consommation :");
                        validationAlert.setHeaderText(null);
                        validationAlert.setContentText("Quantite dois contenir que des nombres strictement positives!");
                        validationAlert.showAndWait();
                        return;
                    }
                }
                    query.ajouterAction(act);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Gestion De Consommation Alert!");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Action ajoutée avec succès!");
                    successAlert.showAndWait();
                    List<Action> actionList = query.afficherActions();
                    ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
                    tableView.setItems(observableList);
                    tableView.refresh();
            }
        }
        else{
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Ajout invalide ");
            successAlert.showAndWait();
        }
    }
    @FXML
    private void handleModifierButton(ActionEvent event) {
        Action selectedRow = (Action) tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            ObservableList<Action> items = tableView.getItems();
            Type.setValue(selectedRow.getType_id());
            Description.setText(selectedRow.getDescription());
            Date.setValue(selectedRow.getDate());
            if (selectedRow.getQuantite_time() != null) {
                String[] timeParts = selectedRow.getQuantite_time().split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                int second = Integer.parseInt(timeParts[2]);
                hourComboBox.setValue(hour);
                minuteComboBox.setValue(minute);
                secondComboBox.setValue(second);
                timeToggle.setSelected(true);
                quantiteToggle.setSelected(false);
                timeLabel.setVisible(true);
                hourComboBox.setVisible(true);
                minuteComboBox.setVisible(true);
                secondComboBox.setVisible(true);
                quantiteLabel.setVisible(false);
                Quantite.setVisible(false);
            } else {
                timeToggle.setSelected(false);
                quantiteToggle.setSelected(true);
                timeLabel.setVisible(false);
                hourComboBox.setVisible(false);
                minuteComboBox.setVisible(false);
                secondComboBox.setVisible(false);
                quantiteLabel.setVisible(true);
                Quantite.setVisible(true);
                Quantite.setText(Double.toString(selectedRow.getQuantite()));
            }
            Description.setText(selectedRow.getDescription());
            tableView.refresh();
        }
    }
    @FXML
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
    }
    public void showtypes(){
        ObservableList<TypeName> types = FXCollections.observableArrayList(query2.afficherTypeName());;
        Type.setItems(types);
    }
    public void showAction() {
        List<Action> actionList = query.afficherActions();
        ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
        ColTypeName.setCellValueFactory(new PropertyValueFactory<Action,Integer>("type_id"));
        ColDanger.setCellValueFactory(new PropertyValueFactory<Action,Integer>("niveau_danger"));
        ColDate.setCellValueFactory(new PropertyValueFactory<Action,Integer>("Date"));
        ColDescription.setCellValueFactory(new PropertyValueFactory<Action,Integer>("Description"));
        tableView.setItems(observableList);
    }
    public void handleModifierAct(ActionEvent event){
        Action selectedRow = (Action) tableView.getSelectionModel().getSelectedItem();
        int hour = 0;
        if (hourComboBox.getValue() != null) {
            hour=hourComboBox.getValue();
        }
        int minute = 0;
        if (minuteComboBox.getValue() != null) {
            minute=minuteComboBox.getValue();
        }
        int second = 0;
        if (secondComboBox.getValue() != null) {
            second=secondComboBox.getValue();
        }
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        Double k = 0.0;
        if (!Quantite.getText().isEmpty()) {
            k = Double.parseDouble(Quantite.getText());
        }
        if ((!Quantite.getText().isEmpty() || time!=null) && (Type.getValue()!=null) && (Date.getValue()!=null)) {
            Action act = new Action(Type.getValue(), k, Date.getValue(), Description.getText(), time);
            act = query.calculerScoreEtDanger(act);
            act.setId(selectedRow.getId());
            act.setUser_id(selectedRow.getUser_id());
            act.setLocation_id(selectedRow.getLocation_id());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir modifier cette action?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                double quantite = 0.0;
                if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", Description.getText())) {
                    Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                    validationAlert.setTitle("Gestion De Consommation :");
                    validationAlert.setHeaderText(null);
                    validationAlert.setContentText("Description doit contenir que des lettes, des nombres et des escpaces!");
                    validationAlert.showAndWait();
                    return;
                }
                else if (!Quantite.getText().isEmpty()) {
                    try {
                        quantite = Double.parseDouble(Quantite.getText());
                        if (quantite <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                        validationAlert.setTitle("Gestion Consommation :");
                        validationAlert.setHeaderText(null);
                        validationAlert.setContentText("Quantite dois contenir que des nombres strictement positives!");
                        validationAlert.showAndWait();
                        return;
                    }
                }
                query.modifierAction(act.getId(), act);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action modifiée avec succès!");
                successAlert.showAndWait();
                List<Action> actionList = query.afficherActions();
                ObservableList<Action> observableList = FXCollections.observableArrayList(actionList);
                tableView.setItems(observableList);
                tableView.refresh();
            }
        }
    }
    public void handleRéinitialisationButton(ActionEvent event) {
        minuteComboBox.setValue(null);
        secondComboBox.setValue(null);
        hourComboBox.setValue(null);
        Quantite.clear();
        Date.setValue(null);
        Type.setValue(null);
        Description.setText(null);
        Type.getSelectionModel().clearSelection();
    }
}
