package client.socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SSLConnection implements IClientConnection {
    /*
    Name of key store file
     */
    private final String KEY_STORE_NAME = "clientkeystore";
    /*
    Password to the key store file
     */
    private final String KEY_STORE_PASSWORD = "storepass";
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

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
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) sslSocketFactory.createSocket(serverAddress, serverPort);
            socket.startHandshake();
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
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
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message as a string over the secure channel and receives
     * answer from the server
     *
     * @param message input message
     * @return response from server
     */
    public String send(String message) {
        String response = "";
        try {
            outputStream.println(message);
            outputStream.flush();
            response = inputStream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }


    /**
     * Returns the input stream
     *
     * @return inputStream
     */
    public BufferedReader getInputStream() {
        return inputStream;
    }

    /**
     * Returns the output stream
     *
     * @return outputStream
     */
    public PrintWriter getOutputStream() {
        return outputStream;
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
