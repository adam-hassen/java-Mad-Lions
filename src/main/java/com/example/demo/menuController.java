package com.example.demo;

import EDU.userjava1.controllers.Login;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    private GridPane menu_gridPane;
    @FXML
    private TableColumn<?, ?> menu_col_price;

    @FXML
    private TableColumn<?, ?> menu_col_productName;

    @FXML
    private TableColumn<?, ?> menu_col_quantity;
    @FXML
    private TableView<produit> menu_tableView;
    @FXML
    private Label menu_total;
    @FXML
    private Button menu_payBtn;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Alert alert;
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
    private ObservableList<produit> cardListData = FXCollections.observableArrayList();
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
        ObservableList<produit> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM produit_command WHERE customer_id = " + Login.v.getId();

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
        String total = "SELECT SUM(price) FROM produit_command WHERE customer_id = " + Login.v.getId();

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
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner la commande que vous souhaitez supprimer");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM produit_command WHERE id = " + getid;
            connect = database.connectDB();
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
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
    @FXML
    private void redirectToPayment(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment.fxml"));
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
    public void generatePDF() throws FileNotFoundException, MalformedURLException, SQLException {
        ObservableList<produit> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM produit_command WHERE customer_id = " + Login.v.getId();

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
        Paragraph customerID = new Paragraph("Customer ID: " + Login.v.getId())
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuDisplayCard();
        menuShowOrderData();
        menuDisplayTotal();
    }
}
