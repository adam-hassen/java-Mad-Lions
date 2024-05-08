package Recyclage.entities;

import EDU.userjava1.entities.User1;

import java.util.Date;

public class ProduitRecyclable {
    private int id;
    private String nom;
    private String description;
    private int quantite;
    private String type;
    private Date dateDepot;
    private EcoDepot ecoDepot;
    private User1 user;
    private long ecodepot_id;
    private long UserID;

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public ProduitRecyclable(String nom, String description, int quantite, String type, Date dateDepot, EcoDepot ecoDepot, User1 user) {
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.type = type;
        this.dateDepot = dateDepot;
        this.ecoDepot = ecoDepot;
        this.ecodepot_id = ecoDepot.getId(); // Assurez-vous que ecoDepot est déjà initialisé
        this.user = user;
        this.UserID = user.getId();
    }

    public ProduitRecyclable() {}

    public User1 getUser() {
        return user;
    }

    public void setUser(User1 user) {
        this.user = user;
    }
// Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(Date dateDepot) {
        this.dateDepot = dateDepot;
    }

    public EcoDepot getEcoDepot() {
        return ecoDepot;
    }

    public void setEcoDepot(EcoDepot ecoDepot) {
        this.ecoDepot = ecoDepot;
        this.ecodepot_id = ecoDepot.getId();
    }

    public long getEcodepot_id() {
        return ecodepot_id;
    }

    public void setEcodepot_id(long ecodepot_id) {
        this.ecodepot_id = ecodepot_id;
    }

    @Override
    public String toString() {
        return "ProduitRecyclable{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", quantite=" + quantite +
                ", type='" + type + '\'' +
                ", dateDepot=" + dateDepot +
                ", ecoDepot=" + ecoDepot +
                ", ecodepot_id=" + ecodepot_id +
                '}';
    }
}
