package tcp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServerThread extends Thread {

    private Socket socket = null;
    private ChatServer server = null;
    private int id = -1;
    private DataInputStream streamIn = null;

    public ChatServerThread(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.id = socket.getPort();
    }

    public void run() {
        System.out.println("Server Thread " + id + " running.");
        while (true) {
            try {
                System.out.println(streamIn.readUTF());
            } catch (IOException e) {

            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (streamIn != null)
            streamIn.close();
    }
}
