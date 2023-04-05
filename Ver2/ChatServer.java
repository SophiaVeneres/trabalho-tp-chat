import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private List<ClientHandler> clients = new ArrayList<>();
    private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Chat server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostName());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting chat server: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String name;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Error creating input/output streams for client: " + e.getMessage());
            }
        }

        public void run() {
            try {
                // Get client name
                name = reader.readLine();
                System.out.println(name + " joined the chat.");
                broadcast(name + " joined the chat.");

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println(name + ": " + message);
                    broadcast(name + ": " + message);
                }

                // Client disconnected
                System.out.println(name + " left the chat.");
                clients.remove(this);
                broadcast(name + " left the chat.");

                // Close client socket and streams
                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.writer.println(message);
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(5000);
        chatServer.start();
    }
}
