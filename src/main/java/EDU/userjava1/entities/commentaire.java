package EDU.userjava1.entities;

public class commentaire {
    private int id;
    private int user1_id;
    private String userName; // Ajout du nom de l'utilisateur

    private int conver_id;
    private String reponse;
    public commentaire(int user1_id, String reponse,int conver_id) {
        this.user1_id = user1_id;
        this.reponse = reponse;
    this.conver_id=conver_id;
    }
    public commentaire() {
        // Constructeur par défaut
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(int user1_id) {
        this.user1_id = user1_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConver_id() {
        return conver_id;
    }

    public void setConver_id(int conver_id) {
        this.conver_id = conver_id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
