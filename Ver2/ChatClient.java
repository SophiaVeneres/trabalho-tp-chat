import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame implements ActionListener {

    private JTextField messageField;
    private JTextArea chatArea;
    private JButton sendButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ChatClient() {
        super("Chat Client");

        // Create GUI components
        messageField = new JTextField();
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Add components to frame
        Container contentPane = getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Connect to server
        try {
            socket = new Socket("127.0.0.1", 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            System.exit(1);
        }

        // Start receiving messages from server in a separate thread
        Thread messageReceiver = new Thread(new MessageReceiver());
        messageReceiver.start();

        // Show frame
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Called when Send button is clicked
    public void actionPerformed(ActionEvent e) {
        String message = messageField.getText();
        writer.println(message);
        messageField.setText("");
    }

    // Thread for receiving messages from server
    private class MessageReceiver implements Runnable {
        public void run() {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error receiving message: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}