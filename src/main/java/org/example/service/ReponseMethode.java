package org.example.service;

import org.example.entity.Question;
import org.example.entity.Reponse;
import org.example.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReponseMethode {
    public static Reponse addReponse(Reponse reponse) {
        String requete = "INSERT INTO reponse (question_id,contenu)" +
                "VALUES ( ? , ? )";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1,reponse.getQuestion_id());
            pst.setString(2, reponse.getContenu());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("reponse Generated ID: " + generatedId);
                reponse.setId(generatedId);
            }

        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
        return  reponse;
    }
    public  static  void remove(Reponse reponse){
        String requete = "DELETE FROM Course WHERE id = ?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete);
            pst.setInt(1,reponse.getId());
            pst.executeUpdate();

        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
    }
    public  static void remove_by_quesiton(int id_question){
        String requete = "DELETE FROM reponse WHERE question_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete);
            pst.setInt(1,id_question);
            pst.executeUpdate();
            System.out.print("\"delete succesffull\" = " + "delete succesffull");
            System.out.println("id_question = " + id_question);
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }

    }
    public static void update_reponse(Reponse reponse) {
        String requete = "UPDATE reponse set contenu=? where id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement
                    (requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, reponse.getContenu());
            pst.setInt(2,reponse.getId());
            pst.executeUpdate();
            int row = pst.executeUpdate();
            if (row != 0) {
                System.out.println("reponse = " + reponse.getId());
            }
        } catch (SQLException e) {
            System.out.println("+++" + e.getMessage() + "+++");
        }
    }
}
