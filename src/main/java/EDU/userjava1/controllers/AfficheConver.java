package EDU.userjava1.controllers;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.interfaces.MyListener3;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AfficheConver {

    @FXML
    private Label Username;

    @FXML
    private Label pub;

    private Conver conver;

    private MyListener3 myListener3;

    public void setData(Conver conver, MyListener3 myListener3) {
        this.conver = conver;
        this.myListener3 = myListener3;

        // Set original data
        Username.setText(conver.getUserName());
        pub.setText(conver.getPublication());

        // Translate message and response
    }
}
