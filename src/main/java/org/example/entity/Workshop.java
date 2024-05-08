package org.example.entity;
import java.time.LocalDate;
import java.time.LocalTime;

public class Workshop {
    private Integer id;
    private String nom;
    private String type;
    private LocalDate date;
    private LocalTime heure;
    private String cours;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Workshop() {
    }

    public Workshop(Integer id, String nom, String type, LocalDate date, LocalTime heure, String cours) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.heure = heure;
        this.cours = cours;
    }
    public Workshop( String nom, String type, LocalDate date, LocalTime heure, String cours) {
        this.nom = nom;
        this.type = type;
        this.date = date;
        this.heure = heure;
        this.cours = cours;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Workshop{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", cours='" + cours + '\'' +
                '}';
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }
}
