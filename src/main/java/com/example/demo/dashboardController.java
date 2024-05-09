package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private StackedBarChart<?, ?> dashboard_incomeChart;
    @FXML
    private AnchorPane dashboard_form;
    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    public void dashboardDisplayNC() {

        String sql = "SELECT COUNT(user1_id) FROM commande";
        connect = database.connectDB();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(user1_id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayTI() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(montant_totale) FROM commande WHERE date_commande = '"
                + sqlDate + "'";

        connect = database.connectDB();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble("SUM(montant_totale)");
            }

            dashboard_TI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalI() {
        String sql = "SELECT SUM(montant_totale) FROM commande";

        connect = database.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(montant_totale)");
            }
            dashboard_TotalI.setText("$" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNSP() {

        String sql = "SELECT COUNT(quantity) FROM produit_command";

        connect = database.connectDB();

        try {
            int q = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt("COUNT(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date_commande, SUM(montant_totale) FROM commande GROUP BY date_commande ORDER BY TIMESTAMP(date_commande)";
        connect = database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboardDisplayNC();
        dashboardDisplayTI();
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();

    }
}
