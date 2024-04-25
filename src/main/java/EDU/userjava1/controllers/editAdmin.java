package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class editAdmin implements Initializable {

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

    private profile profileAdminController;

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
        User1 utilisateurModifie = new User1(nouvelEmail, nouveaumdp, nouveauNom, nouveladress, nouveauNum, nouveauGenre, nouveauPrenom);

        userServices.modifieruser(utilisateurModifie, Login.v.getId());

        System.out.println("Utilisateur modifié avec succès");
        profileAdminController.rafraichirInformationsUtilisateur(utilisateurModifie);

        // Vider les champs du formulaire
        clearFields();

        Stage stage = (Stage) modifier.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nom.setText(Login.v.getName());
        prenom.setText(Login.v.getPrenom());
        num.setText(String.valueOf(Login.v.getNumero()));
        genre.setText(Login.v.getGenre());
        email.setText(Login.v.getUsername());
        mdp.setText(Login.v.getPassword());
        adress.setText(Login.v.getAdress());
    }

    public void setProfileController(profile profileAdminController) {
        this.profileAdminController = profileAdminController;
    }

    public void clearFields() {
        nom.clear();
        prenom.clear();
        num.clear();
        genre.clear();
        email.clear();
        mdp.clear();
        adress.clear();
    }
}
