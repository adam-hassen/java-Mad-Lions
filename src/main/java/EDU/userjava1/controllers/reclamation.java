package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import EDU.userjava1.services.reclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class reclamation {

    @FXML
    private TextField reclamation;

    @FXML
    private TextField type;

    @FXML
    void ajouter(ActionEvent event) {
        int USER_ID = Login.v.getId();
        String RECLAMATION = reclamation.getText();
        String TYPE = type.getText();
        reclamationService pcd = new reclamationService();

        Reclamation t = new Reclamation(USER_ID,RECLAMATION,TYPE);
        pcd.ajoutreclamation(t);
    }

}
