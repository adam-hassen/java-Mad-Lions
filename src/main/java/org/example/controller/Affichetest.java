package org.example.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entity.Test;
import org.example.interfaces.MyListener2;
import org.example.service.testMethode;
import org.example.controller.itemController2;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Affichetest  implements Initializable {

        @FXML
        private VBox chosenFruitCard;

        @FXML
        private GridPane grid;

        @FXML
        private Label question_1;

        @FXML
        private Label question_2;

        @FXML
        private Label question_3;

        @FXML
        private Label reponce_8;

        @FXML
        private Label reponce_9;

        @FXML
        private Label reponse_1;

        @FXML
        private Label reponse_2;

        @FXML
        private Label reponse_3;

        @FXML
        private Label reponse_4;

        @FXML
        private Label reponse_5;

        @FXML
        private Label reponse_6;

        @FXML
        private Label reponse_7;



        @FXML
        void profile(ActionEvent event) {

        }


        @FXML
        void modifier(MouseEvent event) throws IOException {
                FXMLLoader root2 = new  FXMLLoader(getClass().getResource("/Workshop/updatetest.fxml"));
                session2.test=id_test;

                Scene scene2 = new Scene(root2.load());
                updatetest controller=root2.getController();
                controller.loader(id_test);
                Stage stage2;
                stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage2.setScene(scene2);
                stage2.show();
        }
        @FXML
        void suprimer(MouseEvent event) {
                if (id_test!=null){
                        testMethode tm=new testMethode();
                        tm.supprimertest(id_test);
                        miseajourtable();
                        id_test=null;
                }
        }


        @FXML
        private java.util.List<Test> fruits = new ArrayList<>();
        private Image image;
        private MyListener2 myListener2;
        testMethode gs = new testMethode();
        Test id_test=null;
        private java.util.List<Test> getData() {
                List<Test> fruits = new ArrayList<>();
                fruits = gs.listeDestest();
                return fruits;
        }

        private void setChosenFruit(Test fruit) {
                this.id_test=fruit;
                question_1.setText(fruit.getQuestion_1().getContenu());
                question_2.setText(fruit.getQuestion_2().getContenu());
                question_3.setText(fruit.getQuestion_3().getContenu());
                reponse_1.setText(fruit.getReponse_1()[0].getContenu());
                reponse_2.setText(fruit.getReponse_1()[1].getContenu());
                reponse_3.setText(fruit.getReponse_1()[2].getContenu());
                reponse_4.setText(fruit.getReponse_2()[0].getContenu());
                reponse_5.setText(fruit.getReponse_2()[1].getContenu());
                reponse_6.setText(fruit.getReponse_2()[2].getContenu());
                reponse_7.setText(fruit.getReponse_3()[0].getContenu());
                reponce_8.setText(fruit.getReponse_3()[1].getContenu());
                reponce_9.setText(fruit.getReponse_3()[2].getContenu());

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
                        System.out.println("fruit size ="+fruits.size());
                        setChosenFruit(fruits.get(0));
                        myListener2 = new MyListener2() {

                                @Override
                                public void onClickListener(Test t) {
                                        setChosenFruit(t);
                                }
                        };
                }
                int column = 0;
                int row = 1;
                try {
                        for (int i = 0; i < fruits.size(); i++) {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/Workshop/items2.fxml"));


                                AnchorPane anchorPane = fxmlLoader.load();

                                itemController2 itemController2 = fxmlLoader.getController();
                                itemController2.setData(fruits.get(i), myListener2);
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
}
