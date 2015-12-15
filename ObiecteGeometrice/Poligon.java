/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Dragos-Alexandru
 */
public class Poligon extends GeometricalObject{
    String name;
    LinkedList<Point> points;
    LinkedList<Point> tempPoints;
    LinkedList<Line> lines;
    Color color;
    //Begining for data to test weak triangulation 
    ArrayList<Triangle> triangles;
    
    //End of data 
    public Poligon(LinkedList<Point> points) throws CloneNotSupportedException{
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
        for(int left = 0;  left < tempPoints.size();  left++){
            Triangle temp = new Triangle(tempPoints.get(i),tempPoints.get(j),tempPoints.get(k));
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
        triangles.add(new Triangle(tempPoints.get(i),tempPoints.get(j),tempPoints.get(k)));
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
    
}
