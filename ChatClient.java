import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            Socket clientSocket = new Socket("localhost", 8080);
            System.out.println("Connected to server.");

            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            // Thread for receiving messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = dis.readUTF();
                        System.out.println("Server says: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // Thread for sending messages to server
            Thread sendThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String message = reader.readLine();
                        dos.writeUTF(message);
                        dos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sendThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
