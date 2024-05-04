package org.example.entity;

public class TypeName {
    private int id;
    private int action_type_id;
    private String nom;
    private double score;
    private String materiel;
    private String type;
    private double util_max;

    public TypeName() {
    }

    public TypeName(int action_type_id, String nom, double score, String materiel, String type, double util_max) {
        this.action_type_id = action_type_id;
        this.nom = nom;
        this.score = score;
        this.materiel = materiel;
        this.type = type;
        this.util_max = util_max;
    }

    @Override
    public String toString() {
        return  nom ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction_type_id() {
        return action_type_id;
    }

    public void setAction_type_id(int action_type_id) {
        this.action_type_id = action_type_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getMateriel() {
        return materiel;
    }

    public void setMateriel(String materiel) {
        this.materiel = materiel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getUtil_max() {
        return util_max;
    }

    public void setUtil_max(double util_max) {
        this.util_max = util_max;
    }
}
