package org.example.service;

import javafx.scene.control.Alert;
import org.example.Connexion.connexion;
import org.example.entity.ActionLocation;
import org.example.entity.TypeName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationService {
    Connection cn;

    public LocationService() {
        cn = connexion.getInstance().getCn();
    }
    public void ajouterActionLocation(ActionLocation actionLocation) {
        try {
            String requete = "INSERT INTO action_location (nom, address, latitude, longitude) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, actionLocation.getNom());
            pst.setString(2, actionLocation.getAddress());
            pst.setString(3, actionLocation.getLatitude());
            pst.setString(4, actionLocation.getLongitude());
            pst.executeUpdate();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("ActionLocation ajoutée avec succès!");
            successAlert.showAndWait();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Ajout echoué!");
            successAlert.showAndWait();
        }
    }

    public List<ActionLocation> afficherActionLocations() {
        List<ActionLocation> listeActionLocations = new ArrayList<>();
        try {
            String requete = "SELECT * FROM ACTION_LOCATION";
            PreparedStatement pst = cn.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ActionLocation actionLocation = new ActionLocation();
                actionLocation.setId(rs.getInt("id"));
                actionLocation.setNom(rs.getString("nom"));
                actionLocation.setAddress(rs.getString("address"));
                actionLocation.setLatitude(rs.getString("latitude"));
                actionLocation.setLongitude(rs.getString("longitude"));
                listeActionLocations.add(actionLocation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listeActionLocations;
    }

    public void supprimerActionLocation(int id) {
        try {
            String requete = "DELETE FROM ACTION_LOCATION WHERE ID = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            int row = pst.executeUpdate();
            if (row > 0) {
                System.out.println("Delete succeed");
            } else {
                System.out.println("Delete failed. Possibly invalid ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifierActionLocation(int id, ActionLocation actionLocation) {
        try {
            String requete = "UPDATE ACTION_LOCATION SET nom=?, address=?, latitude=?, longitude=? WHERE ID = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, actionLocation.getNom());
            pst.setString(2, actionLocation.getAddress());
            pst.setString(3, actionLocation.getLatitude());
            pst.setString(4, actionLocation.getLongitude());
            pst.setInt(5, id);
            int row = pst.executeUpdate();
            if (row > 0) {
                System.out.println("Modify succeed");
            } else {
                System.out.println("Modify failed. Possibly invalid ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public ActionLocation chercherLocation() {
        ActionLocation act = null;
        try {
            String requete = "SELECT * FROM action_location WHERE id = LAST_INSERT_ID()";
            PreparedStatement pst = cn.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String address = rs.getString("address");
                String latitude = rs.getString("latitude");
                String longitude = rs.getString("longitude");
                act = new ActionLocation(nom, address, latitude, longitude);
                act.setId(id);
                return act;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return act;
    }
}

