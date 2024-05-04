package EDU.userjava1.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home111 implements Initializable {

    @FXML
    private Button email;

    @FXML
    void profile(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/account.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email.setText(Login.v.getName());

    }
    @FXML
    void consomation(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/HomeGestionAction.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void evenement(ActionEvent event) {

    }

    @FXML
    void produit(ActionEvent event) {

    }


    @FXML
    void recyclage(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ProduitRecyclable/AfficherProduitRecyclable.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            // Créer une transition de fondu pour la nouvelle scène
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
            fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
            fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

            // Démarrer la transition de fondu
            fadeTransition.play();

            // Afficher la nouvelle scène dans une nouvelle fenêtre
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle après la transition
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void workshop(ActionEvent event) {

    }

}
