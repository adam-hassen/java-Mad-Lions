package EDU.userjava1.controllers;

import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class donnerpwd implements Initializable {

    @FXML
    private Button confirmer;

    @FXML
    private PasswordField password;
    @FXML
    private TextField email;
    @FXML
    void confirmer(ActionEvent event) throws IOException {
        UserServices GS = new UserServices();
        int id;
        id = GS.Login(email.getText(), password.getText());
        if (id > 0) { // Modification de la condition
            // Fermer la fenêtre actuelle (profil)
            Stage stage = (Stage) confirmer.getScene().getWindow();
            // Fermer la fenêtre du profil
            stage.close();


            // Supprimer l'utilisateur
            GS.supprimeruser(Login.v.getId());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Utilisateur supprimé !");
            alert.showAndWait();

            // Ouvrir la nouvelle fenêtre de connexion

            Parent root2 = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
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


    }
    @FXML
    void back(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
