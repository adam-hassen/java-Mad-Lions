package org.example.controller;

import org.example.controller.MyConnection;
import org.example.entity.Avis;
import org.example.entity.Evenements;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class EvenementsFrontController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Label lblJoursRestants;

    @FXML
    private Label lblPage;

    @FXML
    private Label lblTotalPages;
    @FXML
    private Label lblPagesRange;

    @FXML
    private ImageView logo;

    @FXML
    private FlowPane flowPane;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> triComboBox;
    @FXML
    private VBox vbox;

    private TranslateTransition daysRemainingAnimation;




    private int currentPage = 0;
    private int eventsPerPage = 4;
    private final DoubleProperty scrollPosition = new SimpleDoubleProperty();


    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Chargez les données dans le FlowPane pour la première page
        showEventsForPage(currentPage);
        updateJoursRestants();

        // Configurez les actions des boutons "Précédent" et "Suivant"
        prevButton.setOnAction(event -> showPreviousPage());
        nextButton.setOnAction(event -> showNextPage());

        // Mettez à jour le texte du label pour afficher le nombre de pages
        updatePagesRange();
        // Initialisation de l'animation des jours restants
        daysRemainingAnimation = new TranslateTransition(Duration.seconds(8), vbox);
        daysRemainingAnimation.setFromX(400);
        daysRemainingAnimation.setToX(-400);
        daysRemainingAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        daysRemainingAnimation.setAutoReverse(true); // Faites l'animation en arrière une fois terminée
        daysRemainingAnimation.play();



        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterEventsByTitle(newValue);
            }
        });
        triComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier la nouvelle valeur sélectionnée et trier en conséquence
            if (newValue != null) {
                switch (newValue) {
                    case "Titre":
                        trierParTitre();
                        break;
                    case "Date":
                        trierParDate();
                        break;
                    case "Lieu":
                        trierParLieu();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void trierParTitre() {
        // Obtenez tous les événements et triez-les par titre
        ObservableList<Evenements> evenementsList = getEvenements();
        evenementsList.sort(Comparator.comparing(Evenements::getTitre));

        // Affichez les événements triés
        showFilteredEvents(evenementsList);
    }

    private void trierParDate() {
        // Obtenez tous les événements et triez-les par date
        ObservableList<Evenements> evenementsList = getEvenements();
        evenementsList.sort(Comparator.comparing(Evenements::getDate));

        // Affichez les événements triés
        showFilteredEvents(evenementsList);
    }

    private void trierParLieu() {
        // Obtenez tous les événements et triez-les par lieu
        ObservableList<Evenements> evenementsList = getEvenements();
        evenementsList.sort(Comparator.comparing(Evenements::getLieu));

        // Affichez les événements triés
        showFilteredEvents(evenementsList);
    }



    private void filterEventsByTitle(String title) {
        // Récupérez tous les événements
        ObservableList<Evenements> allEvents = getEvenements();

        // Créez une liste temporaire pour stocker les événements filtrés
        ObservableList<Evenements> filteredEvents = FXCollections.observableArrayList();

        // Parcourez tous les événements et ajoutez ceux dont le titre correspond à la recherche
        for (Evenements evenement : allEvents) {
            if (evenement.getTitre().toLowerCase().contains(title.toLowerCase())) {
                filteredEvents.add(evenement);
            }
        }

        // Affichez les événements filtrés dans le FlowPane
        showFilteredEvents(filteredEvents);
    }
    private void showFilteredEvents(ObservableList<Evenements> filteredEvents) {
        // Nettoyez le FlowPane avant d'ajouter de nouveaux éléments
        flowPane.getChildren().clear();

        // Pour chaque événement filtré, créez un AnchorPane et ajoutez-le au FlowPane
        for (Evenements evenement : filteredEvents) {
            AnchorPane anchorPane = createEventPane(evenement);
            flowPane.getChildren().add(anchorPane);
            // Ajoutez une marge au FlowPane pour espacer les événements
            FlowPane.setMargin(anchorPane, new Insets(10, 10, 10, 10)); // 10 pixels bottom margin
        }

        // Mettez à jour l'état des boutons de pagination
        updatePaginationButtons();
    }


    private void updatePagesRange() {
        int startPage = currentPage * eventsPerPage + 1;
        int endPage = Math.min((currentPage + 1) * eventsPerPage, getEvenements().size());
        int totalPages = getTotalPages();

        // Afficher la première plage de pages uniquement
        lblPage.setText((currentPage + 1) + " <font color='black'>sur</font> " + getTotalPages());
    }



    private void showEventsForPage(int page) {
        ObservableList<Evenements> list = getEventsForPage(page);

        // Nettoyez le FlowPane avant d'ajouter de nouveaux éléments
        flowPane.getChildren().clear();

        // Pour chaque événement de la page, créez un AnchorPane et ajoutez-le au FlowPane
        for (Evenements evenement : list) {
            AnchorPane anchorPane = createEventPane(evenement);
            flowPane.getChildren().add(anchorPane);
            // Ajoutez une marge au FlowPane pour espacer les événements
            FlowPane.setMargin(anchorPane, new Insets(10, 10, 10, 10)); // 10 pixels bottom margin
        }

        // Mettez à jour l'état des boutons de pagination
        updatePaginationButtons();

        // Mettez à jour les labels de la page et du nombre total de pages
        lblPage.setText(String.valueOf(currentPage + 1)); // Ajoutez 1 car les pages sont indexées à partir de 0
        lblTotalPages.setText(String.valueOf(getTotalPages()));
    }


    private ObservableList<Evenements> getEventsForPage(int page) {
        // Calculez l'index de début et de fin pour les événements de la page spécifiée
        int startIndex = page * eventsPerPage;
        int endIndex = Math.min(startIndex + eventsPerPage, getEvenements().size());

        // Obtenez la sous-liste d'événements pour la page actuelle
        return FXCollections.observableArrayList(getEvenements().subList(startIndex, endIndex));
    }

    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            showEventsForPage(currentPage);
        }
    }

    private void showNextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            showEventsForPage(currentPage);
        }
    }

    private int getTotalPages() {
        // Calculez le nombre total de pages en fonction du nombre total d'événements et du nombre d'événements par page
        return (int) Math.ceil((double) getEvenements().size() / eventsPerPage);
    }

    private void updatePaginationButtons() {
        // Mettez à jour l'état des boutons de pagination en fonction de la page actuelle
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage == getTotalPages() - 1);
    }

    private AnchorPane createEventPane(Evenements evenement) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(180, 100); // Réduisez davantage la taille de la case ici
        anchorPane.setStyle("-fx-background-color: rgba(244, 244, 244, 0.6); " + // Couleur de fond avec opacité
                "-fx-border-color: #ccc; " + // Couleur de la bordure
                "-fx-border-width: 1px; " + // Largeur de la bordure
                "-fx-border-radius: 5px; " + // Rayon de la bordure
                "-fx-padding: 10px;");
        // Espacement interne

        // Créez des labels pour chaque attribut de l'événement
        // Add CSS classes to the labels for styling
        Label titreLabel = new Label("Titre de l'événement: " + evenement.getTitre());
        titreLabel.getStyleClass().add("event-label");
        Label lieuLabel = new Label("Lieu de l'événement: " + evenement.getLieu());
        lieuLabel.getStyleClass().add("event-label");
        Label dateLabel = new Label("Date de l'événement: " + evenement.getDate().toString());
        dateLabel.getStyleClass().add("event-label");
        Label descriptionLabel = new Label("Description de l'événement: " + evenement.getDescription());
        descriptionLabel.getStyleClass().add("event-label");

        // Créez le bouton pour donner un avis
        Button avisButton = new Button("Donner un avis");

