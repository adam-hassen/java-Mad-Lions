package EDU.userjava1.interfaces;

import EDU.userjava1.entities.commentaire;

import java.util.List;

public interface CommentaireInterface {
    public void ajoutcommentaire(commentaire commentaire);
    public List<commentaire> getAllcommentaire();
}
