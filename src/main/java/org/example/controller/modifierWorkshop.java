package org.example.controller;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.entity.Workshop;
import org.example.interfaces.WorkshopService;
import org.example.service.WorkshopMethode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class modifierWorkshop  implements Initializable {


    @FXML
    private Label LoginMessageLabel1;

    @FXML
    private Button cancelButton1;

    @FXML
    private TextField cours;

    @FXML
    private DatePicker date;

    @FXML
    private TextField heure;

    @FXML
    private Button inscrire;

    @FXML
    private Button login111;

    @FXML
    private TextField nom;

    @FXML
    private AnchorPane side_ankerpane1;

    @FXML
    private TextField type;
    public Workshop workshop;
    @FXML
    void liste(MouseEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/Workshop/AfficherWorkshop.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2;
        stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }
    @FXML
    void getDate1() {
        // Get today's date
        LocalDate today = LocalDate.now();
// Add 2 years to today's date
        LocalDate futureDate = today.plusYears(2);
// Set the DatePicker value to the future date
        date.setValue(futureDate);
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    void inscrire(ActionEvent event)throws IOException {
        if (nom.getText().isEmpty() || type.getText().isEmpty() ||
                date.getValue() == null || date.getValue().isBefore(LocalDate.now())||
                heure.getText().isEmpty()||
                cours.getText().isEmpty()){
            // Alert user to fill in all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.showAndWait();
            return;}
        String Nom = nom.getText();
        String Type = type.getText();
        LocalDate Date = date.getValue();
        String HeureString = heure.getText();
        String Cours = cours.getText();

        if (!Nom.matches("^[a-zA-Z]+$")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur !");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir un nom valide !");
            alert.showAndWait();
            return;
        }
        try {
            LocalTime.parse(HeureString);
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur !");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir une heure valide !");
            alert.showAndWait();
            return;
        }
        WorkshopMethode pcd = new WorkshopMethode();
        LocalTime Heure = LocalTime.parse(HeureString);
        Workshop w = new Workshop(Nom, Type, Date, Heure, Cours);
        pcd.modifierWorkshop(w,session.workshop.getId());



        System.out.println("Done!");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

    }


    @FXML
    void login(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // getDate1();
        workshop=session.workshop;
        nom.setText(workshop.getNom());
        type.setText(workshop.getType());
        date.setValue(workshop.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // Adjust the pattern as needed
        String heureText = workshop.getHeure().format(formatter);
        heure.setText(heureText);
        cours.setText(workshop.getCours());
        System.out.println(workshop);
    }
    public void compteur(String c){
        nom.setText("mehdiiiii");
    }

    public TextField getNom() {
        return nom;
    }
}
