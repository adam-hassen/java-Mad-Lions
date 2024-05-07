package EDU.userjava1.controllers;
import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.interfaces.MyListener3;
import EDU.userjava1.interfaces.MyListerner4;
import EDU.userjava1.services.commentaireServices;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.entities.commentaire;
import EDU.userjava1.services.converServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CommentAdd implements Initializable {
    public void setMyListener4(MyListerner4 myListener4) {
        this.myListener4 = myListener4;
    }

    @FXML
    private TextField comment;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button share;
    private int CONVER_ID;

    private List<commentaire> fruits = new ArrayList<>();
    private MyListerner4 myListener4;
    private commentaireServices gs = new commentaireServices();

    public void setSelectedConver(Conver selectedConver) {
        this.CONVER_ID = selectedConver.getId();
    }

    private List<commentaire> getData() {
        List<commentaire> commentaires = new ArrayList<>();
        if (CONVER_ID != 0) {
            commentaires = gs.getCommentairesByConverId(CONVER_ID);
        }
        return commentaires;
    }
    private void refreshGrid() {
        afficheCommentaire();
    }
    private void afficheCommentaire() {
       // fruits.clear();
        fruits.addAll(getData());

        try {
            int column = 0;
            int row = 1;
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/showcomment.fxml"));

                AnchorPane anchorPane = fxmlLoader.load();
                Showcomment controller = fxmlLoader.getController();
                controller.setData(fruits.get(i), myListener4);

                grid.add(anchorPane, column, row);

                column++;
                if (column == 1) {
                    column = 0;
                    row++;
                }

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void share(ActionEvent event) {
        int USER_ID = Login.v.getId();
        String REPONSE = comment.getText();

        commentaire newcommentaire = new commentaire(USER_ID, REPONSE, CONVER_ID);
        commentaireServices service = new commentaireServices();
        service.ajoutcommentaire(newcommentaire);
        comment.clear();
        refreshGrid();
    }
    @FXML
    void affiche(ActionEvent event) {

        refreshGrid();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            fruits.addAll(getData());

            try {
                int column = 0;
                int row = 1;
                for (int i = 0; i < fruits.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/showcomment.fxml"));

                    AnchorPane anchorPane = fxmlLoader.load();
                    Showcomment controller = fxmlLoader.getController();
                    controller.setData(fruits.get(i), myListener4);

                    grid.add(anchorPane, column, row);

                    column++;
                    if (column == 1) {
                        column = 0;
                        row++;
                    }

                    GridPane.setMargin(anchorPane, new Insets(10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  }

