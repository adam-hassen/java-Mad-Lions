package EDU.userjava1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    void pub(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/Converadd.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }

}
