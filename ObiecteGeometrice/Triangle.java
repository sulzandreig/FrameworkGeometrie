package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class Triangle extends GeometricalObject{
    String name;
    ArrayList<Point> points;
    ArrayList<Line> lines;
    Color color;
    double epsilon =1e-10;
    public Triangle(Point A, Point B, Point C){
        points = new ArrayList<>();
        lines = new ArrayList<>();
        name = A.name+B.name+C.name;
        color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        points.add(A);
        lines.add(new Line("AB", A, B));
        points.add(B);
        lines.add(new Line("BC", B, C));
        points.add(C);
        lines.add(new Line("CA", C, A));
        for(Point p:points){
            p.color = color;
        }
        for(Line l:lines){
            l.color = color;
        }
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
    @Override
    public String toString(){
        String s = "";
        s += "Triunghi "+name+" <"+points+">";
        return s;
    }
}
