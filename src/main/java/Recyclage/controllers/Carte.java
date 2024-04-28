package Recyclage.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Carte implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private TextField searchField;

    private WebEngine webEngine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webView.getEngine();
        // Charger le fichier HTML contenant la carte Google Maps
        webEngine.load(getClass().getResource("/HTML/Map.html").toExternalForm());
    }

    @FXML
    void searchAddress(ActionEvent event) {
        String address = searchField.getText().trim();
        if (!address.isEmpty()) {
            webView.getEngine().executeScript("searchEcoDepotAddress('" + address + "');");
        }

    }
}
