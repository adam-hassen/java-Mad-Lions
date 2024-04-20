package Recyclage.interfaces;

import java.util.List;

public interface EcodepotService<E> {
    public void ajouterEcodepot(E e);
    public void supprimerEcodepot(E e);
    public void modifierEcodepot(E e,int id);
    public List<E> listeDesEcodepots();
}
