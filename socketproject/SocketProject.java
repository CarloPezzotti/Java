/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javafx.scene.chart.PieChart;

/**
 *
 * @author Carlo
 */
public class SocketProject {

    public final static int PORT = 5555; 
    public final static String EXIT_PROTOCOL = "quit";
    public final static String TIME_PROTOCOL = "time";

    public static void main(String[] args) { 
        try { 
            ServerSocket server = new ServerSocket(PORT); 
            System.out.println("Listening on port " + PORT);
            Socket client = server.accept();
            System.out.println("Connection from " + client.getRemoteSocketAddress().toString());
            InputStream in = client.getInputStream(); 
            OutputStream out = client.getOutputStream();
            int read = 0; 
            String messaggio = "";
            String exit = "";
            String time = "";
            
            while ((read = in.read()) != -1){ 
                
                if(read != (int)'\n'){
                    if(read != (int)'\r')
                        messaggio += (char)read;
                }else{
                    System.out.println(messaggio);
                    if(messaggio.equals(EXIT_PROTOCOL)){
                        break;
                    }else if(messaggio.equals(TIME_PROTOCOL)){
                        out.write(new Date().toString().getBytes());
                    }
                    messaggio = "";
                }
                
                out.write(read);
            }
            out.close(); 
            in.close(); 
            client.close(); 
        } catch (IOException ex) { 
            System.out.println("Error: " + ex.getMessage()); 
        }
    } 
}
