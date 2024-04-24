package EDU.userjava1.entities;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import java.util.Collections;
import javax.persistence.Convert;

public class User1 {
    private int id;
    private String prenom;
    private String name;
    private int numero;
    private String username;
    private String adress;
    private String password;
    private String genre;

    private String  roles;




    // constructeur par defaut
    public User1() {
    }
// constructeur with id


    public User1(  String username,  String password,String name,String adress,  int numero,  String genre ,String prenom) {

        this.prenom = prenom;
        this.name = name;
        this.numero = numero;
        this.username = username;
        this.adress = adress;
        this.password = password;
        this.genre = genre;
       // this.roles = roles;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int Numero) {
        this.numero = Numero;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public String getRoles() {
        return roles;
    }

    // Setter method for 'roles'
    public void setRoles(String roles) {
        this.roles = roles;
    }



    @Override
    public String toString() {
        return "User1{" + "id=" + id + ", prenom=" + prenom + ", name=" + name + ", numero=" + numero + ", username=" + username + ", adress=" + adress + ", password=" + password + ", genre=" + genre + ", roles=" + roles + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User1 user1 = (User1) o;
        return id == user1.id && numero == user1.numero && Objects.equals(prenom, user1.prenom) && Objects.equals(name, user1.name) && Objects.equals(username, user1.username) && Objects.equals(adress, user1.adress) && Objects.equals(password, user1.password) && Objects.equals(genre, user1.genre) && Objects.equals(roles, user1.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prenom, name, numero, username, adress, password, genre, roles);
    }
}
