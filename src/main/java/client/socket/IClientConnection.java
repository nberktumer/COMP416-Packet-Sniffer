package client.socket;

import config.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.*;

public abstract class IClientConnection {
    private int ack = 0;
    private String response = null;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);

    private BufferedReader inputStream;
    private PrintWriter outputStream;

    /**
     * Connects to the specified server by serverAddress and serverPort
     */
    public abstract void connect();

    /**
     * Disconnects the socket and closes the buffers
     */
    public abstract void disconnect();

    /**
     * Sends a message as a string over the socket and receives
     * answer from the server
     *
     * @param message input message
     * @return response from server
     */
    public String send(String message) {
        int trials = 0;
        while (response == null) {
            if (trials > 0)
                System.out.println("Resending");
            outputStream.println(ack + message);
            outputStream.flush();
            try {
                Future future = executor.submit(() -> {
                    do {
                        try {
                            response = inputStream.readLine();
                        } catch (IOException ignore) {

                        }
                    } while (response == null || Character.getNumericValue(response.charAt(0)) != ack);
                });

                future.get(Constants.TIMEOUT, TimeUnit.MILLISECONDS);

                if (response == null || Character.getNumericValue(response.charAt(0)) != ack)
                    continue;

                int receivedAck = Character.getNumericValue(response.charAt(0));
                response = response.substring(1);
                if (receivedAck == ack && response.substring(0, Constants.OK.length()).equals(Constants.OK))
                    ack = (ack + 1) % 2;
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            trials++;
        }
        String result = response.substring(Constants.OK.length()).trim();
        response = null;
        return result;
    }

    /**
     * Returns the input stream
     *
     * @return inputStream
     */
    public BufferedReader getInputStream() {
        return inputStream;
    }

    protected void setInputStream(BufferedReader inputStream) {
        this.inputStream = inputStream;
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
     * Sets the output stream
     *
     * @param outputStream output stream
     */
    protected void setOutputStream(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }
}