// Créez l'icône "star" sous forme de Label
        Label starIcon = new Label("★");

// Changez la couleur de l'icône en jaune
        starIcon.setStyle("-fx-text-fill: gold;");

// Ajoutez l'icône au bouton
        avisButton.setGraphic(starIcon);

// Ajoutez un gestionnaire d'événements au bouton
        avisButton.setOnAction(event -> {
            // Appeler la méthode pour gérer l'événement de donner un avis
            handleAvisButtonClicked(evenement);
        });

// Créez le bouton pour afficher les avis
        Button afficherAvisButton = new Button("Les avis");

// Créez l'icône pour le bouton d'affichage des avis
        Label avisIcon = new Label("\uD83D\uDCAC");

// Changez la couleur de l'icône en bleu (par exemple)
        avisIcon.setStyle("-fx-text-fill: white;");

// Ajoutez l'icône au bouton
        afficherAvisButton.setGraphic(avisIcon);

// Ajoutez un gestionnaire d'événements au bouton
        afficherAvisButton.setOnAction(event -> showAllAvisForEvent(evenement.getId()));


        // Apply styles to the AnchorPane
        anchorPane.getStyleClass().add("event-pane");

        // Apply styles to the pagination buttons
        prevButton.getStyleClass().add("pagination-button");
        nextButton.getStyleClass().add("pagination-button");

        // Add some margin to each AnchorPane to create spacing between events

        anchorPane.getChildren().addAll(titreLabel, lieuLabel, dateLabel, descriptionLabel,avisButton,afficherAvisButton);
        AnchorPane.setTopAnchor(titreLabel, 10.0);
        AnchorPane.setLeftAnchor(titreLabel, 10.0);
        AnchorPane.setTopAnchor(lieuLabel, 30.0); // Ajustez la position verticale
        AnchorPane.setLeftAnchor(lieuLabel, 10.0);
        AnchorPane.setTopAnchor(dateLabel, 50.0); // Ajustez la position verticale
        AnchorPane.setLeftAnchor(dateLabel, 10.0);
        AnchorPane.setTopAnchor(descriptionLabel, 70.0); // Ajustez la position verticale
        AnchorPane.setLeftAnchor(descriptionLabel, 10.0);
        AnchorPane.setTopAnchor(avisButton, 90.0);
        AnchorPane.setLeftAnchor(avisButton, 10.0);
        AnchorPane.setTopAnchor(afficherAvisButton, 90.0);
        AnchorPane.setLeftAnchor(afficherAvisButton, 170.0);

        return anchorPane;
    }




    public ObservableList<Evenements> getEvenements() {
        ObservableList<Evenements> evenementsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM evenements";
        con = MyConnection.getInstance().cnx;

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                Evenements evenement = new Evenements();
                evenement.setId(rs.getInt("id"));
                evenement.setTitre(rs.getString("titre"));
                evenement.setLieu(rs.getString("lieu"));
                evenement.setDate(rs.getDate("date"));
                evenement.setDescription(rs.getString("description"));
                evenementsList.add(evenement);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Assurez-vous de fermer les ressources
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return evenementsList;
    }
    private void updateJoursRestants() {
        // Obtenez la date actuelle
        java.util.Date currentDate = new java.util.Date();

        // Initialisez la date du prochain événement avec une date future arbitrairement grande
        java.util.Date prochainEvenement = new java.util.Date(Long.MAX_VALUE);

        // Parcourez toutes les dates des événements pour trouver la date la plus proche dans le futur
        for (Evenements evenement : getEvenements()) {
            if (evenement.getDate().after(currentDate) && evenement.getDate().before(prochainEvenement)) {
                prochainEvenement = evenement.getDate();
            }
        }

        // Calculez la différence entre la date actuelle et la date du prochain événement pour obtenir le nombre de jours restants
        long diffEnMillisecondes = prochainEvenement.getTime() - currentDate.getTime();
        long joursRestants = diffEnMillisecondes / (1000 * 60 * 60 * 24); // Conversion en jours

        // Mettez à jour le texte du Label
        lblJoursRestants.setText("Il reste " + joursRestants + " jours pour le prochain événement.");
    }



    private void handleAvisButtonClicked(Evenements evenement) {
        // Créer une boîte de dialogue pour saisir le commentaire
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Donner un avis");
        dialog.setHeaderText("Donnez votre avis sur cet événement");

        // Set the button types.
        ButtonType submitButtonType = new ButtonType("Soumettre", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        // Create and configure the comment field.
        TextArea commentaireArea = new TextArea();
        commentaireArea.setPromptText("Commentaire");

        // Create star rating
        FlowPane starsPane = new FlowPane();
        starsPane.setHgap(5); // space between stars
        for (int i = 1; i <= 5; i++) {
            Button starButton = new Button("\u2605"); // Unicode character for star
            int finalI = i;
            starButton.setOnAction(event -> {
                // Set the note to the selected star count
                updateStarRating(starsPane, finalI);
            });
            starsPane.getChildren().add(starButton);
        }

        GridPane grid = new GridPane();
        grid.add(new Label("Votre note:"), 0, 0);
        grid.add(starsPane, 1, 0);
        grid.add(new Label("Commentaire:"), 0, 1);
        grid.add(commentaireArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a commentaire when the submit button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return commentaireArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        // Add logic to add the comment and rating to the database
        result.ifPresent(commentaire -> {
            int note = getStarRating(starsPane);
            addAvis(evenement, note, commentaire);
        });
    }

    // Method to update the star rating UI based on user selection
    private void updateStarRating(FlowPane starsPane, int rating) {
        // Parcourir tous les boutons étoile dans le conteneur
        for (Node node : starsPane.getChildren()) {
            Button starButton = (Button) node;
            // Vérifier si le bouton étoile est avant ou égal à la note sélectionnée
            int starIndex = starsPane.getChildren().indexOf(node) + 1; // Index de l'étoile (à partir de 1)
            if (starIndex <= rating) {
                // Si l'index de l'étoile est inférieur ou égal à la note, le marquer comme sélectionné
                starButton.setStyle("-fx-text-fill: gold;"); // Highlight stars up to the selected rating
            } else {
                // Sinon, désactivez l'étoile
                starButton.setStyle("-fx-text-fill: black;");
            }
        }
    }


    // Method to get the current star rating based on the UI
    private int getStarRating(FlowPane starsPane) {
        int rating = 0;
        for (Node node : starsPane.getChildren()) {
            Button starButton = (Button) node;
            if (starButton.getStyle().contains("gold")) {
                rating++;
            }
        }
        return rating;
    }

    private void addAvis(Evenements evenement, int note, String commentaire) {
        // Créez un nouvel objet Avis avec les informations fournies
        Avis nouvelAvis = new Avis();
        nouvelAvis.setEvent_id(evenement.getId());
        nouvelAvis.setNote(note);
        nouvelAvis.setCommentaire(commentaire);

        // Vérifiez si tous les champs de l'avis sont valides
        if (nouvelAvis.getCommentaire().isEmpty() || nouvelAvis.getNote() <= 0 || nouvelAvis.getNote() > 5) {
            showAlert("Champs invalides", "Veuillez fournir une note valide (entre 1 et 5) et un commentaire.");
            return;
        }

        // Insérez le nouvel avis dans la base de données
        String query = "INSERT INTO avis (commentaire, note, event_id) VALUES (?, ?, ?)";
        try {
            st = con.prepareStatement(query);
            st.setString(1, nouvelAvis.getCommentaire());
            st.setInt(2, nouvelAvis.getNote());
            st.setInt(3, nouvelAvis.getEvent_id());
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                // Affichage d'un message de succès
                showAlert("Succès", "Votre avis a été ajouté avec succès.");

                // Afficher tous les avis pour cet événement dans la même boîte de dialogue
                showAllAvisForEvent(nouvelAvis.getEvent_id());
            } else {
                // Gérer les erreurs d'insertion
                showAlert("Erreur", "Impossible d'ajouter l'avis. Veuillez réessayer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'avis. Veuillez réessayer.");
        }
    }


    private void showAllAvisForEvent(int eventId) {
        // Récupérer tous les avis pour cet événement depuis la base de données
        String query = "SELECT * FROM avis WHERE event_id = ?";
        try {
            st = con.prepareStatement(query);
            st.setInt(1, eventId);
            rs = st.executeQuery();

            StringBuilder avisBuilder = new StringBuilder();
            boolean avisFound = false; // Indicateur pour vérifier si des avis ont été trouvés

            while (rs.next()) {
                int note = rs.getInt("note");
                String commentaire = rs.getString("commentaire");
                avisBuilder.append("Note: ").append(note).append("\nCommentaire: ").append(commentaire).append("\n\n");
                avisFound = true; // Indiquer qu'au moins un avis a été trouvé
            }

            // Si aucun avis n'a été trouvé, afficher un message
            if (!avisFound) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Avis pour cet événement");
                alert.setHeaderText(null);
                alert.setContentText("Aucun avis n'a été enregistré pour cet événement. Soyez le premier à donner votre avis!");
                alert.showAndWait();
            } else {
                // Sinon, afficher tous les avis dans une boîte de dialogue
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Avis pour cet événement");
                alert.setHeaderText(null);
                alert.setContentText(avisBuilder.toString());
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
            showAlert("Erreur", "Une erreur s'est produite lors de la récupération des avis. Veuillez réessayer.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}