package Recyclage.interfaces;

import java.util.List;

public interface ProduitRecyclableService <E>{
    public void ajouterProduit(E e);
    public  boolean supprimerProduit(E e);
    public boolean modifierProduit(E e,int id);
    public List<E> listeDesProduits(int id);
}
