package tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

    private Socket server = null;
    private Scanner dataIn = null;
    private DataOutputStream dataOut =  null;

    public ChatClient(String servername, int serverport) {
        System.out.println("Establishing connection, please wait...");
        try {
            server = new Socket(servername, serverport);
            System.out.println("Connected: " + server);
            dataIn = new Scanner(System.in);
            dataOut = new DataOutputStream(server.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";
        while (!line.equals(".exit")) {
            try {
//                line = new StringBuilder();
//                char lineSep = System.getProperty("line.separator").charAt(0);
//                char chr;
//                while ((chr = dataIn.readChar()) != lineSep)
//                    line.append(chr);
                line = dataIn.nextLine();
                dataOut.writeUTF(line.toString());
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            if (dataIn != null)
                dataIn.close();
            if (dataOut != null)
                dataOut.close();
            if (server != null)
                server.close();
        }
        catch(IOException ioe) {
            System.out.println("Error closing ...");
        }
    }

    public static void main(String[] args) {
        ChatClient client = null;
        if (args.length != 2) {
            System.out.println("connecting to default localhost on port 1500");
            client = new ChatClient("localhost", 1500);
        } else
            client = new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}
