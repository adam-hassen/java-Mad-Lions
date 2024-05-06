package EDU.userjava1.services;


import EDU.userjava1.entities.Conver;
import EDU.userjava1.entities.commentaire;
import EDU.userjava1.interfaces.CommentaireInterface;
import EDU.userjava1.tools.MyConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commentaireServices implements CommentaireInterface {

    private Connection cnx;

    public commentaireServices() {
        this.cnx = MyConnexion.getInstance().getCnx();
    }
    @Override
    public void ajoutcommentaire(commentaire commentaire) {
        String req = "INSERT INTO commentaire(user1_id, conver_id , reponse) VALUES ( ?,?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, commentaire.getUser1_id());
            ps.setInt(2, commentaire.getConver_id());
            ps.setString(3, commentaire.getReponse());

            ps.executeUpdate();
            System.out.println("commentaire ajoutée avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la commentaire : " + ex.getMessage());
        }
    }

    @Override
    public List<commentaire> getAllcommentaire() {
        List<commentaire> commentaireList = new ArrayList<>();
        String req = "SELECT commentaire.id, commentaire.user1_id,commentaire.conver_id , commentaire.reponse, user1.username " +
                "FROM commentaire " +
                "INNER JOIN user1 ON conver.user1_id = user1.id";
        try {
            Statement stmt = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(req);
            while (rs.next()) {
                commentaire commentaire = new commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setUser1_id(rs.getInt("user1_id"));
                commentaire.setConver_id(rs.getInt("Conver_id"));

                commentaire.setReponse(rs.getString("reponse"));
                commentaire.setUserName(rs.getString("username"));

                commentaireList.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaire: " + e.getMessage());
        }
        return commentaireList;
    }
    public List<commentaire> getCommentairesByConverId(int converId) {
        List<commentaire> commentaireList = new ArrayList<>();
        String req = "SELECT * FROM commentaire WHERE conver_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, converId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                commentaire commentaire = new commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setUser1_id(rs.getInt("user1_id"));
                commentaire.setConver_id(rs.getInt("conver_id"));
                commentaire.setReponse(rs.getString("reponse"));
                // Vous pouvez également récupérer d'autres champs si nécessaire
                commentaireList.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires: " + e.getMessage());
        }
        return commentaireList;
    }
}

