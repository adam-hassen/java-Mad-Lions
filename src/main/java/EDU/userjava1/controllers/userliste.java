package EDU.userjava1.controllers;

import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.services.UserServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class userliste implements Initializable {

    @FXML
    private Label adresse;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label email;

    @FXML
    private GridPane grid;

    @FXML
    private Label id;

    @FXML
    private Label jeton;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Button profile;

    @FXML
    private Label role;

    @FXML
    private ScrollPane scroll;


    private List<User1> fruits = new ArrayList<>();
    private Image image;
    private MyListener myListener;
    UserServices gs = new UserServices();

    private List<User1> getData() {
        List<User1> fruits = new ArrayList<>();

        fruits = gs.afficheruser();


        return fruits;
    }

    private void setChosenFruit(User1 fruit) {
        role.setText(fruit.getRoles());
        //image = new Image(getClass().getResourceAsStream(fruit.getImgSrc()));
        //fruitImg.setImage(image);
        nom.setText(fruit.getName());
        prenom.setText(fruit.getPrenom());
        email.setText(fruit.getUsername());
        //  adresse.setText(fruit.getAdress());
        jeton.setText(String.valueOf(fruit.getNumero()));


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fruits.addAll(getData());
        if (fruits.size() > 0) {
            setChosenFruit(fruits.get(0));
            myListener = new MyListener() {


                @Override
                public void onClickListener(User1 u) {
                    setChosenFruit(u);
                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/items.fxml"));


                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(fruits.get(i), myListener);

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
    void profile(ActionEvent event) throws IOException{
        Parent root1 = FXMLLoader.load(getClass().getResource("/ProfilAdmin.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }





}
