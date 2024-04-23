package org.example.controller;

import org.example.entity.Evenements;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class EvenementsController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnEffacer;

    @FXML
    private ImageView logo;



    @FXML
    private Button btnEnregistrer;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private DatePicker tDate;

    @FXML
    private TextArea tDescription;

    @FXML
    private TextField tLieu;

    @FXML
    private TextField tTitre;
    @FXML
    private TableColumn<Evenements, Date> colDate;

    @FXML
    private TableColumn<Evenements, String> colDescription;

    @FXML
    private TableColumn<Evenements, String> colLieu;

    @FXML
    private TableColumn<Evenements, String> colTitre;

    @FXML
    private TableView<Evenements> table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Affichage des événements existants lors du chargement de la page
        showEvenements();

        // Ajout d'un EventHandler pour le DatePicker
        tDate.getEditor().addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Vérification de la validité de la date à chaque saisie de caractère
                if (!isValidDateFormat(tDate.getEditor().getText() + event.getCharacter())) {
                    // Si la date n'est pas au bon format, consommez l'événement pour éviter l'entrée invalide
                    event.consume();
                }
            }
        });

    }


    public ObservableList<Evenements> getEvenements() {
        ObservableList<Evenements> Evenements = FXCollections.observableArrayList();
        String query = "select * from evenements";
        con = MyConnection.getInstance().cnx;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Evenements st = new Evenements();
                st.setId(rs.getInt("id"));
                st.setTitre(rs.getString("titre"));
                st.setLieu(rs.getString("lieu"));
                st.setDate(rs.getDate("date"));
                st.setDescription(rs.getString("description"));
                Evenements.add(st);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Evenements;
    }

    public void showEvenements() {
        ObservableList<Evenements> list = getEvenements();
        table.setItems(list);
        colTitre.setCellValueFactory(new PropertyValueFactory<Evenements, String>("titre"));
        colLieu.setCellValueFactory(new PropertyValueFactory<Evenements, String>("lieu"));
        colDate.setCellValueFactory(new PropertyValueFactory<Evenements, Date>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Evenements, String>("description"));

    }

    @FXML
    void clearField(ActionEvent event) {
        clear();

    }

    @FXML
    void createEvenement(ActionEvent event) {
        if (tTitre.getText().isEmpty() || tLieu.getText().isEmpty() || tDate.getValue() == null || tDescription.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires.");
            return;
        }
        LocalDate selectedDate = tDate.getValue();
        if (isDateInPast(selectedDate)) {
            showAlert("Erreur de saisie", "La date sélectionnée est dans le passé.");
            return;
        }
        Evenements nouvelEvenement = new Evenements();
        nouvelEvenement.setTitre(tTitre.getText());
        nouvelEvenement.setLieu(tLieu.getText());
        nouvelEvenement.setDate(java.sql.Date.valueOf(tDate.getValue()));
        nouvelEvenement.setDescription(tDescription.getText());

        // Vérifier si un événement avec les mêmes informations existe déjà dans la liste
        if (existeDeja(nouvelEvenement)) {
            showAlert("Erreur", "Un événement avec ces informations existe déjà.");
            return;
        }

        String requete2 = "INSERT INTO evenements (titre,lieu,date,description) VALUES (?,?,?,?)";
        con = MyConnection.getInstance().cnx;
        try {
            st = con.prepareStatement(requete2);
            st.setString(1, tTitre.getText());
            st.setString(2, tLieu.getText());
            st.setDate(3, java.sql.Date.valueOf(tDate.getValue()));
            st.setString(4, tDescription.getText());
            st.executeUpdate();
            clear();
            showEvenements();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Evenements selectedEvent;

    @FXML
    void getData(MouseEvent event) {
        // Affecter la valeur de l'événement sélectionné à selectedEvent
        selectedEvent = table.getSelectionModel().getSelectedItem();
        tTitre.setText(selectedEvent.getTitre());
        tLieu.setText(selectedEvent.getLieu());
        tDate.setValue(selectedEvent.getDate().toLocalDate());
        tDescription.setText(selectedEvent.getDescription());
        btnEnregistrer.setDisable(true);
    }

    void clear() {
        tTitre.setText(null);
        tLieu.setText(null);
        tDate.setValue(null);
        tDescription.setText(null);
        btnEnregistrer.setDisable(false);


    }

    @FXML
    void deleteEvenement(ActionEvent event) {

        Evenements selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer l'événement avec le titre \"" + selectedEvent.getTitre() + "\" ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a appuyé sur le bouton "OK", donc supprimer l'événement
            deleteSelectedEvent(selectedEvent);
        }
    }

    private void deleteSelectedEvent(Evenements selectedEvent) {
        // Supprimer d'abord tous les partenaires associés à cet événement
        String deletePartenairesQuery = "DELETE FROM partenaire WHERE evenement_id = ?";
        try {
            PreparedStatement deletePartenairesSt = con.prepareStatement(deletePartenairesQuery);
            deletePartenairesSt.setInt(1, selectedEvent.getId());
            deletePartenairesSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
            return; // Sortir de la méthode si une erreur se produit lors de la suppression des partenaires
        }

        String deleteAvisQuery = "DELETE FROM Avis WHERE event_id = ?";
        try {
            PreparedStatement deleteAvisSt = con.prepareStatement(deleteAvisQuery);
            deleteAvisSt.setInt(1, selectedEvent.getId());
            deleteAvisSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
            return; // Sortir de la méthode si une erreur se produit lors de la suppression des partenaires
        }
        // Ensuite, supprimer l'événement lui-même
        String deleteEvenementQuery = "DELETE FROM evenements WHERE id = ?";
        try {
            PreparedStatement deleteEvenementSt = con.prepareStatement(deleteEvenementQuery);
            deleteEvenementSt.setInt(1, selectedEvent.getId());
            deleteEvenementSt.executeUpdate();
            showEvenements();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isDateInPast(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        return date.isBefore(currentDate);
    }



    @FXML
    void updateEvenement(ActionEvent event) {
        Evenements selectedEvent = table.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
            return;
        }


        // Afficher une alerte de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de modification");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment modifier l'événement \"" + selectedEvent.getTitre() + "\" ?");

        // Obtenir la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Vérifier si l'utilisateur a appuyé sur le bouton OK
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Vérifier que tous les champs nécessaires sont remplis
            if (tTitre.getText().isEmpty() || tLieu.getText().isEmpty() || tDate.getValue() == null || tDescription.getText().isEmpty()) {
                showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires.");
                return;
            }
            LocalDate selectedDate = tDate.getValue();
            if (isDateInPast(selectedDate)) {
                showAlert("Erreur de saisie", "La date sélectionnée est dans le passé.");
                return;
            }

            // Créer un nouvel événement avec les données modifiées
            Evenements nouvelEvenement = new Evenements();
            nouvelEvenement.setId(selectedEvent.getId());
            nouvelEvenement.setTitre(tTitre.getText());
            nouvelEvenement.setLieu(tLieu.getText());
            nouvelEvenement.setDate(java.sql.Date.valueOf(tDate.getValue()));
            nouvelEvenement.setDescription(tDescription.getText());

            // Vérifier si un événement avec les mêmes informations existe déjà dans la liste
            if (existeDeja(nouvelEvenement)) {
                showAlert("Erreur", "Un événement avec ces informations existe déjà.");
                return;
            }

            // Mettre à jour l'événement dans la base de données
            executeUpdate(selectedEvent);

            // Mettre à jour l'affichage et effacer les champs
            showEvenements();
            clear();
        }
    }

    // Méthode pour vérifier le format de la date
    private boolean isValidDateFormat(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(regex);
    }







    private void executeUpdate(Evenements selectedEvent) {
        String update = "UPDATE evenements SET titre = ?, lieu = ?, date = ?, description = ? WHERE id = ?";
        con = MyConnection.getInstance().cnx;
        try {
            st = con.prepareStatement(update);
            st.setString(1, tTitre.getText());
            st.setString(2, tLieu.getText());
            st.setDate(3, java.sql.Date.valueOf(tDate.getValue()));
            st.setString(4, tDescription.getText());
            st.setInt(5, selectedEvent.getId());
            st.executeUpdate();
            showEvenements();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean existeDeja(Evenements nouvelEvenement) {
        ObservableList<Evenements> listeEvenements = table.getItems();
        for (Evenements evenement : listeEvenements) {
            if (evenement.getTitre().equals(nouvelEvenement.getTitre()) &&
                    evenement.getLieu().equals(nouvelEvenement.getLieu()) &&
                    evenement.getDate().equals(nouvelEvenement.getDate())&&
                    evenement.getDescription().equals(nouvelEvenement.getDescription())) {
                return true;
            }
        }
        return false;
    }
    @FXML
    void openCalendarPage(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Calendrier.fxml"));
            Parent root = loader.load();

            // Créer la scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Calendrier des événements");

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
