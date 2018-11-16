package client.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class IClientConnection {
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    public abstract void connect();

    public abstract void disconnect();

    /**
     * Sends a message as a string over the socket and receives
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

    protected void setInputStream(BufferedReader inputStream) {
        this.inputStream = inputStream;
    }

    protected void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }
}
