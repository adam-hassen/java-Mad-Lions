
package EDU.userjava1.tests;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //   MyConnection mc = new MyConnection();

       // UserServices  ps = new UserServices ();

       // User1 p = new User1("hayert@esprit.tnnn","hahaha","ncib","g",11111,"ff","ff");
       // ps.ajouteruser(p);

            reclamationService service = new reclamationService();

        // Récupérer toutes les réclamations
        List<Reclamation> reclamations = service.getAllReclamations();

        // Afficher les réclamations
        for (Reclamation reclamation : reclamations) {
            System.out.println("ID: " + reclamation.getId());
            System.out.println("Nom d'utilisateur: " + reclamation.getUserName());
            System.out.println("Message: " + reclamation.getMessage());
            System.out.println("Date: " + reclamation.getDate());
            System.out.println("Type: " + reclamation.getType());
            System.out.println("Réponse: " + (reclamation.getReponse() != null ? reclamation.getReponse() : ""));
            System.out.println("--------------------------------------");
        }
    }

        }