package fourthTask;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Create RMI registry on port 1099
            ResourceManager resourceManager = new ResourceManagerImpl();
            Naming.rebind("rmi://localhost:5099/", resourceManager);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
