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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
   /* private Reservation reservation;
    private Terrain terrain;*/
    //The Account Sid and Token at console.twilio.com
    public static final String ACCOUNT_SID = "ACe1969f27c9ebaba39c1c2e19532653e5";
    public static final String AUTH_TOKEN = "53b72d6c70636292563f45e93c8725f3";

    /*public void setData(Reservation r) {
        String value;
        this.reservation = r;
        TerrainService ts = new TerrainService();
        terrain = ts.diplay(this.reservation.getTerrain_id());
        if (terrain != null) {
            value = "This confirms that we've just received your online payment for your Reservation of the Staduim : " + terrain.getName() + ",";
            payment_txt.setText(value);
        }
       // send_sms_to_Client(50190957); // this will be replaced with the client number 
    }*/

   /* public void export_pdf(String html, String fileName) throws IOException, DocumentException {
        // Create a file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        // Create a document
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // Export to PDF
        try (FileOutputStream os = new FileOutputStream(file)) {
            renderer.createPDF(os);
        }
        // Open the PDF file
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        }
    }*/

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       /* this.reservation = new Reservation();
        this.terrain = new Terrain();*/
    }


   /* private void getReceiptPdf(ActionEvent event) {
        float total = (this.reservation.getNbPerson() * this.terrain.getRentPrice());
        DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_res = formatter_date.format(reservation.getDateReservation());
        String start_time = formatter_time.format(reservation.getStartTime());
        String end_time = formatter_time.format(reservation.getEndTime());
        String html = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title>Receipt</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: Arial, sans-serif;\n"
                + "            font-size: 17px;\n"
                + "        }\n"
                + "        .header {\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "        .header h1 {\n"
                + "            margin: 0;\n"
                + "            font-size: 24px;\n"
                + "        }\n"
                + "        .info {\n"
                + "            margin-top: 20px;\n"
                + "            margin-bottom: 20px;\n"
                + "            border: 1px solid #ee1e46;\n"
                + "            padding: 10px;\n"
                + "        }\n"
                + "        .items {\n"
                + "            margin-top: 20px;\n"
                + "            margin-bottom: 20px;\n"
                + "            border-collapse: collapse;\n"
                + "            width: 100%;\n"
                + "            font-size: 16px;\n"
                + "        }\n"
                + "        .items th, .items td {\n"
                + "            border: 1px solid #ccc;\n"
                + "            padding: 10px;\n"
                + "            text-align: left;\n"
                + "        }\n"
                + "        .items th {\n"
                + "            background-color: #eee;\n"
                + "            font-weight: bold;\n"
                + "        }\n"
                + "        .items td {\n"
                + "            vertical-align: middle;\n"
                + "        }\n"
                + "        .items tr:hover {\n"
                + "            background-color: #f5f5f5;\n"
                + "        }\n"
                + "        .total {\n"
                + "            text-align: right;\n"
                + "            font-weight: bold;\n"
                + "            font-size: 18px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<header>\n"
                + " <img src=\"https://i.im.ge/2023/04/23/LxdZBT.icon.png\" alt=\"Company Logo\" width=\"240\" height=\"180\" />\n"
                + "  <h1>Receipt :</h1>\n"
                + "</header>\n"
                + "<body>\n"
                + "    \n"
                + "    <div class=\"info\">\n"
                + "        <p><strong>Client Information:</strong></p>\n"
                + "        <p><strong>Firstname:</strong> " + "client-name-To-Do" + "</p>\n"
                + "        <p><strong>Lastname:</strong> " + "client-lastname-To-Do" + "</p>\n"
                + "        <p><strong>Email:</strong> " + "client-email-To-Do" + "</p>\n"
                + "        <p><strong>Telephone:</strong> " + "client-telephone-To-Do" + "</p>\n"
                + "    </div>\n"
                + "    <table class=\"items\">\n"
                + "        <thead>\n"
                + "            <tr>\n"
                + "                <th>Date</th>\n"
                + "                <th>Start Time</th>\n"
                + "                <th>End Time</th>\n"
                + "                <th>Number of people</th>\n"
                + "                <th>Subtotal</th>\n"
                + "            </tr>\n"
                + "        </thead>\n"
                + "        <tbody>\n"
                + "            \n"
                + "            <tr>\n"
                + "                <td>" + date_res + "</td>\n"
                + "                <td>" + start_time + "</td>\n"
                + "                <td>" + end_time + "</td>\n"
                + "                <td>" + String.valueOf(this.reservation.getNbPerson()) + "</td>\n"
                + "                <td>" + String.valueOf(total) + ".DT</td>\n"
                + "            </tr>\n"
                + "            <tr>\n"
                + "                <td colspan=\"3\" align=\"right\">Total:</td>\n"
                + "                <td class=\"total\">" + String.valueOf(total) + ".DT</td>\n"
                + "            </tr>\n"
                + "        </tbody>\n"
                + "    </table>\n"
                + "    <br></br><br></br><br></br>\n"
                + "     <div>\n"
                + "                            <p>\n"
                + "                    © All rights reserved | This template is made with ♥ by Creative Crew \n"
                + "                            </p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";
        String txt = "Reservation-" + String.valueOf(reservation.getId())+"-"+generateUniqueString()+ ".pdf";
        String fileName = txt;
        try {
            export_pdf(html, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void redirectToListReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Reservation_view_client.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Scene scene = new Scene(root);
            Stage stage = (Stage) back_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

   /* private void send_sms_to_Client(int phone) {

        try {
            //init twilio
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String send_number = "+216" + String.valueOf(phone);
            String from_number = "+15075650863";
            String body = "Sportify: Thank you for making a reservation with us! We look forward to seeing you on : ";
            DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String date_res = formatter_date.format(reservation.getDateReservation());
            String start_time = formatter_time.format(reservation.getStartTime());
            String end_time = formatter_time.format(reservation.getEndTime());
            body += date_res + ", at : " + start_time + " ,ends at : " + end_time;
            Message message = Message
                    .creator(
                            new PhoneNumber(send_number),
                            new PhoneNumber(from_number),
                            body
                    )
                    .create();

        } catch (final ApiException e) {

            System.err.println(e);

        }
    }*/
    private String generateUniqueString() {      
        String unique = UUID.randomUUID().toString(); // Generate a unique String
        return unique;
    }
}
