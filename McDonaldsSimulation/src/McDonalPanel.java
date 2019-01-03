import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlo
 */
public class McDonalPanel extends JPanel implements ClientInterface,WorkerInterface,MouseListener{

    public final int DIM = 34;
    public final int LATE = 30;
    public final int CASSEY = 8;
    
    private int[] row = new int[5];
    private List<Client> clients = new ArrayList<>();
    private Worker[] workers = new Worker[5];
    private List<Table> tables = new ArrayList<>();
    private boolean[] freeDesk = new boolean[5];
    
    
    private int selledBurger = 0;
    private int totalClient = 0;
    
    public McDonalPanel(){
        createNewClient();
        
        for (int i = 1; i < 6; i++) {
            workers[i-1] = new Worker(this, (2*(i-1)+1)*2,100,"Worker " + i);
            workers[i-1].start();
            freeDesk[i-1] = false;
        }
        tables.add(new Table(new Point(25,16), new Dimension(4, 2), Color.magenta));
        this.addMouseListener(this);
    }
    
    @Override
    public synchronized void paintComponent(Graphics g) {
        super.repaint();
        g.setColor(Color.white);
        g.fillRect(0, 0, LATE*(DIM), LATE*(DIM));
        
        
        
         //tavoli
        for(Table t: tables){
            t.paint(g);
        }
        
        //burger
        g.setColor(Color.black);
        g.drawString("Burger: " + selledBurger, LATE*(DIM-2), LATE);
        
        //bancone
        g.setColor(Color.darkGray);
        g.fillRect(LATE*2, LATE*6, LATE*(DIM-2), LATE*2);
        
        //casse
        
        g.setColor(Color.green);
        for (int i = 1; i < 5*2; i += 2) {
            g.fillRect(LATE*2*i, LATE*8, 2*LATE, 2*LATE);
        }
       
        
        //clients
        try{
            for(Client client: clients){
                client.paint(g);
            }
        }catch(ConcurrentModificationException cme){}
        //worker
        
        for(Worker worker: workers){
            worker.paint(g);
        }
        
        //griglia
        g.setColor(Color.black);
        for (int i = 0; i < DIM+1; i++) {
            g.drawLine(0, i*LATE, LATE*DIM, i*LATE);
        }
        for (int i = 0; i < DIM+1; i++) {
            g.drawLine(i*LATE, 0, LATE*i, (DIM)*LATE);
        }
    }
    
    public synchronized void incrementSelled(){
        selledBurger++;
        repaint();
    }

    @Override
    public void clientMoved(Client source) {
        repaint();
    }

    @Override
    public synchronized Client createNewClient() {
        if(clients.size() < 30){
            Client go = new Client(this,"Client " + totalClient);
            clients.add(go);
            go.start();
            totalClient++;
            return go;
        }
        return null;
    }  
    
    public synchronized Client createNewClient(int row) {
        if(clients.size() < 30){
            Client go = new Client(this,"Client " + totalClient,2);
            clients.add(go);
            go.start();
            totalClient++;
            return go;
        }
        return null;
    }

    @Override
    public void workerTakeHamburger() {        
    }

    @Override
    public void workerMoved(Worker source) {
        repaint();
    }

    @Override
    public void workerServed(Client source) {
    }

    @Override
    public Worker getWorker(int desk) {
        return workers[desk];
    }

    @Override
    public synchronized List<Client> getClients() {
        return clients;
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public boolean[] getFreeDesk() {
        return freeDesk;
    }

    @Override
    public void setFreeDesk(int row,boolean value) {
        freeDesk[row] = value;
    }

    @Override
    public List<Table> getTable() {
        return tables;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getPoint().x/LATE;
        int y = e.getPoint().y/LATE;
        
        if(e.getButton() == 1){
            if(x%2 != 0 && y%2==0)
                tables.add(new Table(new Point(x, y), new Dimension(2, 2), Color.magenta));         
        }
        else if(e.getButton() == 3){
            if(x>=0 && x<=5)
                createNewClient(2);
            else if(x>=6 && x<=9)
                createNewClient(6);
            else if(x>=10 && x<=13)
                createNewClient(10);
            else if(x>=14 && x<=17)
                createNewClient(14);
            else
                createNewClient(18);
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
