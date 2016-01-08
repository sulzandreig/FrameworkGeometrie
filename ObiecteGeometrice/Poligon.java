/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObiecteGeometrice;

import ProiectGeometrie.Drawable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author Dragos-Alexandru
 */
public class Poligon extends GeometricalObject implements Drawable{
    public String name;
    LinkedList<Point> points;
    LinkedList<Point> tempPoints;
    LinkedList<Line> lines;
    Color color;
    //Begining for data to test weak triangulation 
    ArrayList<Triangle> triangles;
    
    //End of data 
    public Poligon(LinkedList<Point> points) throws CloneNotSupportedException{
        if(!points.isEmpty())
            points.add(points.get(0));
        this.points = new LinkedList<>();
        for(Point p:points){
            this.points.add(p.clone());
        }
        triangles = new ArrayList<>();
        lines = new LinkedList<>();
        name = "";
        color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        for(int i = 0; i<points.size()-1;i++){
            name = name + points.get(i).name;
            lines.add(new Line(points.get(i).name+points.get(i+1).name, points.get(i), points.get(i+1)));
            points.get(i).linesMadeByThisPoint.add(lines.getLast());
            points.get(i+1).linesMadeByThisPoint.add(lines.getLast());
        }
        points.stream().forEach((p) -> {
           p.color = color;
        });
        lines.stream().forEach((l) -> {
            l.color = color;
        });
    }
    /*
        This functions tells us if Point p[j] can be erased (if it's an ear and the triangle p[i],p[j],p[j] doesn't contain any other poligon point inside
        it
    */
    private boolean canErase(int i, int j, int k){
        boolean ok;
        ok = tempPoints.get(k).isLeftTurn(tempPoints.get(i),tempPoints.get(j));
        Triangle temp = new Triangle(tempPoints.get(i),tempPoints.get(j),tempPoints.get(k));
        for(int left = 0;  left < tempPoints.size()-1;  left++){
            if( left != i && left != j && left != k && temp.contains(tempPoints.get(left)))
                ok = false;
        }
        return ok;
    }
    /*
        This function shifts with 1 position to the left all the points from
        position pos+1 to remainingPoints
    */
    private void erasePoint(int pos){
        tempPoints.remove(pos);
    }
    /*
        This function adds the triangle formed by the Points mPoints[i],
        mPonts[j],mPoints[k] to the return list of triangles
    */
    private void addTriangle(int i, int j, int k){
        Triangle t = new Triangle(tempPoints.get(i),tempPoints.get(j),tempPoints.get(k));
        triangles.add(t);
        tempPoints.get(i).triangles.add(t);
        tempPoints.get(j).triangles.add(t);
        tempPoints.get(k).triangles.add(t);
        //System.out.println(triangles.get(triangles.size()-1));
    }
    /*
        This function returns the triangles from the triangulation
    */
    public ArrayList<Triangle> weakEarCuttingTriangulation() throws CloneNotSupportedException{
        tempPoints = new LinkedList<>();
        for(Point p:points){
            tempPoints.add(p.clone());
        }
        
        while(tempPoints.size() > 4){
            for(int i = 0 ; i < tempPoints.size()-2 && tempPoints.size()>4;){
                //System.out.println(tempPoints.get(i)+" "+tempPoints.get(i+1)+" "+tempPoints.get(i+2)+" ");
                if(canErase(i,i+1,i+2)){
                   addTriangle(i,i+1,i+2);
                   erasePoint(i+1); 
                }
                else
                    i++;
            }
        }
        addTriangle(0,1,2);
        return triangles;
    }
    
    public Poligon getPointVisibility(Point p) throws CloneNotSupportedException{
        Poligon visibility;
        triangles.clear();
        ArrayList<Triangle> aux = new ArrayList<>();
        //Luam toate triunghiurile din urma triangulari
        triangles = weakEarCuttingTriangulation();
        Triangle originalTriangle = null;
        //Trecem print toate triunghiurile si vedem daca punctul este interior poligonului
        for(Triangle t:triangles){
            if(t.contains(p)){
                originalTriangle = t;
                break;
            }
        }
        //Daca este in interiorul poligonului
        if(originalTriangle != null){
            System.out.println("Punctul se afla in interiorul poligonului, mai exact in triunghiul "+originalTriangle);
            ArrayList<Triangle> toProcess;
            toProcess = originalTriangle.getAdiacentTriangles();
            createVisibility(aux, toProcess, p);
            LinkedList<Point> puncteVisibile = new LinkedList<>();
            for(Point punct:points){
                if(punct.isVisible)
                    puncteVisibile.add(punct);
            }
            visibility = new Poligon(puncteVisibile);
            visibility.setColor(Color.BLACK);
        }else{
            visibility = new Poligon(new LinkedList<>());
        }
        return visibility;
    }
    
