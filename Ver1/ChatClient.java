import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.SocketAddress;
import java.util.Scanner;

public class ChatClient implements Runnable {
    private static final String SERVER_ADRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner scanner; 

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        String msg;
        while ((msg = clientSocket.getMessage()) != null) {
            System.out.printf("\n Menssagem recebida do servidor: %s\n", msg);
        }
    }

    public void start() throws UnknownHostException, IOException {
        try{
        clientSocket = new ClientSocket(new Socket(SERVER_ADRESS, ChatServer.PORT));

        System.out.println("Client conected to the server in " +
                SERVER_ADRESS + ";" + ChatServer.PORT);

        new Thread(this).start();
        messageLoop();
        } finally{
            clientSocket.close(); 
         }

    } 
    private void messageLoop() throws IOException {
        String msg;
        do {
            System.out.print("Type a massage, type (sair) to finish: ");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);

        } while (!msg.equalsIgnoreCase("sair"));
    }

    public static void main(String[] args) {

        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException e) {
            System.out.println("Error when initializing client");
        }
        System.out.println("Client has ended");
    }

}
