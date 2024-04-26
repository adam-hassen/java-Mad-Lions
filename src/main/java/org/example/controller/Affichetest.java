package org.example.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entity.Workshop;
import org.example.interfaces.MyListener;
import org.example.service.WorkshopMethode;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Affichetest implements Initializable {

        @FXML
        private VBox chosenFruitCard;

        @FXML
        private Label cours;

        @FXML
        private Label date;

        @FXML
        private GridPane grid;

        @FXML
        private Label heure;

        @FXML
        private Label nom;

        @FXML
        private Label type;
        @FXML
        private Button modifier;


        @FXML
        private Button suprimer;


        @FXML
        void modifier(MouseEvent event) throws IOException {
                FXMLLoader root2 = new  FXMLLoader(getClass().getResource("/Workshop/Affichetest.fxml"));
                session.workshop=id_workshop;
                Scene scene2 = new Scene(root2.load());
                Stage stage2;
                stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage2.setScene(scene2);
                stage2.show();
               }
        @FXML
        void suprimer(MouseEvent event) {
if (id_workshop!=null){
        WorkshopMethode wm=new WorkshopMethode();
        wm.supprimerWorkshop(id_workshop);
        miseajourtable();
        id_workshop=null;
}
        }


        @FXML
        private List<Workshop> fruits = new ArrayList<>();
        private Image image;
        private MyListener myListener;
        WorkshopMethode gs = new WorkshopMethode();
Workshop id_workshop=null;
        private List<Workshop> getData() {
                List<Workshop> fruits = new ArrayList<>();
                fruits = gs.listeDesWorkshop();
                return fruits;
        }

        private void setChosenFruit(Workshop fruit) {
                nom.setText(fruit.getNom());
                //image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
                //fruitImg.setImage(image);
                type.setText(fruit.getType());
                date.setText(fruit.getDate().toString());
                heure.setText(fruit.getHeure().toString());
                cours.setText(fruit.getCours());
id_workshop= fruit;


        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
               miseajourtable();
        }
public void miseajourtable(){
        ObservableList<Node> children = grid.getChildren();
        grid.getChildren().clear();
        fruits.clear();
        fruits.addAll(getData());
        if (fruits.size() > 0) {
                setChosenFruit(fruits.get(0));
                myListener = new MyListener() {

                        @Override
                        public void onClickListener(Workshop w) {
                                setChosenFruit(w);
                        }
                };
        }
        int column = 0;
        int row = 1;
        try {
                for (int i = 0; i < fruits.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/Workshop/items.fxml"));


                        AnchorPane anchorPane = fxmlLoader.load();

                        ItemController itemController = fxmlLoader.getController();
                        itemController.setData(fruits.get(i), myListener);
                        fxmlLoader.getController();
                        if (column == 3) {
                                column = 0;
                                row++;
                        }

                        grid.add(anchorPane, column++, row); //(child,column,row)
                        //set grid width
                        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        grid.setMaxWidth(Region.USE_PREF_SIZE);

                        //set grid height
                        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        grid.setMaxHeight(Region.USE_PREF_SIZE);

                        GridPane.setMargin(anchorPane, new Insets(10));
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
}
        @FXML
        void profile(ActionEvent event) {

        }





}



