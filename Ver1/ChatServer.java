import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * CharSever
 */
public class ChatServer {
    public static final int PORT = 4000;
    private ServerSocket serverSocket;

    public void start() throws IOException{
        System.out.println("Server initizlized");
        serverSocket = new ServerSocket(PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop(){
        while (true) {  
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client " + clientSocket.getRemoteSocketAddress() + " Conected");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String msg;
                while((msg = in.readLine()) != null){
                    System.out.println("Message received from client " + clientSocket.getRemoteSocketAddress()
                + ": " + msg);
                }
                
            } catch (IOException e) {
                System.out.println("Error when accepting connection");
            }
        }
    }
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException e) {
           System.out.println("Error when initializing server");
        }
        System.out.println("Server ended");
    }
}
