/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProiectGeometrie;

import ObiecteGeometrice.Point;
import java.awt.BorderLayout;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.io.*;
import ObiecteGeometrice.Poligon;
import java.util.LinkedList;

/**
 *
 * @author Dragos-Alexandru
 */
public class ProiectGeometrie extends JFrame{
    public static DrawingBoard drawingBoard;
    public static int width = 600;
    public static int height = 600;
    public static int zoom = 20;
    public ProiectGeometrie(){
        super("LabGeo");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        drawingBoard = new DrawingBoard(width,height,zoom);
        this.add(drawingBoard,BorderLayout.CENTER);
        //this.setUndecorated(true);
        this.setVisible(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProiectGeometrie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void start(){
        boolean exit = false;
        while(!exit){
            if(this.isActive()){
                drawingBoard.repaint(this.getGraphics());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProiectGeometrie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                exit = true;
            }
            exit = true;
        }
    }
    
    public static void main(String[] args) throws CloneNotSupportedException, FileNotFoundException {
        ProiectGeometrie frame = new ProiectGeometrie();
        Scanner fs = null;
        try
        {
            fs = new Scanner(new File("poligon2.in"));
        }
        catch(FileNotFoundException fnf)
        {
            System.out.print("Fisier inexistent");
        }
        int n = fs.nextInt();
        LinkedList<Point> pointsInPoligon = new LinkedList<>();
        for(int i = 0 ; i < n ; i ++){
            pointsInPoligon.add(new Point(fs.next(), fs.nextDouble(), fs.nextDouble(), 0, Point.USER_POINT));
        }
        Poligon P = new Poligon((LinkedList<Point>) pointsInPoligon.clone());
        drawingBoard.poligons.add(P);
        drawingBoard.triangles = P.weakEarCuttingTriangulation();
        frame.start();
    }
}
