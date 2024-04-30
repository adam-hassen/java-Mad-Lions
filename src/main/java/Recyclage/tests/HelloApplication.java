package Recyclage.tests;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ProduitRecyclable/AjouterProduitRecyclable.fxml"));
        Parent root = fxmlLoader.load();

        // Créer une transition de fondu pour la nouvelle scène
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
        fadeTransition.setFromValue(0.0); // Définir la transparence initiale à 0
        fadeTransition.setToValue(1.0); // Définir la transparence finale à 1

        // Démarrer la transition de fondu
        fadeTransition.play();
        Scene scene = new Scene(root);
        stage.setTitle("ProduitRecyclable");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}