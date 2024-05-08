/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import EDU.userjava1.controllers.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author WINDOWS 10
 */
public class cardProductController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Spinner<Integer> prod_spinner;

    @FXML
    private Button prod_addBtn;

    private produit prodData;
    private Image image;

    private String prodID;
    private String categorie;
    private String prod_date;
    private String prod_image;

    private SpinnerValueFactory<Integer> spin;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void setData(produit prodData) {
        this.prodData = prodData;

        prod_image = prodData.getImage();
        categorie = prodData.getCategorie() ;
        prodID = String.valueOf(prodData.getId());
        prod_name.setText(prodData.getNom());
        prod_price.setText("TND " + String.valueOf(prodData.getPrix()));
        String path = "File:" + prodData.getImage();
        image = new Image(path, 190, 94, false, true);
        prod_imageView.setImage(image);
        pr = prodData.getPrix();

    }
    private int qty;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalP;
    private double pr;
    public void addBtn(){

        ProduitController mForm = new ProduitController();
        mForm.customerID();

        qty = prod_spinner.getValue();
        String check = "";
        String checkAvailable = "SELECT description FROM produit WHERE id  = '"
                + prodID + "'";

        connect = database.connectDB();

        try {
            int checkStck = 0;
            String checkStock = "SELECT quantite_stock FROM produit WHERE id  = '"
                    + prodID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("quantite_stock");
            }

            if(checkStck == 0){

                String updateStock = "UPDATE produit SET nom = '"
                        + prod_name.getText() + "', categorie = '"
                        + categorie + "', quantite_stock = 0, prix = " + pr
                        + ", description = 'Unavailable', image = '"
                        + prod_image + "' WHERE id  = '"
                        + prodID + "'";
                prepare = connect.prepareStatement(updateStock);
                prepare.executeUpdate();

            }

            prepare = connect.prepareStatement(checkAvailable);
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("description");
            }

            if (!check.equals("Available") || qty == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :3");
                alert.showAndWait();
            } else {

                if (checkStck < qty) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalide. Ce produit est en rupture de stock");
                    alert.showAndWait();
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");

                    String insertData = "INSERT INTO produit_command "
                            + "(customer_id, prod_id, prod_name, quantity, price, date) "
                            + "VALUES(?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(Login.v.getId()));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, String.valueOf(qty));

                    totalP = (qty * pr);
                    prepare.setString(5, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(6, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    int upStock = checkStck - qty;


                    //System.out.println("Date: " + prod_date);
                    System.out.println(Login.v.getId());
                    System.out.println(prodID);
                    System.out.println(prod_name);
                    System.out.println(qty);

                    System.out.println("Image: " + prod_image);
                    System.out.println(categorie);
                    System.out.println(pr);

                    String updateStock = "UPDATE produit SET nom = '"
                            + prod_name.getText() + "', description = '"
                            + check +"', prix = '" + pr + "', quantite_stock = " + upStock +
                            ", categorie = '"
                            + categorie + "', image = '"
                            + prod_image + "' WHERE id = '"
                            + prodID + "'";

                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Ajouté avec succès!");
                    alert.showAndWait();

                    //mForm.menuGetTotal();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
    }

}
