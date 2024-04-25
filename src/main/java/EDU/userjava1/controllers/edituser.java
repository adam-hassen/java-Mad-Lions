package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private profile profileController;

    @FXML
    void modifier(ActionEvent event) {
        UserServices userServices = new UserServices();

        // Récupérer les valeurs modifiées depuis les champs de texte
        String nouveauNom = nom.getText();
        String nouveauPrenom = prenom.getText();
        String nouveauNumString = num.getText();

        // Vérifier si la longueur du numéro est exactement 8
        if (nouveauNumString.length() != 8) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Numéro invalide", "Le numéro doit contenir exactement 8 chiffres.");
            return;
        }

        int nouveauNum;
        try {
            nouveauNum = Integer.parseInt(nouveauNumString);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Numéro invalide", "Le numéro doit être un nombre entier.");
            return;
        }

        String nouveauGenre = genre.getText();
        String nouvelEmail = email.getText();
        String nouveaumdp = mdp.getText();
        String nouvelAdress = adress.getText();
        if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty() || nouveauNumString.isEmpty() || nouvelEmail.isEmpty() ||
                nouveaumdp.isEmpty() || nouvelAdress.isEmpty() || nouveauGenre.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier les saisies avec des expressions régulières
        if (!nouvelEmail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Adresse e-mail invalide", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!nouveauGenre.equalsIgnoreCase("homme") && !nouveauGenre.equalsIgnoreCase("femme")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Genre invalide", "Le genre doit être 'homme' ou 'femme'.");
            return;
        }

        // Créer un nouvel utilisateur avec les valeurs modifiées
        User1 utilisateurModifie = new User1(nouvelEmail, nouveaumdp, nouveauNom, nouvelAdress, nouveauNum, nouveauGenre, nouveauPrenom);

        userServices.modifieruser(utilisateurModifie, Login.v.getId());

        showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Utilisateur modifié avec succès", null);

        profileController.rafraichirInformationsUtilisateur(utilisateurModifie);

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

    public void setProfileController(profile profileController) {
        this.profileController = profileController;
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

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
