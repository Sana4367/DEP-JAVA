import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class chatApplication {

    private static List<ClientHandler> clients = new ArrayList<>();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'server' to start the server or 'client' to start the client, or 'both' to start both:");
        String mode = scanner.nextLine();

        if (mode.equalsIgnoreCase("server")) {
            startServer();
        } else if (mode.equalsIgnoreCase("client")) {
            startClient();
        } else if (mode.equalsIgnoreCase("both")) {
            Thread serverThread = new Thread(chatApplication::startServer);
            Thread clientThread = new Thread(chatApplication::startClient);
            serverThread.start();
            clientThread.start();
        } else {
            System.out.println("Invalid mode selected. Please restart and choose 'server', 'client', or 'both'.");
        }
    }

    private static void startServer() {
        try {
            @SuppressWarnings("resource")
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

    private static void startClient() {
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
