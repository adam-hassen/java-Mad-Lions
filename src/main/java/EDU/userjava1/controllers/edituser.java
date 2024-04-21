package EDU.userjava1.controllers;

import EDU.userjava1.controllers.Login;
import EDU.userjava1.controllers.profile;
import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class edituser implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private TextField mdp;
    @FXML
    private TextField adress;
    @FXML
    private TextField genre;

    @FXML
    private Button modifier;

    @FXML
    private TextField nom;

    @FXML
    private TextField num;

    @FXML
    private TextField prenom;

    @FXML
    void modifier(ActionEvent event) {
        UserServices userServices = new UserServices();

        // Récupérer les valeurs modifiées depuis les champs de texte
        String nouveauNom = nom.getText();
        String nouveauPrenom = prenom.getText();
        int nouveauNum = Integer.parseInt(num.getText());
        String nouveauGenre = genre.getText();
        String nouvelEmail = email.getText();
        String nouveaumdp = mdp.getText();
        String nouveladress = adress.getText();
        // Créer un nouvel utilisateur avec les valeurs modifiées
        User1 utilisateurModifie = new User1(nouvelEmail,nouveaumdp, nouveladress ,nouveauNom,   nouveauNum, nouveauGenre, nouveauPrenom);

        userServices.modifieruser(utilisateurModifie, Login.v.getId());

        System.out.println("Utilisateur modifié avec succès");

        // Rafraîchir l'affichage dans la page profile avec les nouvelles informations de l'utilisateur modifié
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/account.fxml"));
            Parent root = loader.load();
            profile controller = loader.getController();
            controller.rafraichirAffichage(utilisateurModifie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nom.setText(Login.v.getName());
        prenom.setText(Login.v.getPrenom());
        num.setText(String.valueOf(Login.v.getNumero()));
        genre.setText(Login.v.getGenre());
        email.setText(Login.v.getUsername());
        mdp.setText(Login.v.getGenre());
        adress.setText(Login.v.getUsername());
    }
}
