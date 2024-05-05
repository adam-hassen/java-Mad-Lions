package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;
import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class reclamation {

    @FXML
    private TextField reclamation;

    @FXML
    private TextField type;

    @FXML
    private Button back;

    private ProfanityFilter profanityFilter = new ProfanityFilter();

    private int profanityCount = 0;

    @FXML
    void ajouter(ActionEvent event) throws IOException {
        int USER_ID = Login.v.getId();
        String RECLAMATION = reclamation.getText();
        String TYPE = type.getText();

        // Check for profanity
        String profanityText = findProfanity(RECLAMATION);
        if (!profanityText.isEmpty()) {
            // Increment profanity count
            profanityCount++;

            // Check if the profanity count exceeds the threshold
            if (profanityCount >= 3) {
                // Show black interface for 5 minutes
                showBlackInterfaceForFiveMinutes();
                return;
            }

            // Show alert for profanity
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Profanity Detected");
            alert.setHeaderText("Profanity has been detected in the text.");
            alert.setContentText("Please refrain from using offensive language.");
            alert.showAndWait();
            return;
        } else {
            // Reset profanity count since no profanity detected
            profanityCount = 0;
        }

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

    private String findProfanity(String text) {
        String text1 = "";
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
            Profanity profanityen = profanityFilter.find("en", text);
            Profanity profanityfr = profanityFilter.find("fr", text);
            if (containsProfanityEn)
                text1 = profanityen.text();
            else {
                text1 = profanityfr.text();
            }
        }
        return text1;
    }

    private void showBlackInterfaceForFiveMinutes() {
        // Create a new stage for the black interface
        Stage blackStage = new Stage();
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black");
        Scene scene = new Scene(root, 1500, 800);
        blackStage.setScene(scene);
        blackStage.setTitle("Black Interface");

        // Show the black interface
        blackStage.show();

        // Schedule task to close the black interface after 5 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            Platform.runLater(() -> {
                // Close the black interface
                blackStage.close();
            });
        }, 5, TimeUnit.MINUTES);
    }


    @FXML
    void back(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/account.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }
}
