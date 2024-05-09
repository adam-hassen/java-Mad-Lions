package org.example.service;

import org.example.entity.Question;
import org.example.entity.Reponse;
import org.example.entity.Test;
import org.example.interfaces.testService;
import org.example.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class testMethode implements testService<Test> {


    @Override
    public void addtest(Test test) {
        String requete = "INSERT INTO test (workshop_id,score)" +
                "VALUES (?,? )";
        System.out.println("test ajoutée avec succès");
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, (test.getId_workshop()));
            pst.setInt(2, (test.getScore()));
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + generatedId);
                test.setId(generatedId);
            }
            test.getQuestion_1().setTest_id(test.getId());
            test.getQuestion_3().setTest_id(test.getId());
            test.getQuestion_2().setTest_id(test.getId());

            QuestionMethode.addQuestion(test.getQuestion_1());
            QuestionMethode.addQuestion(test.getQuestion_2());
            QuestionMethode.addQuestion(test.getQuestion_3());


            for (int i=0;i<test.getReponse_1().length;i++){
                test.getReponse_1()[i].setQuestion_id(test.getQuestion_1().getId());
                ReponseMethode.addReponse(test.getReponse_1()[i]);
                System.out.println("test.getReponse_1()[0].getQuestion_id() = " + test.getReponse_1()[0].getQuestion_id());
                test.getReponse_2()[i].setQuestion_id(test.getQuestion_2().getId());
                ReponseMethode.addReponse(test.getReponse_2()[i]);

                test.getReponse_3()[i].setQuestion_id(test.getQuestion_3().getId());
                ReponseMethode.addReponse(test.getReponse_3()[i]);
            }
            QuestionMethode.update_reponse_correct(test.getReponse_1()[0]);
            QuestionMethode.update_reponse_correct(test.getReponse_2()[0]);
            QuestionMethode.update_reponse_correct(test.getReponse_3()[0]);

           /* Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Test a ajouter");
            alert.showAndWait();*/

        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
           /* Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();*/
        }
    }

    @Override
    public boolean supprimertest(Test test) {
       /* ReponseMethode.remove_by_quesiton(test.getQuestion_1().getTest_id());
        ReponseMethode.remove_by_quesiton(test.getQuestion_2().getTest_id());
        ReponseMethode.remove_by_quesiton(test.getQuestion_3().getTest_id());
        QuestionMethode.remove_by_test(test.getId());
       */
        String requete ="DELETE FROM test WHERE id = ?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            pst.setInt(1, test.getId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("test supprimé");
                return true; // Retourne true si au moins une ligne a été supprimée
            } else {
                System.out.println("Aucun test  trouve avec cet ID");
                return false; // Retourne false si aucune ligne n'a été supprimée
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du test : " + e.getMessage());
            return false; // En cas d'erreur, retourne false
        }
    }






    @Override
    public boolean modifiertest(Test test, int x) {
        String requete = "UPDATE test SET score=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            pst.setInt(2,x);
            pst.setString(1, Integer.toString(test.getScore()));
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("test modifié avec succès");
                return false;
            } else {
                System.out.println("Aucun test trouvé avec cet ID, aucune modification n'a été effectuée");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du test: " + e.getMessage());
        }
        return false;
    }



    @Override
    public List<Test> listeDestest() {

        List<Test> listeTest = new ArrayList<>();

        String requete = "SELECT test.id AS id, test.workshop_id AS workshop_id, test.score AS score," +
                "       question.id AS question_id, question.reponse_correct_id AS response_correct_id," +
                "       question.contenu AS question_content," +
                "       reponse.id AS response_id, reponse.question_id AS response_question_id," +
                "       reponse.contenu AS response_content" +
                " FROM test" +
                " JOIN question ON test.id = question.test_id" +
                " JOIN reponse ON question.id = reponse.question_id;";
        int indice =0;
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            Test test = new Test();
            Question question1=new Question();
            Question question2=new Question();
            Question question3=new Question();
            Reponse[]reponses1=new Reponse[3];
            Reponse[]reponses2=new Reponse[3];
            Reponse[]reponses3=new Reponse[3];
            while (rs.next()) {
                if(test.getId()!=null&&test.getId()!=rs.getInt("id")){
                    test.setQuestion_1(question1);
                    test.setQuestion_3(question3);
                    test.setQuestion_2(question2);
                    test.setReponse_1(reponses1);
                    test.setReponse_2(reponses2);
                    test.setReponse_3(reponses3);
                    listeTest.add(test);
                    test=new Test();
                    question1=new Question();
                    question2=new Question();
                    question3=new Question();
                    reponses1=new Reponse[3];
                    reponses2=new Reponse[3];
                    reponses3=new Reponse[3];
                    indice=0;
                }
                test.setId(rs.getInt("id"));
                test.SetId_workshop(rs.getInt("workshop_id"));
                test.setScore(rs.getInt("score"));
                Question question=new Question();
                question.setid(rs.getInt("question_id"));
                question.setTest_id(rs.getInt("id"));
                question.setContenu(rs.getString("question_content"));
                Reponse reponse =new Reponse();
                reponse.setId(rs.getInt("response_id"));
                reponse.setQuestion_id(rs.getInt("response_question_id"));
                reponse.setContenu(rs.getString("response_content"));
                if(indice<3){
                    question1=question;
                    reponses1[indice]=reponse;
                }else if (indice<6){
                    question2=question;
                    reponses2[indice%reponses2.length]=reponse;
                }else{
                    question3=question;
                    reponses3[indice%reponses3.length]=reponse;
                }
                indice++;

                //listeTest.add(test);
               // System.out.println(indice-1+" "+test.getId());
              //  System.out.println("rs"+rs.getString("response_content"));
            }

            test.setQuestion_1(question1);
            test.setQuestion_3(question3);
            test.setQuestion_2(question2);
            test.setReponse_1(reponses1);
            test.setReponse_2(reponses2);
            test.setReponse_3(reponses3);
            listeTest.add(test);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des test : " + e.getMessage());
        }

        System.out.println("finished");

        return listeTest;
    }

    public List<Test> listeDestest_workshop(int id_workshop) {

        List<Test> listeTest = new ArrayList<>();

        String requete = "SELECT test.id AS id, test.workshop_id AS workshop_id, test.score AS score," +
                "       question.id AS question_id, question.reponse_correct_id AS response_correct_id," +
                "       question.contenu AS question_content," +
                "       reponse.id AS response_id, reponse.question_id AS response_question_id," +
                "       reponse.contenu AS response_content" +
                " FROM test" +
                " JOIN question ON test.id = question.test_id" +
                " JOIN reponse ON question.id = reponse.question_id where test.workshop_id= ?;";
        int indice =0;
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
pst.setInt(1,id_workshop);
            ResultSet rs = pst.executeQuery();
            Test test = new Test();
            Question question1=new Question();
            Question question2=new Question();
            Question question3=new Question();
            Reponse[]reponses1=new Reponse[3];
            Reponse[]reponses2=new Reponse[3];
            Reponse[]reponses3=new Reponse[3];
            while (rs.next()) {
                if(test.getId()!=null&&test.getId()!=rs.getInt("id")){
                    test.setQuestion_1(question1);
                    test.setQuestion_3(question3);
                    test.setQuestion_2(question2);
                    test.setReponse_1(reponses1);
                    test.setReponse_2(reponses2);
                    test.setReponse_3(reponses3);
                    listeTest.add(test);
                    test=new Test();
                    question1=new Question();
                    question2=new Question();
                    question3=new Question();
                    reponses1=new Reponse[3];
                    reponses2=new Reponse[3];
                    reponses3=new Reponse[3];
                    indice=0;
                }
                test.setId(rs.getInt("id"));
                test.SetId_workshop(rs.getInt("workshop_id"));
                test.setScore(rs.getInt("score"));
                Question question=new Question();
                question.setid(rs.getInt("question_id"));
                question.setTest_id(rs.getInt("id"));
                question.setContenu(rs.getString("question_content"));
                Reponse reponse =new Reponse();
                reponse.setId(rs.getInt("response_id"));
                reponse.setQuestion_id(rs.getInt("response_question_id"));
                reponse.setContenu(rs.getString("response_content"));
                if(indice<3){
                    question1=question;
                    reponses1[indice]=reponse;
                }else if (indice<6){
                    question2=question;
                    reponses2[indice%reponses2.length]=reponse;
                }else{
                    question3=question;
                    reponses3[indice%reponses3.length]=reponse;
                }
                indice++;

                //listeTest.add(test);

                System.out.println("rs"+rs.getString("response_content"));
            }

            test.setQuestion_1(question1);
            test.setQuestion_3(question3);
            test.setQuestion_2(question2);
            test.setReponse_1(reponses1);
            test.setReponse_2(reponses2);
            test.setReponse_3(reponses3);
            listeTest.add(test);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des test : " + e.getMessage());
        }

        System.out.println("finished");

        return listeTest;
    }
}