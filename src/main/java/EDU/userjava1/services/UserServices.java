package EDU.userjava1.services;
import EDU.userjava1.entities.RolesConverter;
import EDU.userjava1.tools.MyConnexion;
import EDU.userjava1.interfaces.Userinterface;
import EDU.userjava1.entities.User1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
public class UserServices implements Userinterface {

    private Statement ste;
    Connection cnx = MyConnexion.getInstance().getCnx();

    public void ajouteruser(User1 p) {
        String mdp = EncryptMdp(p.getPassword());

        String req = "INSERT INTO `user1`(`prenom`, `name`, `numero`, `username`, `adress`, `password`, `genre`,`roles`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getPrenom());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getNumero());
            ps.setString(4, p.getUsername());
            ps.setString(5, p.getAdress());
            ps.setString(6, mdp);
            ps.setString(7, p.getGenre());
            String rolesJson = new RolesConverter().convertToDatabaseColumn(p.getRoles());
            ps.setString(8, rolesJson);

            ps.executeUpdate();
            System.out.println("User ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<User1> afficheruser() {

        List<User1> personnes = new ArrayList<>();
        String request = "SELECT * FROM user1 ";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                User1 c = adduser(rs);
                //
                personnes.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personnes;

    }



    private User1 adduser(ResultSet rs) {
        User1 p = new User1();


        try {
            p.setId(rs.getInt(1));
            p.setUsername(rs.getString(2));
            p.setRoles(rs.getString(3));
            p.setPassword(rs.getString(4));
            p.setName(rs.getString(5));
            p.setAdress(rs.getString(6));
            p.setNumero(rs.getInt(7));
            p.setGenre(rs.getString(8));
            p.setPrenom(rs.getString(9));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }

    @Override
    public User1 getbyid_user(int id) {
        return null;
    }

    @Override
    public void supprimeruser(int p) {
        String req = "DELETE FROM `User1` WHERE id='" + p + "'";
        try {
            ste = cnx.prepareStatement(req);
            ste.executeUpdate(req);
            System.out.println("Utlisateur est supprime");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void modifieruser(User1 p, int x) {
        String mdp = EncryptMdp(p.getPassword());

        try {
            String req;
            req = "UPDATE `User1` SET `prenom`=?,`name`=?,`numero`=?,`username`=?,`adress`=?,`genre`=?  WHERE id='" + x + "'";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getPrenom());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getNumero());
            ps.setString(4, p.getUsername());
            ps.setString(5, p.getAdress());
            ps.setString(6, p.getGenre());


            ps.executeUpdate();
            System.out.println("Utlisateur est modifié");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public void modifierMDP(User1 p, int x) {
        String mdp = EncryptMdp(p.getPassword());

        try {
            String req;
            req = "UPDATE `User1` SET `password`=?  WHERE id='" + x + "'";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, mdp);



            ps.executeUpdate();
            System.out.println("le mot de passe est modifié");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public boolean test_used_email(User1 u1) {
        int a;
        String req = "select id from user1 WHERE username = '" + u1.getUsername() + "'";
        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(req);
            rs.next();
            a = rs.getInt("id");
            return false;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

/*
    public List<User1> getAllData() {

        List<User1> data = new ArrayList<>();
        String requete = "SELECT * FROM User1";
        try {
            Statement st = MyConnexion.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                User1 p = new User1();
                p.setName(rs.getString("Name"));
                p.setPrenom(rs.getString("prenom"));
                p.setUsername(rs.getString("Username"));
                p.setAdress(rs.getString("adress"));
                p.setGenre(rs.getString("genre"));
                p.setNumero(rs.getInt("numero"));
                p.setRoles(rs.getString("roles"));

                data.add(p);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }*/

    @Override
    public int Login(String email, String password) {
        String mdp_enc = EncryptMdp(password);
        System.out.println("Mot de passe encodé : " + mdp_enc);
        int id = -1; // Initialisez à une valeur par défaut
        String req = "SELECT * FROM user1 WHERE username ='" + email + "' AND password ='" + mdp_enc + "' AND roles = '[\"ROLE_USER\"]'";
        System.out.println("Requête SQL : " + req);
        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(req);
            if (rs.next()) { // Vérifiez s'il y a des résultats
                id = rs.getInt(1); // Extrait l'ID seulement si des résultats sont trouvés
                System.out.println("Connexion réussie pour l'utilisateur avec l'ID : " + id);
            } else {
                System.out.println("Aucun utilisateur correspondant trouvé.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }




    public int Login1(String email, String password) {
        String mdp_enc = EncryptMdp(password);
        int id1 = -1; // Initialisez à une valeur par défaut
        String req = "SELECT * FROM user1 WHERE username ='" + email + "' AND password ='" + mdp_enc + "' AND roles = '[\"ROLE_ADMIN\"]'";
        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(req);
            if (rs.next()) { // Vérifiez s'il y a des résultats
                id1 = rs.getInt(1); // Extrait l'ID seulement si des résultats sont trouvés
                System.out.println(id1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id1;
    }



    public User1 getbyemail_user(String a) {
        User1 c = new User1();
        String request;
        request = "SELECT * FROM user1 WHERE username ='" + a + "'";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(request);
            while (rs.next()) {
                c = adduser(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
    public String EncryptMdp (String mdp_input)
    {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest. update(mdp_input.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray)
            {
                sb.append (String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

}

