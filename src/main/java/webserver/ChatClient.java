package webserver;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatClient extends WebSocketClient {

    private String name;

    public ChatClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        ChatMessage message = new ChatMessage();
        message.setCommand("greeting");
        message.setContent("Hello server, it is client");
        send(new Gson().toJson(message));
    }

    @Override
    public void onMessage(String msg) {
        ChatMessage message = new Gson().fromJson(msg, ChatMessage.class);
        switch (message.getCommand()) {
            case ChatMessage.RENAME:
                if (!message.getContent().equals("denied!"))
                    this.name = message.getContent();
                break;
            case ChatMessage.BROADCAST:
            case ChatMessage.DIRECT:
                System.out.println(message.getSender() + ": " + message.getContent());
                break;
            default:
                System.out.println(message.getContent());
                break;
        }
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
        ChatClient client = new ChatClient(new URI("ws://localhost:1500"));
        client.connect();

        Scanner scan = new Scanner(System.in);
        String line = "";
        while (!(line = scan.nextLine()).equals(".quit")) {
            String[] command = line.split(" ", 2);
            switch (command[0]) {
                case "NAME":
                    System.out.println(client.name);
                    break;
                case ChatMessage.RENAME:
                    client.rename(command[1]);
                    break;
                case ChatMessage.BROADCAST:
                    if (client.name == null) {
                        System.out.println("must have a name to send a message");
                        break;
                    }
                    client.broadcast(command[1]);
                    break;
                case ChatMessage.DIRECT:
                    if (client.name == null) {
                        System.out.println("must have a name to send a message");
                        break;
                    }
                    command = line.split("@");
                    List<String> recipients = new ArrayList<>();
                    for (int i = 0; i < command.length - 1; i++)
                        recipients.add(command[i].trim());
                    recipients.add(command[command.length-1].split(" ")[0]);
                    client.direct(recipients.toArray(new String[0]), command[command.length-1].split(" ")[1]);
                    break;
            }
        }
    }

    private void rename(String new_name) {
        ChatMessage message = new ChatMessage();
        message.setCommand(ChatMessage.RENAME);
        message.setSender(this.name);
        message.setContent(new_name);
        this.send(new Gson().toJson(message));

    }

    private void broadcast(String msg) {
        ChatMessage message = new ChatMessage();
        message.setCommand(ChatMessage.BROADCAST);
        message.setSender(this.name);
        message.setContent(msg);
        this.send(new Gson().toJson(message));
    }

    private void direct(String[] recipients, String msg) {
        ChatMessage message = new ChatMessage();
        message.setCommand(ChatMessage.DIRECT);
        message.setSender(this.name);
        message.setContent(msg);
        message.setRecipients(recipients);
        this.send(new Gson().toJson(message));
    }
}
