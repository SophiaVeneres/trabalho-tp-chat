import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADRESS = "127.0.0.1";
    private Socket clientSocket;
    private Scanner scanner;
    private BufferedWriter out;

    public ChatClient(){
        scanner = new Scanner(System.in);
    }
    
    public void start() throws UnknownHostException, IOException{
        clientSocket = new Socket(SERVER_ADRESS, ChatServer.PORT);
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        
        System.out.println("Client conected to the server in " + 
            SERVER_ADRESS + ";" + ChatServer.PORT);
        messageLoop();

    }

    private void messageLoop() throws IOException{
        String msg;
        do{
            System.out.print("Type a massage, type (sair) to finish: ");
            msg = scanner.nextLine();
            System.out.println(msg);
            out.write(msg);
            out.newLine();
            out.flush();
            
        }while(!msg.equalsIgnoreCase("sair"));
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
