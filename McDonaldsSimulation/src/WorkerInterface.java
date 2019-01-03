/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlo
 */
public interface WorkerInterface {
    
    public void workerTakeHamburger();
    public void workerMoved(Worker source);
    public void workerServed(Client source);
    public Worker getWorker(int desk);
    public boolean[] getFreeDesk();
    public void setFreeDesk(int row,boolean value);
}
