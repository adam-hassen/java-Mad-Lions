package EDU.userjava1.interfaces;

import EDU.userjava1.entities.Reclamation;
import EDU.userjava1.services.reclamationService;

import java.util.List;

public interface reclamationInterface {

    public void ajoutreclamation(Reclamation reclamation);

    public void repondreReclamation(int idReclamation, String reponse );
    public List<Reclamation> getAllReclamations();
}
