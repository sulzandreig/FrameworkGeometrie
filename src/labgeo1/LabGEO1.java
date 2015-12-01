/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labgeo1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Dragos-Alexandru
 */
public class LabGEO1 extends JFrame{
    public static DrawingBoard drawingBoard;
    public JButton exit;
    public static int width = 600;
    public static int height = 600;
    public static int zoom = 30;
    public LabGEO1(){
        super("LabGeo");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        drawingBoard = new DrawingBoard(width,height,zoom);
        exit = new JButton("Exit");
        exit.addActionListener((ActionEvent e) -> {
            if (exit.getParent().getParent().getParent().getParent() instanceof LabGEO1) {
                LabGEO1 parent1 = (LabGEO1)exit.getParent().getParent().getParent().getParent();
                parent1.dispose();
            }
        });
        this.add(drawingBoard,BorderLayout.CENTER);
        this.add(exit,BorderLayout.SOUTH);
        //this.setUndecorated(true);
        this.setVisible(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(LabGEO1.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(LabGEO1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                exit = true;
            }
            
        }
    }

    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Point A,B,C,D,M;
        System.out.print("A = ");
        A = new Point("A",s.nextDouble(),s.nextDouble(),0, Point.USER_POINT);
        System.out.print("B = ");
        B = new Point("B",s.nextDouble(),s.nextDouble(),0, Point.USER_POINT);
        System.out.print("C = ");
        C = new Point("C",s.nextDouble(),s.nextDouble(),0, Point.USER_POINT);
        System.out.print("D = ");
        D = new Point("D",s.nextDouble(),s.nextDouble(),0, Point.USER_POINT);
        
        LabGEO1 frame = new LabGEO1();
        Quadrilateral patrulater = new Quadrilateral(A, B, C, D);
        patrulater.pointPositionOverOuterCircle(3);
        System.out.println("circumscriptibil: " + patrulater.checkDistrict());
        drawingBoard.quadrilaters.add(patrulater);
        frame.start();
    }
}
