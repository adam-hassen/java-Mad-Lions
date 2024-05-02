package EDU.userjava1.entities;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Reclamation {

    private int id;
    private int user1_id;
    private String userName; // Ajout du nom de l'utilisateur

    private String message;
    private Date date;
    private String type;
    private String reponse;

    public Reclamation(int user1_id, String message, String type) {
        this.user1_id = user1_id;
        this.message = message;
        this.type = type;
        this.date = new Date(); // Date actuelle
    }
    public Reclamation() {
        // Constructeur par défaut
    }
    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(int userId) {
        this.user1_id = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
