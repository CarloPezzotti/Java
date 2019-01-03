
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlo
 */
public class Client extends Thread{

    private Point location;
    private String name;
    private final int DIM = 2;
    private int late = 0;
    private int row = 0;
    private ClientInterface container;
    private Client waitClient = null;
    private Table table = null;
    private boolean wait = false;
    private boolean finish = false;
    private Color color;
    
    public Client(ClientInterface container, String name){
        this.late = ((McDonalPanel)container).LATE;
        this.name = name;
        this.container = container;
        row = new Random().nextInt(5);
        color = Color.BLACK;
        
        if(row == 0)
            this.location = new Point(2, 40);
        else if(row == 1)
            this.location = new Point(6, 40);
        else if(row == 2)
            this.location = new Point(10, 40);
        else if(row == 3)
            this.location = new Point(14, 40);
        else if(row == 4)
            this.location = new Point(18, 40);
    }
    
    public Client(ClientInterface container, String name,int row){
        this.late = ((McDonalPanel)container).LATE;
        this.name = name;
        this.container = container;
        color = Color.BLACK;
        this.location = new Point(row, 40);
    }
    
    public void paint(Graphics g){
        g.setColor(color);
        g.fillOval(location.x*late, location.y*late, DIM*late, DIM*late);
        
        g.setColor(Color.white);
        int width = g.getFontMetrics().stringWidth(name);
        g.drawString(name, location.x*late + (width/6), (location.y+(DIM/2))*late );
    }
    
    @Override
    public void run() {
        boolean flag = true;
        while (flag) { 
            try {
                if(!wait){
                    container.clientMoved(this);
                    int x = location.x;
                    int y = location.y;

                    if(!finish){
                        y = location.y - (DIM/2);
                        for(Client client: container.getClients()){
                            if(y-DIM <((McDonalPanel)container).CASSEY){
                                container.createNewClient();
                                ((WorkerInterface)container).getWorker(row).clientWantHamburger(this);
                                this.join();
                            }
                            else if(((y+1 > client.location.y && y-2 < client.location.y) && x == client.location.x)){
                                client.setWait(this);
                                wait = true;
                            }
                        }
                        if(waitClient != null)
                            waitClient.wait = false;
                    }
                    else{
                        color = Color.ORANGE;
                        while (table == null) {                                
                            for(Table t : container.getTable()){
                                Point tableLocation = t.getFirsttFreePlace();
                                if(tableLocation != null){
                                    x = tableLocation.x;
                                    y = tableLocation.y;
                                    table = t;
                                    break;
                                }
                            }
                        }
                        container.clientMoved(this);
                        container.createNewClient();
                        flag= false;
                        if(waitClient != null)
                            waitClient.wait = false;
                    }
                    if(!wait)
                        location = new Point(x, y);               
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                finish = true;
            } catch (ConcurrentModificationException cme){
            }
        }
        try {
            Thread.sleep(3000);
            table.togglePlace(location);
            container.removeClient(this);
        } catch (InterruptedException ex) {
            container.removeClient(this);
        }
    }
    
    public boolean canGo(){
        return finish;
    }
    
    public void setWait(Client c){
        this.waitClient = c;
    }
    public Point getLocation(){
        return location;
    }
    
}
