package client.events;

public class LoggerMessage {
    public String message;
    public String header;
    public LoggerMessage(String header, String message) {
        this.header = header;
        this.message = message;
    }
}
