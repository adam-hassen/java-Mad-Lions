package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;
import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class reclamation {

    @FXML
    private TextField reclamation;

    @FXML
    private TextField type;
    @FXML
    private Button back;
    private ProfanityFilter profanityFilter = new ProfanityFilter();

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        int USER_ID = Login.v.getId();
        String RECLAMATION = reclamation.getText();
        String TYPE = type.getText();

        // Check for profanity
        String profanityText = findProfanity(RECLAMATION);
        if (!profanityText.isEmpty()) {
            // Show alert for profanity
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Profanity Detected");
            alert.setHeaderText("Profanity has been detected in the text.");
            alert.setContentText("Please refrain from using offensive language.");
            alert.showAndWait();
            Parent root2 = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
        } else {
            // Proceed with submission if no profanity is detected
            reclamationService pcd = new reclamationService();
            Reclamation t = new Reclamation(USER_ID, RECLAMATION, TYPE);
            pcd.ajoutreclamation(t);

            // Clear text fields after successful submission
            reclamation.clear();
            type.clear();

            // Optionally, show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Reclamation added successfully!");
            successAlert.showAndWait();
            Parent root2 = FXMLLoader.load(getClass().getResource("/account.fxml"));
            Scene scene2 = new Scene(root2);
            Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage2.setScene(scene2);
            stage2.show();
        }
    }

    private String findProfanity(String text) {
        String text1="";
        // Test if the string contains profanity
        boolean containsProfanityEn = profanityFilter.test("en", text);
        boolean containsProfanityFr = profanityFilter.test("fr", text);

        // Check if profanity is found in any language
        if (containsProfanityEn || containsProfanityFr) {
            // Show an alert indicating profanity is found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Profanity Detected");
            alert.setHeaderText("Profanity has been detected in the text.");
            alert.setContentText("Please refrain from using offensive language.");
            alert.showAndWait();
            Profanity profanityen=profanityFilter.find("en",text);
            Profanity profanityfr=profanityFilter.find("fr",text);
            if(containsProfanityEn)
                text1=profanityen.text();
            else {
                text1=profanityfr.text();
            }
        }
        return text1;

    }/*
    private void deleteString(String text,String stringToDelete) {
        // Check if the new text contains the string to delete
        if (text.contains(stringToDelete)) {
            // Delete the string from the text
            Platform.runLater(() -> {
                // Calculate start and end indices of the string to delete
                int startIndex = text.indexOf(stringToDelete);
                int endIndex = startIndex + stringToDelete.length();
                // Update the text area content with the string deleted
                description.replaceText(startIndex, endIndex, "");
                // Move the caret position to the end of the text area
                description.positionCaret(description.getText().length());
            });
        }
    }*/
    @FXML
    void back(ActionEvent event)throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/account.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

}