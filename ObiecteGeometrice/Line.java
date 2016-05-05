package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
import ProiectGeometrie.ProiectGeometrie;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Dragos-Alexandru
 */
public class Line extends GeometricalObject implements Cloneable{
    String name;
    Point X,Y;
    double[][] matrix;
    public ArrayList<Triangle> triangles;
    Color color;
    private static int nr = 0;
    int thisNr;
    public Line(String name, Point X, Point Y){
        matrix = new double[3][];
        matrix[0] = new double[3];
        matrix[1] = new double[3];
        matrix[2] = new double[3];
        this.name = name;
        this.thisNr = nr;
        nr++;
        color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        this.X = X;
        this.X.color = color;
        this.Y = Y;
        this.Y.color = color;
        matrix[0][0] = 0;
        matrix[0][1] = 0;
        matrix[0][2] = 1;
        matrix[1][0] = X.x;
        matrix[1][1] = X.y;
        matrix[1][2] = 1;
        matrix[2][0] = Y.x;
        matrix[2][1] = Y.y;
        matrix[2][2] = 1;
    }
    
    /**
     *  <p>Primeste o alta linie si rezolva sistemul intersectie lor</p>
     * <p>Momentan functia adauga si intersectia in drawingBoard (daca exista)</p>
     * @param L
     * @param ignoreOriginalPointsAndLines
     * @return
     */
    public ArrayList<Point> intersects(Line L, boolean ignoreOriginalPointsAndLines, Point originalPoint){
        System.out.println("Intersectia lui "+this+" cu "+L);
        ArrayList<Point> intersections = new ArrayList<>();
        double[][] mSys = new double[2][];
        mSys[0] = new double[3];
        mSys[1] = new double[3];
        mSys[0][0] = this.X.y-this.Y.y;
        mSys[0][1] = this.X.x-this.Y.x;
        mSys[0][2] = this.X.x*this.Y.y-this.Y.x*this.X.y;
        mSys[1][0] = L.X.y-L.Y.y;
        mSys[1][1] = L.X.x-L.Y.x;
        mSys[1][2] = L.X.x*L.Y.y-L.Y.x*L.X.y;
        if(mSys[0][0]*mSys[1][1]-mSys[0][1]*mSys[1][0] != 0){
            double[][] det1 = getMatrix2(-mSys[0][2], mSys[0][1], -mSys[1][2], mSys[1][1]);
            double[][] det2 = getMatrix2(mSys[0][0], -mSys[0][2], mSys[1][0], -mSys[1][2]);
            double[][] det3 = getMatrix2(mSys[0][0], mSys[0][1], mSys[1][0], mSys[1][1]);
            double x = calcDeter2(det1)/calcDeter2(det3);
            double y = calcDeter2(det2)/calcDeter2(det3);
            Point i = new Point(L.Y.name+"'",x,y,0,Point.USER_POINT);
            Point[] aP = new Point[5];
            aP[0] = this.X;
            aP[1] = this.Y;
            aP[2] = L.X;
            aP[3] = L.Y;
            aP[4] = i;
            Arrays.sort(aP,null);
            if(apartineSegmenului(L, i) && apartineSegmenului(this, i)){
                System.out.println("Punctul "+i+" este intersectia");
                if(ignoreOriginalPointsAndLines){
                    System.out.println(this + " si " +L + " contine pe "+i+"?");
                    if(this.contains(i) && L.contains(i)){
                        System.out.println("true");
                        return intersections;
                    }
                    System.out.println("false");
                    for(Line lAux:originalPoint.linesMadeByThisPoint){
                        System.out.println("Linia "+lAux+" contine punctul "+i+"?");
                        if(lAux.isOnSegment(i)){
                            System.out.println("true");
                            return intersections;
                        }
                        System.out.println("false");
                    }
                }
                System.out.println(this + " se intersecteaza in "+ i + " cu " + L);
                //ProiectGeometrie.drawingBoard.points.add(new Point("Inter "+X.name+Y.name, x,y,0,Point.USER_POINT));
                intersections.add(i);
                return intersections;
            }else{
                System.out.println(this + " nu se intersecteaza cu " + L);
                return intersections;
            }
        }else{
            if(mSys[0][1]*mSys[1][2]-mSys[0][2]*mSys[1][1] != 0){
                System.out.println("Segmentele sunt paralele");
                return intersections;
            }else{
                Point[] aP = new Point[4];
                aP[0] = this.X;
                aP[1] = this.Y;
                aP[2] = L.X;
                aP[3] = L.Y;
                Arrays.sort(aP,null);
                for(Point p:aP){
                    System.out.println(p);
                }
                if(ignoreOriginalPointsAndLines){
                    return intersections;
                }
                if(apartineSegmenului(L, X) && apartineSegmenului(L, Y)){
                    System.out.println("Intersectia segmentelor este "+X + " si "+Y);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+X+Y,X,Y));
                    intersections.add(X);
                    intersections.add(Y);
                    return intersections;
                }else if(apartineSegmenului(this, L.X) && apartineSegmenului(this, L.Y)){
                    System.out.println("Intersectia segmentelor este "+L.X + " si "+L.Y);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+L.X+L.Y,L.X,L.Y));
                    intersections.add(L.X);
                    intersections.add(L.Y);
                    return intersections;
                }else if(apartineSegmenului(L, X) && !apartineSegmenului(L, Y) && apartineSegmenului(this, L.X)){
                    System.out.println("Intersectia segmentelor este "+X + " si "+L.X);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+X+L.X,X,L.X));
                    intersections.add(X);
                    intersections.add(L.X);
                    return intersections;
                }else if(apartineSegmenului(L, X) && !apartineSegmenului(L, Y) && apartineSegmenului(this, L.Y)){
                    System.out.println("Intersectia segmentelor este "+X + " si "+L.Y);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+X+L.Y,X,L.Y));
                    intersections.add(X);
                    intersections.add(L.Y);
                    return intersections;
                }else if(!apartineSegmenului(L, X) && apartineSegmenului(L, Y) && apartineSegmenului(this, L.X)){
                    System.out.println("Intersectia segmentelor este "+Y + " si "+L.X);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+Y+L.X,Y,L.X));
                    intersections.add(Y);
                    intersections.add(L.X);
                    return intersections;
                }else if(!apartineSegmenului(L, X) && apartineSegmenului(L, Y) && apartineSegmenului(this, L.Y)){
                    System.out.println("Intersectia segmentelor este "+Y + " si "+L.Y);
                    //ProiectGeometrie.drawingBoard.lines.add(new Line("Inter "+Y+L.Y,Y,L.Y));
                    intersections.add(Y);
                    intersections.add(L.Y);
                    return intersections;
                }
            }
        }
        System.out.println("Segmentele nu se intersecteaza");
        return intersections;
    }
    
    /**
     *  Verifica daca un punct apartine segmentului determinat de cele doua puncte
     * de referinta a liniei trimise ca parametru
     * @param l
     * @param p
     * @return
     */
    public static boolean apartineSegmenului(Line l, Point p){
        return Math.abs(l.X.x-p.x) + Math.abs(l.Y.x-p.x) == Math.abs(l.X.x - l.Y.x)
                && Math.abs(l.X.y-p.y) + Math.abs(l.Y.y-p.y) == Math.abs(l.X.y - l.Y.y);
    }
    
    /**
     * Matrix is like: |x1 x2|
     *                 |x3 x4|
     * @param x1
     * @param x2
     * @param x3
     * @param x4
     * @return
     */
    public double[][] getMatrix2(double x1, double x2, double x3, double x4){
        double [][] newMatrix = new double[2][];
        newMatrix[0] = new double[2];
        newMatrix[1] = new double[2];
        newMatrix[0][0] = x1;
        newMatrix[0][1] = x2;
        newMatrix[1][0] = x3;
        newMatrix[1][1] = x4;
        return newMatrix;
    }
    
    /**
     *  Calculeaza un determinant de gradul 3 prin regula triunghiului
     * @param matrix
     * @return
     */
    public double calcDeter3(double[][] matrix){
        return matrix[0][0]*matrix[1][1]*matrix[2][2]+
                matrix[0][1]*matrix[1][2]*matrix[2][0]+
                matrix[1][0]*matrix[2][1]*matrix[0][2]-
                matrix[0][2]*matrix[1][1]*matrix[2][0]-
                matrix[0][1]*matrix[1][0]*matrix[2][2]-
                matrix[1][2]*matrix[2][1]*matrix[0][0];
    }
    
    /**
     *  Calculeaza un determinant de gradul 2
     * @param matrix
     * @return
     */
    public double calcDeter2(double[][] matrix){
        return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];
    }
    
    /**
     *  Calculeaza determinantul ecuatie liniei in care x si y sunt inlocuiti cu
     * x-ul si y-ul punctului dat
     * @param Z
     * @return
     */
    public double getDeter(Point Z){
        double[][] matrixCopy = (double[][])matrix.clone();
        matrixCopy[0][0] = Z.x;
        matrixCopy[0][1] = Z.y;
        double deter = calcDeter3(matrixCopy);
        return deter;
    }
    
    /**
     *  Inlocuieste coordonatele punctului dat ca parametru in matricea ecuatiei
     * liniei si returneaza adevarat daca determinantul acestei matrici este egal
     * cu 0
     * @param Z
     * @return
     */
    public boolean isOnLine(Point Z){
        double deter = getDeter(Z);
        return deter == 0;
    }
    
    public boolean isOnSegment(Point Z){
        if(isOnLine(Z)){
            if(Math.abs(X.x-Z.x) + Math.abs(Z.x-Y.x) == Math.abs(X.x - Y.x)){
                return true;
            }
        }
        return false;
    }
    
    /**
     *  Verifica daca linia contine punctul dat in punctele ei suport
     * @param Z
     * @return
     */
    public boolean contains(Point Z){
        return Z.equals(X) || Z.equals(Y);
    }
    
    /**
     *  Returneaza valoarea abscisei ca ar avea-o punctul cu ordonata trimisa ca
     * parametru
     * @param x
     * @return
     */
    public double getValueOf(double x){
        double yValue;
        yValue = ((X.y-Y.y)/(X.x-Y.x))*(x-X.x) + X.y;
        if(Double.isNaN(yValue)){
            if(X.y < Y.y)
                yValue = Integer.MAX_VALUE;
            else
                yValue = Integer.MIN_VALUE;
        }
        return -yValue;
    }
    
    public void setColor(Color color){
        X.color = color;
        Y.color = color;
        this.color = color;
    }
    
    @Override
    public void draw(Graphics2D graphics,int centrulX, int centrulY, int zoom, boolean drawName){
        Color previousColor = graphics.getColor();
        graphics.setColor(color);
        if(drawName){
            graphics.drawString(name, ((int)(X.x*zoom+X.z/2*zoom))+centrulX - 2, ((int)(X.y*zoom-X.z/2*zoom))+centrulY-2);
        }
        graphics.drawLine(((int)(X.x*zoom+X.z/2*zoom))+centrulX, ((int)(X.y*zoom-X.z/2*zoom))+centrulY,
                ((int)(Y.x*zoom+Y.z/2*zoom))+centrulX, ((int)(Y.y*zoom-Y.z/2*zoom))+centrulY);
        X.draw(graphics, centrulX, centrulY, zoom, true);
        Y.draw(graphics, centrulX, centrulY, zoom, false);
        graphics.setColor(previousColor);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.X);
        hash = 41 * hash + Objects.hashCode(this.Y);
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
        final Line other = (Line) obj;
        if (!(Objects.equals(this.X, other.X) || Objects.equals(this.X, other.Y))) {
            return false;
        }
        return Objects.equals(this.Y, other.Y) || Objects.equals(this.Y, other.X);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
    
    @Override
    public String toString(){
        String s = "Line "+name;/*+"\n" +"|"+matrix[0][0]+" "+matrix[0][1]+" "+matrix[0][2]+"|\n"
                + "|"+matrix[1][0]+" "+matrix[1][1]+" "+matrix[1][2]+"|\n"
                + "|"+matrix[2][0]+" "+matrix[2][1]+" "+matrix[2][2]+"|\n";*/
        return s;
    }
}
