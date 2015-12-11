package StructuriDate;

import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author Dragos-Alexandru
 * @param <T>
 */
public class DoubleConnectedEdgeList<T extends Comparable> implements Iterable{
    NodDCEL<T> start;
    NodDCEL<T> finish;
    
    public DoubleConnectedEdgeList(){
        start = new NodDCEL<>(null);
        finish = new NodDCEL<>(null);
        start.next = finish;
        finish.previous = start;
    }
    
    public T popFirst(){
        NodDCEL<T> first = start.next;
        NodDCEL<T> second;
        if(first == finish){
            return null;
        }else{
            second = first.next;
            start.next = second;
            second.previous = start;
            first.next = null;
            first.previous = null;
            return first.info;
        }
    }
    
    public T popLast(){
        NodDCEL<T> last = finish.previous;
        NodDCEL<T> secondLast;
        if(last == start){
            return null;
        }else{
            secondLast = last.previous;
            finish.previous = secondLast;
            secondLast.next = finish;
            last.previous = null;
            last.next = null;
            return last.info;
        }
    }
    
    public void addFirst(T newInfo){
        NodDCEL<T> newNod = new NodDCEL(newInfo);
        if(start.next == finish){
            start.next = newNod;
            newNod.previous = start;
            newNod.next = finish;
            finish.previous = newNod;
        }else{
            start.next.previous = newNod;
            newNod.previous = start;
            newNod.next = start.next;
            start.next = newNod;
        }
    }
    
    public void addLast(T newInfo){
        NodDCEL<T> newNod = new NodDCEL(newInfo);
        if(finish.previous == start){
            start.next = newNod;
            newNod.previous = start;
            newNod.next = finish;
            finish.previous = newNod;
        }else{
            finish.previous.next = newNod;
            newNod.previous = finish.previous;
            newNod.next = finish;
            finish.previous = newNod;
        }
    }
    
    /**
     *  Adauga un nod in lista in functie de compareTo-ul clasei template
     * @param newInfo
     */
    public void add(T newInfo){
        NodDCEL<T> newNod = new NodDCEL(newInfo);
        NodDCEL<T> curent = start;
        NodDCEL<T> curent2 = curent.next;
        while(curent2 != finish && curent2.compareTo(newNod) >= 0){
            curent = curent.next;
            curent2 = curent2.next;
        }
        if(curent2 == finish){
            curent.next = newNod;
            newNod.next = finish;
            finish.previous = newNod;
            newNod.previous = curent;
        }else if(curent2.compareTo(newNod) < 0){
            curent.next = newNod;
            newNod.previous = curent;
            curent2.previous = newNod;
            newNod.next = curent2;
        }
    }

    @Override
    public String toString() {
        return "DCEL:" + toStringRec(start.next);
    }
    
    private String toStringRec(NodDCEL<T> curent){
        if(curent != finish){
            return curent +" "+ toStringRec(curent.next);
        }
        return "";
    }
    
    @Override
    public Iterator iterator() {
        return new NodIterator(start);
    }
}

class NodIterator<T extends Comparable> implements Iterator{
    NodDCEL<T> current;
    
    public NodIterator(NodDCEL<T> start){
        current = start;
    }
    
    @Override
    public boolean hasNext() {
        return current.next.info != null;
    }

    @Override
    public Object next(){
        return current.next.info;
    }    
}

class NodDCEL<T extends Comparable> implements Comparable<NodDCEL<T>>{

    NodDCEL<T> previous;
    NodDCEL<T> next;
    T info;
    public NodDCEL(T info){
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
        final NodDCEL<?> other = (NodDCEL<?>) obj;
        return Objects.equals(this.info, other.info);
    }

    @Override
    public String toString() {
        return ""+info;
    }

    @Override
    public int compareTo(NodDCEL<T> o) {
        return info.compareTo(o.info);
    }
}
