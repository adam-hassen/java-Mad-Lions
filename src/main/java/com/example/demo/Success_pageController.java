/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

/*import com.lowagie.text.DocumentException;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;*/
import EDU.userjava1.controllers.Login;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/*import org.xhtmlrenderer.pdf.ITextRenderer;
import sportify.edu.entities.Reservation;
import sportify.edu.entities.Terrain;
import sportify.edu.services.TerrainService;*/

//import java.awt.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * FXML Controller class
 *
 * @author WALID
 */
public class Success_pageController implements Initializable {

    @FXML
    private Label payment_txt;
    @FXML
    private Button pdf_btn;
    @FXML
    private Button back_btn;





    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    private int cID;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
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

    @FXML
    private void redirectToListReservation(ActionEvent event) {
        Node source = (Node) event.getSource();
        // Obtenez la scène à partir de la source
        Scene scene = source.getScene();
        // Obtenez la fenêtre à partir de la scène
        Stage stage = (Stage) scene.getWindow();

        // Fermez la fenêtre
        stage.close();
    }



}
