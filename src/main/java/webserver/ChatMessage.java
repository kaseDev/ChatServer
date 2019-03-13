package webserver;

public class ChatMessage {

    // Messages will be of the following form
    // rename [name]
    // broadcast [message]
    // direct [@name(s)] [message]
    private String command;
    private String[] recipients;
    private String content;
    private String sender;

    public static final String RENAME = "rename";
    public static final String BROADCAST = "broadcast";
    public static final String DIRECT = "direct";

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
