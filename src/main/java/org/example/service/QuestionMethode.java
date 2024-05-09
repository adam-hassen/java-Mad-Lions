package org.example.service;

import org.example.entity.Question;
import org.example.entity.Reponse;
import org.example.tools.MyConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionMethode {
    public static Question addQuestion(Question question) {
        String requete = "INSERT INTO question (reponse_correct_id,test_id,contenu)" +
                "VALUES ( NULL , ? , ? )";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, question.getTest_id());
            pst.setString(2, question.getContenu());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("question Generated ID: " + generatedId);
                question.setid(generatedId);
            }
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
        return  question;
    }
    public  static  boolean update_reponse_correct(Reponse reponse){
        String requete = "UPDATE question set reponse_correct_id=? where id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, reponse.getId());
            pst.setInt(2, reponse.getQuestion_id());
            pst.executeUpdate();
            int row = pst.executeUpdate();
            if (row!=0) {
                System.out.println("mise a jour Generated ID: " + reponse.getQuestion_id());
            }
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        return false;
        }

    return  true;
    }
    public  static void remove_by_test(int id_test){
        String requete = "DELETE FROM question WHERE question.test_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete);
            pst.setInt(1,id_test);
            pst.executeUpdate();
            System.out.println("\"delete question\""+id_test);
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
    }
    public static void update_question(Question question) {
        String requete = "UPDATE question set contenu=? where id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, question.getContenu());
            pst.setInt(2,question.getId());
            pst.executeUpdate();
            int row = pst.executeUpdate();
            if (row != 0) {
                System.out.println("question = " + question.getId());
            }
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
    }
}
