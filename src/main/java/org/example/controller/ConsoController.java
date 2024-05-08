package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;
import org.example.service.ActionService;

public class ConsoController {
    @FXML
    public Text LastActionDate;
    @FXML
    public Text Nom;
    @FXML
    public Text Email;
    @FXML
    public Text Address;
    @FXML
    public Text Danger;
    @FXML
    public WebView webview;
    @FXML
    private Button button;
    @FXML
    private Button button1;
    @FXML
    private TextField textField;
    @FXML
    private VBox vboxside;
    private ActionService query;
    @FXML
    public void initialize() throws UnsupportedEncodingException {
        query = new ActionService();
        button.setOnAction(this::naviguerVersGestion);
        button1.setOnAction(this::naviguerVersSuivre);
        System.out.println("djo" + Login.v.getId());
        LastActionDate.setText(String.valueOf(query.lastAction(Login.v.getId()).getDate()));
        Email.setText(Login.v.getUsername());
        Nom.setText(Login.v.getName());
        Address.setText(Login.v.getAdress());
        double dang=query.moyenneDanger(Login.v.getId());
        if (dang< 2){
            Danger.setText("Vous ne presentez pas de danger");
        }
        else if ((dang<=2) && (dang<= 4)){
            Danger.setText("Niveau de danger Moyen");
        }
        else if (dang>4){
            Danger.setText("Vous presentez un danger!");
        }
        //Search
        String queryy ="Comment limiter l'utilisation du";
        String dangerList = query.ListeDanger(Login.v);
        if (dangerList != null && dangerList.contains("plastique")){
            queryy += " plastique ";
        }
        if (dangerList != null && dangerList.contains("carburant")){
            queryy += " ,carburant ";
        }
        if (dangerList != null && dangerList.contains("gaz")){
            queryy += " ,gaz ";
        }
        if (dangerList != null && dangerList.contains("electrique")){
            queryy += " ,electrique ";
        }
        else{
            queryy="null";
        }
        WebEngine webEngine = webview.getEngine();
        System.out.println(queryy);
        String encodedQuery = java.net.URLEncoder.encode(queryy, "UTF-8");
        String url = "https://www.google.com/search?q=" + encodedQuery;
        webEngine.load(url);
    }
    @FXML
    public void naviguerVersGestion(ActionEvent event) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/GestionnerConso.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();

                // Créer une transition de fondu pour la nouvelle scène
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                // Démarrer la transition de fondu
                fadeTransition.play();

                // Afficher la nouvelle scène dans une nouvelle fenêtre
                stage.setScene(new Scene(root));
                stage.show();

                // Fermer la fenêtre actuelle après la transition
                //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                //fadeTransition.setOnFinished(e -> currentStage.close());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    @FXML
    public void naviguerVersSuivre(ActionEvent event) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Gestion Consommation/SuivreConso.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();

                // Créer une transition de fondu pour la nouvelle scène
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
                fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

                // Démarrer la transition de fondu
                fadeTransition.play();

                // Afficher la nouvelle scène dans une nouvelle fenêtre
                stage.setScene(new Scene(root));
                stage.show();

                // Fermer la fenêtre actuelle après la transition
                //  Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                //fadeTransition.setOnFinished(e -> currentStage.close());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    public void addHoverAnimation(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);

        label.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        label.setOnMouseExited(event -> {
            scaleTransition.stop();
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }
}
