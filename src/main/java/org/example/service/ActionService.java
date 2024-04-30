package org.example.service;

import javafx.scene.control.Alert;
import org.example.Connexion.connexion;
import org.example.entity.Action;
import org.example.entity.TypeName;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionService {
    Connection cn;

    public ActionService() {
        cn = connexion.getInstance().getCn();
    }
    public void ajouterAction(Action act){
        try {
            String requete = "INSERT INTO ACTION (type_id,user_id,quantite,date,description,quantite_time,action_score,niveau_danger) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,act.getType_id().getId());
            pst.setInt(2,act.getUser_id());
            pst.setDouble(3, act.getQuantite());
            pst.setDate(4,Date.valueOf(act.getDate()));
            pst.setString(5, act.getDescription());
            pst.setString(6, act.getQuantite_time());
            pst.setDouble(7, act.getAction_score());
            pst.setInt(8,act.getNiveau_danger());
            pst.executeUpdate();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Action ajoutée avec succès!");
            successAlert.showAndWait();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Gestion De Consommation Alert!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Ajout echoué!");
            successAlert.showAndWait();
        }
    }

    public List<Action> afficherActions(int id) {
        List<Action> ListeAct = new ArrayList<>();
        try {
            String requete = "SELECT * FROM ACTION WHERE user_id=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Retrieve the data from the result set and create Action objects
                Action act = new Action();
                act.setId(rs.getInt("id"));
                int a = rs.getInt("type_id");
                TypeNameService typenameService = new TypeNameService();
                TypeName tp = typenameService.cherchertypename(a);
                act.setType_id(tp);
                act.setUser_id(rs.getInt("user_id"));
                act.setQuantite(rs.getDouble("quantite"));
                act.setAction_score(rs.getDouble("action_score"));
                act.setNiveau_danger(rs.getInt("niveau_danger"));
                act.setLocation_id(rs.getInt("location_id"));
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                ListeAct.add(act);
                System.out.println(tp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ListeAct;
    }
    public void supprimerAction(int id){
        try {
            String requete = "DELETE FROM ACTION WHERE ID = ?";
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

    public void modifierAction(int id,Action act){
        try {
            String requete = "UPDATE ACTION SET type_id=?, user_id=?, quantite=?" +
                    ", date=STR_TO_DATE(?, '%Y-%m-%d'), description=?, action_score=?" +
                    ", quantite_time=TIME_FORMAT(?, '%H:%i:%s'), niveau_danger=?" +
                    " WHERE ID = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,act.getType_id().getId());
            pst.setInt(2,act.getUser_id());
            pst.setDouble(3,act.getQuantite());
            pst.setDate(4, Date.valueOf(act.getDate()));
            pst.setString(5,act.getDescription());
            pst.setDouble(6,act.getAction_score());
            pst.setString(7,act.getQuantite_time());
            pst.setInt(8,act.getNiveau_danger());
            pst.setInt(9,id);
            int row = pst.executeUpdate();
            if (row > 0) System.out.println("Modify succeed");
            else System.out.println("Modify Failed Probably infound ID");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public class ChartData {
        private List<Double> data;
        private List<String> labels;

        public ChartData(List<Double> data, List<String> labels) {
            this.data = data;
            this.labels = labels;
        }

        public List<Double> getData() {
            return data;
        }

        public List<String> getLabels() {
            return labels;
        }
    }

    public ChartData firstChart(int id) {
        LocalDate currentDate = LocalDate.now();
        List<Action> actions = this.afficherActions(id);

        Map<String, Double> sums = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Action action : actions) {
            String type = action.getType_id().getNom();
            double score = action.getAction_score();

            if (!sums.containsKey(type)) {
                sums.put(type, 0.0);
            }

            if (action.getDate().isEqual(currentDate)) {
                sums.put(type, sums.get(type) + score);
                LocalDate date = action.getDate();
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                labels.add(formattedDate);
            }

            data.add(action.getAction_score());
        }

        List<Double> data2 = new ArrayList<>(sums.values());
        List<String> labels2 = new ArrayList<>(labels);
        return new ChartData(data2, labels2);
    }
    public ChartData scatterchart(int id) {
        LocalDate currentDate = LocalDate.now();
        LocalDate tenDaysEarlier = currentDate.minusDays(10); // 10 days earlier

        List<Action> actions = this.afficherActions(id);

        Map<String, Double> sums = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Action action : actions) {
            String type = action.getType_id().getNom();
            double score = action.getAction_score();
            LocalDate date = action.getDate();

            if (!sums.containsKey(type)) {
                sums.put(type, 0.0);
            }

            if (date.isAfter(tenDaysEarlier) && date.isBefore(currentDate.plusDays(1))) {
                sums.put(type, sums.get(type) + score);
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                labels.add(formattedDate);
            }

            data.add(score);
        }

        List<Double> data2 = new ArrayList<>(sums.values());
        List<String> labels2 = new ArrayList<>(labels);
        return new ChartData(data2, labels2);
    }

    public Action calculerScoreEtDanger(Action act){
        Double score;
        if (act.getQuantite() > 0.0) {
            score = act.getType_id().getScore() * act.getQuantite();
            act.setAction_score(score);
            System.out.println(act.getAction_score() + "sssss" + score);
           // System.out.println("\n score quantite =  " + score + "\n");
        }
        else {
            Double typescore = act.getType_id().getScore();
            String quantiteTimeStr = act.getQuantite_time();
            LocalTime quantiteTime = LocalTime.parse(quantiteTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            int time = quantiteTime.getHour() * 3600 + quantiteTime.getMinute() * 60 + quantiteTime.getSecond();
            score = typescore * time;
            act.setAction_score(score);
        }
        act.setNiveau_danger(1);
        if (score < (2 * act.getType_id().getUtil_max() / 5) && score >= (act.getType_id().getUtil_max() / 5)) {
            act.setNiveau_danger(2);
        } else if (score < (3 * act.getType_id().getUtil_max() / 5) && score >= (2 * act.getType_id().getUtil_max() / 5)) {
            act.setNiveau_danger(3);
        } else if (score < (4 * act.getType_id().getUtil_max() / 5) && score >= (3 * act.getType_id().getUtil_max() / 5)) {
            act.setNiveau_danger(4);
        } else if (score < act.getType_id().getUtil_max() && score >= (4 * act.getType_id().getUtil_max() / 5)) {
            act.setNiveau_danger(5);
        } else if (score == act.getType_id().getUtil_max()) {
            act.setNiveau_danger(6);
        }
        return act;
    }
    public Action checrherAction(int id){
        Action act = new Action();
        try {
            String requete = "SELECT * FROM ACTION WHERE ID=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                act.setId(rs.getInt("id"));
                int a = rs.getInt("type_id");
                TypeNameService typenameService = new TypeNameService();
                TypeName tp = typenameService.cherchertypename(a);
                act.setType_id(tp);
                act.setUser_id(rs.getInt("user_id"));
                act.setQuantite(rs.getDouble("quantite"));
                act.setAction_score(rs.getDouble("action_score"));
                act.setNiveau_danger(rs.getInt("niveau_danger"));
                act.setLocation_id(rs.getInt("location_id"));
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                LocalTime quantiteTime = rs.getTime("quantite_time").toLocalTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String quantiteTimeString = quantiteTime.format(formatter);
                act.setQuantite_time(quantiteTimeString);
                System.out.println(tp);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return act;
    }
}