package ObiecteGeometrice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import ProiectGeometrie.ProiectGeometrie;

/**
 *
 * @author Dragos-Alexandru
 */
public class Quadrilateral extends GeometricalObject{
    String name;
    ArrayList<Point> points;
    ArrayList<Line> lines;
    Color color;
    public Quadrilateral(Point A, Point B, Point C, Point D){
        points = new ArrayList<>();
        lines = new ArrayList<>();
        name = A.name+B.name+C.name+D.name;
        color = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        points.add(A);
        lines.add(new Line("AB", A, B));
        points.add(B);
        lines.add(new Line("BC", B, C));
        points.add(C);
        lines.add(new Line("CD", C, D));
        points.add(D);
        lines.add(new Line("DA", D, A));
        for(Point p:points){
            p.color = color;
        }
        for(Line l:lines){
            l.color = color;
        }
    }
    
    public boolean isConvex(){
        
        boolean convex = true;
        for(Line line:lines){
            double rezultatEcDreapta = 1;
            for(Point point:points){
                if(!line.contains(point)){
                    rezultatEcDreapta *= line.getDeter(point);
                }
            }
            if(rezultatEcDreapta < 0){
                convex = false;
            }
        }
        
        return convex;
    }
    
    public void pointPositionRelativeToOuterCircle(int indexPoint){
        Point D = points.get(indexPoint);
        Point A = points.get(((indexPoint+1)%4));
        Point B = points.get(((indexPoint+2)%4));
        Point C = points.get(((indexPoint+3)%4));
        VectorGeo BA = new VectorGeo(B, A);
        VectorGeo BC = new VectorGeo(B, C);
        VectorGeo DA = new VectorGeo(D, A);
        VectorGeo DC = new VectorGeo(D, C);
        double masuraB = BA.angle(BC);
        double masuraD = DA.angle(DC);
        if(masuraB+masuraD == Math.PI){
            System.out.println("Punctul "+D+" apartine cercului circumscris");
        }else if(masuraB+masuraD < Math.PI){
            System.out.println("Punctul "+D+" apartine exteriorului cercului circumscris");
        }else{
            System.out.println("Punctul "+D+" apartine interiorului cercului circumscris");
        }
    }
    
    public boolean checkDistrict(){
        
        VectorGeo AB = new VectorGeo(points.get(0), points.get(1));
        VectorGeo CD = new VectorGeo(points.get(2), points.get(3));
        VectorGeo AD = new VectorGeo(points.get(0), points.get(3));
        VectorGeo BC = new VectorGeo(points.get(1), points.get(2));
        
        return AB.norm()+CD.norm() == AD.norm()+BC.norm();
    }
    
    public boolean containsInConvexCover(Point M){
        for(int i = 0; i < 2 ; i++){
            for(int j = i+1; j < 3 ; j++){
                for(int l = j+1 ; l<4 ;l++){
                    Triangle t = new Triangle(points.get(i), points.get(j), points.get(l));
                    double ariaTMare = t.getArea();
                    Triangle t1 = new Triangle(points.get(i), points.get(j), M);
                    Triangle t2 = new Triangle(points.get(l), points.get(j), M);
                    Triangle t3 = new Triangle(points.get(i), points.get(l), M);
                    if(ariaTMare == t1.getArea()+t2.getArea()+t3.getArea()){
                        ProiectGeometrie.drawingBoard.triangles.add(t1);
                        ProiectGeometrie.drawingBoard.triangles.add(t2);
                        ProiectGeometrie.drawingBoard.triangles.add(t3);
                        ProiectGeometrie.drawingBoard.points.add(M);
                        return true;
                    }
                }
            }
        }
        ProiectGeometrie.drawingBoard.quadrilaters.add(this);
        ProiectGeometrie.drawingBoard.points.add(M);
        return false;
    }
    
    @Override
    public void draw(Graphics2D graphics,int centrulX, int centrulY, int zoom, boolean drawName){
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
