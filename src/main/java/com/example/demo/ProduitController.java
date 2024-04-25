package com.example.demo;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.scene.layout.GridPane;

public class ProduitController implements Initializable{

    @FXML
    private Button dashboard_btn;
    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<produit, String> inventory_col_productCategorie;

    @FXML
    private TableColumn<produit, String> inventory_col_productDescription;

    @FXML
    private TableColumn<produit, String> inventory_col_productID;

    @FXML
    private TableColumn<produit, String> inventory_col_productName;

    @FXML
    private TableColumn<produit, String> inventory_col_productPrice;

    @FXML
    private TableColumn<produit, String> inventory_col_productStock;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private ComboBox<?> inventory_productCategorie;

    @FXML
    private TextField inventory_productDescription;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private TextField inventory_productPrice;

    @FXML
    private TextField inventory_productStock;

    @FXML
    private TableView<produit> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button menu_btn;
    @FXML
    private TextField menu_amount;


    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<?> menu_tableView;

    @FXML
    private Label menu_total;
    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    private ObservableList<produit> cardListData = FXCollections.observableArrayList();

    // LETS MAKE A BEHAVIOR FOR IMPORT BTN FIRST
    public void inventoryImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 182, 161, false, true);

            inventory_imageView.setImage(image);
        }
    }
    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_productCategorie.getSelectionModel().getSelectedItem() == null
                || inventory_productDescription.getText().isEmpty()
                || inventory_productStock.getText().isEmpty()
                || inventory_productPrice.getText().isEmpty()
                || data.path == null) {

            // Show error alert if any required field is empty
            showAlert("Please fill all blank fields", AlertType.ERROR);

        } else {
            try {
                // Parse quantite_stock and prix
                int stock = Integer.parseInt(inventory_productStock.getText());
                double price = Double.parseDouble(inventory_productPrice.getText());

                // Check if quantite_stock and prix are not less than 0
                if (stock < 0 || price < 0) {
                    showAlert("Quantite Stock and Prix must not be less than 0", AlertType.ERROR);
                    return; // Stop further execution
                }

                // Check if quantite_stock and prix contain only digits
                if (!inventory_productStock.getText().matches("\\d+") || !inventory_productPrice.getText().matches("\\d+(\\.\\d+)?")) {
                    showAlert("Quantite Stock and Prix must contain only digits", AlertType.ERROR);
                    return; // Stop further execution
                }

                // CHECK PRODUCT ID
                String checkProdID = "SELECT nom FROM produit WHERE id = '"
                        + inventory_productName.getText() + "'";

                connect = database.connectDB();
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    // Show error alert if product ID is already taken
                    showAlert(inventory_productName.getText() + " is already taken", AlertType.ERROR);
                } else {
                    // Insert new product data into the database
                    String insertData = "INSERT INTO produit "
                            + "(id, nom, description, prix, quantite_stock, categorie, image) "
                            + "VALUES(?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(6, (String) inventory_productCategorie.getSelectionModel().getSelectedItem());
                    prepare.setString(3, inventory_productDescription.getText());
                    prepare.setString(5, inventory_productStock.getText());
                    prepare.setString(4, inventory_productPrice.getText());

                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    prepare.setString(7, path);

                    prepare.executeUpdate();

                    // Show success alert
                    showAlert("Successfully Added!", AlertType.INFORMATION);

                    // Refresh inventory data
                    inventoryShowData();
                }
            } catch (NumberFormatException e) {
                // Show error alert if invalid number format for quantite_stock or prix
                showAlert("Quantite Stock and Prix must be numeric", AlertType.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void inventoryUpdateBtn() {

        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_productCategorie.getSelectionModel().getSelectedItem() == null
                || inventory_productDescription.getText().isEmpty()
                || inventory_productStock.getText().isEmpty()
                || inventory_productPrice.getText().isEmpty()
                || data.path == null || data.id == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE produit SET "
                    + "id = '" + inventory_productID.getText() + "', nom = '"
                    + inventory_productName.getText() + "', description = '"
                    + inventory_productDescription.getText() + "', prix = '"
                    + inventory_productPrice.getText() +"', quantite_stock = '"
                    + inventory_productStock.getText() +"', categorie = '"
                    + inventory_productCategorie.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "' WHERE id = " + data.id;

            connect = database.connectDB();

            try {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE PRoduct ID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void inventoryDeleteBtn() {
        if (data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM produit WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }
    public void inventoryClearBtn() {

        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_productCategorie.getSelectionModel().clearSelection();
        inventory_productDescription.setText("");
        inventory_productStock.setText("");
        inventory_productPrice.setText("");
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);

    }
    // MERGE ALL DATA
    public ObservableList<produit> inventoryDataList() {

        ObservableList<produit> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM produit";


        try {

            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs =  st.executeQuery(sql);

            produit prodData;

            while (rs.next()) {

                prodData = new produit();
                        prodData.setId(rs.getInt("id"));
                        prodData.setNom(rs.getString("nom"));
                        prodData.setDescription(rs.getString("description"));
                        prodData.setPrix(rs.getFloat("prix"));
                        prodData.setQuantité_stock(rs.getInt("quantite_stock"));
                        prodData.setCategorie(rs.getString("categorie"));
                        //result.getString("image");


                listData.add(prodData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    // TO SHOW DATA ON OUR TABLE
    private ObservableList<produit> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        inventory_col_productDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        inventory_col_productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        inventory_col_productStock.setCellValueFactory(new PropertyValueFactory<>("quantité_stock"));
        inventory_col_productCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        inventory_tableView.setItems(inventoryListData);

    }


    public void inventorySelectData() {

        produit prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(String.valueOf(prodData.getId()));
        inventory_productName.setText(prodData.getNom());
        inventory_productDescription.setText(prodData.getDescription());
        inventory_productPrice.setText(String.valueOf(prodData.getPrix()));
        inventory_productStock.setText(String.valueOf(prodData.getQuantité_stock()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        //data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 182, 161, false, true);
        inventory_imageView.setImage(image);
    }
    private String[] typeList = {"Textiles  recyclé", "Papier recyclé", "Plastique recyclé"};
    public void inventoryTypeList() {

        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_productCategorie.setItems(listData);
    }
    public ObservableList<produit> menuGetData() {

        String sql = "SELECT * FROM produit";

        ObservableList<produit> listData = FXCollections.observableArrayList();


        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs =  st.executeQuery(sql);

            produit prod;

            while (rs.next()) {
                prod = new produit();
                prod.setId(rs.getInt("id"));
                prod.setNom(rs.getString("nom"));
                prod.setPrix(rs.getFloat("prix"));
                prod.setImage(rs.getString("image"));
                prod.setCategorie(rs.getString("categorie"));


                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 4) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private int cID;
    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM produit_commande";
        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM command";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            //customers_form.setVisible(false);

            //dashboardDisplayNC();
            //dashboardDisplayTI();
            //dashboardTotalI();
            //dashboardNSP();
            //dashboardIncomeChart();
            //dashboardCustomerChart();

        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            //customers_form.setVisible(false);

            inventoryTypeList();
            //inventoryStatusList();
            inventoryShowData();
        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            //customers_form.setVisible(false);

            menuDisplayCard();
            //menuDisplayTotal();
            //menuShowOrderData();
        } /*else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(true);

            customersShowData();
        }*/

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryTypeList();
        inventoryShowData();
        menuDisplayCard();
    }

}