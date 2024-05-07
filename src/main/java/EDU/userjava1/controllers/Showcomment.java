package EDU.userjava1.controllers;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.entities.commentaire;
import EDU.userjava1.interfaces.MyListener3;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import EDU.userjava1.interfaces.MyListerner4;

public class Showcomment {

    @FXML
    private Label Username;

    @FXML
    private Label pub;
    private commentaire commentaire;

    private MyListerner4 myListener4;

    public void setData(commentaire commentaire, MyListerner4 myListener4) {
        this.commentaire = commentaire;
        this.myListener4 = myListener4;

        // Set original data
        Username.setText(commentaire.getUserName());
        pub.setText(commentaire.getReponse());

        // Translate message and response
    }
}
