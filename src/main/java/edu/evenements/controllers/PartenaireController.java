package EDU.evenements.controllers;
import EDU.evenements.controllers.MyConnection;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import EDU.evenements.entities.Evenements;
import EDU.evenements.entities.Partenaire;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class PartenaireController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnEffacer1;

    @FXML
    private Button btnEnregistrer1;

    @FXML
    private Button btnModifier1;

    @FXML
    private Button btnSupprimer1;

    @FXML
    private TableColumn<Partenaire, String> colEmail;

    @FXML
    private TableColumn<Partenaire, String> colEvenement;

    @FXML
    private TableColumn<Partenaire, String> colNom;

    @FXML
    private TableColumn<Partenaire, String> colType;

    @FXML
    private TextField tEmail;

    @FXML
    private ChoiceBox<String> tEvenement;

    @FXML
    private TextField tNom;

    @FXML
    private ChoiceBox<String> tType;

    @FXML
    private TableView<Partenaire> table1;

    @FXML
    private ImageView logo;
    @FXML
    private Button btnPageEvenements;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = MyConnection.getInstance().cnx;
        showPartenaire();
        populateTypesChoiceBox();
        populateEvenementsChoiceBox();
        table1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        btnModifier1.setDisable(true);
        btnSupprimer1.setDisable(true);


    }

    private void populateEvenementsChoiceBox() {
        ObservableList<String> evenementsTitlesList = getEvenementsTitlesList();
        tEvenement.setItems(evenementsTitlesList);
    }

    private ObservableList<String> getEvenementsTitlesList() {
        ObservableList<String> evenementsTitlesList = FXCollections.observableArrayList();
        String query = "SELECT titre FROM evenements";
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                String titre = rs.getString("titre");
                evenementsTitlesList.add(titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenementsTitlesList;
    }

    public ObservableList<Partenaire> getPartenaire() {
        ObservableList<Partenaire> partenaires = FXCollections.observableArrayList();
        String query = "SELECT p.*, e.titre AS event_titre FROM partenaire p JOIN evenements e ON p.evenement_id = e.id";
        con = MyConnection.getInstance().cnx;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Partenaire partenaire = new Partenaire();
                partenaire.setId(rs.getInt("id"));
                partenaire.setNom(rs.getString("nom"));
                partenaire.setEmail(rs.getString("email"));
                partenaire.setType(rs.getString("type"));

                Evenements evenement = new Evenements();
                evenement.setTitre(rs.getString("event_titre"));
                // Vous pouvez ajouter d'autres propriétés de l'événement si nécessaire

                partenaire.setEvenement(evenement);
                partenaires.add(partenaire);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return partenaires;
    }

    private void populateTypesChoiceBox() {
        ObservableList<String> typesList = FXCollections.observableArrayList("Organisateur", "Sponsor");
        tType.setItems(typesList);
    }

    public void showPartenaire() {
        ObservableList<Partenaire> list = getPartenaire();
        table1.setItems(list);
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colEvenement.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvenement().getTitre()));
    }


    @FXML
    void clearField1(ActionEvent event) {
        clear();
    }

    @FXML
    void createPartenaire(ActionEvent event) {
        String nom = tNom.getText();
        String email = tEmail.getText();
        String type = tType.getValue();
        String evenementTitre = tEvenement.getValue();

        // Vérifier si tous les champs sont remplis et si l'email est au bon format
        if (nom.isEmpty() || email.isEmpty() || type == null || evenementTitre == null || !isValidEmail(email)) {
            showAlert("Champs obligatoires", "Veuillez remplir tous les champs et fournir une adresse e-mail valide.");
            return;
        }

        // Récupérer l'ID de l'événement en fonction du titre
        int evenementId = getEvenementIdByTitre(evenementTitre);

        // Créer un nouveau partenaire
        Partenaire nouveauPartenaire = new Partenaire();
        nouveauPartenaire.setNom(nom);
        nouveauPartenaire.setEmail(email);
        nouveauPartenaire.setType(type);
        Evenements evenement = new Evenements();
        evenement.setTitre(evenementTitre);
        nouveauPartenaire.setEvenement(evenement);

        // Vérifier si un partenaire avec les mêmes informations existe déjà
        if (existeDeja(nouveauPartenaire)) {
            showAlert("Erreur", "Un partenaire avec ces informations existe déjà.");
            return;
        }

        // Insérer le partenaire dans la base de données
        String query = "INSERT INTO partenaire (nom, email, type, evenement_id) VALUES (?, ?, ?, ?)";
        try {
            st = con.prepareStatement(query);
            st.setString(1, nom);
            st.setString(2, email);
            st.setString(3, type);
            st.setInt(4, evenementId);
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                // Affichage d'un message de succès ou actualisation de l'affichage
                clear(); // Effacer les champs après l'insertion réussie
                showPartenaire(); // Actualiser l'affichage de la liste des partenaires
            } else {
                // Gérer les erreurs d'insertion
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }
    }


    private boolean isValidEmail(String email) {
        // Expression régulière pour vérifier le format de l'email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    // Méthode pour récupérer l'ID de l'événement en fonction de son titre
    private int getEvenementIdByTitre(String titre) {
        int evenementId = -1; // Valeur par défaut si l'événement n'est pas trouvé
        String query = "SELECT id FROM evenements WHERE titre = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, titre);
            rs = st.executeQuery();
            if (rs.next()) {
                evenementId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }
        return evenementId;
    }


    void clear() {
        tNom.setText(null);
        tEmail.setText(null);
        tType.setValue(null);
        tEvenement.setValue(null);
        btnEnregistrer1.setDisable(false);
    }

    @FXML
    void deletePartenaire(ActionEvent event) {
        Partenaire partenaire = table1.getSelectionModel().getSelectedItem();
        if (partenaire != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce partenaire?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Obtenir le titre de l'événement du partenaire à supprimer
                    String evenementTitre = partenaire.getEvenement().getTitre();

                    // Supprimer le partenaire de la base de données en utilisant le titre de l'événement
                    PreparedStatement stmt = con.prepareStatement("DELETE FROM partenaire WHERE nom = ? AND evenement_id = ?");
                    stmt.setString(1, partenaire.getNom());
                    stmt.setInt(2, getEvenementIdByTitreDelete(evenementTitre));
                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows > 0) {
                        // Suppression réussie
                        showAlert("Suppression réussie", "Le partenaire a été supprimé avec succès.");
                        // Actualiser l'affichage de la liste des partenaires
                        showPartenaire();
                    } else {
                        // Aucune ligne n'a été supprimée (peut-être que le partenaire n'existe plus)
                        showAlert("Aucune action effectuée", "Le partenaire n'a pas été trouvé dans la base de données.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Erreur de suppression", "Une erreur s'est produite lors de la suppression du partenaire.");
                }
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un partenaire à supprimer.");
        }
    }

    private int getEvenementIdByTitreDelete(String titre) {
        int evenementId = -1; // Valeur par défaut si l'événement n'est pas trouvé
        String query = "SELECT id FROM evenements WHERE titre = ?";
        try {
            st = con.prepareStatement(query);
            st.setString(1, titre);
            rs = st.executeQuery();
            if (rs.next()) {
                evenementId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        }
        return evenementId;
    }


    @FXML
    void getData1(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Partenaire partenaire = table1.getSelectionModel().getSelectedItem();
            if (partenaire != null) {
                tNom.setText(partenaire.getNom());
                tEmail.setText(partenaire.getEmail());
                tType.setValue(partenaire.getType());

                // Sélectionner la valeur correcte dans la liste déroulante tEvenement
                String evenementTitre = partenaire.getEvenement().getTitre();
                tEvenement.getSelectionModel().select(evenementTitre);

                btnEnregistrer1.setDisable(true);
                btnModifier1.setDisable(false); // Activer le bouton "Modifier"
                btnSupprimer1.setDisable(false); // Activer le bouton "Supprimer"
            } else {
                // Désactiver les boutons "Modifier" et "Supprimer" si aucune ligne n'est sélectionnée
                btnModifier1.setDisable(true);
                btnSupprimer1.setDisable(true);
            }
        }
    }


    @FXML
    public void updatePartenaire(ActionEvent event) {
        Partenaire partenaire = table1.getSelectionModel().getSelectedItem();
        if (partenaire != null) {
            String nom = tNom.getText();
            String email = tEmail.getText();
            String type = tType.getValue();
            String evenementTitre = tEvenement.getValue();

            // Vérifier si tous les champs sont remplis et si l'email est au bon format
            if (nom.isEmpty() || email.isEmpty() || type == null || evenementTitre == null || !isValidEmail(email)) {
                showAlert("Champs obligatoires", "Veuillez remplir tous les champs et fournir une adresse e-mail valide.");
                return;
            }

            // Créer un nouveau partenaire avec les données mises à jour
            Partenaire nouveauPartenaire = new Partenaire();
            nouveauPartenaire.setNom(nom);
            nouveauPartenaire.setEmail(email);
            nouveauPartenaire.setType(type);
            Evenements evenement = new Evenements();
            evenement.setTitre(evenementTitre);
            nouveauPartenaire.setEvenement(evenement);

            // Vérifier si un partenaire avec les mêmes informations existe déjà
            if (existeDeja(nouveauPartenaire)) {
                showAlert("Erreur", "Un partenaire avec ces informations existe déjà.");
                return;
            }

            // Récupérer l'ID de l'événement en fonction du titre
            int evenementId = getEvenementIdByTitre(evenementTitre);

            // Mettre à jour le partenaire dans la base de données
            String query = "UPDATE partenaire SET nom = ?, email = ?, type = ?, evenement_id = ? WHERE id = ?";
            try {
                st = con.prepareStatement(query);
                st.setString(1, nom);
                st.setString(2, email);
                st.setString(3, type);
                st.setInt(4, evenementId);
                st.setInt(5, partenaire.getId());

                int affectedRows = st.executeUpdate();
                if (affectedRows > 0) {
                    // Affichage d'un message de succès ou actualisation de l'affichage
                    clear(); // Effacer les champs après la mise à jour réussie
                    showPartenaire(); // Actualiser l'affichage de la liste des partenaires
                } else {
                    // Gérer les erreurs de mise à jour
                    showAlert("Erreur de mise à jour", "Aucune ligne n'a été affectée. Veuillez vérifier les valeurs mises à jour.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les exceptions SQL
                showAlert("Erreur de mise à jour", "Une erreur SQL s'est produite lors de la mise à jour du partenaire : " + e.getMessage());
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un partenaire à mettre à jour.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean existeDeja(Partenaire nouveauPartenaire) {
        ObservableList<Partenaire> listePartenaires = table1.getItems();
        for (Partenaire partenaire : listePartenaires) {
            if (partenaire.getNom().equals(nouveauPartenaire.getNom()) &&
                    partenaire.getEmail().equals(nouveauPartenaire.getEmail()) &&
                    partenaire.getType().equals(nouveauPartenaire.getType()) &&
                    partenaire.getEvenement().getTitre().equals(nouveauPartenaire.getEvenement().getTitre())) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void goToEvenementsPage(ActionEvent event) {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Ouvrir la nouvelle fenêtre Evenements.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Evenements.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void generatePDF(ActionEvent event) {
        // Crée un nouveau document PDF
        PdfDocument pdfDocument = null;
        try {
            pdfDocument = new PdfDocument(new PdfWriter("partners.pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document document = new Document(pdfDocument);

        // Ajouter l'image
        String imagePath = "C:\\Users\\adamh\\IdeaProjects\\GitEcogardienJava\\java-Mad-Lions\\logo.png";
        try {
            com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(ImageDataFactory.create(imagePath));
            img.setAutoScale(true);
            document.add(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Ajoute un titre au document avec une couleur personnalisée
        Paragraph title = new Paragraph("Liste Des Partenaires")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(30)
                .setFontColor(DeviceRgb.BLACK) // Couleur personnalisée
                .setMarginBottom(30); // Marge en bas pour séparer le titre du tableau
        document.add(title);

        // Crée un tableau pour afficher les partenaires
        Table table = new Table(new float[]{2, 2, 2, 3}); // Définit la largeur des colonnes
        table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Centre le tableau sur la page
        float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth();
        table.setWidth(UnitValue.createPercentValue(100)); // 80% de la largeur de la page
        table.setMarginBottom(20); // Marge en bas pour séparer le tableau du reste du contenu

        // Style de la première ligne du tableau (titre des colonnes)
        Style headerStyle = new Style()
                .setBackgroundColor(new DeviceRgb(35, 155, 86)) // Couleur de fond personnalisée
                .setFontColor(DeviceRgb.WHITE) // Couleur du texte
                .setBold(); // Texte en gras

        // Ajoute les en-têtes de colonnes au tableau
        table.addCell(new Cell().add(new Paragraph("Nom").addStyle(headerStyle)));
        table.addCell(new Cell().add(new Paragraph("Email").addStyle(headerStyle)));
        table.addCell(new Cell().add(new Paragraph("Type").addStyle(headerStyle)));
        table.addCell(new Cell().add(new Paragraph("Evenement").addStyle(headerStyle)));

        // Style des cellules de données
        Style cellStyle = new Style()
                .setPadding(5) // Définit la marge interne des cellules
                .setBorder(Border.NO_BORDER); // Supprime les bordures

        // Ajoute les données des partenaires au tableau
        ObservableList<Partenaire> partenaires = table1.getItems();
        for (Partenaire partenaire : partenaires) {
            table.addCell(new Cell().add(new Paragraph(partenaire.getNom()).addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(partenaire.getEmail()).addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(partenaire.getType()).addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(partenaire.getEvenement().getTitre()).addStyle(cellStyle)));
        }

        document.add(table);

        LocalDateTime currentTime = LocalDateTime.now();

        // Formatez l'heure actuelle en une chaîne de caractères
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        // Ajoutez un paragraphe pour afficher l'heure actuelle
        Paragraph timeParagraph = new Paragraph("Heure de téléchargement du PDF : " + formattedTime)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setFontColor(DeviceRgb.BLACK)
                .setMarginTop(20); // Marge en haut pour séparer le texte du tableau
        document.add(timeParagraph);

        // Ajouter le paragraphe de pied de page
        Paragraph footer = new Paragraph("Cette Gestion est gérée par Amine Mensi")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setFontColor(DeviceRgb.BLACK)
                .setMarginTop(20); // Marge en haut pour séparer le texte du tableau
        document.add(footer);

        // Ferme le document
        document.close();

        // Téléchargez et ouvrez automatiquement le PDF
        try {
            // Récupérez le chemin absolu du PDF généré
            String pdfFilePath = new File("partners.pdf").getAbsolutePath();
            // Créez un objet File pour le PDF
            File pdfFile = new File(pdfFilePath);
            // Ouvrez le PDF
            Desktop.getDesktop().open(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Succès", "PDF généré avec succès : partners.pdf");
    }



}