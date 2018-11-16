package client.socket;

public class ConnectionFactory {
    private static volatile ConnectionFactory _instance = null;

    private ConnectionFactory() {
    }

    public static ConnectionFactory getInstance() {
        if (_instance == null) {
            synchronized (ConnectionFactory.class) {
                if (_instance == null) {
                    _instance = new ConnectionFactory();
                }
            }
        }
        return _instance;
    }

    public IClientConnection createConnection(ConnectionType type, String ipAddress, int port) {
        switch (type) {
            case TCP:
                return new TCPConnection(ipAddress, port);
            case SSL:
                return new SSLConnection(ipAddress, port);
        }
        throw new IllegalArgumentException("Connection type not supported");
    }
}
