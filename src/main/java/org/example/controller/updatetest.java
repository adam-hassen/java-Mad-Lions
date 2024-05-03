package org.example.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.entity.Test;
import org.example.service.QuestionMethode;
import org.example.service.ReponseMethode;
import org.example.service.testMethode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;



public class updatetest {

private  Test test;

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
    private Button update2;
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
    void inscrire(MouseEvent event)throws IOException {
        test.getQuestion_1().setContenu(question1.getText());
        test.getReponse_1()[0].setContenu(reponce1_1.getText());
        test.getReponse_1()[1].setContenu(reponce1_2.getText());
        test.getReponse_1()[2].setContenu(reponce1_3.getText());

        test.getQuestion_2().setContenu(question2.getText());
        test.getReponse_2()[0].setContenu(reponce2_1.getText());
        test.getReponse_2()[1].setContenu(reponce2_2.getText());
        test.getReponse_2()[2].setContenu(reponce2_3.getText());

        test.getQuestion_3().setContenu(question3.getText());
        test.getReponse_3()[0].setContenu(reponce3_1.getText());
        test.getReponse_3()[1].setContenu(reponce3_2.getText());
        test.getReponse_3()[2].setContenu(reponce3_3.getText());

        QuestionMethode.update_question(test.getQuestion_1());
        QuestionMethode.update_question(test.getQuestion_2());
        QuestionMethode.update_question(test.getQuestion_3());

        for (int i=0;i<3;i++){
            ReponseMethode.update_reponse(test.getReponse_1()[i]);
            ReponseMethode.update_reponse(test.getReponse_2()[i]);
            ReponseMethode.update_reponse(test.getReponse_3()[i]);
        }
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
    public void loader(Test test){
        this.test=test;
        question1.setText(test.getQuestion_1().getContenu());
        question2.setText(test.getQuestion_2().getContenu());
        question3.setText(test.getQuestion_3().getContenu());

        reponce1_1.setText(test.getReponse_1()[0].getContenu());
        reponce1_2.setText(test.getReponse_1()[1].getContenu());
        reponce1_3.setText(test.getReponse_1()[2].getContenu());

        reponce2_1.setText(test.getReponse_2()[0].getContenu());
        reponce2_2.setText(test.getReponse_2()[1].getContenu());
        reponce2_3.setText(test.getReponse_2()[2].getContenu());

        reponce3_1.setText(test.getReponse_3()[0].getContenu());
        reponce3_2.setText(test.getReponse_3()[1].getContenu());
        reponce3_3.setText(test.getReponse_3()[2].getContenu());
    }
}

