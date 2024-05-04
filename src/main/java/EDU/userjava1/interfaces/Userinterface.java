package EDU.userjava1.interfaces;
import EDU.userjava1.entities.User1;
import java.util.List;

public interface Userinterface {
    public void ajouteruser(User1 p );

    public List<User1> afficheruser();



    public User1 getbyid_user(int id);

    public void supprimeruser(int p) ;

    public void modifieruser(User1 p , int x) ;
    public int Login(String email,String password);



}
