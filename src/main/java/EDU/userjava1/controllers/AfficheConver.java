package EDU.userjava1.controllers;
import EDU.userjava1.interfaces.MyListerner4;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.interfaces.MyListener3;
import EDU.userjava1.services.commentaireServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class AfficheConver {

    @FXML
    private Label Username;

    @FXML
    private Button comment;

    @FXML
    private Label pub;

    @FXML
    private Label nbr_comment;
    private Conver conver;
    private MyListerner4 myListener4;

    private commentaireServices commentaireService; // Injecting commentaireService

    private MyListener3 myListener3;

    public void setData(Conver conver, MyListener3 myListener3) {
        this.conver = conver;
        this.myListener3 = myListener3;

        // Set original data
        Username.setText(conver.getUserName());
        pub.setText(conver.getPublication());


    }
    @FXML
    void comment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommentAdd.fxml"));
        Parent root2 = loader.load();
        CommentAdd controller = loader.getController();
        controller.setSelectedConver(conver);
        controller.setMyListener4(myListener4); // Set myListener4 in CommentAdd
        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();
    }
}
