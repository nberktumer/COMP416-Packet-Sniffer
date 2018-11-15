package client.socket;

public interface ClientConnection {
    public void connect();
    public void disconnect();
    public String send(String message);
}
