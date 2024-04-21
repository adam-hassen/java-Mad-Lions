package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profile implements Initializable {

    @FXML
    private Button delete;
    @FXML
    private Label address;
    @FXML
    private Button edit;
    @FXML
    private Label email;

    @FXML
    private Label genre;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;
    @FXML
    private Label num;

    @FXML
    private Pane pane_1111;

    @FXML
    private Pane pane_11111;

    @FXML
    private Pane pane_111111;

    @FXML
    private Pane pane_13;

    @FXML
    private Pane pane_131;

    @FXML
    private Pane pane_1311;

    @FXML
    private Pane pane_132;

    @FXML
    private HBox root;

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
    void delete(ActionEvent event)throws IOException {

        // Ouvrir la nouvelle fenêtre de confirmation
        Parent root2 = FXMLLoader.load(getClass().getResource("/donnerpassword.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();

    }

    @FXML
    void edit(ActionEvent event)throws IOException  {
        Parent root2 = FXMLLoader.load(getClass().getResource("/edituser.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }
    public void rafraichirAffichage(User1 utilisateurModifie) {
        nom.setText(utilisateurModifie.getName());
        email.setText(utilisateurModifie.getUsername());
        num.setText(String.valueOf(utilisateurModifie.getNumero()));
        prenom.setText(utilisateurModifie.getPrenom());
        genre.setText(utilisateurModifie.getGenre());
    }
    }


