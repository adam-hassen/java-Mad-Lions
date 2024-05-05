package org.example.controller;

import EDU.userjava1.controllers.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import org.example.entity.Action;
import org.example.service.ActionService;
import org.w3c.dom.events.Event;
import EDU.userjava1.entities.User1;
import EDU.userjava1.interfaces.MyListener;
import EDU.userjava1.interfaces.MyListener1;
import EDU.userjava1.services.UserServices;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class ChallContainers {
    @FXML
    public Text predictPlastique;
    @FXML
    public Text predictPlastiqueProp;
    @FXML
    public Text predictElectricity;
    @FXML
    public Text predictElectTime;
    @FXML
    public Text predictGazProp;
    @FXML
    public Text predictGaz;
    @FXML
    public Text predictCarburant;
    @FXML
    public Text predictCarburantProp;
    @FXML
    private HBox ChallContainer;
    private ActionService query;
    @FXML
    public void initialize() {
        query = new ActionService();
        List<Action> actions=query.afficherActionToday(Login.v.getId());
        LocalDate currentDate = LocalDate.now();
        List<Action> plast = new ArrayList<>();
        List<Action> gaz = new ArrayList<>();
        List<Action> carbu = new ArrayList<>();
        List<Action> elect = new ArrayList<>();

        for (Action action : actions) {
            LocalDate actionDate = action.getDate();
            long daysDifference = ChronoUnit.DAYS.between(actionDate, currentDate);
            if (daysDifference <= 5) {
                switch (action.getType_id().getType()) {
                    case "plastique":
                        plast.add(action);
                        break;
                    case "gaz":
                        gaz.add(action);
                        break;
                    case "carburant":
                        carbu.add(action);
                        break;
                    case "electrique":
                        elect.add(action);
                        break;
                    default:
                        break;
                }
            }
        }

        double predectPlastique = getTodaysObjective(plast);
        double predectCarburant = getTodaysObjective(carbu);
        double predectGaz = getTodaysObjective(gaz);
        double predectElectricite = getTodaysObjective(elect);

// Make percentage for bar
        double sumPlastique = 0;
        double sumCarburant = 0;
        double sumGaz = 0;
        double sumElectricite = 0;

        for (Action action : actions) {
                double score = action.getAction_score();
                String type = action.getType_id().getType();
                switch (type) {
                    case "plastique":
                        sumPlastique += score;
                        break;
                    case "carburant":
                        sumCarburant += score;
                        break;
                    case "gaz":
                        sumGaz += score;
                        break;
                    case "electricite":
                        sumElectricite += score;
                        break;
                }
        }

        double percentagePlastique = (sumPlastique / predectPlastique) * 100;
        double percentageCarburant = (sumCarburant / predectCarburant) * 100;
        double percentageGaz = (sumGaz / predectGaz) * 100;
        double percentageElectricite = (sumElectricite / predectElectricite) * 100;

// Convert to PC time
        double predectElectriciteProp = predectElectricite ;
        predectElectriciteProp = predectElectricite / 0.0028;
        java.util.Date predectElectriciteDate = new java.util.Date((long) predectElectriciteProp * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        String predectElectTime = sdf.format(predectElectriciteDate);
        predictElectTime.setText(predectElectTime);
        predictElectricity.setText(String.valueOf(predectElectricite));
// Convert to km de voiture
        double predectCarburantProp = predectCarburant;
        predectCarburantProp = predectCarburantProp / 0.03;
        String predectCarburantT = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date((long) predectCarburantProp * 1000));
        predictCarburant.setText(String.valueOf(predectCarburant));
        predictCarburantProp.setText(String.valueOf(predectCarburantT));

// Convert to cuisinier
        double predectGazP = predectGaz;
        System.out.println("predectGazP" +predectGazP);
        predectGazP = predectGazP / 0.05;
        System.out.println("predectGazProp" +predectGazP);
        String predectGazProp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date((long) predectGazP * 1000));
        System.out.println("predectGazProp" +predectGazProp);
        predictGaz.setText(String.valueOf(predectGaz));
        predictGazProp.setText(String.valueOf(predectGazProp));
// Convert to bouteille 1L
        double predectPlastiqueProp = predectPlastique;
        predectPlastiqueProp = predectPlastiqueProp / 1.5;
        predictPlastique.setText(String.valueOf(predectPlastique));
        predictPlastiqueProp.setText(String.valueOf(predectPlastiqueProp));
        //predictPlastique.setText(String.valueOf(predectPlastique));
        //predictPlastiqueProp.setText(String.valueOf(predectPlastiqueProp));
    }
    public void showChallenges(Pane challSpace){
        challSpace.getChildren().add(ChallContainer);
    }

    public  double getTodaysObjective(List<Action> actions) {
        double totalScore = 0;

        for (Action action : actions) {
            totalScore += action.getAction_score();
            System.out.println(action.getType_id().getType());
        }

        double averageScore = totalScore / actions.size();

        double reductionAmount = averageScore * 0.5;
        double rs = averageScore - reductionAmount;
        System.out.println("averageScore : " + averageScore);
        System.out.println("reductionAmount : " + reductionAmount);
        System.out.println("Moyenne de danger : " + (averageScore - reductionAmount) + " =>" + Math.round(averageScore - reductionAmount));
        return rs;
    }
}
