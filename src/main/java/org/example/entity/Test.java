package org.example.entity;

public class Test {
    private Integer id;
    private String question;
    private String reponse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Test() {
    }

    public Test(Integer id, String question, String reponse) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;

    }


}
