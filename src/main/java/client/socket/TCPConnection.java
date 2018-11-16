package client.socket;

import config.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPConnection extends IClientConnection {
    private Socket socket;

    private String serverAddress;
    private int serverPort;

    /**
     * @param address IP address of the server
     * @param port    port number of the server
     */
    public TCPConnection(String address, int port) {
        serverAddress = address;
        serverPort = port;
    }

    /**
     * Connects to the specified server by serverAddress and serverPort
     */
    public void connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            socket.setSoTimeout(Constants.TIMEOUT);

            // Read and write buffers on the socket
            setInputStream(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            setOutputStream(new PrintWriter(socket.getOutputStream()));

            System.out.println("Successfully connected to " + serverAddress + ":" + serverPort);
        } catch (IOException e) {
            System.err.println("No server has been found on " + serverAddress + ":" + serverPort);
        }
    }

    /**
     * Disconnects the socket and closes the buffers
     */
    public void disconnect() {
        try {
            getInputStream().close();
            getOutputStream().close();
            socket.close();

            System.out.println("TCPConnection closed");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while closing the connection.");
        }
    }
}
