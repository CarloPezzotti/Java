
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import sun.applet.Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlo
 */
public class Worker extends Thread{

    private Point location;
    private String name;
    private final int DIM = 2;
    private int late = 0;
    private boolean goTakeHamburger = false;
    private boolean  hamburgerTaken = false;
    private long efficency = 0;
    private Client clientToServe;
    private WorkerInterface container;
    
    public Worker(WorkerInterface container,int desk,long effincency,String name){
        this.late = ((McDonalPanel)container).LATE;
        this.container = container;
        this.location = new Point(desk, 4);
        this.efficency = effincency;
        this.name = name;
    }
    
    public void paint(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval(location.x*late, location.y*late, DIM*late, DIM*late);
        if(hamburgerTaken){
            g.setColor(Color.RED);
            g.fillOval((location.x+1)*late, (location.y+1)*late, (DIM-1)*late, (DIM-1)*late);
        }
        g.setColor(Color.white);
        int width = g.getFontMetrics().stringWidth(name);
        g.drawString(name, location.x*late + (width/6), (location.y+(DIM/2))*late );
    }
    
    boolean flag = true;
    @Override
    public void run() {
        int x = location.x;
        int y = location.y;
        while (flag) {            
            try {
                if(goTakeHamburger){
                    y = y-DIM;
                    if(y == -2){
                        hamburgerTaken = true;
                        goTakeHamburger = false;
                    }
                }
                if(hamburgerTaken){
                   y = y+DIM;
                   if(y == 4){          
                       ((McDonalPanel)container).incrementSelled();
                       clientToServe.interrupt();
                       hamburgerTaken = false;
                   }
                }
                location = new Point(x, y);
                Thread.sleep(efficency);
            } 
            
            
            catch (InterruptedException e) {
            }  
            container.workerMoved(this);
        }      
    }
    public void clientWantHamburger(Client source){
        goTakeHamburger = true;
        clientToServe = source;
    }
}
