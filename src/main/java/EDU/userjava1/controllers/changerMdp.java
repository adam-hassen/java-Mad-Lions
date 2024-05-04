package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class changerMdp implements Initializable {
    @FXML
    private TextField adress;

    @FXML
    private Button confirmer1;

    @FXML
    private TextField email;

    @FXML
    private TextField genre;

    @FXML
    private TextField mdp;

    @FXML
    private TextField mdp1;

    @FXML
    private TextField nom;

    @FXML
    private TextField num;

    @FXML
    private TextField prenom;
    @FXML
    void confirmer(ActionEvent event) throws IOException  {
        UserServices GS = new UserServices();
        UserServices userServices = new UserServices();
        int id;
        id = GS.Login(email.getText(),mdp.getText());
    if (id > 0) {
        // Récupérer les valeurs modifiées depuis les champs de texte
        String nouveauNom = nom.getText();
        String nouveauPrenom = prenom.getText();
        int nouveauNum = Integer.parseInt(num.getText());
        String nouveauGenre = genre.getText();
        String nouvelEmail = email.getText();
        String nouveaumdp = mdp1.getText();
        String nouveladress = adress.getText();
        if (!nouveaumdp.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            // Afficher une alerte si le mot de passe ne respecte pas les critères
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur !");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe doit contenir au moins 8 caractères et au moins un symbole !");
            alert.showAndWait();
            return;
        }
        // Créer un nouvel utilisateur avec les valeurs modifiées
        User1 utilisateurModifie = new User1(nouvelEmail,nouveaumdp, nouveauNom,nouveladress,   nouveauNum, nouveauGenre, nouveauPrenom);


        // Supprimer l'utilisateur
        userServices.modifierMDP(utilisateurModifie, Login.v.getId());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Le mot de passe changer!");
            alert.showAndWait();


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur !");
            alert.setHeaderText(null);
            alert.setContentText("Mot de passe incorrect !");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        UserServices GS = new UserServices();


        email.setText(Login.v.getUsername());
        nom.setText(Login.v.getName());
        prenom.setText(Login.v.getPrenom());
        num.setText(String.valueOf(Login.v.getNumero()));
        genre.setText(Login.v.getGenre());
        email.setText(Login.v.getUsername());
        adress.setText(Login.v.getAdress());

    }

    }


