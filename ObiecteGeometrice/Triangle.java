package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Objects;

public class Triangle extends GeometricalObject implements Cloneable{
    public String name;
    ArrayList<Point> points;
    ArrayList<Line> lines;
    public Color color;
    double epsilon =1e-10;
    public boolean isProccesed = false;
    public ArrayList<Point> visiblePoints;
    
    public Triangle(Point A, Point B, Point C){
        points = new ArrayList<>();
        lines = new ArrayList<>();
        visiblePoints = new ArrayList<>();
        name = A.name+B.name+C.name;
        color = Color.RED;
        points.add(A);
        points.add(B);
        points.add(C);
        lines.add(new Line("AB", A, B));
        lines.add(new Line("BC", B, C));
        lines.add(new Line("CA", C, A));
        points.stream().forEach((p) -> {
            p.color = color;
        });
        lines.stream().forEach((l) -> {
            l.color = color;
        });
    }
    
    public double getAngle(Point D){
        Point A = null, B = null ,C = null;
        for(Point P:points){
            if(P.equals(D)){
                B = P;
            }else{
                if(A == null)
                    A = P;
                else
                    C = P;
            }
        }
        VectorGeo BA = new VectorGeo(B, A);
        VectorGeo BC = new VectorGeo(B, C);
        double cosB = BA.angle(BC);
        return cosB;
    }
    
    public double getArea(){
        Line AB = new Line("d", points.get(0), points.get(1));
        double area = AB.getDeter(points.get(2));
        return Math.abs(area);
    }
    
    @Override
    public void draw(Graphics2D graphics,int centrulX, int centrulY, int zoom, boolean drawName){
        Color previousColor = graphics.getColor();
        graphics.setColor(color);
        if(drawName){
            int medieX = 0;
            int medieY = 0;
            int medieZ = 0;
            for(Point p:points){
                medieX += p.x;
                medieY += p.y;
                medieZ += p.z;
            }
            medieX /= points.size();
            medieY /= points.size();
            medieZ /= points.size();
            graphics.drawString(name, ((int)(medieX*zoom+medieZ/2*zoom))+centrulX - 2, ((int)(medieY*zoom-medieZ/2*zoom))+centrulY-2);
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
    public ArrayList<Line> getLines() throws CloneNotSupportedException{
        ArrayList<Line> copyLines = new ArrayList<>();
        for(Line l: lines){
            copyLines.add((Line) l.clone());
        }
        return copyLines;
    }
    /*
        Function contains tells us if the point recived as parameter is inside the triangle
    */
    public boolean contains(Point A)
    {
        double areaWithPointA = 0;
        Line AB = new Line("AB",points.get(0),points.get(1));
        areaWithPointA += Math.abs(AB.getDeter(A));
        Line BC = new Line("BC",points.get(1),points.get(2));
        areaWithPointA += Math.abs(BC.getDeter(A));
        Line AC = new Line("AC",points.get(0),points.get(2));
        areaWithPointA += Math.abs(AC.getDeter(A));
        return ( Math.abs(areaWithPointA - Math.abs(getArea())) <= epsilon);
    }
    
    public boolean adiacent(Triangle x){
        int aux = 0;
        for (Point point : x.points) {
            if (this.points.contains(point)) {
                aux++;
            }
        }
        return aux == 2;
    }
    
    public void unLink(){
        for(Line l:lines){
            l.triangles.remove(this);
        }
    }
    
    public void setColor(Color color){
        this.color = color;
        for(Line l:lines){
            l.color = color;
        }
    }
    
    public ArrayList<Triangle> getAdiacentTriangles(){
        ArrayList<Triangle> adiacentTriangles = new ArrayList<>();
        for(int i = 0; i < points.size(); i++){
            for(int j = 0; j < points.size(); j++){
                if(i!=j){
                    for(Triangle t1:points.get(i).triangles){
                        if(t1.equals(this))
                            continue;
                        for(Triangle t2:points.get(j).triangles){
                            if(t2.equals(this))
                                continue;
                            if(t1.equals(t2) && !adiacentTriangles.contains(t1))
                                adiacentTriangles.add(t2);
                        }
                    }
                }
            }
        }
        return adiacentTriangles;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.name);
        hash = 19 * hash + Objects.hashCode(this.points);
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
        final Triangle other = (Triangle) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.points, other.points)) {
            return false;
        }
        return true;
    }
    
    public boolean equalsModificat(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triangle other = (Triangle) obj;
        boolean different = false;
        for(Point p:points){
            if(!this.points.contains(p))
                different = true;
        }
        return different;
    }
    
    @Override
    public String toString(){
        String s = "";
        s += "Triunghi "+name+" <"+points+">";
        return s;
    }
    
    @Override
    public Triangle clone() throws CloneNotSupportedException{
        Triangle t = (Triangle) super.clone();
        return t;
    
    }
}
