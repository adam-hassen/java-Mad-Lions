package org.example.entity;

public class Test {
    private Integer id;
    private Integer id_workshop;
    private Integer workshopId;
    private  Integer reponse_correct;
    private Integer score;
    Question question_1;
    Reponse reponse_1[]=new Reponse[3];
    Question question_2;
    Reponse reponse_2[]=new Reponse[3];
    Question question_3;
    Reponse reponse_3[]=new Reponse[3];
    public Integer getId() {
        return id;
    }
    public Integer getReponse_correct(){return reponse_correct;}
    public void setReponse_correct(Integer id_reponse){reponse_correct=id_reponse;}
    public  Integer getScore(){return  score;}
    public void setScore(Integer score){this.score=score;}
    public  Integer getId_workshop(){return  id_workshop;}
    public void SetId_workshop(Integer id_workshop){
        this.id_workshop=id_workshop;
    }
    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestion_1() {
        return question_1;
    }

    public void setQuestion_1(Question question_1) {
        this.question_1 = question_1;
    }

    public Reponse[] getReponse_1() {
        return reponse_1;
    }

    public void setReponse_1(Reponse[] reponse_1) {
        this.reponse_1 = reponse_1;
    }

    public Question getQuestion_2() {
        return question_2;
    }

    public void setQuestion_2(Question question_2) {
        this.question_2 = question_2;
    }

    public Reponse[] getReponse_2() {
        return reponse_2;
    }

    public void setReponse_2(Reponse[] reponse_2) {
        this.reponse_2 = reponse_2;
    }

    public Question getQuestion_3() {
        return question_3;
    }

    public void setQuestion_3(Question question_3) {
        this.question_3 = question_3;
    }

    public Reponse[] getReponse_3() {
        return reponse_3;
    }

    public void setReponse_3(Reponse[] reponse_3) {
        this.reponse_3 = reponse_3;
    }


    public Test() {
    }




}
