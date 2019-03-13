package webserver;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ChatServer extends WebSocketServer {

    private BiMap<String, WebSocket> users = HashBiMap.create();

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket client, ClientHandshake clientHandshake) {
        ChatMessage message = new ChatMessage();
        message.setCommand("greeting");
        message.setContent("Hello Client");
        client.send(new Gson().toJson(message));
    }

    @Override
    public void onClose(WebSocket client, int code, String reason, boolean remote) {
        System.out.println("goodbye client");
        users.remove(users.inverse().get(client));
    }

    @Override
    public void onMessage(WebSocket client, String msg) {
        ChatMessage message = new Gson().fromJson(msg, ChatMessage.class);
        switch (message.getCommand()) {
            case ChatMessage.RENAME:
                if (users.keySet().contains(message.getContent())) {
                    message.setContent("denied!");
                    client.send(new Gson().toJson(message));
                    break;
                } else {
                    users.remove(message.getSender());
                    users.put(message.getContent(), client);
                    client.send(new Gson().toJson(message));
                    break;
                }
            case ChatMessage.BROADCAST:
                for (WebSocket user : users.values())
                    user.send(msg);
            case ChatMessage.DIRECT:
                for (int i = 0; i < message.getRecipients().length; i++)
                    users.get(message.getRecipients()[i]).send(msg);
                break;
            default:
                System.out.println(message.getContent());
                break;
        }
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
