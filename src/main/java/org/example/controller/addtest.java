package org.example.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.entity.Test;
import org.example.service.testMethode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class addtest {

    @FXML
    private Label LoginMessageLabel1;

    @FXML
    private Button cancelButton1;

    @FXML
    private Button inscrire;

    @FXML
    private TextField nom;

    @FXML
    private TextField type;

    @FXML
    private TextField type1;

    @FXML
    private TextField type2;

    @FXML
    private TextField type3;

    @FXML
    private TextField type31;

    @FXML
    private TextField type32;

    @FXML
    private TextField type33;

    @FXML
    private TextField type331;

    @FXML
    private TextField type3311;

    @FXML
    private TextField type3312;

    @FXML
    private TextField type3313;



    @FXML
    void inscrire(MouseEvent event) {

    }
    @FXML
    void listee(MouseEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/Workshop/Affichetest.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2;
        stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    void inscrire(ActionEvent event)throws IOException {
        if (question.getText().isEmpty() || repense.getText().isEmpty() ){
            // Alert user to fill in all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.showAndWait();
            return;}
        String question = question.getText();
        String repense = repense.getText();
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
        WorkshopMethode pcd = new WorkshopMethode();
        LocalTime Heure = LocalTime.parse(HeureString);
        Workshop w = new Workshop(Nom, Type, Date, Heure, Cours);
        pcd.ajouterWorkshop(w);



        System.out.println("Done!");
        Parent root2 = FXMLLoader.load(getClass().getResource("/Workshop/AfficherWorkshop.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2;
        stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();

    }


    @FXML
    void login(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
}
