package org.example.interfaces;

import org.example.entity.Test;

import java.util.List;

public interface testService<E>{
    public void addtest(E e);
    public  boolean supprimertest(E e);
    public boolean modifiertest(E e,int id);
    public List<E> listeDestest();


}
