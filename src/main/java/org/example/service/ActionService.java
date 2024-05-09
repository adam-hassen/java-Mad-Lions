package org.example.service;

import EDU.userjava1.entities.User1;
import javafx.scene.control.Alert;
import org.example.Connexion.connexion;
import org.example.entity.Action;
import org.example.entity.ActionLocation;
import org.example.entity.TypeName;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.stream.Collectors;

public class ActionService {
    Connection cn;

    public ActionService() {
        cn = connexion.getInstance().getCn();
    }
    public void ajouterAction(Action act){
        try {
            String requete = "INSERT INTO ACTION (type_id,user_id,quantite,date,description,quantite_time,action_score,niveau_danger,location_id) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,act.getType_id().getId());
            pst.setInt(2,act.getUser_id());
            pst.setDouble(3, act.getQuantite());
            pst.setDate(4,Date.valueOf(act.getDate()));
            pst.setString(5, act.getDescription());
            pst.setString(6, act.getQuantite_time());
            pst.setDouble(7, act.getAction_score());
            pst.setInt(8,act.getNiveau_danger());
            pst.setInt(9,27);
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
    public List<Action> afficherTous(){
        List<Action> ListeAct = new ArrayList<>();
        try {
            String requete = "SELECT * FROM ACTION ";
            Statement pst = cn.createStatement();
            ResultSet rs = pst.executeQuery(requete);

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
                int b = rs.getInt("location_id");
                ActionLocation loc = this.chercherLocation(b);
                act.setLocation_id(loc);
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                ListeAct.add(act);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ListeAct;
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
                int b = rs.getInt("location_id");
                ActionLocation loc = this.chercherLocation(b);
                act.setLocation_id(loc);
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                ListeAct.add(act);
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
                    ",location_id=? WHERE ID = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,act.getType_id().getId());
            pst.setInt(2,act.getUser_id());
            pst.setDouble(3,act.getQuantite());
            pst.setDate(4, Date.valueOf(act.getDate()));
            pst.setString(5,act.getDescription());
            pst.setDouble(6,act.getAction_score());
            pst.setString(7,act.getQuantite_time());
            pst.setInt(8,act.getNiveau_danger());
            pst.setInt(9,act.getLocation_id().getId());
            pst.setInt(10, act.getId());
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

            labels.add(type);

            if (action.getDate().isEqual(currentDate)) {
                sums.put(type, sums.get(type) + score);
            }

            data.add(action.getAction_score());
        }

        List<Double> data2 = new ArrayList<>(sums.values());
        List<String> labels2 = new ArrayList<>(labels);
        return new ChartData(data2, labels2);
    }
    public ChartData scatterchart(int id) {
        LocalDate currentDate = LocalDate.now();
        LocalDate tenDaysEarlier = currentDate.minusDays(15);

        List<Action> actions = this.afficherActions(id);

        Map<LocalDate, Double> dailyAverages = new HashMap<>();
        List<LocalDate> sortedDates = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Action action : actions) {
            double dangerLevel = action.getNiveau_danger();
            LocalDate date = action.getDate();

            if (date.isAfter(tenDaysEarlier) && date.isBefore(currentDate.plusDays(1))) {
                dailyAverages.merge(date, dangerLevel, (prev, current) -> (prev + current) / 2.0);
            }
        }

        sortedDates.addAll(dailyAverages.keySet());
        sortedDates.sort(Comparator.naturalOrder());

        for (LocalDate date : sortedDates) {
            data.add(dailyAverages.get(date));
        }

        List<String> labels = sortedDates.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collect(Collectors.toList());

        return new ChartData(data, labels);
    }

    public Action calculerScoreEtDanger(Action act) {
        Double score;
        if (act.getQuantite() > 0.0) {
            score = act.getType_id().getScore() * act.getQuantite();
            act.setAction_score(score);
        } else {
            Double typescore = act.getType_id().getScore();
            String quantiteTimeStr = act.getQuantite_time();
            LocalTime quantiteTime = LocalTime.parse(quantiteTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            int time = quantiteTime.toSecondOfDay();
            score = typescore * time;
            act.setAction_score(score);
        }

        if (score < (act.getType_id().getUtil_max() / 5)) {
            act.setNiveau_danger(1);
        } else if ((score < (2 * act.getType_id().getUtil_max() / 5)) && (score >= (act.getType_id().getUtil_max() / 5))) {
            act.setNiveau_danger(2);
        } else if ((score < (3 * act.getType_id().getUtil_max() / 5)) && (score >= (2 * act.getType_id().getUtil_max() / 5))) {
            act.setNiveau_danger(3);
        } else if ((score < (4 * act.getType_id().getUtil_max() / 5)) && (score >= (3 * act.getType_id().getUtil_max() / 5))) {
            act.setNiveau_danger(4);
        } else if ((score < act.getType_id().getUtil_max()) && (score >= ((4 * act.getType_id().getUtil_max()) / 5))) {
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
                act.setLocation_id(null);
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
    public void sendEmail(String recipient, String subject, String body) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("techwork414@gmail.com", "pacrvzlvscatwwkb");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("techwork414@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
    public double moyenneDanger(int id) {
        LocalDate today = LocalDate.now();
        double averageDangerLevel=0.0;
        try {
            String query = "SELECT AVG(niveau_danger) AS average_danger_level " +
                    "FROM ACTION " +
                    "WHERE user_id = ? AND date = ?";
            PreparedStatement preparedStatement = cn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, java.sql.Date.valueOf(today));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                averageDangerLevel = resultSet.getDouble("average_danger_level");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return averageDangerLevel;
    }
    public ActionLocation chercherLocation(int id){
        ActionLocation act = new ActionLocation();
        try {
            String requete = "SELECT * FROM action_location WHERE id=?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1,id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                act.setId(rs.getInt("id"));
                act.setAddress(rs.getString("address"));
                act.setNom(rs.getString("nom"));
                act.setLatitude(rs.getString("latitude"));
                act.setLongitude(rs.getString("longitude"));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return act;
    }
    public String ListeDanger(User1 user){
        List<Action> ls=afficherActions(user.getId());
        Map<String, List<Action>> actionsByType = new HashMap<>();

        for (Action action : ls) {
            String actionType = action.getType_id().getType();

            if (actionsByType.containsKey(actionType)) {
                actionsByType.get(actionType).add(action);
            } else {
                List<Action> actionList = new ArrayList<>();
                actionList.add(action);
                actionsByType.put(actionType, actionList);
            }
        }

        StringBuilder averageDangerString = new StringBuilder();
        int i=0;
        for (Map.Entry<String, List<Action>> entry : actionsByType.entrySet()) {
            String actionType = entry.getKey();
            List<Action> collectedActions = entry.getValue();

            double averageDanger = ControleDeDanger(collectedActions);
            if (averageDanger > 3) {
                i=1;
                averageDangerString.append("Action Type: ").append(actionType)
                        .append(", Moyenne Danger: ").append(averageDanger)
                        .append(System.lineSeparator());
            }
        }
        if (i==0){
            return null;
        }
        else {
            String averageDangerStringResult = averageDangerString.toString();
            return averageDangerStringResult;
        }
    }
    public  float ControleDeDanger(List<Action> actions) {
        float totalDanger = 0;
        int count = actions.size();

        for (Action action : actions) {
            totalDanger += action.getNiveau_danger();
        }

        if (count > 0) {
            float averageDanger = totalDanger / count;
            System.out.println("Moyenne de danger : " + averageDanger);
            return averageDanger;
        } else {
            return 0;
        }
    }
    public List<Action> afficherActionToday(int id) {
        List<Action> ListeAct = new ArrayList<>();
        try {
            String requete = "SELECT * FROM ACTION WHERE user_id=? AND DATE(date) = CURDATE()";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
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
                int b = rs.getInt("location_id");
                ActionLocation loc = this.chercherLocation(b);
                act.setLocation_id(loc);
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                ListeAct.add(act);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ListeAct;
    }
    public Action lastAction(int id) {
        try {
            String requete = "SELECT * FROM ACTION WHERE user_id=? ORDER BY id DESC LIMIT 1";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
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
                int b = rs.getInt("location_id");
                ActionLocation loc = this.chercherLocation(b);
                act.setLocation_id(loc);
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                return act;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        Action a = new Action();
        return a;
    }
    public List<Action> chercherByUser(String nom){
        List<Action> ListeAct = new ArrayList<>();
        try {
            String requete = "SELECT a.* FROM ACTION a " +
                    "JOIN USER1 u ON a.user_id = u.id " +
                    "WHERE u.username  = ?";
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1,  nom);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
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
                int b = rs.getInt("location_id");
                ActionLocation loc = this.chercherLocation(b);
                act.setLocation_id(loc);
                act.setDescription(rs.getString("description"));
                act.setDate(rs.getDate("date").toLocalDate());
                ListeAct.add(act);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ListeAct;
    }

}