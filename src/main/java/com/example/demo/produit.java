package com.example.demo;

public class produit {
    private int id;
    private String nom;
    private String description;
    private float prix;
    private int quantité_stock;
    private String categorie;
    private String image;

    public produit() {
    }

    public produit(int id, String nom, float prix, String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.image = image;
    }

    public produit(int id, String nom, String description, float prix, int quantité_stock, String categorie, String image) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantité_stock = quantité_stock;
        this.categorie = categorie;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getNom() {
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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantité_stock() {
        return quantité_stock;
    }

    public void setQuantité_stock(int quantité_stock) {
        this.quantité_stock = quantité_stock;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", quantité_stock=" + quantité_stock +
                ", categorie='" + categorie + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
