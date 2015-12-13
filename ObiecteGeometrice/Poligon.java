/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Dragos-Alexandru
 */
public class Poligon extends GeometricalObject{
    String name;
    LinkedList<Point> points;
    LinkedList<Line> lines;
    Color color;
    //Begining for data to test weak triangulation 
    Triangle mTriangles[];
    Point [] mPoints;
    int remainingPoints;
    int triNr;
    
    //End of data 
    public Poligon(LinkedList<Point> points){
        this.points = points;
        points.add(points.get(0));
        color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        for(int i = 0; i<points.size()-1;i++){
            name = "" + points.get(i);
            lines.add(new Line(points.get(i).name+points.get(i+1), points.get(i), points.get(i+1)));
        }
        points.stream().forEach((p) -> {
           p.color = color;
        });
        lines.stream().forEach((l) -> {
            l.color = color;
        });
    }
    /*
        Added constructor that accepts a Point array and a size used for weak triangulation
    */
    public Poligon(Point[] points,int size){
        mTriangles = new Triangle[size];
        triNr = 0 ;
        mPoints = new Point[size+1];
        remainingPoints = size;
        for(int i = 1 ; i <= size ; i++)
            mPoints[i] =  new Point(points[i]);
    }
    /*
        This functions tells us if Point p[j] can be erased (if it's an ear and the triangle p[i],p[j],p[j] doesn't contain any other poligon point inside
        it
    */
    boolean canErase(int i, int j, int k){
        boolean ok = true;
        ok = mPoints[k].isLeftTurn(mPoints[i],mPoints[k]);
        for(int left = 1;  left <= remainingPoints;  left++){
            Triangle temp = new Triangle(mPoints[i],mPoints[j],mPoints[k]);
            if( left != i && left != j && left != k && temp.contains(mPoints[left]))
                ok = false;
        }
        return ok;
    }
    /*
        This function shifts with 1 position to the left all the points from
        position pos+1 to remainingPoints
    */
    void erasePoint(int pos){
        for(int i = pos ; i < remainingPoints ; i++)
            mPoints[i] = mPoints[i+1];
        remainingPoints--;
    }
    /*
        This function adds the triangle formed by the Points mPoints[i],
        mPonts[j],mPoints[k] to the return list of triangles
    */
    void addTriangle(int i, int j, int k){
        mTriangles[ ++ triNr] = new Triangle(mPoints[i],mPoints[j],mPoints[k]);
    }
    /*
        This function returns the triangles from the triangulation
    */
    public Triangle[] weakEarCuttingTriangulation(){
        
        while(remainingPoints > 3){
            for(int i = 1 ; i<= remainingPoints-2 && remainingPoints>3;){
                if(canErase(i,i+1,i+2)){
                   addTriangle(i,i+1,i+2);
                    erasePoint(i+1); 
                }
                else
                    i++;
            }
        }
         addTriangle(1,2,3);
        return mTriangles;
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
    
}
