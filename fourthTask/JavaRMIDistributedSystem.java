package fourthTask;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

// Define the Remote Interface
interface ResourceManager extends Remote {
    String getResource(String name) throws RemoteException;
    void addResource(String name, String resource) throws RemoteException;
}

// Implement the Remote Interface
class ResourceManagerImpl extends UnicastRemoteObject implements ResourceManager {
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

// RMI Server
class RMIServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Create RMI registry on port 1099
            ResourceManager resourceManager = new ResourceManagerImpl();
            Naming.rebind("rmi://localhost/ResourceManager", resourceManager);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// RMI Client
class RMIClient {
    public static void main(String[] args) {
        try {
            ResourceManager resourceManager = (ResourceManager) Naming.lookup("rmi://localhost/ResourceManager");

            // Add resources
            resourceManager.addResource("Resource1", "This is the first resource.");
            resourceManager.addResource("Resource2", "This is the second resource.");

            // Get resources
            String resource1 = resourceManager.getResource("Resource1");
            String resource2 = resourceManager.getResource("Resource2");

            System.out.println("Resource1: " + resource1);
            System.out.println("Resource2: " + resource2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
