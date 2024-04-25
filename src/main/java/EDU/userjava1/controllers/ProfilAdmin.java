package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilAdmin implements Initializable {

    @FXML
    private Button changer;

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private Label email;

    @FXML
    private Label prenom;

    @FXML
    private Label genre;

    @FXML
    private Button home;

    @FXML
    private Button logout;

    @FXML
    private Label nom;

    @FXML
    private Label num;

    @FXML
    private Pane pane_131;

    @FXML
    private Pane pane_1311;

    @FXML
    private Pane pane_132;

    private editAdmin editAdminController1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afficherInformationsUtilisateur();
    }

    private void afficherInformationsUtilisateur() {
        nom.setText(Login.v.getName());
        email.setText(Login.v.getUsername());
        num.setText(String.valueOf(Login.v.getNumero()));
        prenom.setText(Login.v.getPrenom());
        genre.setText(Login.v.getGenre());
    }

    @FXML
    void delete(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/donnerpassword.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

    @FXML
    void edit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edituser.fxml"));
        Parent root2 = loader.load();
      //  editAdminController1 = loader.getController();

        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }

    @FXML
    void changer(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/changerMdp.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("deconnexion!");
        alert.showAndWait();
        Parent root2 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

    public void rafraichirInformationsUtilisateur(User1 utilisateur) {
        nom.setText(utilisateur.getName());
        email.setText(utilisateur.getUsername());
        num.setText(String.valueOf(utilisateur.getNumero()));
        prenom.setText(utilisateur.getPrenom());
        genre.setText(utilisateur.getGenre());
    }



    @FXML
    void home(ActionEvent event) {

    }



}
