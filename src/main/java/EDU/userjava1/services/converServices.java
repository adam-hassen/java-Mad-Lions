package EDU.userjava1.services;

import EDU.userjava1.entities.Conver;
import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.interfaces.ConverIterface;
import EDU.userjava1.tools.MyConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class converServices implements ConverIterface {
    private Connection cnx;

    public converServices() {
        this.cnx = MyConnexion.getInstance().getCnx();
    }
    @Override
    public void ajoutconver(Conver conver) {
        String req = "INSERT INTO conver(user1_id, publication) VALUES ( ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, conver.getUser1_id());
            ps.setString(2, conver.getPublication());

            ps.executeUpdate();
            System.out.println("conver ajoutée avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la conver : " + ex.getMessage());
        }
    }



    @Override
    public List<Conver> getAllConver() {
        List<Conver> converList = new ArrayList<>();
        String req = "SELECT conver.id, conver.user1_id, conver.publication, user1.username " +
                "FROM conver " +
                "INNER JOIN user1 ON conver.user1_id = user1.id";
        try {
            Statement stmt = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(req);
            while (rs.next()) {
                Conver conver = new Conver();
                conver.setId(rs.getInt("id"));
                conver.setUser1_id(rs.getInt("user1_id"));
                conver.setPublication(rs.getString("publication"));
                conver.setUserName(rs.getString("username")); // Setting the username directly

                converList.add(conver);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
        }
        return converList;
    }



}
