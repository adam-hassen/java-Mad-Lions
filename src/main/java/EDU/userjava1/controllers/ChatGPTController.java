/*package EDU.userjava1.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;

import java.io.IOException;
public class ChatGPTController {




        private static final String OPENAI_API_KEY = "sk-proj-Q6PYzoqNenuvs9l7N7LQT3BlbkFJqiYRSyDJKvkACDTXLuYd";
    private static final String model = "gpt-3.5-turbo"; // current model of chatgpt api

        @FXML
        private TextField inputField;

        @FXML
        private Button submitButton;

        @FXML
        private TextArea chatArea;

        @FXML
        void handleSubmit(ActionEvent event) {
            String question = inputField.getText();
            if (!question.isEmpty()) {
                try {
                    String response = getChatGPTResponse(question);
                    chatArea.appendText("User: " + question + "\n");
                    chatArea.appendText("ChatGPT: " + response + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputField.clear();
            }
        }

    private static String getChatGPTResponse(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 50,\"model\": \"" + model + "\"}");
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}*/