    private void createVisibility(ArrayList<Triangle> foundTriangles, ArrayList<Triangle> toProcess, Point A) throws CloneNotSupportedException{
        ArrayList<Triangle> toAddToProcess = new ArrayList<>();
        //Luam toate triunghiurile ce trebuie procesate
        for(Triangle t:toProcess){
            //Daca triunghiul a fost procesat, nu facem nimic
            if(t.isProccesed){
                continue;
            }
            //Altfel
            t.isProccesed = true;
            System.out.println("Procesam: "+t);
            //Luam fiecare puncti din triunghi
            for(Point p:t.points){
                System.out.println("Luam punctul "+p);
                //Facem o linie intre punctul interior si punctul din triunghi
                Line aux = new Line(A.name+p.name, A, p);
                //Daca punctul nu a fost procesat
                if(!p.isProccesed){
                    //Tinem minte daca i s-a schimbat vizibilitatea
                    boolean changed = false;
                    //Trecem prin toate liniile poligonului
                    for(Line l:lines){
                        //Daca intersectia liniei create cu linia din poligon returneaza ceva
                        ArrayList<Point> intersectie = aux.intersects(l, true, p);
                        if(!intersectie.isEmpty()){
                            boolean intersectieValida = true;
                            for(Point punctIntersectie:intersectie){
                                if(punctIntersectie.equals(p))
                                    intersectieValida = false;
                            }
                            if(!intersectieValida)
                                break;
                            System.out.println(aux+" intersecteaza "+l);
                            /*Stim ca triunghiul nu e complet vizibil si ca 
                            vizibilitatea punctului s-a schimbat*/
                            changed = true;
                            //Pentru ca deja am gasit o linie a poligonului ce
                            //intersecteaza linia vizibilitatii nu trebuie sa mai 
                            //cautam
                            break;
                        }
                    }
                    //Punctul a fost deja procesat
                    p.isProccesed = true;
                    //Daca punctul nu s-a schimbat, inseamna ca e visibil
                    if(!changed)
                        p.isVisible = true;
                }
                if(p.isVisible){
                    //Daca punctul e vizibil il adaugam in lista de puncte vizibile
                    //ale triunghiului si setam si punctul din poligon sa fie vizibil
                    t.visiblePoints.add(p);
                    points.get(points.indexOf(p)).isVisible = true;
                }
                System.out.println("visible "+p.isVisible);
            }
            //Dupa ce am verificat vizibilitatea celor 3 puncte din triunghi
            //daca toate punctele sunt vizibile
            System.out.println(t + " are "+t.visiblePoints.size()+" puncte vizibile");
            if(t.visiblePoints.size()==3){
                //Adaugam triunghiul la triunghiurile complet vizibile
                foundTriangles.add(t);
                System.out.println(t + " este complet vizibil");
            }else{
                //Daca unu sau niciun punct nu este vizibil inseamna ca triunghi-ul
                //nu este deloc vizibil
                if(t.visiblePoints.size() <=1){
                    System.out.println(t + " nu este vizibil");
                }else{
                    //Daca 2 puncte sunt vizibile inseamna ca triunghiul este partial
                    //vizibil
                    System.out.println(t + " este partial vizibil");
                    //Parcurgem fiecare punct vizibil din triung
                    for(Point p:t.visiblePoints){
                        if(p.hasCreatedExtreme)
                            continue;
                        p.hasCreatedExtreme = true;
                        System.out.println("Creat extrema cu " + p);
                        //Cream o dreapta intre punctul interior si punctul vizibil
                        //din triunghi
                        Line lAux = new Line("lAux", A, p);
                        double auxX;
                        if(A.x < p.x){
                            auxX = 100;
                        }else{
                            auxX = -100;
                        }
                        //Gasim un punct apartinand dreptei create care se afla 
                        //in exteriorul poligonului
                        double auxY = lAux.getValueOf(auxX);
                        while(Double.isInfinite(auxY)){
                            System.out.println("auxX="+auxX+" auxY=" + auxY);
                            auxY = lAux.getValueOf(auxX++);
                        }
                        //Punctul de care vorbeam
                        Point extrema = new Point("EXTREM", auxX, auxY, 0, Point.USER_POINT);
                        //Verificam ca punctul respectiv sa apartina dreptei (de scos)
                        if(!lAux.contains(extrema)){
                            //Cream o linie intre punctul interior si punctul extrem
                            lAux = new Line("EXTREMA "+p.name, extrema, A);
                            lAux.setColor(Color.orange);
                            System.out.println(lAux);
                            //Structura ce va contine toate intersectiile dreptei
                            ArrayList<Pair<ArrayList<Point>,Line>> intersections = new ArrayList<>();
                            //Trecem prin toate liniile poligonului
                            for(Line line:lines){
                                //Trecem prin liniile punctului (liniile facute cu ajutorul lui)
                                //vizibil din triunghiul ce este procesat
                                for(Line l:p.linesMadeByThisPoint){
                                    if(!line.equals(l)){
                                        //Adaugam intersectiile
                                        intersections.add(new Pair<>(lAux.intersects(line, true, p), line));
                                    }
                                }
                            }
                            //Cea mai apropiata pereche (punct de intersectie)-linie fata
                            //de punctul interior
                            Pair<Point,Line> closest = null;
                            //Trecem print toate intersectiile
                            for(Pair<ArrayList<Point>,Line> pairAux:intersections){
                                //Trecem prin toate punctele acelor intersectii
                                //(daca gasim un segment ce se afla in interior liniei
                                //extreme, luam cel mai apropiat punct din acel segment
                                for(Point pointAux:pairAux.getKey()){
                                    if(closest == null){
                                        closest = new Pair(pointAux,pairAux.getValue());
                                    }else{
                                        //Folosind clasa vector aflam distanta de la 
                                        //punctul interior la un punct de intersectie
                                        //iar vectorul cu cea mai mica norma este creat
                                        //cu punctul cel mai apropiat
                                        if(new VectorGeo(A, closest.getKey()).norm() > new VectorGeo(A, pointAux).norm()
                                                && new VectorGeo(A,p).norm() < new VectorGeo(A, pointAux).norm()){
                                            closest = new Pair(pointAux,pairAux.getValue());
                                        }
                                    }
                                }
                            }
                            //Daca exista cel mai apropiat punct de intersectie
                            if(closest != null){
                                //Adaugam linia extrema sa apara pe ecran
                                //ProiectGeometrie.drawingBoard.lines.add((Line) lAux.clone());
                                //Trecem prin toate punctele poligonului si adaugam
                                //punctul de intersectie in poligon intre 2 puncte ce apartin
                                //deja poligonului
                                for(int i = 0; i<points.size()-1;i++){
                                    lAux = new Line(points.get(i).name+points.get(i+1), points.get(i), points.get(i+1));
                                    if(lAux.equals(closest.getValue())){
                                        System.out.println("Adaugat "+closest + " la indexul "+(i+1));
                                        //Setam noul punct adaugat ca fiind vizibil si procesat
                                        points.add(i+1, closest.getKey());
                                        points.get(i+1).isVisible = true;
                                        points.get(i+1).isProccesed = true;
                                        break;
                                    }
                                }
                                //Resetam toate punctele
                                for(Point pPrim:points){
                                    pPrim.linesMadeByThisPoint.clear();
                                }
                                //Golim lista de linii
                                lines.clear();
                                name = "";
                                //Cream noile linii ale poligonului de vizibilitate
                                for(int i = 0; i<points.size()-1;i++){
                                    if(i<points.size()-2 && points.get(i).name.equals(points.get(i+1).name)){
                                        points.remove(i);
                                        i--;
                                        continue;
                                    }
                                    name = name + points.get(i).name;
                                    lines.add(new Line(points.get(i).name+points.get(i+1).name,
                                            points.get(i), points.get(i+1)));
                                    points.get(i).linesMadeByThisPoint.add(lines.getLast());
                                    points.get(i+1).linesMadeByThisPoint.add(lines.getLast());
                                }
                                //Setam aceasi culoare pe toate liniile
                                lines.stream().forEach((l) -> {
                                    l.color = color;
                                });
                            }
                        }
                    }
                }
            }
            //Daca triunghiul are mai mult de un punct vizibil, adaugam triunghiurile adiacente
            //la procesare atat timp cat nu au fost adaugate deja 
            if(t.visiblePoints.size()>0){
                ArrayList<Triangle> aux = t.getAdiacentTriangles();
                for(Triangle tAdiacent:aux){
                    /*boolean found = false;
                    for(Triangle f:foundTriangles){
                        if(tAdiacent.equalsModificat(f)){
                            found = true;
                            break;
                        }
                    }*/
                    if(!tAdiacent.isProccesed){
                        toAddToProcess.add(tAdiacent);
                    }
                }
            }
        }
        toProcess.clear();
        toProcess.addAll(toAddToProcess);
        if(!toProcess.isEmpty())
            createVisibility(foundTriangles, toProcess, A);
    }
    
    @Override
    public void draw(Graphics2D graphics, int centrulX, int centrulY, int zoom, boolean drawName) {
        Color previousColor = graphics.getColor();
        graphics.setColor(color);
        if(drawName){
            graphics.drawString(name, ((int)(points.get(0).x*zoom+points.get(0).z/2*zoom))+centrulX - 2, ((int)(points.get(0).y*zoom-points.get(0).z/2*zoom))+centrulY-2);
        }
        lines.stream().forEach((line) -> {
            line.draw(graphics, centrulX, centrulY, zoom, false);
        });
        graphics.setColor(previousColor);
    }
    
    public ArrayList<Point> getPoints() throws CloneNotSupportedException{
        ArrayList<Point> copyPoints = new ArrayList<>();
        for(Point p:points){
            copyPoints.add(p.clone());
        }
        return copyPoints;
    }
    
    @Override
    public String toString(){
        String s= name+"\n";
        for(Line l:lines){
            s += l.toString();
        }
        return s;
    }
    
    public void setColor(Color color){
        this.color = color;
        for(Line l:lines){
            l.setColor(color);
        }
    }
}
