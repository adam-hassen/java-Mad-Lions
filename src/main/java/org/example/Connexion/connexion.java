package org.example.Connexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class connexion {
    private static final String URL = "jdbc:mysql://localhost:3306/ecogardienintegrationfinale";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    Connection cn;
    public static connexion instance;
    private connexion() {
        try {
            cn =  DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection succeed.");
        } catch (SQLException e) {
            System.out.println("Connection Failed \n, ERROR:" + e.getMessage());
        }
    }

    public Connection getCn() {
        return cn;
    }
    public static connexion getInstance() {
        if (instance == null ){
            instance = new connexion();
        }
        return instance;
    }
}
