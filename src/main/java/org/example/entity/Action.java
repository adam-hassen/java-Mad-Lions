package org.example.entity;

import java.time.LocalDate;

public class Action {
    private int id;
    private TypeName type_id;
    private int user_id;
    private double quantite;
    private LocalDate date;
    private String description;
    private double action_score;
    private String quantite_time;
    private int niveau_danger;
    private ActionLocation location_id;

    public Action(TypeName type_id, double quantite, LocalDate date, String description, String quantite_time) {
        this.type_id=type_id;
        this.quantite = quantite;
        this.date = date;
        this.description = description;
        this.quantite_time = quantite_time;
    }

    public Action() {
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", type_id=" + type_id +
                ", user_id=" + user_id +
                ", quantite=" + quantite +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", action_score=" + action_score +
                ", quantite_time='" + quantite_time + '\'' +
                ", niveau_danger=" + niveau_danger +
                ", location_id=" + location_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeName getType_id() {
        return type_id;
    }

    public void setType_id(TypeName type_id) {
        this.type_id = type_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAction_score() {
        return action_score;
    }

    public void setAction_score(double action_score) {
        this.action_score = action_score;
    }

    public String getQuantite_time() {
        return quantite_time;
    }

    public void setQuantite_time(String quantite_time) {
        this.quantite_time = quantite_time;
    }

    public int getNiveau_danger() {
        return niveau_danger;
    }

    public void setNiveau_danger(int niveau_danger) {
        this.niveau_danger = niveau_danger;
    }

    public ActionLocation getLocation_id() {
        return location_id;
    }

    public void setLocation_id(ActionLocation location_id) {
        this.location_id = location_id;
    }
}
