package EDU.userjava1.controllers;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
    private Reclamation fruit;
    private MyListener1 myListener1;

    public void setData(Reclamation reclamation, MyListener1 myListener1) {
        this.fruit = reclamation; // Utiliser le paramètre reclamation plutôt que this.fruit
        this.myListener1 = myListener1;
        reclamation1.setText(reclamation.getMessage());
        date.setText(reclamation.getDate().toString());
        usermail.setText(reclamation.getUserName());
        reponse.setText(reclamation.getReponse());

    }
}
