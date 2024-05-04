package EDU.userjava1.services;

import EDU.userjava1.controllers.reclamation;
import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.reclamationInterface;
import EDU.userjava1.tools.MyConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class reclamationService implements reclamationInterface {
    private Connection cnx;

    public reclamationService() {
        this.cnx = MyConnexion.getInstance().getCnx();
    }
@Override
    public void ajoutreclamation(Reclamation reclamation) {
        String req = "INSERT INTO reclamation(user1_id, message, date, type) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, reclamation.getUser1_id());
            ps.setString(2, reclamation.getMessage());
            ps.setDate(3, new java.sql.Date(reclamation.getDate().getTime()));
            ps.setString(4, reclamation.getType());
            ps.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + ex.getMessage());
        }
    }
@Override
    public void repondreReclamation(int idReclamation, String reponse) {
        String req = "UPDATE reclamation SET reponse = ? WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reponse);
            ps.setInt(2, idReclamation);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réponse ajoutée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec l'ID spécifié.");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la réponse : " + ex.getMessage());
        }
    }
    @Override
    public List<Reclamation> getAllReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation";
        try {
            Statement stmt = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(req);
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUser1_id(rs.getInt("user1_id"));
                reclamation.setMessage(rs.getString("message"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setType(rs.getString("type"));
                reclamation.setReponse(rs.getString("reponse"));

                // Récupérer le nom d'utilisateur associé à partir de l'ID utilisateur
                String userName = getUserNameById(reclamation.getUser1_id());
                reclamation.setUserName(userName);

                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
        }
        return reclamations;
    }

    // Méthode pour récupérer le nom d'utilisateur à partir de l'ID utilisateur
    private String getUserNameById(int userId) {
        String userName = null;
        String req = "SELECT username FROM user1 WHERE id = ?";
        try {
            PreparedStatement ps = MyConnexion.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userName = rs.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom d'utilisateur: " + e.getMessage());
        }
        return userName;
    }


    public List<Reclamation> afficherreclamation() {

            List<Reclamation> reclamations = new ArrayList<>();
            String request = "SELECT * FROM reclamation";

            try {
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(request);
                while (rs.next()) {
                    Reclamation c = addreclamation(rs);
                    reclamations.add(c);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return reclamations;
        }

        private Reclamation addreclamation(ResultSet rs) {
        Reclamation p = new Reclamation();


        try {
            p.setId(rs.getInt(1));
            p.setUser1_id(rs.getInt(2));
            p.setMessage(rs.getString(3));
            p.setDate(rs.getDate(4));
            p.setType(rs.getString(5));
            p.setReponse(rs.getString(6));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }
    public List<Reclamation> getReclamationsByUserId(int userId) {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation WHERE user1_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUser1_id(rs.getInt("user1_id"));
                reclamation.setMessage(rs.getString("message"));
                reclamation.setDate(rs.getDate("date"));
                reclamation.setType(rs.getString("type"));
                reclamation.setReponse(rs.getString("reponse"));

                // Récupérer le nom d'utilisateur associé à partir de l'ID utilisateur
                String userName = getUserNameById(reclamation.getUser1_id());
                reclamation.setUserName(userName);

                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
        }
        return reclamations;
    }

}
