package client.socket;

public interface IClientConnection {
    public void connect();

    public void disconnect();

    public String send(String message);
}
