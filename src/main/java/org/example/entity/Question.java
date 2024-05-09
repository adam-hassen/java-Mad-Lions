package org.example.entity;

public class Question {
    int id;
    private int test_id;
    private  String contenu;
    public void setid(int id){this.id=id;}

    public int getId(){return id;}
    public void setTest_id(int test_id){this.test_id=test_id;}
    public int getTest_id(){return test_id; }
    public void setContenu(String contenu){this.contenu=contenu;}
    public String getContenu(){return  contenu;}
}