package EDU.userjava1.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ChatGPTController {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyBvuWtcVuCkSj2HSy4bDBH0JSn-IY3KGGg"; // Replace with the actual API endpoint
    private static final String API_KEY = "AIzaSyBvuWtcVuCkSj2HSy4bDBH0JSn-IY3KGGg"; // Replace with your actual API key

    @FXML
    private TextField inputField;

    @FXML
    private Button submitButton;

    @FXML
    private TextArea chatArea;

    @FXML
    void handleSubmit(ActionEvent event) {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost request = new HttpPost(API_URL);
                request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
                request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

                // Construct JSON payload
                StringEntity jsonEntity = new StringEntity("{\"text\":\"" + userInput + "\"}");
                request.setEntity(jsonEntity);

                // Send the request
                HttpResponse response = httpClient.execute(request);

                // Process the response
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    // Handle the response, update chatArea or perform other actions
                    chatArea.appendText("Response from Gemini: " + responseBody + "\n");
                } else {
                    // Handle error response
                    chatArea.appendText("Error: Failed to get response from Gemini API\n");
                }

                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception
                chatArea.appendText("Error: Failed to communicate with Gemini API\n");
            }
        } else {
            // Alert user to enter text
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter some text");
            alert.showAndWait();
        }
    }
}