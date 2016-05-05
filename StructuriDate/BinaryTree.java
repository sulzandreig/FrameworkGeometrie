/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuriDate;

import java.util.Objects;

/**
 *
 * @author Dragos-Alexandru
 * @param <T>
 */
public class BinaryTree<T extends Comparable>{
    NodTree<T> root;
    NodTree<T> aux;
    
    public void add(T newInfo){
        NodTree<T> newNod = new NodTree(newInfo);
        if(root == null){
            root = newNod;
        }else
            addRec(root,newNod);
        checkAvl(root);
    }
    
    private void addRec(NodTree<T> father, NodTree<T> newNod){
        if(newNod.compareTo(father) <= 0){
            if(father.left == null){
                father.weightL++;
                father.left = newNod;
                newNod.top = father;
            }else{
                addRec(father.left,newNod);
                father.weightL = Math.max(father.left.weightL, father.left.weightR) + 1;
            }
        }else{
            if(father.right == null){
                father.weightR++;
                father.right = newNod;
                newNod.top = father;
            }else{
                addRec(father.right,newNod);
                father.weightR = Math.max(father.right.weightL, father.right.weightR) + 1;
            }
        }
    }
    
    private void checkAvl(NodTree<T> father){
        if(father.weightL - father.weightR <= -2){
            if(father.right.weightL - father.right.weightR < 0){
                simpleLeft(father);
            }else{
                doubleLeft(father);
            }
        }else if(father.weightL - father.weightR >= 2){
            if(father.left.weightL - father.left.weightR > 0){
                simpleRight(father);
            }else{
                doubleRight(father);
            }
        }
        if(father.left != null)
            checkAvl(father.left);
        if(father.right != null)
            checkAvl(father.right);
    }
    
    private void simpleLeft(NodTree<T> father){
        aux = father;
        father = father.right;
        aux.right = father.left;
        father.left = aux;
        if(aux.top!=null){
            if(aux.top.left == aux)
                aux.top.left = father;
            else
                aux.top.right = father;
        }else{
            root = father;
        }
        aux.weightR = father.weightL;
        father.weightL = Math.max(aux.weightL,aux.weightR) + 1;
        father.top = aux.top;
        aux.top = father;
    }

    private void simpleRight(NodTree<T> father){
        aux = father;
        father = father.left;
        aux.left = father.right;
        father.right = aux;
        if(aux.top!=null){
            if(aux.top.right == aux)
                aux.top.right = father;
            else
                aux.top.left = father;
        }else{
            root = father;
        }
        aux.weightL = father.weightR;
        father.weightR = Math.max(aux.weightL, aux.weightR) + 1;
        father.top = aux.top;
        aux.top = father;
    }
    
    private void doubleLeft(NodTree<T> father){
        simpleRight(father.right);
        simpleLeft(father);
    }
    
    private void doubleRight(NodTree<T> father){
        simpleLeft(father.left);
        simpleRight(father);
    }
    @Override
    public String toString() {
        return toStringRec(root.left) + " [" + root + "] " + toStringRec(root.right);
    }
    
    private String toStringRec(NodTree<T> curent){
        if(curent != null)
            return toStringRec(curent.left) + " " + curent + " " + toStringRec(curent.right);
        return "";
    }
    
    
}

class NodTree<T extends Comparable> implements Comparable<NodTree<T>>{

    NodTree<T> left;
    NodTree<T> right;
    NodTree<T> top;
    int weightL = 0;
    int weightR = 0;
    T info;
    public NodTree(T info){
        this.info = info;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.info);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodTree<?> other = (NodTree<?>) obj;
        return Objects.equals(this.info, other.info);
    }

    @Override
    public String toString() {
        return "("+weightL+")"+info+"("+weightR+")";
    }

    @Override
    public int compareTo(NodTree<T> o) {
        return info.compareTo(o.info);
    }
}