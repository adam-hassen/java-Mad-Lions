package org.example.service;

import org.example.entity.Test;
import org.example.interfaces.testService;
import org.example.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class testMethode implements testService<Test> {
    @Override
    public void addtest(Test test) {
        String requete = "INSERT INTO workshop (question,reponse)" +
                "VALUES (? , ? ,? ,? ,?)";
        System.out.println("test ajoutée avec succès");
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, test.getQuestion());
            pst.setString(2, test.getReponse());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + generatedId);
                test.setId(generatedId);
            }
           /* Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("WorkshopAjouter");
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
        String requete = "DELETE FROM workshop WHERE id = ?";
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
    public boolean modifiertest(Test test, int id) {
        return false;
    }

    @Override
    public List<Test> listeDestest() {
        return null;
    }

    @Override
    public boolean modifierTest(Test test, int x) {
        String requete = "UPDATE test SET question=?, reponse=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            pst.setString(1, test.getQuestion());
            pst.setString(2, test.getReponse());
pst.setInt(3,x);
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
    public List<Test> listeDesTest() {

        List<Test> listeTest = new ArrayList<>();
        String requete = "SELECT * FROM test";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String reponse = rs.getString("reponse");


                Test test = new Test(id,question,reponse);
                listeTest.add(test);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des test : " + e.getMessage());
        }

        return listeTest;

    }
}