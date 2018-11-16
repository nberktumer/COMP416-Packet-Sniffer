package client.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPConnection implements IClientConnection {
    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

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
     * Establishes a socket connection to the server that is identified by the
     * serverAddress and the serverPort
     */
    public void connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            /*
             * Read and write buffers on the socket
             */
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());

            System.out.println("Successfully connected to " + serverAddress + ":" + serverPort);
        } catch (IOException e) {
            System.err.println("No server has been found on " + serverAddress + ":" + serverPort);
        }
    }

    /**
     * Sends the message String to the server and retrieves the answer
     *
     * @param message input message string to the server
     * @return the received server answer
     */
    public String send(String message) {
        String response = "";
        try {
            /*
             * Sends the message to the server via PrintWriter
             */
            outputStream.println(message);
            outputStream.flush();
            /*
             * Reads a line from the server via Buffer Reader
             */
            response = inputStream.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Socket read error while sending a message.");
        }
        return response;
    }

    /**
     * Disconnects the socket and closes the buffers
     */
    public void disconnect() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();

            System.out.println("TCPConnection closed");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while closing the connection.");
        }
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
