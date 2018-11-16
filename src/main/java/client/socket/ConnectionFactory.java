package client.socket;

public class ConnectionFactory {
    private static volatile ConnectionFactory _instance = null;

    private ConnectionFactory() {
    }

    /**
     * Returns the ConnectionFactory instance
     *
     * @return ConnectionFactory instance
     */
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

    /**
     * Creates an IClientConnection according to the given params
     *
     * @param type      Connection type (SSL or TCP)
     * @param ipAddress IP address
     * @param port      Port number
     * @return IClientConnection instance
     */
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
