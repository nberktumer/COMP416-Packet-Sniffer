package client.socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SSLConnection extends IClientConnection {
    /*
    Name of key store file
     */
    private final String KEY_STORE_NAME = "client.jks";
    /*
    Password to the key store file
     */
    private final String KEY_STORE_PASSWORD = "kocuniv";

    private SSLSocket socket;

    private String serverAddress;
    private int serverPort;

    public SSLConnection(String address, int port) {
        serverAddress = address;
        serverPort = port;

        //Loads the keystore's address of client
        System.setProperty("javax.net.ssl.trustStore", KEY_STORE_NAME);

        // Loads the keystore's password of client
        System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
    }

    /**
     * Connects to the specified server by serverAddress and serverPort
     */
    public void connect() {
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket(serverAddress, serverPort);
            socket.startHandshake();
            setInputStream(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            setOutputStream(new PrintWriter(socket.getOutputStream()));
            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Disconnects form the specified server
     */
    public void disconnect() {
        try {
            getInputStream().close();
            getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the socket
     *
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }
}
