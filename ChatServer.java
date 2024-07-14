
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server waiting for clients on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                dis = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = dis.readUTF();
                    System.out.println("Received from client: " + message);

                    // Broadcast message to all clients
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.send(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    dis.close();
                    dos.close();
                    clientSocket.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void send(String message) {
            try {
                dos.writeUTF(message);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
