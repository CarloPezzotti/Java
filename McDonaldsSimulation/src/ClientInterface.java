
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlo
 */
public interface ClientInterface {
    public void clientMoved(Client source);
    public Client createNewClient();
    public List<Client> getClients();
    public void removeClient(Client client);
    public List<Table> getTable();
}
