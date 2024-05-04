package EDU.userjava1.controllers;

import EDU.userjava1.controllers.Home;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home222 implements Initializable {

    @FXML
    private GridPane grid1;

    @FXML
    private ScrollPane scroll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            affiche();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button gestConso;
    @FXML
    void GestionConsommation(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Client/Gestion Consommation/HomeGestionAction.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    private void affiche() throws IOException {
        int column = 0;
        int row = 1;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/home111.fxml"));

        AnchorPane anchorPane = fxmlLoader.load();
        home111 controller = fxmlLoader.getController();

        if (column == 3) {
            column = 0;
            row++;
        }

        grid1.add(anchorPane, column++, row); //(child,column,row)
        //set grid width
        grid1.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid1.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid1.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid1.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid1.setMaxHeight(Region.USE_PREF_SIZE);

        GridPane.setMargin(anchorPane, new Insets(10));
    }
}
