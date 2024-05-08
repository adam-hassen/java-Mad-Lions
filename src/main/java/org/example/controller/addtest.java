package org.example.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.entity.Question;
import org.example.entity.Reponse;
import org.example.entity.Test;
import org.example.entity.Workshop;
import org.example.service.WorkshopMethode;
import org.example.service.testMethode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;



public class addtest implements Initializable {

    @FXML
    private Label LoginMessageLabel1;

    @FXML
    private Button cancelButton1;

    @FXML
    private Button inscrire;

    @FXML
    private TextField question1;

    @FXML
    private TextField question2;

    @FXML
    private TextField question3;

    @FXML
    private TextField reponce1_1;

    @FXML
    private TextField reponce1_2;

    @FXML
    private TextField reponce1_3;

    @FXML
    private TextField reponce2_1;

    @FXML
    private TextField reponce2_2;

    @FXML
    private TextField reponce2_3;

    @FXML
    private TextField reponce3_1;

    @FXML
    private TextField reponce3_2;

    @FXML
    private TextField reponce3_3;

    @FXML
    private ComboBox<String > workshop;
    @FXML
    void liste(MouseEvent event) {

    }

    @FXML
    void listee(MouseEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("/Workshop/Affichetest.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2;
        stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }
    @FXML
    private Button ajoutertest;
    @FXML
    void inscrire(MouseEvent event)throws IOException {

        String questionText1 = question1.getText().trim();
        String responseText1_1 = reponce1_1.getText().trim();
        String responseText1_2 = reponce1_2.getText().trim();
        String responseText1_3 = reponce1_3.getText().trim();

        String questionText2 = question2.getText().trim();
        String responseText2_1 = reponce2_1.getText().trim();
        String responseText2_2 = reponce2_2.getText().trim();
        String responseText2_3 = reponce2_3.getText().trim();

        String questionText3 = question3.getText().trim();
        String responseText3_1 = reponce3_1.getText().trim();
        String responseText3_2 = reponce3_2.getText().trim();
        String responseText3_3 = reponce3_3.getText().trim();


        Test test = new Test();
        test.setScore(20);
        int selectedIndex = workshop.getSelectionModel().getSelectedIndex();
        Workshop workshopcombo=listeDesWorkshop.get(selectedIndex);
        test.SetId_workshop(workshopcombo.getId());
        testMethode testMethode= new testMethode();

        Question question1=new Question();
        question1.setContenu(questionText1);
        test.setQuestion_1(question1);

        Question question2=new Question();
        question2.setContenu(questionText2);
        test.setQuestion_2(question2);

        Question question3=new Question();
        question3.setContenu(questionText3);
        test.setQuestion_3(question3);

        Reponse reponse1_1=new Reponse();
        reponse1_1.setContenu(responseText1_1);
        Reponse reponse1_2=new Reponse();
        reponse1_2.setContenu(responseText1_2);
        Reponse reponse1_3=new Reponse();
        reponse1_3.setContenu(responseText1_3);
        Reponse[]reponses1={reponse1_1,reponse1_2,reponse1_3};
        test.setReponse_1(reponses1);

        Reponse reponse2_1=new Reponse();
        reponse2_1.setContenu(responseText2_1);
        Reponse reponse2_2=new Reponse();
        reponse2_2.setContenu(responseText2_2);
        Reponse reponse2_3=new Reponse();
        reponse2_3.setContenu(responseText2_3);
        Reponse[]reponses2={reponse2_1,reponse2_2,reponse2_3};
        test.setReponse_2(reponses2);

        Reponse reponse3_1=new Reponse();
        reponse3_1.setContenu(responseText3_1);
        Reponse reponse3_2=new Reponse();
        reponse3_2.setContenu(responseText3_2);
        Reponse reponse3_3=new Reponse();
        reponse3_3.setContenu(responseText3_3);
        Reponse[]reponses3={reponse3_1,reponse3_2,reponse3_3};
        test.setReponse_3(reponses3);

        testMethode.addtest(test);

/*
        testMethode.addtest(questionText1, responseText1_1, responseText1_2, responseText1_3);
        testMethode.addtest(questionText2, responseText2_1, responseText2_2, responseText2_3);
        testMethode.addtest(questionText3, responseText3_1, responseText3_2, responseText3_3);
*/

        System.out.println("Done!");
        Parent root2 = FXMLLoader.load(getClass().getResource("/Workshop/Affichetest.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage2;
        stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.setScene(scene2);
        stage2.show();

    }
    @FXML
    void test(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("/Workshop/Affichetest.fxml"));
        Scene scene1 = new Scene(root1);
        Stage stage1;
        stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage1.setScene(scene1);
        stage1.show();
    }

    @FXML
    void login(ActionEvent event) {

    }
private List<Workshop> listeDesWorkshop ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WorkshopMethode w =new WorkshopMethode();
        listeDesWorkshop = w.listeDesWorkshop();
        System.out.println("aloo");
        for (Workshop workshop1: listeDesWorkshop) {
            System.out.println(workshop1);
            workshop.getItems().add(workshop1.getNom());
        }
    }
}

