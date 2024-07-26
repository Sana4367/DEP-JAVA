package fourthTask;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ResourceManager extends Remote {
   public String getResource(String name) throws RemoteException;
   public void addResource(String name, String resource) throws RemoteException;
}
