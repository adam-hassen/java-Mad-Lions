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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
public class AdminGestType {
    @FXML
    private TableView<TypeName> tableView;
    @FXML
    private TableColumn<TypeName, Integer> colId;
    @FXML
    private TableColumn<TypeName, Integer> colActionTypeId;
    @FXML
    private TableColumn<TypeName, String> colNom;
    @FXML
    private TableColumn<TypeName, Double> colScore;
    @FXML
    private TableColumn<TypeName, String> colMateriel;
    @FXML
    private TableColumn<TypeName, String> colType;
    @FXML
    private TableColumn<TypeName, Double> colUtilMax;
    @FXML
    private TextField actionTypeIdField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField scoreField;
    @FXML
    private ComboBox<String> materielField;
    @FXML
    private ComboBox<String> typeField;
    @FXML
    private TextField utilMaxField;
    private ActionService query;
    private TypeNameService query2;
    @FXML
    private Button home;
    @FXML
    private VBox vboxside;
    @FXML
    public Button Home;
    @FXML
    public void initialize() {
        //home.setOnAction(this::naviguerVersHome);
        query2 = new TypeNameService();
        query = new ActionService();

        List<String> materielValues = Arrays.asList("solid", "temps");
        materielField.setItems(FXCollections.observableArrayList(materielValues));
        showAction();
        List<String> typeValues = Arrays.asList("electrique", "carburant", "plastique", "gaz");
        typeField.setItems(FXCollections.observableArrayList(typeValues));
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
    public void showAction() {
        List<TypeName> actionList = query2.afficherTypeName();
        ObservableList<TypeName> observableList = FXCollections.observableArrayList(actionList);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colActionTypeId.setCellValueFactory(new PropertyValueFactory<>("action_type_id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colMateriel.setCellValueFactory(new PropertyValueFactory<>("materiel"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUtilMax.setCellValueFactory(new PropertyValueFactory<>("util_max"));
        tableView.setItems(observableList);
    }

        public void AjouterTypeAction(ActionEvent event) {
            if (nomField.getText().isEmpty() || scoreField.getText().isEmpty() || materielField.getValue().isEmpty() || typeField.getValue().isEmpty() || utilMaxField.getText().isEmpty()) {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Gestion Consommation :");
                validationAlert.setHeaderText(null);
                validationAlert.setContentText("Tous les champs doivent être remplis!");
                validationAlert.showAndWait();
                return;
            }

            TypeName act = new TypeName(1, nomField.getText(), 0, materielField.getValue(), typeField.getValue(), 0);
            double score = 0.0;

            if (!Pattern.matches("^[a-zA-Z\\s]+$", nomField.getText())) {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Gestion Consommation :");
                validationAlert.setHeaderText(null);
                validationAlert.setContentText("Le nom doit être composé uniquement de lettres!");
                validationAlert.showAndWait();
                return;
            }

            if (!scoreField.getText().isEmpty()) {
                try {
                    score = Double.parseDouble(scoreField.getText());
                    if (score <= 0) {
                        throw new NumberFormatException();
                    }
                    act.setScore(score);
                } catch (NumberFormatException e) {
                    Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                    validationAlert.setTitle("Gestion Consommation :");
                    validationAlert.setHeaderText(null);
                    validationAlert.setContentText("Le score doit être un nombre positif!");
                    validationAlert.showAndWait();
                    return;
                }
            }
            if (!utilMaxField.getText().isEmpty()) {
                try {
                    score = Double.parseDouble(utilMaxField.getText());
                    if (score <= 0) {
                        throw new NumberFormatException();
                    }
                    act.setUtil_max(score);
                } catch (NumberFormatException e) {
                    Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                    validationAlert.setTitle("Gestion Consommation :");
                    validationAlert.setHeaderText(null);
                    validationAlert.setContentText("L'utilisation maximale doit être un nombre positif!");
                    validationAlert.showAndWait();
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir ajouter cette action?");
            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                query2.ajouterTypeName(act);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action ajoutée avec succès!");
                successAlert.showAndWait();
                List<TypeName> actionList = query2.afficherTypeName();
                ObservableList<TypeName> observableList = FXCollections.observableArrayList(actionList);
                tableView.setItems(observableList);
                tableView.refresh();
            }
            else {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action annulée!");
                successAlert.showAndWait();
            }
        }
    @FXML
    private void handleModifierButton(ActionEvent event) {
        TypeName selectedRow = (TypeName) tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            ObservableList<TypeName> items = tableView.getItems();
            nomField.setText(selectedRow.getNom());
            scoreField.setText(String.valueOf(selectedRow.getScore()));
            materielField.setValue(selectedRow.getMateriel());
            typeField.setValue(selectedRow.getType());
            utilMaxField.setText(String.valueOf(selectedRow.getUtil_max()));
            tableView.refresh();
        }
    }
    public void handleModifierAct(ActionEvent event){
        TypeName selectedRow = (TypeName) tableView.getSelectionModel().getSelectedItem();

        if (nomField.getText().isEmpty() || scoreField.getText().isEmpty() || materielField.getValue() == null || typeField.getValue() == null || utilMaxField.getText().isEmpty()) {
            Alert validationAlert = new Alert(Alert.AlertType.ERROR);
            validationAlert.setTitle("Gestion Consommation :");
            validationAlert.setHeaderText(null);
            validationAlert.setContentText("Tous les champs doivent être remplis!");
            validationAlert.showAndWait();
            return;
        }

        if (!Pattern.matches("^[a-zA-Z\\s]+$", nomField.getText())) {
            Alert validationAlert = new Alert(Alert.AlertType.ERROR);
            validationAlert.setTitle("Gestoin Consommation :");
            validationAlert.setHeaderText(null);
            validationAlert.setContentText("Le nom doit être composé uniquement de lettres!");
            validationAlert.showAndWait();
            return;
        }

        double score = 0.0;
        if (!utilMaxField.getText().isEmpty()) {
            try {
                score = Double.parseDouble(utilMaxField.getText());
                if (score <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Gestion Consommation :");
                validationAlert.setHeaderText(null);
                validationAlert.setContentText("L'utilisation maximal doit être un entier strictement positif!");
                validationAlert.showAndWait();
                return;
            }
        }

        score = 0.0;
        if (!scoreField.getText().isEmpty()) {
            try {
                score = Double.parseDouble(scoreField.getText());
                if (score <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Alert validationAlert = new Alert(Alert.AlertType.ERROR);
                validationAlert.setTitle("Gestion Consommation :");
                validationAlert.setHeaderText(null);
                validationAlert.setContentText("Score doit être un entier strictement positif!");
                validationAlert.showAndWait();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir modifier cette action?");
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            TypeName act = new TypeName(1, nomField.getText(), Double.parseDouble(scoreField.getText()), materielField.getValue(), typeField.getValue(), Double.parseDouble(utilMaxField.getText()));
            query2.modifierTypename(selectedRow.getId(), act);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Action modifiée avec succès!");
            successAlert.showAndWait();
            List<TypeName> actionList = query2.afficherTypeName();
            ObservableList<TypeName> observableList = FXCollections.observableArrayList(actionList);
            tableView.setItems(observableList);
            tableView.refresh();
        }
        else {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Action annulée!");
            successAlert.showAndWait();
        }
    }
    @FXML
    private void handleSupprimerButton(ActionEvent event) {
        TypeName selectedRow = (TypeName) tableView.getSelectionModel().getSelectedItem();
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
                query2.supprimerTypeName(selectedRow.getId());
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Gestion De Consommation Alert!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Action supprimée avec succès");
                successAlert.showAndWait();
                ObservableList<TypeName> items = tableView.getItems();
                items.remove(selectedRow);
                tableView.refresh();
            }
        }
    }
    public void handleHome(){
        try{
            Parent root= FXMLLoader.load(getClass().getResource("/Admin/Gestion Consommation/AdminGestConso.fxml"));
            Home.getScene().setRoot(root);
        }
        catch (IOException ex){
            System.err.println("Error loading FXML document: " + ex);
            ex.printStackTrace();
        }
    }
}
