
package org.example.controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import EDU.userjava1.controllers.Login;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entity.Test;
import org.example.entity.Workshop;
import org.example.interfaces.MyListener2;
import org.example.service.testMethode;

import javafx.scene.control.CheckBox;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;


public class quiz  implements Initializable {

    @FXML
    private VBox chosenFruitCard;
    @FXML
    private CheckBox c1;

    @FXML
    private CheckBox c2;

    @FXML
    private CheckBox c3;

    @FXML
    private CheckBox c4;

    @FXML
    private CheckBox c5;

    @FXML
    private CheckBox c6;

    @FXML
    private CheckBox c7;

    @FXML
    private CheckBox c8;

    @FXML
    private CheckBox c9;

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


private Workshop workshopselectionner;

    public Workshop getWorkshopselectionner() {
        return workshopselectionner;
    }

    public void setWorkshopselectionner(Workshop workshopselectionner) {
        this.workshopselectionner = workshopselectionner;
    }

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
    private List<Test> fruits = new ArrayList<>();
    private Image image;
    private MyListener2 myListener2;
    testMethode gs = new testMethode();
    Test id_test=null;
    private List<Test> getData() {
        List<Test> fruits = new ArrayList<>();
        fruits = gs.listeDestest_workshop(workshopselectionner.getId());
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

    private double score;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    void valider(ActionEvent event) {
        CheckBox[] group1 = {c1, c2, c3};
        CheckBox[] group2 = {c4, c5, c6};
        CheckBox[] group3 = {c7, c8, c9};

        // Compter le nombre de cases cochées dans chaque groupe
        int checkedGroup1 = countChecked(group1);
        int checkedGroup2 = countChecked(group2);
        int checkedGroup3 = countChecked(group3);

        // Vérifier si au moins une case est cochée dans chaque groupe
        if (checkedGroup1 > 0 && checkedGroup2 > 0 && checkedGroup3 > 0) {
            // Vérifier qu'au maximum une case est cochée par groupe
            if (checkedGroup1 <= 1 && checkedGroup2 <= 1 && checkedGroup3 <= 1) {
                // Calculer la note en fonction des cases cochées
                score = calculateNote(checkedGroup1, checkedGroup2, checkedGroup3);
                System.out.println("g1"+checkedGroup1);
                System.out.println("g2"+checkedGroup2);
                System.out.println("g3"+checkedGroup3);
                // Vérifier si le score est supérieur ou égal à 10 pour générer le certificat
                if (score >= 10) { Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès !");
                    alert.setHeaderText(null);
                    alert.setContentText("Votre note est de " + score);
                    alert.showAndWait();

                    generateCertificate(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("failed !");
                    alert.setHeaderText(null);
                    alert.setContentText("Votre note est de " + score);
                    alert.showAndWait();
                    // Afficher un message d'erreur si le score est inférieur à 10
                    displayError("Votre score est inférieur à 10. Vous devez avoir un score de 10 ou plus pour générer le certificat.");
                }
            } else {
                // Plus d'une case est cochée dans au moins un groupe, afficher un message d'erreur
                displayError("Vous ne pouvez choisir qu'une seule réponse par question.");
            }
        } else {
            // Au moins un groupe n'a aucune case cochée, afficher un message d'erreur
            displayError("Vous devez choisir au moins une réponse dans chaque question.");
        }
    }


    // Méthode pour compter le nombre de cases cochées dans un groupe
   @FXML
    private int countChecked(CheckBox[] group) {
        int count = 0;
        for (CheckBox checkBox : group) {
            if (checkBox.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @FXML
    private double calculateNote(int checkedGroup1, int checkedGroup2, int checkedGroup3) {
        double score = 0; // Initialisez la note à 0
        if (c1.isSelected()) {
            score += 6.66; // Incrémentez la note de 6.66 si c1 est cochée
        }
        if (c4.isSelected()) {
            score += 6.66; // Incrémentez la note de 6.66 si c4 est cochée
        }
        if (c7.isSelected()) {
            score += 6.66; // Incrémentez la note de 6.66 si c7 est cochée
        }
        if (c1.isSelected()&& c4.isSelected()&& c7.isSelected())
        {
            score= 20;
        }
        return score;
    }




    // Méthode pour afficher un message d'erreur
    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    public void miseajourtable(){
        ObservableList<Node> children = grid.getChildren();
        grid.getChildren().clear();
        fruits.clear();
        fruits.addAll(getData());
        if (fruits.size() > 0) {
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
    @FXML
    void generateCertificate(ActionEvent event) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        try {
            // Create the PDF document
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("certificateworkshop.pdf"));
            document.open();

            // Set background color
            Rectangle pageSize = document.getPageSize();
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.setColorFill(BaseColor.WHITE); // Blanc
            canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
            canvas.fill();

            // Load and position the logo
            Image logo = Image.getInstance("C:\\Users\\adamh\\IdeaProjects\\GitEcogardienJava\\java-Mad-Lions\\src\\main\\resources\\img\\Sanstitre-1.png");
            float logoWidth = 400;
            float logoHeight = logoWidth * logo.getScaledHeight() / logo.getScaledWidth();
            logo.scaleAbsolute(logoWidth, logoHeight);
            logo.setAbsolutePosition(50,pageSize.getHeight() - logoHeight - 36);
            document.add(logo);

// Add space between logo and title
            document.add(new Paragraph("\n")); // Add an empty paragraph for vertical space
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));// Define styles using Font and Paragraph
            document.add(new Paragraph("\n"));            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 32, BaseColor.BLUE);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 20, BaseColor.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20,BaseColor.GREEN);

            // Add decorative header
            Paragraph header = new Paragraph("Certificat de Participation", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Add spacing between lines
            document.add(new Paragraph("\n"));

            // Add certificate text with CSS styling
            String certificateTextCSS = "font-family: Helvetica; font-size: 20px; color: #333; text-align: justify; line-height: 1.5;";
            Paragraph certificateText = new Paragraph();
            certificateText.setAlignment(Element.ALIGN_JUSTIFIED);
            certificateText.setSpacingAfter(80);
            certificateText.add(new Chunk("Ceci est à certifier que ", normalFont).setGenericTag("name"));document.add(new Paragraph("\n"));
            certificateText.add(new Chunk(Login.v.getName(), boldFont).setGenericTag("name"));
            certificateText.add(new Chunk(" a participé avec succès à l'atelier sur compostage et de niveau Avancé.\n", normalFont));
            certificateText.add(new Chunk("Organisé par : ", normalFont));
            certificateText.add(new Chunk("ecogardien\n", boldFont));
            certificateText.add(new Chunk("Date de l atelier : ", normalFont));
            certificateText.add(new Chunk(formattedDate + "\n\n", boldFont));
            certificateText.add(new Chunk("Ce certificat atteste que "+Login.v.getName()+"  a satisfait aux exigences nécessaires pour obtenir la certification avec succès, démontrant ainsi son engagement et son expertise dans ce domaine.", normalFont));
            document.add(certificateText);

            // Add decorative footer
            Paragraph footer = new Paragraph("Presented on this " + formattedDate, normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            // Add decorative border
            PdfContentByte cb = writer.getDirectContent();
            cb.setColorStroke(BaseColor.BLACK);
            cb.setLineWidth(3);
            cb.rectangle(20, 20, document.getPageSize().getWidth() - 40, document.getPageSize().getHeight() - 40);
            cb.stroke();



            // Close the document
            document.close();

            // Display a success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Certificate generated successfully.", ButtonType.OK);
            alert.showAndWait();

            // Open the generated PDF file
            openPDF("certificateworkshop.pdf");
        } catch (Exception e) {
            // Display an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error generating certificate.", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }




    // Méthode pour ouvrir un fichier PDF
    private void openPDF(String filePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File(filePath);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}