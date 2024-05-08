package com.example.demo;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import com.itextpdf.io.image.ImageDataFactory;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.List;

public class ProduitController implements Initializable{

    @FXML
    private Button dashboard_btn;
    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane commande_form;

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
    private TextField filterField;


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
    private TableView<produit> menu_tableView;

    @FXML
    private Label menu_total;
    @FXML
    private TableView<commande> command_tableview;

    @FXML
    private TableColumn<?, ?> commande_col_commandeID;

    @FXML
    private TableColumn<?, ?> commande_col_customerID;

    @FXML
    private TableColumn<?, ?> commande_col_date;

    @FXML
    private TableColumn<?, ?> commande_col_total;
    @FXML
    private Button command_btn;
    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private StackedBarChart<?, ?> dashboard_incomeChart;

    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;


    public void dashboardDisplayNC() {

        String sql = "SELECT COUNT(id) FROM command";
        connect = database.connectDB();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) FROM command WHERE date = '"
                + sqlDate + "'";

        connect = database.connectDB();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble("SUM(total)");
            }

            dashboard_TI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT SUM(total) FROM command";

        connect = database.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(total)");
            }
            dashboard_TotalI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNSP() {

        String sql = "SELECT COUNT(quantity) FROM produit_command";

        connect = database.connectDB();

        try {
            int q = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt("COUNT(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM command GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
            showAlert("Veuillez remplir tous les champs vides", AlertType.ERROR);

        } else {
            try {
                // Parse quantite_stock and prix
                int stock = Integer.parseInt(inventory_productStock.getText());
                double price = Double.parseDouble(inventory_productPrice.getText());

                // Check if quantite_stock and prix are not less than 0
                if (stock < 0 || price < 0) {
                    showAlert("La quantité de stock et le prix ne doivent pas être inférieurs à 0", AlertType.ERROR);
                    return; // Stop further execution
                }

                // Check if quantite_stock and prix contain only digits
                if (!inventory_productStock.getText().matches("\\d+") || !inventory_productPrice.getText().matches("\\d+(\\.\\d+)?")) {
                    showAlert("Quantite Stock et Prix doivent contenir uniquement des chiffres", AlertType.ERROR);
                    return; // Stop further execution
                }

                // CHECK PRODUCT ID
                String checkProdID = "SELECT nom FROM produit WHERE nom = '"
                        + inventory_productName.getText() + "'";

                connect = database.connectDB();
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    // Show error alert if product ID is already taken
                    showAlert(inventory_productName.getText() + " est déjà pris", AlertType.ERROR);
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
                    showAlert("ajouté avec succès !", AlertType.INFORMATION);

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
            alert.setContentText("Veuillez remplir tous les champs vides");
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
                alert.setContentText("Etes-vous sûr de vouloir METTRE À JOUR LE PRODUITID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Mise à jour réussie !");
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
            alert.setContentText("Veuillez remplir tous les champs vides");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir SUPPRIMER le produit ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM produit WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Supprimé avec succès !");
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
    public ObservableList<produit> menuGetOrder() {
        customerID();
        ObservableList<produit> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM produit_command WHERE customer_id = " + cID;

        try {

            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs =  st.executeQuery(sql);

            produit prod;

            while (rs.next()) {
                prod = new produit();
                prod.setId(rs.getInt("id"));
                prod.setNom(rs.getString("prod_name"));
                prod.setQuantité_stock(rs.getInt("quantity"));
                //prod.setDescription(rs.getString("description"));
                prod.setPrix(rs.getFloat("price"));

                //prod.setCategorie(rs.getString("categorie"));
                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<produit> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantité_stock"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("prix"));

        menu_tableView.setItems(menuOrderListData);
    }
    private int getid;

    public void menuSelectOrder() {
        produit prod = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        // TO GET THE ID PER ORDER
        getid = prod.getId();

    }
    private double totalP;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM produit_command WHERE customer_id = " + cID;

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("TND" + totalP);
    }
    public void menuRemoveBtn() {

        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner la commande que vous souhaitez supprimer");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM produit_command WHERE id = " + getid;
            connect = database.connectDB();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette commande ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }

                menuShowOrderData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void menuRestart() {
        totalP = 0;
        menu_total.setText("TND 0.0");
    }
    private int cID;
    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM produit_command";
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
    public ObservableList<commande> customersDataList() {

        ObservableList<commande> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM command";
        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            commande cData;

            while (result.next()) {
                cData = new commande(result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getDouble("total"),
                        result.getDate("date"));

                listData.add(cData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<commande> commandeListData;

    public void commandeShowData() {
        commandeListData = customersDataList();
        commande_col_commandeID.setCellValueFactory(new  PropertyValueFactory<>("id"));
        commande_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        commande_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        commande_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        command_tableview.setItems(commandeListData);
    }

    @FXML
    private void redirectToPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Scene scene = new Scene(root);
            Stage stage = (Stage) menu_payBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            commande_form.setVisible(false);

            dashboardDisplayNC();
            dashboardDisplayTI();
            dashboardTotalI();
            dashboardNSP();
            dashboardIncomeChart();

        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            commande_form.setVisible(false);

            inventoryTypeList();
            //inventoryStatusList();
            inventoryShowData();
            search_prod();
        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            commande_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        } else if (event.getSource() == command_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            commande_form.setVisible(true);

            commandeShowData();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryTypeList();
        inventoryShowData();
        menuDisplayCard();
        menuShowOrderData();
        menuDisplayTotal();
        commandeShowData();
        dashboardDisplayNC();
        dashboardDisplayTI();
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();
        search_prod();
    }
    void search_prod(){
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        inventory_col_productDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        inventory_col_productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        inventory_col_productStock.setCellValueFactory(new PropertyValueFactory<>("quantité_stock"));
        inventory_col_productCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        inventory_tableView.setItems(inventoryListData);
        FilteredList<produit> filteredData = new FilteredList<>(inventoryListData,b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue)->{
            filteredData.setPredicate(produit -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
               String lowerCaseFilter = newValue.toLowerCase();
                if (produit.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches username
                }
                return false; // Does not match
            });
        });
        SortedList<produit> sorteddata = new SortedList<>(filteredData);
        sorteddata.comparatorProperty().bind(inventory_tableView.comparatorProperty());
        inventory_tableView.setItems(sorteddata);
    }
        public void generatePDF() throws FileNotFoundException, MalformedURLException, SQLException {
            customerID();
            ObservableList<produit> listData = FXCollections.observableArrayList();
            String sql = "SELECT * FROM produit_command WHERE customer_id = " + cID;

            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                produit prod = new produit();
                prod.setId(rs.getInt("id"));
                prod.setNom(rs.getString("prod_name"));
                prod.setQuantité_stock(rs.getInt("quantity"));
                prod.setPrix(rs.getFloat("price"));
                listData.add(prod);
            }

            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter("receipt.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Set document alignment to center both horizontally and vertically
            document.setTextAlignment(TextAlignment.CENTER);
            //document.setVerticalAlignment(VerticalAlignment.MIDDLE);

            // Add logo image (adjust width and height as needed)
            String imagePath = "C:\\Users\\adamh\\IdeaProjects\\GitEcogardienJava\\java-Mad-Lions\\logo.png"; // Specify the path to your image file
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(ImageDataFactory.create(imagePath));

            // Set the width and height of the image (in points)
            float desiredWidth = 500; // Adjust this value as needed
            float desiredHeight = 300; // Adjust this value as needed
            image.setWidth(desiredWidth);
            image.setHeight(desiredHeight);

            // Add the image to the document
            document.add(image);

            // Add title with increased font size
            Paragraph title = new Paragraph("Receipt")
                    .setBold()
                    .setFontSize(30); // Set font size to 30
            document.add(title);

            // Add date with larger font size
            Paragraph date = new Paragraph("Date: " + LocalDate.now())
                    .setFontSize(20); // Set font size to 20
            document.add(date);

            // Add customer ID with larger font size
            Paragraph customerID = new Paragraph("Customer ID: " + cID)
                    .setFontSize(20); // Set font size to 20
            document.add(customerID);

            // Add empty paragraph for spacing
            document.add(new Paragraph(" "));

            // Add table with increased font size
            Table table = new Table(3).setWidth(500);
            table.addCell(new Paragraph("Product Name").setFontSize(20)); // Set font size to 20
            table.addCell(new Paragraph("Quantity").setFontSize(20)); // Set font size to 20
            table.addCell(new Paragraph("Price").setFontSize(20)); // Set font size to 20

            for (produit prodd : listData) {
                table.addCell(new Paragraph(prodd.getNom()).setFontSize(18)); // Set font size to 18
                table.addCell(new Paragraph(String.valueOf(prodd.getQuantité_stock())).setFontSize(18)); // Set font size to 18
                table.addCell(new Paragraph(String.format("%.2f", prodd.getPrix())).setFontSize(18)); // Set font size to 18
            }

            // Add table to document
            document.add(table);

            // Add empty paragraph for spacing
            document.add(new Paragraph(" "));

            // Calculate and add total amount with larger font size
            //float totalP = calculateTotalPrice(listData);
            Paragraph totalAmount = new Paragraph("Your total is: TND " + String.format("%.2f", totalP))
                    .setFontSize(24); // Set font size to 24
            document.add(totalAmount);
            Paragraph Thank = new Paragraph("Thank Youuu <3 (;" )
                    .setFontSize(24); // Set font size to 24
            document.add(Thank);

            // Close document
            document.close();
            try {
                // Récupérez le chemin absolu du PDF généré
                String pdfFilePath = new File("receipt.pdf").getAbsolutePath();
                // Créez un objet File pour le PDF
                File pdfFile = new File(pdfFilePath);
                // Ouvrez le PDF
                Desktop.getDesktop().open(pdfFile);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
}

