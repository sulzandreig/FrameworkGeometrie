/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObiecteGeometrice;

import ProiectGeometrie.Drawable;
import ProiectGeometrie.ProiectGeometrie;
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
    String name;
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
        triangles = weakEarCuttingTriangulation();
        boolean containsPoint = false;
        Triangle originalTriangle = null;
        for(Triangle t:triangles){
            if(t.contains(p)){
                containsPoint = true;
                originalTriangle = t;
                break;
            }
        }
        if(containsPoint){
            System.out.println("Punctul se afla in interiorul poligonului, mai exact in triunghiul "+originalTriangle);
            ArrayList<Triangle> toProcess;
            toProcess = originalTriangle.getAdiacentTriangles();
            findTriangles(aux, toProcess, p);
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
    
    private void findTriangles(ArrayList<Triangle> foundTriangles, ArrayList<Triangle> toProcess, Point A) throws CloneNotSupportedException{
        ArrayList<Triangle> toAddToProcess = new ArrayList<>();
        for(Triangle t:toProcess){
            if(t.isProccesed){
                continue;
            }
            t.isProccesed = true;
            System.out.println("Procesam: "+t);
            boolean intersecteazaCeva = false;
            int nrPointsVisible = 0;
            for(Point p:t.points){
                System.out.println("Luam punctul "+p);
                Line aux = new Line("aux", A, p);
                if(p.name.equals("B")){
                    boolean a = true;
                }
                if(!p.isProccesed){
                    boolean changed = false;
                    for(Line l:lines){
                        if(!aux.intersects(l,true).isEmpty()){
                            System.out.println(aux+" intersecteaza "+l);
                            intersecteazaCeva = true;
                            p.color = Color.BLACK;
                            changed = true;
                            p.isVisible = false;
                            break;
                        }
                    }
                    p.isProccesed = true;
                    if(!changed)
                        p.isVisible = true;
                }else{
                    if(!p.isVisible)
                        intersecteazaCeva = !p.isVisible;
                }
                if(p.isVisible){
                    t.visiblePoints.add(p);
                    points.get(points.indexOf(p)).isVisible = true;
                }
            }
            if(!intersecteazaCeva){
                foundTriangles.add(t);
                System.out.println(t + " este complet vizibil");
            }else{
                if(t.visiblePoints.size() <=1){
                    System.out.println(t + " nu este vizibil");
                }else{
                    System.out.println(t + " este partial vizibil");
                    for(Point p:t.visiblePoints){
                        Line lAux = new Line("lAux", A, p);
                        double auxX;
                        if(A.x < p.x){
                            auxX = 1000;
                        }else{
                            auxX = -1000;
                        }
                        double auxY = lAux.getValueOf(auxX);
                        Point extrema = new Point("EXTREM", auxX, auxY, 0, Point.USER_POINT);
                        if(lAux.contains(extrema)){
                            System.out.println("DA!");
                        }
                        lAux = new Line("lAux", A, extrema);
                        lAux.setColor(Color.orange);
                        ProiectGeometrie.drawingBoard.lines.add((Line) lAux.clone());
                        ArrayList<Pair<ArrayList<Point>,Line>> intersections = new ArrayList<>();
                        for(Line line:lines){
                            intersections.add(new Pair<>(lAux.intersects(line, true), line));
                            System.out.println(line);
                        }
                        Pair<Point,Line> closest = null;
                        for(Pair<ArrayList<Point>,Line> pairAux:intersections){
                            for(Point pointAux:pairAux.getKey()){
                                if(closest == null){
                                    closest = new Pair(pointAux,pairAux.getValue());
                                }else{
                                    if(new VectorGeo(A, closest.getKey()).norm() > new VectorGeo(A, pointAux).norm()
                                            && new VectorGeo(A,p).norm() < new VectorGeo(A, pointAux).norm()){
                                        closest = new Pair(pointAux,pairAux.getValue());
                                    }
                                }
                            }
                        }
                        if(closest != null){
                            for(int i = 0; i<points.size()-1;i++){
                                lAux = new Line(points.get(i).name+points.get(i+1), points.get(i), points.get(i+1));
                                if(lAux.equals(closest.getValue())){
                                    System.out.println("Adaugat "+closest + " la indexul "+(i+1));
                                    points.add(i+1, closest.getKey());
                                    points.get(i+1).isVisible = true;
                                    points.get(i+1).isProccesed = true;
                                    break;
                                }
                            }
                            lines.clear();
                            name = "";
                            for(int i = 0; i<points.size()-1;i++){
                                name = name + points.get(i).name;
                                lines.add(new Line(points.get(i).name+points.get(i+1).name, points.get(i), points.get(i+1)));
                            }
                            lines.stream().forEach((l) -> {
                                l.color = color;
                            });
                        }
                    }
                }
            }
            if(t.visiblePoints.size()>1){
                ArrayList<Triangle> aux = t.getAdiacentTriangles();
                for(Triangle a:aux){
                    boolean found = false;
                    for(Triangle f:foundTriangles){
                        if(a.equalsModificat(f)){
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        toAddToProcess.add(a);
                    }
                }
            }
        }
        toProcess.clear();
        toProcess.addAll(toAddToProcess);
        if(!toProcess.isEmpty())
            findTriangles(foundTriangles, toProcess, A);
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
