package fourthTask;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ResourceManagerImpl extends UnicastRemoteObject implements ResourceManager {
    private Map<String, String> resources;

    protected ResourceManagerImpl() throws RemoteException {
        super();
        resources = new HashMap<>();
    }

    @Override
    public String getResource(String name) throws RemoteException {
        return resources.get(name);
    }

    @Override
    public void addResource(String name, String resource) throws RemoteException {
        resources.put(name, resource);
    }
}
