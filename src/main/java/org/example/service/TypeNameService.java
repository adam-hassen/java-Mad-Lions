package org.example.service;

import org.example.Connexion.connexion;
import org.example.entity.TypeName;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeNameService {
    Connection cn;

    public TypeNameService() {
        cn = connexion.getInstance().getCn();
    }
    public void ajouterTypeName(TypeName Type){
        try {
            String requete = "INSERT INTO type_name (action_type_id,nom,score,materiel,type,util_max) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, Type.getAction_type_id());
            pst.setString(2, Type.getNom());
            pst.setDouble(3, Type.getScore());
            pst.setString(4, Type.getMateriel());
            pst.setString(5, Type.getType());
            pst.setDouble(6, Type.getUtil_max());
            pst.executeUpdate();
            System.out.println("AddTypName succeed!");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public List<TypeName> afficherTypeName(){
        List<TypeName> ListeAct = new ArrayList<>();
        try {
            String requete = "SELECT * FROM type_name";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while(rs.next()){
                TypeName act = new TypeName();
                act.setId(rs.getInt("id"));
                act.setAction_type_id(rs.getInt("action_type_id"));
                act.setNom(rs.getString("nom"));
                act.setScore(rs.getDouble("score"));
                act.setMateriel(rs.getString("materiel"));
                act.setType(rs.getString("type"));
                act.setUtil_max(rs.getDouble("util_max"));
                ListeAct.add(act);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return ListeAct;
    }
    public void supprimerTypeName(int id){
        try {
            String requete = "DELETE FROM type_name WHERE ID = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,id);
            int row = pst.executeUpdate();
            if (row > 0) System.out.println("Delete succeed");
            else System.out.println("Delete Failed Probably infound ID");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void modifierTypename(int id, TypeName act){
        try {
            String requete = "UPDATE type_name SET action_type_id =?, nom=?, score=?" +
                    ", materiel=?, type=?, util_max=? WHERE ID=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,act.getAction_type_id());
            pst.setString(2,act.getNom());
            pst.setDouble(3,act.getScore());
            pst.setString(4,act.getMateriel());
            pst.setString(5,act.getType());
            pst.setDouble(6,act.getUtil_max());
            pst.setDouble(7,id);
            int row = pst.executeUpdate();
            if (row > 0) System.out.println("Modify succeed");
            else System.out.println("Modify Failed Probably infound ID");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public TypeName cherchertypename(String nom){
        TypeName act = new TypeName();
        try {
            String requete = "SELECT * FROM type_name WHERE nom=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1,nom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                act.setId(rs.getInt("id"));
                act.setAction_type_id(rs.getInt("action_type_id"));
                act.setNom(rs.getString("nom"));
                act.setScore(rs.getDouble("score"));
                act.setMateriel(rs.getString("materiel"));
                act.setType(rs.getString("type"));
                act.setUtil_max(rs.getDouble("util_max"));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return act;
    }
    public TypeName cherchertypename(int id) {
        TypeName act = new TypeName();
        try {
            String requete = "SELECT * FROM type_name WHERE ID=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                act.setId(rs.getInt("id"));
                act.setAction_type_id(rs.getInt("action_type_id"));
                act.setNom(rs.getString("nom"));
                act.setScore(rs.getDouble("score"));
                act.setMateriel(rs.getString("materiel"));
                act.setType(rs.getString("type"));
                act.setUtil_max(rs.getDouble("util_max"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return act;
    }
}
