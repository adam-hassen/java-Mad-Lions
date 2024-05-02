package org.example.entity;

public class ActionLocation {
    private int id;
    private String nom;
    private String address;
    private String latitude;
    private String  longitude;

    public ActionLocation(int id, String nom, String address, String latitude, String longitude) {
        this.id = id;
        this.nom = nom;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "ActionLocation{" +
                "nom='" + nom + '\'' +
                '}';
    }

    public ActionLocation() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
