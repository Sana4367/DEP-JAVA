package fourthTask;
import java.rmi.Naming;


public class RMIClient {
    public static void main(String[] args) {
        try {
            ResourceManager resourceManager = (ResourceManager) Naming.lookup("rmi://localhost/5099");

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
