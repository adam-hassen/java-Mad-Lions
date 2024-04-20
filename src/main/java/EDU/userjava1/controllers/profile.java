package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class profile implements Initializable {

    @FXML
    private Label address;

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

        nom.setText(Login.v.getName());
        email.setText(Login.v.getUsername());
        num.setText(String.valueOf(Login.v.getNumero()));
        prenom.setText(Login.v.getPrenom());
        genre.setText(Login.v.getGenre());

    }




    }


