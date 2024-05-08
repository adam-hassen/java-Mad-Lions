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

    @FXML
    private Label prenom;

    private editAdmin editAdminController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editAdmin.fxml"));
        Parent root2 = loader.load();
        editAdminController = loader.getController();
        editAdminController.setProfilAdminController(this); // Pass instance of profile controller
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void rafraichirInformations(User1 admin) {
        nom.setText(admin.getName());
        email.setText(admin.getUsername());
        num.setText(String.valueOf(admin.getNumero()));
        prenom.setText(admin.getPrenom());
        genre.setText(admin.getGenre());
    }
    @FXML
    void home(ActionEvent event) {

    }

}
