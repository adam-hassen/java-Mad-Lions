package EDU.userjava1.controllers;
import EDU.userjava1.tools.MyConnexion;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.regex.Pattern;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.math.BigDecimal;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import static java.awt.SystemColor.window;

public class Mdpoublier {
    @FXML
    private Label labelcheckpassword;
    @FXML
    private Button CodeButton1;
    @FXML
    private Button resetbutton;
    @FXML
    private Label codecheck;

    @FXML
    private TextField codefield;

    @FXML
    private TextField newpasswordfield;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField numField;
    private String codeFromSMS;

    @FXML
    private Label numcheck;
    @FXML
    void showcode(ActionEvent event) throws SQLException {
        String num = numField.getText();
        if (isPhoneNumberAvailable(num)) {
            String query = "SELECT name, prenom FROM user1 WHERE numero = ?";
            Connection con = MyConnexion.getInstance().getCnx();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String fullName = resultSet.getString("name") + " " + resultSet.getString("prenom");
                AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Error", "This is an error message");
                Random random = new Random();
                int code = random.nextInt(10000);
                codeFromSMS = String.format("%04d", code);
                codefield.setVisible(true);
                codecheck.setVisible(true);
                CodeButton1.setVisible(true);
                numcheck.setVisible(false);
                final String ACCOUNT_SID = "AC1c063236d4f47310e26fde59c7e63451";;
                final String AUTH_TOKEN = "2758a4c25dbc315fc010fc5338794aa4";
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Message message = Message.creator(
                        new PhoneNumber("+15856724358"),  // From number
                        new PhoneNumber("+21656282656"),    // To number
                        "Hi " + fullName + ", this is your code for password reset: " + codeFromSMS
                ).create();
            }
        } else {
            numcheck.setText("Invalid phone number");
            numcheck.setStyle("-fx-text-fill: red");
            numcheck.setVisible(true);
        }
    }

    private boolean isPhoneNumberAvailable(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM user1 WHERE numero = ?";
        PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query);

        preparedStatement.setString(1, phoneNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();

    }
    @FXML
    void setchangepassword(ActionEvent event) {
        String codeEntered = codefield.getText();
        if (codeEntered.equals(codeFromSMS)) {
            codecheck.setText("Code is correct");
            codecheck.setStyle("-fx-text-fill: green");
            codecheck.setVisible(true);
            newpasswordfield.setVisible(true);
            confirmPassword.setVisible(true);
            resetbutton.setVisible(true);
            codecheck.setVisible(false);
        } else {
            codecheck.setText("Code is incorrect");
            codecheck.setStyle("-fx-text-fill: red");
            codecheck.setVisible(true);
        }
    }
    @FXML
    void changepassword(ActionEvent event) throws SQLException {
        String newPassword = newpasswordfield.getText();
        String ConfirmPassword = confirmPassword.getText();
        if (checkPasswordForgot()) {
            if (newPassword.equals(ConfirmPassword)) {

                String query = "UPDATE user1 SET password = ? WHERE tel = ?";
                PreparedStatement preparedStatement = MyConnexion.getInstance().getCnx().prepareStatement(query);
                String hashedPwd = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
                preparedStatement.setString(1, hashedPwd);
                preparedStatement.setString(2, numField.getText());
                preparedStatement.executeUpdate();
                AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Error", "This is an error message");

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, null, "Error", "This is an error message");
            }
        }
    }
    public boolean CheckPasswordConstraint(String test) {
        if (test.length() < 8)
            return false;
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;
        boolean hasDigit = false;
        String specialCharacters = "!@#$%^&*()-_+=<>?";
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            if (Character.isUpperCase(c))
                hasUpperCase = true;
            if (specialCharacters.contains(String.valueOf(c)))
                hasSpecialChar = true;
            if (Character.isDigit(c))
                hasDigit = true;
        }
        return hasUpperCase && hasSpecialChar && hasDigit;
    }

    public boolean checkPasswordForgot()
    {
        boolean verif = true;
        if (newpasswordfield.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            labelcheckpassword.setVisible(true);
            labelcheckpassword.setText("Fields cannot be blank.");
            labelcheckpassword.setStyle("-fx-text-fill: red;");
            verif = false;
        } else if (!CheckPasswordConstraint(newpasswordfield.getText()) || !CheckPasswordConstraint(confirmPassword.getText())) {
            labelcheckpassword.setVisible(true);
            labelcheckpassword.setText("Password must be strong: at least one uppercase letter, one special character, and one digit.");
            labelcheckpassword.setStyle("-fx-text-fill: red;");
            verif = false;
        } else {
            labelcheckpassword.setVisible(false);
        }
        return verif;
    }
    public class AlertHelper {
        public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        }

}}