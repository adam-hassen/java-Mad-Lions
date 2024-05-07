package EDU.userjava1.controllers;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Pagination;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;

public class Reclamationback implements Initializable {
    @FXML
    private PieChart pieChart;
    @FXML
    private Button envoyer;

    @FXML
    private TextField reponse;

    @FXML
    private TableColumn<Reclamation, Date> DATE;

    @FXML
    private TableColumn<Reclamation, String> EMAIL;

    @FXML
    private TableColumn<Reclamation, String> RECLAMATION;

    @FXML
    private TableColumn<Reclamation, String> REPONSE;

    @FXML
    private TableColumn<Reclamation, String> TYPE;

    @FXML
    private TableView<Reclamation> table;

    @FXML
    private Button exportButton;

    @FXML
    private Pagination pagination;


    @FXML
    private Button statisticsButton; // Bouton pour afficher les statistiques sur le type de réclamation

    private final reclamationService gs = new reclamationService();
    private final ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList();
    private static final int ROWS_PER_PAGE = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Affichage des événements existants lors du chargement de la page
        loadAllReclamations();

        // Pagination
        int pageCount = (int) Math.ceil((double) allReclamations.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            int pageIndex = newIndex.intValue();
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allReclamations.size());
            table.setItems(FXCollections.observableArrayList(allReclamations.subList(fromIndex, toIndex)));
        });

        // Ajout d'un EventHandler pour détecter les sélections d'éléments dans la TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Afficher les détails de la réclamation sélectionnée
                System.out.println("Réclamation sélectionnée : " + newSelection.getId());
            }
        });
    }

    private void loadAllReclamations() {
        allReclamations.addAll(gs.getAllReclamations());
        showReclamation(0);
    }

    public void showReclamation(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allReclamations.size());
        table.setItems(FXCollections.observableArrayList(allReclamations.subList(fromIndex, toIndex)));
        RECLAMATION.setCellValueFactory(new PropertyValueFactory<>("message"));
        EMAIL.setCellValueFactory(new PropertyValueFactory<>("userName"));
        DATE.setCellValueFactory(new PropertyValueFactory<>("date"));
        REPONSE.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        TYPE.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    void envoyer(ActionEvent event) {
        Reclamation selectedReclamation = table.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            String response = reponse.getText();
            gs.repondreReclamation(selectedReclamation.getId(), response);
            selectedReclamation.setReponse(response);
            table.refresh();
            reponse.clear();
        } else {
            System.out.println("Aucune réclamation sélectionnée.");
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/BackTest.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reclamations");
        ObservableList<Reclamation> reclamationsList = table.getItems();
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Reclamation");
        headerRow.createCell(3).setCellValue("Reponse");
        headerRow.createCell(4).setCellValue("Type");
        int rowNum = 1;
        for (Reclamation reclamation : reclamationsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(reclamation.getDate().toString());
            row.createCell(1).setCellValue(reclamation.getUserName());
            row.createCell(2).setCellValue(reclamation.getMessage());
            row.createCell(3).setCellValue(reclamation.getReponse());
            row.createCell(4).setCellValue(reclamation.getType());
        }
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                System.out.println("Excel file exported successfully.");

                // Ouvrir le fichier Excel avec l'application par défaut
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour trier les réclamations par date


    // Méthode pour afficher les statistiques sur le type de réclamation
    @FXML
    void showStatistics(ActionEvent event) {
        // Calcul des statistiques sur le type de réclamation
        Map<String, Integer> statistics = new HashMap<>();
        for (Reclamation reclamation : allReclamations) {
            String type = reclamation.getType();
            statistics.put(type, statistics.getOrDefault(type, 0) + 1);
        }

        // Création des données pour le graphique PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Affichage du graphique PieChart
        pieChart.setData(pieChartData);
    }
}
