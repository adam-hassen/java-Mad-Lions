package Recyclage.entities;

import java.util.List;

public class EcoDepot {
private int id;
private String nom;
private String adresse;
private String type;
private int capacite_stockage;
private String statut_point_collecte;
private List<ProduitRecyclable> produitsRecyclables;

    public EcoDepot(){};
    public EcoDepot(String nom, String adresse, String type, int capacite_stockage, String statut_point_collecte) {
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
        this.capacite_stockage = capacite_stockage;
        this.statut_point_collecte = statut_point_collecte;
    }


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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacite_stockage() {
        return capacite_stockage;
    }

    public void setCapacite_stockage(int capacite_stockage) {
        this.capacite_stockage = capacite_stockage;
    }

    public String getStatut_point_collecte() {
        return statut_point_collecte;
    }

    public void setStatut_point_collecte(String statut_point_collecte) {
        this.statut_point_collecte = statut_point_collecte;
    }

    @Override
    public String toString() {
        return "EcoDepot{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", type='" + type + '\'' +
                ", capacite_stockage=" + capacite_stockage +
                ", statut_point_collecte='" + statut_point_collecte + '\'' +
                '}';
    }

}
