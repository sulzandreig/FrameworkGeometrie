package ObiecteGeometrice;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Random;

public class VectorGeo extends GeometricalObject{
    public Point X;
    public Point Y;
    public Point value;
    public Color color;
    public VectorGeo(Point X, Point Y){
        this.X = X;
        this.Y = Y;
        value = new Point(X.name+Y.name,Y.x-X.x,Y.y-X.y,Y.z-X.z, Point.USER_POINT);
        Random r = new Random();
        color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }
    public double raport(VectorGeo temp){
        double alpha[] = {0,0,0};
        
        if(temp.value.x != 0){
            alpha[0] = this.value.x/temp.value.x; 
        }else if(this.value.x != 0){
            return 0;
        }
        if(temp.value.y != 0){
            alpha[1] = this.value.y/temp.value.y;
        }else if(this.value.y != 0){
            return 0;
        }
        if(temp.value.z != 0){
            alpha[2] = this.value.z/temp.value.z;
        }else if(this.value.z != 0){
            return 0;
        }
        boolean egale = true;
        for(int i=0;i<3;i++){
            for(int j = i+1;j<3;j++){
                if(alpha[i] != 0 && alpha[j] != 0){
                    if(alpha[i] != alpha[j])
                        egale = false;
                }
            }
        }
        if(egale){
            System.out.println("Punctele sunt coliniare");
            for(int i=0;i<3;i++){
                if(alpha[i] != 0)
                    return alpha[i];
            }
        }
        return 0;
    }
    
    @Override
    public void draw(Graphics2D g,int cX, int cY, int zoom, boolean drawName){
        Color previousColor = g.getColor();
        g.setColor(color);
        BasicStroke normalStroke = (BasicStroke)g.getStroke();
        BasicStroke vStroke = new BasicStroke(2);
        g.setStroke(vStroke);
        g.fillOval(((int)(X.x*zoom + X.z/2*zoom))+cX-5, (int)(X.y*zoom - X.z/2*zoom)+cY-13,15,15);
        g.drawLine(((int)(X.x*zoom+X.z/2*zoom))+cX, ((int)(X.y*zoom-X.z/2*zoom))+cY,
                ((int)(Y.x*zoom+Y.z/2*zoom))+cX, ((int)(Y.y*zoom-Y.z/2*zoom))+cY);
        X.draw(g, cX, cY, zoom, false);
        Y.draw(g, cX, cY, zoom, false);
        g.setStroke(normalStroke);
        g.setColor(previousColor);
    }
    
    /**
     * Dimensiunea unui vector
     * @return norma
     */
    public double norm(){
        double norma;
        norma = Math.sqrt(value.x*value.x + value.y*value.y + value.z*value.z);
        return norma;
    }
    
    /**
     *  Unghiul creat de doi vectori
     * @param vector
     * @return double
     */
    public double angle(VectorGeo vector){
        double cosAngle = vector.value.x*value.x + vector.value.y*value.y + vector.value.z*value.z;
        cosAngle /= this.norm()*vector.norm();
        cosAngle = Math.acos(cosAngle);
        return cosAngle;
    }
    
    public double crossProduct(VectorGeo vector){
        double product = this.norm()*vector.norm()*Math.sin(this.angle(vector));
        System.out.println(product);
        return product;
    }
    
    /**
     * Use only in 2 dimensions(x & y)
     * @param vector
     * @return vectorul produs
     */
    public VectorGeo vectorProduct(VectorGeo vector){
        if((boolean)Point.Coliniare(vector.X, vector.X, this.Y).get("coliniari") == true){
            VectorGeo produs = new VectorGeo(new Point(this.X.name,0,0,0,Point.USER_POINT), new Point(this.Y.name,0,0,0,Point.USER_POINT));
            return produs;
        }else{
            VectorGeo versor = new VectorGeo(new Point("I",X.x,X.y,X.z, Point.USER_POINT), new Point("", X.x, X.y, 1, Point.USER_POINT));
            double angle = this.angle(vector);
            versor.multiplyByScalar(Math.sin(angle)*this.norm()*vector.norm());
            return versor;
        }
    }
    
    /**
     * Inmultirea cu un scalar
     * @param scalar
     */
    public void multiplyByScalar(double scalar){
        this.value.x *= scalar;
        this.value.y *= scalar;
        this.value.z *= scalar;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof VectorGeo){
            VectorGeo objv = (VectorGeo) obj;
            return objv.value.equals(this.value);
        }else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.X);
        hash = 59 * hash + Objects.hashCode(this.Y);
        hash = 59 * hash + Objects.hashCode(this.value);
        return hash;
    }
    
    @Override
    public String toString(){
        return "("+value.name+")<"+value.x+","+value.y+","+value.z+">";
    }
}
