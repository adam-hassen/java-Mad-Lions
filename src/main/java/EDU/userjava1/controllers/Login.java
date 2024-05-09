package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.RememberMeManager;
import EDU.userjava1.services.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//import javax.mail.MessagingException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;

import javax.mail.MessagingException;

public class Login implements Initializable {
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
    private CheckBox rememberMeCheckbox;

    @FXML
    private AnchorPane side_ankerpane1;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] credentials = RememberMeManager.loadCredentials();
        boolean rememberMe = RememberMeManager.loadRememberMe();
        if (credentials != null && rememberMe) {
            EmailLabel1.setText(credentials[0]);
            PasswordLabel1.setText(credentials[1]);
            rememberMeCheckbox.setSelected(true);
        }
    }
    @FXML
    void LoginButtonOnAction(ActionEvent event) throws IOException {
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
            // GS.sendEmail("ncib.yasmine@esprit.tn","aa","aa") ;
            Parent root2 = FXMLLoader.load(getClass().getResource("/home111.fxml"));
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
        if (rememberMeCheckbox.isSelected()) {
            RememberMeManager.saveCredentials(EmailLabel1.getText(), PasswordLabel1.getText(), true);
        } else {
            RememberMeManager.clearCredentials();
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
    private static final String ACCOUNT_SID = "ACdc24d9b8bf13cf17597cc0e081556562";
    private static final String AUTH_TOKEN = "da573e05e2870dc280d553c3b297bc37";
    private static final String FROM_NUMBER = "+12176991497";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    @FXML
    void openForgetPassword(ActionEvent event) throws MessagingException, IOException {
        UserServices GS = new UserServices();
        String phoneNumber = GS.getPhoneNumberByEmail(EmailLabel1.getText());
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Générer le code aléatoire
            String code = generateRandomCode();

            // Stocker le code aléatoire comme mot de passe temporaire dans la base de données
            GS.updateTemporaryPassword(EmailLabel1.getText(), code);
            Parent root2 = FXMLLoader.load(getClass().getResource("/changerMDP.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = new Stage();
            stage2.setScene(scene2);
            stage2.show();
            // Envoyer le code par SMS avec Twilio
            try {
                Message message = Message.creator(
                                new PhoneNumber(phoneNumber),
                                new PhoneNumber(FROM_NUMBER),
                                "Votre code de réinitialisation de mot de passe : " + code)
                        .create();
                System.out.println("Message SID: " + message.getSid());
                // Ajoutez ici le code pour afficher une notification ou un message de succès à l'utilisateur
            } catch (Exception ex) {
                ex.printStackTrace();
                // Ajoutez ici le code pour afficher une notification ou un message d'erreur à l'utilisateur
            }

        } else {
            // Ajoutez ici le code pour informer l'utilisateur que l'adresse e-mail n'est pas associée à un numéro de téléphone
        }

    }


    public String generateRandomCode() {
        // Générer un code aléatoire de 6 chiffres
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
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