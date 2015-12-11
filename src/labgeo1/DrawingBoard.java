package labgeo1;

import ObiecteGeometrice.Quadrilateral;
import ObiecteGeometrice.Line;
import ObiecteGeometrice.Point;
import ObiecteGeometrice.VectorGeo;
import ObiecteGeometrice.Triangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Dragos-Alexandru
 */
public class DrawingBoard extends JPanel{
    public ArrayList<Point> points;
    public ArrayList<VectorGeo> vectors;
    public ArrayList<Line> lines;
    public ArrayList<Quadrilateral> quadrilaters;
    public ArrayList<Triangle> triangles;
    public int zoom;
    public int centerX = LabGEO1.width/2;
    public int centerY = LabGEO1.height/2;
    public int width;
    public int height;
    public BasicStroke dashedStroke;
    
    public DrawingBoard(int width, int height, int zoom){
        super();
        this.width = width;
        this.height = height;
        this.zoom = zoom;
        
        //creating dashedStroke
        float dash1[] = {10.0f};
        dashedStroke = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        1.0f, dash1, 0.0f);
        
        vectors = new ArrayList<>();
        points = new ArrayList<>();
        lines = new ArrayList<>();
        quadrilaters = new ArrayList<>();
        triangles = new ArrayList<>();
    }
    
    public void repaint(Graphics gra){
        Graphics2D g = (Graphics2D)gra;
        
        //background color
        g.setColor(Color.WHITE);
        
        //background
        g.fillRect(0, 0, width, height);
        
        //set grid color
        g.setColor(Color.BLACK);
        
        //drawing grid
        drawGrid(gra);
        g.setColor(Color.blue);
        //drawing vectori
        if(vectors != null){
            vectors.stream().forEach((vector) -> {
                vector.draw(g,centerX, centerY, zoom, true);
            });
        }
        
        g.setColor(Color.red);
        //drawing points
        if(points != null){
            points.stream().forEach((punct) -> {
                punct.draw(g,centerX, centerY, zoom, true);
            });
        }
        
        g.setColor(Color.YELLOW);
        if(lines != null){
            lines.stream().forEach((line) -> {
                line.draw(g, centerX, centerY, zoom, true);
            });
        }
        
        g.setColor(Color.GREEN);
        if(quadrilaters != null){
            quadrilaters.stream().forEach((patrulater) -> {
                patrulater.draw(g, centerX, centerY, zoom, true);
            });
        }
        g.setColor(Color.BLUE);
        if(triangles != null){
            triangles.stream().forEach((triunghi) -> {
                triunghi.draw(g, centerX, centerY, zoom, true);
            });
        }
    }
    
    //draws the grid
    public void drawGrid(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        
        //remembering normalStroke
        BasicStroke normalStroke = (BasicStroke)((Graphics2D)this.getGraphics()).getStroke();
        
        //x axis
        g2.drawLine(0, centerY, width, centerY);
        
        //y axis
        g2.drawLine(centerX,0,centerX,height);
        
        //z axis where z<=0
        //g2.drawLine(0, height, centerX, centerY);
        
        //setting dashed stroke for z axis where z>0
        //g2.setStroke(dashedStroke);
        //g2.drawLine(centerX, centerY, width, 0);
        
        //drawing axis arrows
        //x axis
        int[] xpoints = {width-10,width-10,width};
        int[] ypoints = {centerY-5,centerY+5,centerY};
        Polygon s = new Polygon(xpoints, ypoints, 3);
        
        //y axis
        int[] xpoints2 = {centerX,centerX-5,centerX+5};
        int[] ypoints2 = {0,10,10};
        Polygon s2 = new Polygon(xpoints2, ypoints2, 3);
        
        //z axis
        /*int[] xpoints3 = {width,width-10,width-5};
        int[] ypoints3 = {0,5,10};
        Polygon s3 = new Polygon(xpoints3, ypoints3, 3);*/
        
        g2.fill(s);
        g2.fill(s2);
        //g2.fill(s3);
        
        //drawing unit on axis
        //center of axis
        int val = 0;
        Point unit = new Point(""+val,0,0,0, Point.AXIS_POINT_X);
        unit.draw(g2, centerX, centerY, zoom, false);
        val++;
        
        //x axis
        while(val * zoom < width){
            unit = new Point(""+val,val,0,0, Point.AXIS_POINT_X);
            unit.draw(g2, centerX, centerY, zoom, false);
            unit = new Point("-"+val,-val,0,0, Point.AXIS_POINT_X);
            unit.draw(g2, centerX, centerY, zoom, false);
            val++;
        }
        
        //y axis
        val = 1;
        while(val * zoom < height){
            unit = new Point(""+val,0,val,0, Point.AXIS_POINT_Y);
            unit.draw(g2, centerX, centerY, zoom, false);
            unit = new Point("-"+val,0,-val,0, Point.AXIS_POINT_Y);
            unit.draw(g2, centerX, centerY, zoom, false);
            val++;
        }
        
        //z axis
        /*val = 1;
        while(val * zoom < height){
            unit = new Point(""+val,0,0,val, Point.AXIS_POINT_Z);
            unit.draw(g2, centerX, centerY, zoom, false);
            unit = new Point("-"+val,0,0,-val, Point.AXIS_POINT_Z);
            unit.draw(g2, centerX, centerY, zoom, false);
            val++;
        }*/
        //reseting stroke
        g2.setStroke(normalStroke);
    }
    
}
