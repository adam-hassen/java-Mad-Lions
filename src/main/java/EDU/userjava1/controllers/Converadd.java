package EDU.userjava1.controllers;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.interfaces.MyListener3;
import EDU.userjava1.services.converServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Converadd implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private TextField publication;

    @FXML
    private Button share;

    private converServices converServices = new converServices();

    private List<Conver> fruits = new ArrayList<>();

    private MyListener3 myListener3;
    converServices gs = new converServices();

    private List<Conver> getData() {
        List<Conver> Conver = new ArrayList<>();
        Conver = gs.getAllConver();
        return Conver;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afficheConver();
    }

    @FXML
    void share(ActionEvent event) {
        int USER_ID = Login.v.getId();
        String PUBLICATION = publication.getText();

        Conver newConver = new Conver(USER_ID, PUBLICATION);
        converServices.ajoutconver(newConver);

        publication.clear();

        // Rafraîchir l'affichage
        refreshGrid();
    }

    private void refreshGrid() {
        grid.getChildren().clear(); // Effacer tous les éléments actuels de la grille
        afficheConver(); // Réafficher tous les éléments
    }

    private void afficheConver() {
        fruits.clear(); // Effacez les éléments existants de la liste fruits
        fruits.addAll(getData());

        if (fruits.size() > 0) {
            int column = 0;
            int row = 1;
            try {
                for (int i = 0; i < fruits.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/afficheConver.fxml"));

                    AnchorPane anchorPane = fxmlLoader.load();
                    AfficheConver controller = fxmlLoader.getController();
                    controller.setData(fruits.get(i), myListener3);

                    // Ajouter chaque élément à la position 0 pour afficher en haut
                    grid.add(anchorPane, column, row); // Ajoute l'anchorPane au grid

                    column++; // Passe à la colonne suivante
                    if (column == 1) { // Si nous avons atteint la dernière colonne, passe à la ligne suivante
                        column = 0;
                        row++;
                    }
                    GridPane.setMargin(anchorPane, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



