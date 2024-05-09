package org.example.controller;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entity.Workshop;
import org.example.interfaces.MyListener;
import org.example.service.WorkshopMethode;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;


public class AfficherWorkshop  implements Initializable {
        @FXML
        private TextField searchField;
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
        private List<Workshop> fruits = new ArrayList<>();
        @FXML
        void recherche(KeyEvent event) {

        }

        @FXML
        private Pagination pagination;
        @FXML
        private Button suprimer;



        @FXML
        void modifier(MouseEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshop/modifierWorkshop.fxml"));
                try {
                        session.workshop=id_workshop;
                        Parent root = loader.load();
                        Stage stage = new Stage();

                        // Créer une transition de fondu pour la nouvelle scène
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                        fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                        fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                        // Démarrer la transition de fondu
                        fadeTransition.play();

                        // Afficher la nouvelle scène dans une nouvelle fenêtre
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Fermer la fenêtre actuelle après la transition
                        //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        //fadeTransition.setOnFinished(e -> currentStage.close());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
               }
        @FXML
        void suprimer(MouseEvent event) {
if (id_workshop!=null){
        WorkshopMethode wm=new WorkshopMethode();
        wm.supprimerWorkshop(id_workshop);
        miseajourtable(fruits);
        id_workshop=null;
}
        }
        private Image image;
        private MyListener myListener;
        WorkshopMethode gs = new WorkshopMethode();
Workshop id_workshop=null;
        private java.util.List<Workshop> getData() {
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
        private void loadFruits() {
                fruits.addAll(gs.listeDesWorkshop());
        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {
                loadFruits();
                if (fruits.size() > 0) {
                        setChosenFruit(fruits.get(0));
                        myListener = u -> setChosenFruit(u);
                }
                miseajourtable(fruits);
               // searchField.setOnKeyReleased(event -> {
                 //       String searchText = searchField.getText();
                   //     List<Workshop> searchResults = gs.recherche_user(searchText);
                     //   miseajourtable(searchResults);
                //});

        }



        public void miseajourtable(List<Workshop> fruits){
        ObservableList<Node> children = grid.getChildren();

        grid.getChildren().clear();
        this.fruits.clear();
        this.fruits.addAll(getData());

        if (this.fruits.size() > 0) {
                setChosenFruit(this.fruits.get(0));
                myListener = new MyListener() {

                        @Override
                        public void onClickListener(Workshop w) {
                                setChosenFruit(w);

                        }
                };
        }


                int pageCount = (int) Math.ceil((double) this.fruits.size() / 2);
                System.out.println("data"+ this.fruits.size());
                System.out.println("pageCount"+pageCount);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);
                final ListView<Workshop> listView = new ListView<>();

                pagination.setPageFactory(pageIndex -> {
                        grid.getChildren().clear();
                        int column = 0;
                        int row = 1;
                        int fromIndex = pageIndex * 2;
                        int toIndex = Math.min(fromIndex + 2, this.fruits.size());
                        List<Workshop> sublist = this.fruits.subList(fromIndex, toIndex);

                        listView.setItems(FXCollections.observableArrayList(sublist));
                        System.out.println("list"+listView.getItems());

                        for (Workshop item : listView.getItems()) {

                                System.out.println("item"+item);
                                AnchorPane anchorPane= null;
                                try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/Workshop/items.fxml"));


                                 anchorPane = fxmlLoader.load();

                                ItemController itemController = fxmlLoader.getController();
                                itemController.setData(item, myListener);
                                fxmlLoader.getController();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
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
                        return listView;
                });

                System.out.println("list"+listView.getItems());


}
@FXML
        void ajouterTest(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshop/addtest.fxml"));
                try {
                        Parent root = loader.load();
                        Stage stage = new Stage();

                        // Créer une transition de fondu pour la nouvelle scène
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                        fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                        fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                        // Démarrer la transition de fondu
                        fadeTransition.play();

                        // Afficher la nouvelle scène dans une nouvelle fenêtre
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Fermer la fenêtre actuelle après la transition
                        //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        //fadeTransition.setOnFinished(e -> currentStage.close());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

        @FXML
        void ajouterW(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshop/AjouterWorkshop.fxml"));
                try {
                        Parent root = loader.load();
                        Stage stage = new Stage();

                        // Créer une transition de fondu pour la nouvelle scène
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                        fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                        fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                        // Démarrer la transition de fondu
                        fadeTransition.play();

                        // Afficher la nouvelle scène dans une nouvelle fenêtre
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Fermer la fenêtre actuelle après la transition
                        //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        //fadeTransition.setOnFinished(e -> currentStage.close());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }

        }

        @FXML
        void test(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshop/Affichetest.fxml"));
                try {
                        Parent root = loader.load();
                        Stage stage = new Stage();

                        // Créer une transition de fondu pour la nouvelle scène
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                        fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                        fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                        // Démarrer la transition de fondu
                        fadeTransition.play();

                        // Afficher la nouvelle scène dans une nouvelle fenêtre
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Fermer la fenêtre actuelle après la transition
                        //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        //fadeTransition.setOnFinished(e -> currentStage.close());
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }


}



