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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class home111 implements Initializable {

    @FXML
    private Button email;
    @FXML
    private BorderPane BorderPane;

    @FXML
    void profile(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/account.fxml")));
        BorderPane.setCenter(view);
       // Parent root2 = FXMLLoader.load(getClass().getResource("/account.fxml"));
        //Scene scene2 = new Scene(root2);
        //Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //stage2.setScene(scene2);
        //stage2.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        email.setText(Login.v.getName());

    }
    @FXML
    void consomation(ActionEvent event) throws IOException {
        VBox view= FXMLLoader.load((getClass().getResource("/Client/Gestion Consommation/HomeGestionAction.fxml")));
        BorderPane.setCenter(view);
    }

    @FXML
    void evenement(ActionEvent event) throws IOException {
        VBox  view= FXMLLoader.load((getClass().getResource("/Fxml/EvenementsFront.fxml")));
        BorderPane.setCenter(view);
    }

    @FXML
    void produit(ActionEvent event) throws IOException {
        StackPane view= FXMLLoader.load((getClass().getResource("/com/example/demo/menu.fxml")));
        BorderPane.setCenter(view);

    }


    @FXML
    void recyclage(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/ProduitRecyclable/AfficherProduitRecyclable.fxml")));
        BorderPane.setCenter(view);
    }

    @FXML
    void workshop(ActionEvent event) throws IOException {
        AnchorPane view= FXMLLoader.load((getClass().getResource("/Workshop/Fworkshop.fxml")));
        BorderPane.setCenter(view);
    }
    @FXML
    void pub(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/Converadd.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }
}
