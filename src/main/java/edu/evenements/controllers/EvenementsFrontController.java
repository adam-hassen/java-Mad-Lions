package EDU.evenements.controllers;

import EDU.evenements.entities.Avis;
import EDU.evenements.entities.Evenements;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EvenementsFrontController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Label lblJoursRestants;

    @FXML
    private Label MeilleurEvenement;

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
    @FXML
    private Label weatherInfoLabel;

    private int currentPage = 0;
    private int eventsPerPage = 4;
    private final DoubleProperty scrollPosition = new SimpleDoubleProperty();

    private static final String API_KEY = "38a8279b5fd13c8a44326fef4f5bec3c";

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Chargez les données dans le FlowPane pour la première page
        showEventsForPage(currentPage);
        updateJoursRestants();
        afficherMeilleurEvenement(); // Appel pour afficher le meilleur événement
        //getWeather("Tunis");
        updateWeatherInfo("Tunis");

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
        // Chargez le fichier CSS pour les alertes
        URL cssUrl = this.getClass().getResource("/css/alerts.css");
        if (cssUrl != null) {
            String css = cssUrl.toExternalForm();
            // Continue with your logic here...
        } else {
            // Handle the case where the resource is not found
            System.err.println("CSS file not found!");
        }


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

        anchorPane.getChildren().addAll(titreLabel, lieuLabel, dateLabel, descriptionLabel, avisButton, afficherAvisButton);
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
        commentaireArea.setPrefWidth(200); // Set preferred width

        // Create star rating
        FlowPane starsPane = new FlowPane();
        starsPane.setHgap(5); // space between stars
        for (int i = 1; i <= 5; i++) {
            Button starButton = new Button("\u2605"); // Unicode character for star
            starButton.getStyleClass().add("star-button"); // Add style class for stars
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

        // Add CSS styles
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/alerts.css").toExternalForm());

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
            int avisCounter = 1; // Compteur pour les avis

            while (rs.next()) {
                int note = rs.getInt("note");
                String commentaire = rs.getString("commentaire");
                avisBuilder.append("Avis ").append(avisCounter).append(" : Note: ").append(note).append("\nCommentaire: ").append(commentaire).append("\n\n");
                avisCounter++; // Incrémentez le compteur d'avis
            }

            // Si aucun avis n'a été trouvé, afficher un message
            if (avisCounter == 1) {
                showAlert("Avis pour cet événement", "Aucun avis n'a été enregistré pour cet événement. Soyez le premier à donner votre avis!");
            } else {
                // Sinon, afficher tous les avis dans une boîte de dialogue avec le style CSS
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Avis pour cet événement");
                alert.setHeaderText(null);
                alert.setContentText(avisBuilder.toString());

                // Appliquer le style CSS à la boîte de dialogue d'alerte
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/css/alerts.css").toExternalForm());

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

        // Appliquer le style CSS à la boîte de dialogue d'alerte
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/alerts.css").toExternalForm());

        alert.showAndWait();
    }

    private void afficherMeilleurEvenement() {
        // Récupérer tous les avis de la base de données
        String query = "SELECT * FROM avis";
        Map<Integer, Double> moyenneNotesParEvenement = new HashMap<>();

        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            // Calculer la somme des notes pour chaque événement et compter le nombre d'avis
            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                int note = rs.getInt("note");

                // Si l'événement n'est pas encore dans la map, l'ajouter avec la première note
                moyenneNotesParEvenement.putIfAbsent(eventId, 0.0);

                // Ajouter la note à la somme existante
                double sum = moyenneNotesParEvenement.get(eventId) + note;
                moyenneNotesParEvenement.put(eventId, sum);
            }

            // Calculer la moyenne des notes pour chaque événement
            for (Map.Entry<Integer, Double> entry : moyenneNotesParEvenement.entrySet()) {
                int eventId = entry.getKey();
                double sum = entry.getValue();
                double moyenne = sum / getNombreAvisPourEvenement(eventId);

                // Mettre à jour la moyenne dans la map
                moyenneNotesParEvenement.put(eventId, moyenne);
            }

            // Trouver l'événement avec la meilleure moyenne
            int meilleurEvenementId = -1;
            double meilleureMoyenne = 0.0;
            for (Map.Entry<Integer, Double> entry : moyenneNotesParEvenement.entrySet()) {
                int eventId = entry.getKey();
                double moyenne = entry.getValue();

                if (moyenne > meilleureMoyenne) {
                    meilleurEvenementId = eventId;
                    meilleureMoyenne = moyenne;
                }
            }

            // Afficher le titre du meilleur événement dans "Meilleur Evenement"
            if (meilleurEvenementId != -1) {
                Evenements meilleurEvenement = getEvenementById(meilleurEvenementId);
                MeilleurEvenement.setText("Meilleur événement : " + meilleurEvenement.getTitre() + " (Moyenne des notes : " + meilleureMoyenne + ")");
            } else {
                MeilleurEvenement.setText("Aucun événement trouvé");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNombreAvisPourEvenement(int eventId) {
        // Récupérer le nombre d'avis pour un événement spécifique
        String query = "SELECT COUNT(*) AS count FROM avis WHERE event_id = ?";
        int nombreAvis = 0;

        try {
            st = con.prepareStatement(query);
            st.setInt(1, eventId);
            rs = st.executeQuery();

            if (rs.next()) {
                nombreAvis = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreAvis;
    }

    private Evenements getEvenementById(int eventId) {
        // Récupérer un événement en fonction de son ID
        String query = "SELECT * FROM evenements WHERE id = ?";
        Evenements evenement = new Evenements();

        try {
            st = con.prepareStatement(query);
            st.setInt(1, eventId);
            rs = st.executeQuery();

            if (rs.next()) {
                evenement.setId(rs.getInt("id"));
                evenement.setTitre(rs.getString("titre"));
                evenement.setLieu(rs.getString("lieu"));
                evenement.setDate(rs.getDate("date"));
                evenement.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return evenement;
    }


    @FXML
    private void updateWeatherInfo(String city) {
        try {
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=fr&appid=" + API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.toString());
            JSONArray weatherArray = (JSONArray) json.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String description = (String) weather.get("description");
            JSONObject main = (JSONObject) json.get("main");
            double temperature = ((Number) main.get("temp")).doubleValue() - 273.15;

            weatherInfoLabel.setText("Météo à " + city + ": " + description + ", Température: " + String.format("%.2f", temperature) + "°C");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTemperatureAlert() {
        String city = "Tunis"; // Remplacez YOUR_CITY_NAME par le nom de la ville souhaitée
        double temperature = getCurrentTemperature(city);
        String description = getWeatherDescription(city);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Conseils en fonction de la température");
        alert.setHeaderText("La température actuelle à " + city + " est " + String.format("%.2f", temperature) + "°C.");
        alert.setContentText("Description météo : " + description + "\n\n" );
        String cssPath = getClass().getResource("/CSS/alerts.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssPath);
        alert.showAndWait();
    }

    private double getCurrentTemperature(String city) {
        try {
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=fr&appid=" + API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.toString());
            JSONObject main = (JSONObject) json.get("main");
            double temperature = ((Number) main.get("temp")).doubleValue() - 273.15;

            return temperature;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private String getWeatherDescription(String city) {
        try {
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&lang=fr&appid=" + API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.toString());
            JSONArray weatherArray = (JSONArray) json.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String description = (String) weather.get("description");
            String detailedDescription = "";

            // Ajout de détails en fonction de la description météo
            switch (description.toLowerCase()) {
                case "ciel dégagé":
                    detailedDescription = "Le ciel est clair sans nuages visibles. Le soleil brille, ce qui rend la journée lumineuse et agréable. Les conditions météorologiques sont idéales pour les activités en extérieur.";
                    break;
                case "peu nuageux":
                    detailedDescription = "Le ciel est légèrement nuageux avec quelques nuages épars. Malgré la présence de nuages, le soleil est toujours visible, offrant une atmosphère agréable et lumineuse. Il est recommandé de profiter de cette journée ensoleillée pour des activités de plein air.";
                    break;
                case "nuageux":
                    detailedDescription = "Le ciel est couvert de nuages, avec peu ou pas d'éclaircies. Bien que le soleil puisse être caché, il n'y a pas de précipitations prévues. Il est conseillé de rester à l'intérieur ou de choisir des activités adaptées à un temps nuageux.";
                    break;
                case "couvert":
                    detailedDescription = "Le ciel est complètement couvert de nuages, créant une atmosphère sombre et nuageuse. Avec peu ou pas de lumière directe du soleil, cette journée peut sembler morne. Il est recommandé de rester à l'intérieur et de profiter d'activités relaxantes.";
                    break;
                case "pluie légère":
                    detailedDescription = "Il y a de légères averses de pluie, ce qui peut rendre les routes et les trottoirs légèrement humides. Même si les précipitations sont faibles, il est préférable de porter un imperméable ou d'utiliser un parapluie lors de vos déplacements.";
                    break;
                case "pluie modérée":
                    detailedDescription = "Il pleut modérément avec des précipitations continues. Les routes peuvent devenir glissantes, il est donc recommandé de conduire prudemment. Pour éviter de se mouiller, il est conseillé de rester à l'intérieur ou de porter un imperméable lors de vos déplacements.";
                    break;
                case "pluie forte":
                    detailedDescription = "Il pleut abondamment avec des précipitations fortes et continues. Les risques d'inondations sont accrus, alors assurez-vous de rester en sécurité à l'intérieur. Si vous devez sortir, portez un imperméable et évitez les zones inondées.";
                    break;
                case "averse de pluie":
                    detailedDescription = "Il y a des averses de pluie intermittentes, alternant entre des périodes de pluie et des éclaircies. Soyez prêt à vous abriter rapidement en cas de pluie. Il est recommandé de porter un imperméable ou de garder un parapluie à portée de main.";
                    break;
                case "neige légère":
                    detailedDescription = "Il y a de légères chutes de neige, couvrant le sol d'une fine couche de neige. Les routes peuvent devenir glissantes, alors conduisez avec prudence. Profitez de cette ambiance hivernale pour des activités telles que la construction de bonhommes de neige ou la randonnée en raquettes.";
                    break;
                case "neige modérée":
                    detailedDescription = "Il neige modérément avec des chutes de neige continues. Les routes peuvent devenir dangereuses et glissantes, alors limitez vos déplacements si possible. Si vous devez sortir, portez des vêtements chauds et des chaussures imperméables.";
                    break;
                case "neige forte":
                    detailedDescription = "Il neige abondamment avec des chutes de neige épaisses et continues. Les conditions de conduite sont extrêmement dangereuses, alors évitez de prendre la route si possible. Restez à l'intérieur et profitez de cette journée enneigée pour des activités à la maison.";
                    break;
                case "orage avec pluie":
                    detailedDescription = "Il y a un orage accompagné de pluie, avec des éclairs et des grondements de tonnerre. Restez à l'abri des zones ouvertes pour éviter les dangers liés à la foudre. Si vous êtes à l'extérieur, cherchez un abri sûr jusqu'à ce que l'orage se calme.";
                    break;
                case "orage avec neige":
                    detailedDescription = "Il y a un orage accompagné de neige, avec des éclairs et des grondements de tonnerre. Les routes peuvent devenir dangereuses et glissantes, alors évitez de conduire si possible. Restez à l'intérieur et attendez que l'orage passe avant de sortir.";
                    break;
                case "orage":
                    detailedDescription = "Il y a un orage sans précipitations, avec des éclairs et des grondements de tonnerre. Même s'il ne pleut pas, la foudre peut être dangereuse, alors évitez les zones découvertes. Restez à l'intérieur et attendez que l'orage se calme avant de sortir.";
                    break;
                default:
                    detailedDescription = "Les conditions météorologiques sont variées.";
            }

            return description + ". " + detailedDescription;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return "";
        }
    }







}



