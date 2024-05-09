package org.example.entity;

public class Reponse {
    private int id;
    private int question_id;
    private String contenu;
    public void setId(int id){this.id=id;}
    public int getId(){return id;}
    public int getQuestion_id(){return  question_id;}
    public void setQuestion_id(int question_id) {this.question_id = question_id;}
    public String getContenu() {return contenu;}
    public void setContenu(String contenu) {this.contenu = contenu;}
    public Reponse(){}
    public Reponse(String contenu){
        this.contenu=contenu;
    }
}