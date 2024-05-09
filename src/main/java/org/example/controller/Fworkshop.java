package org.example.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entity.Workshop;
import org.example.interfaces.MyListener;
import org.example.service.WorkshopMethode;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import javafx.scene.control.TextField;

public class Fworkshop implements Initializable {

    @FXML
    private VBox chosenFruitCard;
    @FXML
    private TextField recherche;

    @FXML
    private GridPane grid;

    private List<Workshop> fruits = new ArrayList<>();
    private MyListener myListener;
    private WorkshopMethode gs = new WorkshopMethode();
private Workshop workshopselectionner;

    public Workshop getWorkshopselectionner() {
        return workshopselectionner;
    }

    public void setWorkshopselectionner(Workshop workshopselectionner) {
        this.workshopselectionner = workshopselectionner;
    }
    private void loadFruits() {
        fruits.addAll(gs.listeDesWorkshop());
    }
    private List<Workshop> getData() {
        return gs.listeDesWorkshop();
    }

    private void setChosenFruit(Workshop fruit)
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Workshop/addtest.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFruits();
        if (fruits.size() > 0) {
            setChosenFruit(fruits.get(0));
            myListener = u -> setChosenFruit(u);
        }
        miseajourtable(fruits);
      //  recherche.setOnKeyReleased(event -> {
        //    String searchText = recherche.getText();
          //  List<Workshop> searchResults = gs.recherche_user(searchText);
            //miseajourtable(searchResults);
        //});
    }

    public void miseajourtable(List<Workshop> fruits){
        ObservableList<Node> children = grid.getChildren();
        grid.getChildren().clear();
        fruits.clear();
        fruits.addAll(getData());
        if (fruits.size() > 0) {
            setChosenFruit(fruits.get(0));
            myListener = new MyListener() {

                @Override
                public void onClickListener(Workshop w) {
                    setChosenFruit(w);
                   workshopselectionner= w;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Workshop/quiz.fxml"));
                        Parent root = loader.load();
                        quiz controllor=loader.getController();
                        controllor.setWorkshopselectionner(w);
                        controllor.miseajourtable();
                        Stage stage = new Stage();  // Create a new stage for the new scene
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            };

        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < fruits.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Workshop/items.fxml"));


                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(fruits.get(i), myListener);
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
    void profile(ActionEvent event) {
        // Handle profile button click event
    }

    @FXML
    void generateCertificate(ActionEvent event) {
        try {
            // Créer le document PDF et ajouter le contenu
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("certificateworkshop.pdf"));
            document.open();
            // Load the image
            Image logo = Image.getInstance("src/main/resources/img/Sanstitre-1.png");

            // Scale the image to fit a specific width and height
            float maxWidth = 100; // Set maximum width in points
            float maxHeight = 100; // Set maximum height in points
            logo.scaleToFit(maxWidth, maxHeight);

            // Set absolute position for the image (top left corner)
            logo.setAbsolutePosition(36, PageSize.A4.getHeight() - logo.getScaledHeight() - 36);

            // Add the image to the document
            document.add(logo);
            // Ajouter le contenu du certificat
            Paragraph title = new Paragraph("Certificat de Participation");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.GREEN);

            Paragraph certificateText = new Paragraph();
            certificateText.setAlignment(Element.ALIGN_CENTER);
            certificateText.setFont(normalFont);
            certificateText.add("Ceci est à certifier que\n");
            certificateText.setFont(boldFont);
            certificateText.add("John Doe\n");
            certificateText.setFont(normalFont);
            certificateText.add("a participé avec succès à l'atelier sur JavaFX.\n");
            certificateText.add("Organisé par : ecogardien\n");
            certificateText.add("Date de l'atelier : 2024-05-03\n\n");
            certificateText.setFont(boldFont);
            certificateText.add("Ce certificat atteste que John Doe a satisfait aux exigences nécessaires pour obtenir la certification avec succès, démontrant ainsi son engagement et son expertise dans ce domaine.");

            document.add(certificateText);

            document.close();

            // Afficher une alerte pour informer l'utilisateur que le certificat a été généré
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Certificat généré avec succès.", ButtonType.OK);
            alert.showAndWait();

            // Ouvrir le fichier PDF généré automatiquement
            openPDF("certificate.pdf");
        } catch (Exception e) {
            // Afficher une alerte en cas d'erreur lors de la génération du certificat
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du certificat.", ButtonType.OK);
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
