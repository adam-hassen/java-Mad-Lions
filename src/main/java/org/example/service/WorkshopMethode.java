package org.example.service;

import EDU.userjava1.entities.User1;
import EDU.userjava1.services.UserServices;
import javafx.scene.control.Alert;
import org.example.entity.Workshop;
import org.example.interfaces.WorkshopService;
import org.example.tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkshopMethode implements WorkshopService<Workshop> {
    @Override
    public void ajouterWorkshop(Workshop workshop) {
        String requete = "INSERT INTO workshop (nom ,type ,date,heure,cours)" +
                "VALUES (? , ? ,? ,? ,?)";
        System.out.println("workshop ajoutée avec succès");
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, workshop.getNom());
            pst.setString(2, workshop.getType());
            pst.setDate(3, java.sql.Date.valueOf(workshop.getDate()));
            pst.setTime(4, java.sql.Time.valueOf(workshop.getHeure()));
            pst.setString(5, workshop.getCours());

            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + generatedId);
                workshop.setId(generatedId);
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
    public boolean supprimerWorkshop(Workshop workshop) {
        String requete = "DELETE FROM workshop WHERE id = ?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            pst.setInt(1, workshop.getId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("workshop supprimé");
                return true; // Retourne true si au moins une ligne a été supprimée
            } else {
                System.out.println("Aucun workshop  trouvé avec cet ID");
                return false; // Retourne false si aucune ligne n'a été supprimée
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du workshop : " + e.getMessage());
            return false; // En cas d'erreur, retourne false
        }
    }

    @Override
    public boolean modifierWorkshop(Workshop workshop, int x) {
        String requete = "UPDATE workshop SET nom=?, type=?, date=?, heure=?, cours=? WHERE id=?";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            pst.setString(1, workshop.getNom());
            pst.setString(2, workshop.getType());
            pst.setDate(3, java.sql.Date.valueOf(workshop.getDate()));
            pst.setTime(4, java.sql.Time.valueOf(workshop.getHeure()));
            pst.setString(5, workshop.getCours());
pst.setInt(6,x);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("workshop modifié avec succès");
                return false;
            } else {
                System.out.println("Aucun workshop trouvé avec cet ID, aucune modification n'a été effectuée");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la workshop: " + e.getMessage());
        }
        return false;
    }



        @Override
    public List<Workshop> listeDesWorkshop() {

        List<Workshop> listeWorkshop = new ArrayList<>();
        String requete = "SELECT * FROM workshop";
        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(requete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String type = rs.getString("type");
                LocalTime heure = rs.getTime("heure").toLocalTime();
                LocalDate date = rs.getDate("date").toLocalDate();
                String cours = rs.getString("cours");

                Workshop workshop = new Workshop(id,nom,type,date,heure,cours);
                listeWorkshop.add(workshop);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des workshop : " + e.getMessage());
        }

        return listeWorkshop;

    }

    public Workshop getWorkshopByName(String name, List<Workshop> workshops) {
        for (Workshop workshop : workshops) {
            if (workshop.getNom().equalsIgnoreCase(name)) {
                return workshop;
            }
        }
        return null;
    }
    public List<Workshop> recherche_user(String nom) {
        List<Workshop> personnes = new ArrayList<>();
        String request = "SELECT * FROM workshop WHERE nom LIKE '%" + nom + "%'";
        // Utilisation de LIKE avec le nom pour rechercher des correspondances partielles

        try {
            PreparedStatement pst = MyConnection.getInsatance().getCnx().prepareStatement(request);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                LocalTime heure = rs.getTime("heure").toLocalTime();
                LocalDate date = rs.getDate("date").toLocalDate();
                String cours = rs.getString("cours");

                Workshop workshop = new Workshop(id,nom,type,date,heure,cours);
                personnes.add(workshop);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personnes;
    }

}