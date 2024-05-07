package org.example.interfaces;

import java.util.List;

public interface WorkshopService <E>{
    public void ajouterWorkshop(E e);
    public  boolean supprimerWorkshop(E e);
    public boolean modifierWorkshop(E e,int id);
    public List<E> listeDesWorkshop();

}
