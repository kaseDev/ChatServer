package webserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer extends WebSocketServer {

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket client, ClientHandshake clientHandshake) {
        client.send("Hello Client");
    }

    @Override
    public void onClose(WebSocket client, int code, String reason, boolean remote) {
        System.out.println("goodbye client");
    }

    @Override
    public void onMessage(WebSocket client, String message) {
        System.out.println(message);
    }

    @Override
    public void onError(WebSocket client, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server has started and is awaiting clients...");
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1500;

        WebSocketServer server = new ChatServer(new InetSocketAddress(host, port));
        server.run();
    }
}
