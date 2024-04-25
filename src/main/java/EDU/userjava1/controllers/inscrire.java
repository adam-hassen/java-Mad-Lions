package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class inscrire implements Initializable {
    @FXML
    private ComboBox<String> Combobox;
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
@Override
public void initialize (URL url , ResourceBundle resourceBundle){
        Combobox.setItems(FXCollections.observableArrayList("femme","homme"));}

    @FXML
    void inscrire(ActionEvent event)throws IOException {
        if (name.getText().isEmpty() || prenom.getText().isEmpty() ||
                numero.getText().isEmpty()||
                Password.getText().isEmpty()||
                confirme.getText().isEmpty() || username.getText().isEmpty() || Combobox.getValue().isEmpty()  || adress.getText().isEmpty()  ) {
            // Alert user to fill in all fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs!");
            alert.showAndWait();
            return;}
        String NAME = name.getText();
        String PRENOM = prenom.getText();
        String NUMERO = numero.getText(); // Numéro en tant que String

        //int NUMERO = Integer.valueOf(numero.getText());
        String USERNAME = username.getText();
        String ADRESS = adress.getText();
        String PASSWORD= Password.getText();
        String CONFIRME= confirme.getText();
        String GENRE = Combobox.getValue();
        if(Password.getText().toString().equals(confirme.getText().toString())){
            if (!NUMERO.matches("\\d{8}")) {
                // Afficher une alerte si le numéro n'a pas 8 chiffres
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur !");
                alert.setHeaderText(null);
                alert.setContentText("Le numéro doit avoir exactement 8 chiffres  !");
                alert.showAndWait();
                return;
            }
            int numeroInt = Integer.parseInt(NUMERO);


            if (!PASSWORD.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                // Afficher une alerte si le mot de passe ne respecte pas les critères
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur !");
                alert.setHeaderText(null);
                alert.setContentText("Le mot de passe doit contenir au moins 8 caractères et au moins un symbole !");
                alert.showAndWait();
                return;
            }

            if (!USERNAME.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                // Afficher une alerte si le mot de passe ne respecte pas les critères
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur !");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir une adresse e-mail valide !");
                alert.showAndWait();
                return;
            }
            UserServices pcd = new UserServices();

            User1 t = new User1(USERNAME,PASSWORD,NAME,ADRESS,numeroInt,GENRE,PRENOM);
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
