package EDU.userjava1.entities;

import java.util.Date;

public class Conver {
    private int id;
    private int user1_id;
    private String userName; // Ajout du nom de l'utilisateur

    private String publication;
    public Conver(int user1_id, String publication) {
        this.user1_id = user1_id;
        this.publication = publication;

    }
    public Conver() {
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

    public void setUser1_id(int user1_id) {
        this.user1_id = user1_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }
}
