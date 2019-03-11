package webserver;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ChatClient extends WebSocketClient {

    public ChatClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("Hello server, it is client");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Just left the server");
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new ChatClient(new URI("ws://localhost:1500"));
        client.connect();

        Scanner scan = new Scanner(System.in);
        String line = "";
        while (!(line = scan.nextLine()).equals(".quit")) {
            client.send(line);
        }
    }
}
