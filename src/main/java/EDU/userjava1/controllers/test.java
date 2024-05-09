package EDU.userjava1.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoGardien");
        primaryStage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }


    }
}
