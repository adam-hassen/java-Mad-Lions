package edu.evenements.entities;

import javafx.collections.ObservableList;

public class Partenaire {

    private int id;
    private String nom;
    private String email;
    private String type;
    private int evenement_id;
    private Evenements evenement;
    private Evenements evenementtitre;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEvenement_id() {
        return evenement_id;
    }

    public void setEvenement_id(Integer evenement_id) {
        this.evenement_id = evenement_id;
    }

    public Evenements getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenements evenement) {
        this.evenement = evenement;
    }

    public Evenements getEvenementtitre() {
        return evenementtitre;
    }

    public void setEvenementtitre(Evenements evenementtitre) {
        this.evenementtitre = evenementtitre;
    }
}
