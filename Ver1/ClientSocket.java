import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private static String login;

    public String getLogin() {
        return login;
    }

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
      
        System.out.println("Client " + socket.getRemoteSocketAddress() + " Conected");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
        
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error when closing socket");
        }
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean sendMsg(String msg) {
        out.println(msg);
        return !out.checkError();
    }
}
