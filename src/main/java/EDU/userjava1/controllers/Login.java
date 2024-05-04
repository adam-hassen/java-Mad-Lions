package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    public static User1 v = new User1();
    public static String az;
    @FXML
    private TextField EmailLabel1;

    @FXML
    private Button LoginButton1;

    @FXML
    private Label LoginMessageLabel1;

    @FXML
    private PasswordField PasswordLabel1;

    @FXML
    private Button SignupButton1;

    @FXML
    private Button cancelButton1;

    @FXML
    private Hyperlink forget1;

    @FXML
    private CheckBox showPasswordCheckbox1;

    @FXML
    private AnchorPane side_ankerpane1;

    @FXML
    void LoginButtonOnAction(ActionEvent event) throws IOException{
        User1 u = new User1();
        UserServices GS = new UserServices();
        int id = GS.Login(EmailLabel1.getText().toString(), PasswordLabel1.getText().toString());
        int id1 = GS.Login1(EmailLabel1.getText().toString(), PasswordLabel1.getText().toString());

        System.out.println("id: " + id);
        System.out.println("id1: " + id1);

        if (id != -1) {
            System.out.println("First condition");
            v = GS.getbyemail_user(EmailLabel1.getText().toString());

            System.out.println(v);

            Parent root2 = FXMLLoader.load(getClass().getResource("/home222.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
        } else if (id1 != -1) {
            System.out.println("Second condition");
            v = GS.getbyemail_user(EmailLabel1.getText().toString());

            System.out.println(v);


            Parent root2 = FXMLLoader.load(getClass().getResource("/backTest.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
            az = EmailLabel1.getText();
        } else {
            System.out.println("Neither condition");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Verifier vos données ");
            alert.showAndWait();
        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();

    }

    @FXML
    void openForgetPassword(ActionEvent event) throws IOException{

        Parent root1 = FXMLLoader.load(getClass().getResource("/Mdpoublier.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();


    }




    @FXML
    void openSignUpInterface(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/inscrire.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

}
