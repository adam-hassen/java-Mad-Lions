package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class commandeController implements Initializable {
    @FXML
    private TableView<commande> command_tableview;

    @FXML
    private TableColumn<?, ?> commande_col_commandeID;

    @FXML
    private TableColumn<?, ?> commande_col_customerID;

    @FXML
    private TableColumn<?, ?> commande_col_date;

    @FXML
    private TableColumn<?, ?> commande_col_total;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    public ObservableList<commande> customersDataList() {

        ObservableList<commande> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM commande";
        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            commande cData;

            while (result.next()) {
                cData = new commande(result.getInt("id"),
                        result.getInt("user1_id"),
                        result.getDouble("montant_totale"),
                        result.getDate("date_commande"));

                listData.add(cData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<commande> commandeListData;

    public void commandeShowData() {
        commandeListData = customersDataList();
        commande_col_commandeID.setCellValueFactory(new PropertyValueFactory<>("id"));
        commande_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        commande_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        commande_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        command_tableview.setItems(commandeListData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandeShowData();
    }
}
