package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

    private ServerSocket server = null;
    private Thread thread = null;
    private ChatServerThread client = null;

    public ChatServer(int port) {
        try {
            System.out.println("Binding to port: " + port + ", please wait...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client...");
                addThread(server.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addThread(Socket socket) {
        System.out.println("Client accepted: " + socket);
        client = new ChatServerThread(this, socket);
        try {
            client.open();
            client.start();
        }
        catch(IOException ioe) {
            System.out.println("Error opening thread: " + ioe);
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public static void main(String[] args) {
        ChatServer server = null;
        if (args.length != 1) {
            System.out.println("tcp.ChatServer opening on default port 1500");
            server = new ChatServer(1500);
        } else
            server = new ChatServer(Integer.parseInt(args[0]));
    }
}
