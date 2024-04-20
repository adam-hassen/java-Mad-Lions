package Recyclage.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url="jdbc:mysql://localhost:3306/ecogardienintegrationfinale";
    private String login="root";
    private String pwd="";
    Connection cnx;
    public static MyConnection insatance;
    private MyConnection(){
        try {
            cnx= DriverManager.getConnection(url,login,pwd);
            System.out.println("Connection etablie.....");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInsatance() {
        if(insatance==null){
            insatance=new MyConnection();
        }
        return insatance;
    }
}
