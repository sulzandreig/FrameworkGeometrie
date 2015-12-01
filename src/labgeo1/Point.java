/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labgeo1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Dragos-Alexandru
 */
public class Point extends GeometricalObject implements Comparable<Point>{
    private final static int USER_POINT_SIZE = 4;
    public final static int AXIS_POINT_X = 0;
    public final static int AXIS_POINT_Y = 1;
    public final static int AXIS_POINT_Z = 2;
    public final static int USER_POINT = 3;
    public boolean hoverdOver;
    public String name;
    public double x;
    public double y;
    public double z;
    public int tip;
    public Color color;
    public Point(String name,double x, double y, double z, int tip){
        this.x = x;
        this.y = -y;
        this.z = z;
        this.name = name;
        this.tip = tip;
        hoverdOver = false;
        if(tip == USER_POINT){
            color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        }else{
            color = Color.BLACK;
        }
    }
    
    @Override
    public void draw(Graphics2D graphics, int centrulX, int centrulY, int zoom, boolean drawName){
        Color previousColor  = graphics.getColor();
        graphics.setColor(color);
        if(tip == USER_POINT && drawName){
            graphics.drawString(name, ((int)(x*zoom+z/2*zoom))+centrulX - USER_POINT_SIZE/2, ((int)(y*zoom-z/2*zoom))+centrulY-USER_POINT_SIZE/2);
            graphics.fillOval(((int)(x*zoom+z/2*zoom))+centrulX - USER_POINT_SIZE/2, ((int)(y*zoom-z/2*zoom))+centrulY - USER_POINT_SIZE/2, USER_POINT_SIZE, USER_POINT_SIZE);
            
            //if hover over point, show coordonate lines
            /*if(hoverdOver){
                g.drawLine(((int)(x*zoom+z/2*zoom))+cX - USER_POINT_SIZE/2, ((int)(y*zoom-z/2*zoom))+cY - USER_POINT_SIZE/2, ((int)(x*zoom+z/2*zoom))+cX - USER_POINT_SIZE/2, cY);
            }*/
            
        }else if(tip == AXIS_POINT_X){
            graphics.fillOval(((int)(x*zoom+z/2*zoom))+centrulX - 1/2, ((int)(y*zoom-z/2*zoom))+centrulY - USER_POINT_SIZE/2, 1, 4);
        }else if(tip == AXIS_POINT_Y){
            graphics.fillOval(((int)(x*zoom+z/2*zoom))+centrulX - USER_POINT_SIZE/2, ((int)(y*zoom-z/2*zoom))+centrulY - 1/2, 4, 1);
        }else if(tip == AXIS_POINT_Z){
            graphics.fillOval(((int)(x*zoom+z/2*zoom))+centrulX - USER_POINT_SIZE/4, ((int)(y*zoom-z/2*zoom))+centrulY - USER_POINT_SIZE/4, 2, 2);
        }
        
        graphics.setColor(previousColor);
    }
    
    /**
     *  Primeste 3 puncte, si le testeaza coliniaritatea, returneaza intr-un hash
     * vectori folositi, alpha si boolean (daca sunt sau nu coliniari)
     * 
     * @param A Point
     * @param B Point
     * @param C Point
     * @return HashMap<String,Object> with:
     * vectors - ArrayList<VectorGeo>
     * alpha & beta - double
     * coliniare - boolean
     */
    public static HashMap<String, Object> Coliniare(Point A, Point B, Point C){
        HashMap<String, Object> hash = new HashMap<>();
        ArrayList<VectorGeo> vectors = new ArrayList<>();
        VectorGeo CB = new VectorGeo(C,B);
        VectorGeo CA = new VectorGeo(C,A);
        hash.put("vectors", vectors);
        System.out.println("Vectori sunt "+CB + " si "+ CA);
        vectors.add(CB);
        vectors.add(CA);
        if(A.equals(C)){
            if(A.equals(B)){
                System.out.println("Punctele coincid");
                hash.put("alpha", 1);
                hash.put("beta", 0);
                hash.put("coliniari", true);
            }else{
                System.out.println("Punctele sunt coliniare si "+A + "=1*"+C+" + 0*"+B);
                hash.put("alpha", 1);
                hash.put("beta", 0);
                hash.put("coliniari", true);
            }
            return hash;
        }else if(B.equals(C)){
            
            System.out.println("Punctele sunt coliniare si "+B + "=1*"+C+" + 0*"+A);
            hash.put("alpha", 1);
            hash.put("beta", 0);
            hash.put("coliniari", true);
            return hash;
        }else if(A.equals(B)){
            System.out.println("Punctele sunt coliniare si "+A + "=1*"+B+" + 0*"+C);
            hash.put("alpha", 1);
            hash.put("beta", 0);
            hash.put("coliniari", true);
            return hash;
        }else{
            double alpha, beta;
            alpha = CB.raport(CA);
            if(alpha != 0){
                beta = 1-alpha;
                System.out.println(CB.Y+" = "+alpha+"*"+CA.Y+" + "+beta+"*"+CB.X);
                hash.put("alpha", alpha);
                hash.put("beta", beta);
                hash.put("coliniari", true);
            }else{
                hash.put("coliniari", false);
                System.out.println("Nu sunt coliniare!");
            }
        }
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Point){
            Point objP = (Point)obj;
            return objP.x == this.x && objP.y == this.y && objP.z == this.z;
        }else{
            return false;
        }
    }
    
    public boolean stricEquals(Object obj){
        if(obj instanceof Point){
            Point objP = (Point)obj;
            return objP.x == this.x && objP.y == this.y && objP.z == this.z && objP.name.equals(name);
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }
    
    @Override
    public String toString(){
        return "("+x+","+(-y)+","+z+")";
    }

    @Override
    public int compareTo(Point o) {
        if(o.x == this.x)
            return (int)(this.y - o.y);
        return (int)(this.x - o.x);
    }
}
