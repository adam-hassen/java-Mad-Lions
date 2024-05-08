package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.interfaces.MyListener1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import java.util.List;

public class AfficheReclamationFront {

    @FXML
    private Label date;

    @FXML
    private Label nameLabel1;

    @FXML
    private Label nameLabel2;

    @FXML
    private Label reclamation1;

    @FXML
    private Label reponse;

    @FXML
    private Label usermail;

    @FXML
    private Label type;

    private Reclamation reclamation;
    private MyListener1 myListener1;

    public void setData(Reclamation reclamation, MyListener1 myListener1) {
        this.reclamation = reclamation;
        this.myListener1 = myListener1;

        // Set original data
        reclamation1.setText(reclamation.getMessage());
        date.setText(reclamation.getDate().toString());
        usermail.setText(reclamation.getUserName());
        reponse.setText(reclamation.getReponse());
        type.setText(reclamation.getType());

        // Translate message and response


    }

    @FXML
    void traduction(ActionEvent event) {
     /*   String originalMessage = reclamation1.getText();

        // Attempt translation from Arabic to English
        Translator translator = new Translator();
        String translatedMessage = translator.translate(originalMessage, "fr", "en");

        // Update the label with the translated message
        reclamation1.setText(translatedMessage);*/
    }
}

