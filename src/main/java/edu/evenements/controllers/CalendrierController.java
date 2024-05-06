package EDU.evenements.controllers;

import EDU.evenements.controllers.MyConnection;
import EDU.evenements.entities.Evenements;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendrierController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    // Champ pour stocker les événements récupérés de la base de données
    private List<Evenements> eventsFromDatabase = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();

        // Récupérer les événements de la base de données lors de l'initialisation
        eventsFromDatabase = getEventsFromDatabase(dateFocus.getYear(), dateFocus.getMonthValue());
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        // Effacez les données actuelles du calendrier
        calendar.getChildren().clear();

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        // Obtenir les événements pour le mois actuel
        int monthMaxDate = dateFocus.getMonth().maxLength();
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        Map<Integer, List<Evenements>> calendarEventMap = getCalendarEventsMonth(dateFocus, dateOffset, monthMaxDate, today);

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);

                        List<Evenements> calendarEvents = calendarEventMap.get(currentDate);
                        if (calendarEvents != null && !calendarEvents.isEmpty()) {
                            // S'il y a des événements pour cette date, affichez-les tous
                            VBox eventTitles = new VBox();
                            eventTitles.setAlignment(Pos.CENTER);
                            for (Evenements event : calendarEvents) {
                                Text eventTitle = new Text(event.getTitre());
                                eventTitle.setFill(Color.DARKGREEN);
                                eventTitle.setFont(Font.font("System", FontWeight.BOLD, 12));

                                // Ajouter un événement de clic pour afficher les détails de l'événement
                                eventTitle.setOnMouseClicked(mouseEvent -> {
                                    // Créer une boîte de dialogue personnalisée
                                    Dialog<Void> dialog = new Dialog<>();
                                    dialog.setTitle("Détails de l'événement");
                                    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

                                    // Créer une mise en page personnalisée pour afficher les détails de l'événement
                                    GridPane grid = new GridPane();
                                    grid.setHgap(35);
                                    grid.setVgap(25);
                                    grid.setPadding(new Insets(40));

                                    // Ajouter les détails de l'événement à la mise en page personnalisée
                                    grid.add(new Label("Titre:"), 0, 0);
                                    grid.add(new Label(event.getTitre()), 1, 0);
                                    grid.add(new Label("Lieu:"), 0, 1);
                                    grid.add(new Label(event.getLieu()), 1, 1);
                                    grid.add(new Label("Date:"), 0, 2);
                                    grid.add(new Label(event.getDate().toString()), 1, 2);
                                    grid.add(new Label("Description:"), 0, 3);
                                    grid.add(new Label(event.getDescription()), 1, 3);

                                    dialog.getDialogPane().setContent(grid);
                                    dialog.showAndWait();
                                });

                                eventTitles.getChildren().add(eventTitle);
                            }
                            stackPane.getChildren().addAll(date, eventTitles);
                        } else {
                            stackPane.getChildren().add(date);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }






    private void createCalendarEvents(List<Evenements> calendarEvents, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        // Ne créez pas de nouveaux rectangles pour chaque événement, ajoutez simplement le texte du titre à la case correspondante
        for (Evenements event : calendarEvents) {
            // Utilisez le titre de l'événement comme texte
            Text eventTitle = new Text(event.getTitre());
            eventTitle.setFill(Color.RED); // Vous pouvez définir la couleur en fonction de la catégorie d'événement, etc.

            // Définir la position du texte de l'événement
            double eventX = 0; // Vous pouvez ajuster la position X si nécessaire
            double eventY = 0; // Vous pouvez ajuster la position Y si nécessaire
            eventTitle.setTranslateX(eventX);
            eventTitle.setTranslateY(eventY);

            // Ajouter le texte de l'événement au StackPane
            stackPane.getChildren().add(eventTitle);
        }
    }

    private Map<Integer, List<Evenements>> getCalendarEventsMonth(ZonedDateTime dateFocus, int dateOffset, int monthMaxDate, ZonedDateTime today) {
        Map<Integer, List<Evenements>> eventMap = new HashMap<>();
        for (Evenements event : eventsFromDatabase) {
            int eventDay = event.getDate().toLocalDate().getDayOfMonth();
            int eventMonth = event.getDate().toLocalDate().getMonthValue();
            int eventYear = event.getDate().toLocalDate().getYear();

            // Vérifier si l'événement tombe dans le mois en cours
            if (eventYear == dateFocus.getYear() && eventMonth == dateFocus.getMonthValue()) {
                // Vérifier si la date de début de l'événement est dans le mois en cours
                if (eventDay >= dateOffset && eventDay <= monthMaxDate) {
                    // Ajouter l'événement à la liste associée à cette date
                    eventMap.computeIfAbsent(eventDay, k -> new ArrayList<>()).add(event);
                }
            }
        }
        return eventMap;
    }





    private List<Evenements> getEventsFromDatabase(int year, int month) {
        List<Evenements> events = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Établir une connexion à votre base de données (adapter cela à votre propre configuration)
            conn = MyConnection.getInstance().cnx;

            // Requête SQL pour sélectionner les événements pour le mois donné et les mois suivants de l'année en cours
            String sql = "SELECT * FROM evenements WHERE YEAR(date) = ? AND MONTH(date) >= ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            rs = pstmt.executeQuery();

            // Parcourir les résultats et ajouter chaque événement à la liste des événements
            while (rs.next()) {
                Evenements event = new Evenements();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setLieu(rs.getString("lieu"));
                event.setDate(rs.getDate("date"));
                event.setDescription(rs.getString("description"));
                events.add(event);
            }
        } catch (SQLException e) {
            // Gérer les erreurs SQL (journalisation, affichage d'alertes, etc.)
            e.printStackTrace();
        } finally {
            // Fermer les ressources JDBC
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Gérer les erreurs de fermeture des ressources (journalisation, affichage d'alertes, etc.)
                e.printStackTrace();
            }
        }

        return events;
    }










}
