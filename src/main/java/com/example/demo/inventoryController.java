package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class inventoryController implements Initializable {
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
    private AnchorPane main_form;
    @FXML
    private TextField filterField;
    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;
    public void inventoryImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

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
            showAlert("Veuillez remplir tous les champs vides", Alert.AlertType.ERROR);

        } else {
            try {
                // Parse quantite_stock and prix
                int stock = Integer.parseInt(inventory_productStock.getText());
                double price = Double.parseDouble(inventory_productPrice.getText());

                // Check if quantite_stock and prix are not less than 0
                if (stock < 0 || price < 0) {
                    showAlert("La quantité de stock et le prix ne doivent pas être inférieurs à 0", Alert.AlertType.ERROR);
                    return; // Stop further execution
                }

                // Check if quantite_stock and prix contain only digits
                if (!inventory_productStock.getText().matches("\\d+") || !inventory_productPrice.getText().matches("\\d+(\\.\\d+)?")) {
                    showAlert("Quantite Stock et Prix doivent contenir uniquement des chiffres", Alert.AlertType.ERROR);
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
                    showAlert(inventory_productName.getText() + " est déjà pris", Alert.AlertType.ERROR);
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
                    showAlert("ajouté avec succès !", Alert.AlertType.INFORMATION);

                    // Refresh inventory data
                    inventoryShowData();
                }
            } catch (NumberFormatException e) {
                // Show error alert if invalid number format for quantite_stock or prix
                showAlert("Quantite Stock and Prix must be numeric", Alert.AlertType.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
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

            alert = new Alert(Alert.AlertType.ERROR);
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

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Etes-vous sûr de vouloir METTRE À JOUR LE PRODUITID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Mise à jour réussie !");
                    alert.showAndWait();

                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
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

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides");
            alert.showAndWait();

        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir SUPPRIMER le produit ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM produit WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
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
                alert = new Alert(Alert.AlertType.ERROR);
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
    void search_prod(){
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        inventory_col_productDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        inventory_col_productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        inventory_col_productStock.setCellValueFactory(new PropertyValueFactory<>("quantité_stock"));
        inventory_col_productCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        inventory_tableView.setItems(inventoryListData);
        FilteredList<produit> filteredData = new FilteredList<>(inventoryListData, b -> true);
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
    @FXML
    private Button inventory_updateBtn;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryTypeList();
        inventoryShowData();
        search_prod();
    }
    @FXML
    void listeCommande(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/commande.fxml"));
        try {
            Parent root = fxmlLoader.load();
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
