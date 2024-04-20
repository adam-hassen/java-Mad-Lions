package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class inscrire {

    @FXML
    private Label LoginMessageLabel1;

    @FXML
    private PasswordField Password;

    @FXML
    private TextField adress;

    @FXML
    private Button cancelButton1;

    @FXML
    private PasswordField confirme;

    @FXML
    private TextField genre;

    @FXML
    private Button inscrire;

    @FXML
    private Button login111;

    @FXML
    private TextField name;

    @FXML
    private TextField numero;

    @FXML
    private TextField prenom;

    @FXML
    private CheckBox showPasswordCheckbox1;

    @FXML
    private AnchorPane side_ankerpane1;

    @FXML
    private TextField username;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    void inscrire(ActionEvent event)throws IOException {
        if (name.getText().isEmpty() || prenom.getText().isEmpty() ||
                numero.getText().isEmpty()||
                Password.getText().isEmpty()||
                confirme.getText().isEmpty() || username.getText().isEmpty() || genre.getText().isEmpty()  || adress.getText().isEmpty()  ) {
            // Alert user to fill in all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.showAndWait();
            return;}
        String NAME = name.getText();
        String PRENOM = prenom.getText();
        int NUMERO = Integer.valueOf(numero.getText());
        String USERNAME = username.getText();
        String ADRESS = adress.getText();
        String PASSWORD= Password.getText();
        String CONFIRME= confirme.getText();
        String GENRE = genre.getText();
        if(Password.getText().toString().equals(confirme.getText().toString())){

            UserServices pcd = new UserServices();

            User1 t = new User1(USERNAME,PASSWORD,NAME,ADRESS,NUMERO,GENRE,PRENOM);
            if (pcd.test_used_email(t)) {  pcd.ajouteruser(t);}
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("erreur !");
                alert.setHeaderText(null);
                alert.setContentText("used email!");
                alert.showAndWait();
            }

            System.out.println("Done!");
            Parent root2 = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2;
            stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();

        }
        else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("erreur !");
            alert.setHeaderText(null);
            alert.setContentText("Password incorrect!");
            alert.showAndWait();

        }}

    @FXML
    void login(ActionEvent event)throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();

    }

}
