package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.entity.Workshop;
import org.example.service.WorkshopMethode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class Main  extends Application {

    public static void main(String[] args)  {
        launch(args);
       /* Workshop w=new Workshop();
        w.setNom("assil");
        w.setType("tenin");
        w.setDate(LocalDate.of(2002,01,11));
        w.setHeure(LocalTime.of(11,00,00));
        w.setCours("mehdi test workshop braa");
        WorkshopMethode wm=new WorkshopMethode();
       //wm.ajouterWorkshop(w);
        w.setNom("mimou");
        w.setType("tiwtiw");
       // wm.modifierWorkshop(w, w.getId());
*/
        WorkshopMethode wm=new WorkshopMethode();
        List<Workshop> data=wm.listeDesWorkshop();
int i=0;
        for (Workshop workshop : data) {
            i++;
            System.out.println("workshop number :"+i);
            System.out.println("workshop ID: " + workshop.getId());
            System.out.println("workshop nom: " + workshop.getNom());
            System.out.println("workshop type: " + workshop.getType());
            System.out.println("workshop date: " + workshop.getDate());
            System.out.println("workshop heure: " + workshop.getHeure());
            System.out.println("workshop Cours: " + workshop.getCours());
            // Print other details as needed
            System.out.println(); // Add an empty line for better readability
        }
        //wm.supprimerWorkshop(w);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Workshop/AjouterWorkshop.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
