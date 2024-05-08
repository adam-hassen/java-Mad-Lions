package org.example;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.entity.Question;
import org.example.entity.Reponse;
import org.example.entity.Test;
import org.example.entity.Workshop;
import org.example.service.ReponseMethode;
import org.example.service.WorkshopMethode;
import org.example.service.testMethode;

import java.io.IOException;
import java.util.List;


public class Main  extends Application {

    public static void main(String[] args)  {



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

        /***********************************************/
        testMethode crud=new testMethode();
        Test test=new Test();
        Question question=new Question(),
                question1=new Question(),
                question2=new Question();
        question.setContenu("CRUD test");
        question1.setContenu("CRUD test1");
        question2.setContenu("CRUD test2");
        test.setQuestion_1(question);
        test.setQuestion_2(question1);
        test.setQuestion_3(question2);
        test.SetId_workshop(45);
        test.setScore(20);
        Reponse reponse=new Reponse(),
                reponse1=new Reponse(),
                reponse2=new Reponse();
        reponse.setContenu("djeja 1");
        reponse1.setContenu("djejea 2");
        reponse2.setContenu("djeja 3");

        test.setReponse_1(new Reponse[]{new Reponse("djeja1 mta3 1"),
                        new Reponse("djeja2 mta3 1"),
                        new Reponse("djeja3 mta3 1")
                }
        );
        test.setReponse_2(new Reponse[]{new Reponse("djeja1 mta3 2"),
                        new Reponse("djeja2 mta3 2"),
                        new Reponse("djeja3 mta3 2")
                }
        );
        test.setReponse_3(new Reponse[]{new Reponse("djeja1 mta3 3"),
                        new Reponse("djeja2 mta3 3"),
                        new Reponse("djeja3 mta3 3")
                }
        );

        //crud.addtest(test);

        //crud.supprimertest(test);




        launch(args);

    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
