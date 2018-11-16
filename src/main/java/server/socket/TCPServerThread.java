package server.socket;


import config.Constants;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class TCPServerThread extends Thread {
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private Socket socket;

    private Map<String, String> valueMap = new HashMap<>();

    /**
     * Creates a server thread on the input socket
     *
     * @param socket input socket to create a thread on
     */
    public TCPServerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * Abstract server thread. Initializes the inputStream and outputStream
     */
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;

            while ((line = inputStream.readLine()) != null) {
                String[] commandArr = line.split("\\|");
                String command = commandArr[0];

                switch (command) {
                    case Constants.GET:
                        break;
                    case Constants.SUBMIT: {
                        String keyValuePair = line.substring(line.indexOf(" "));
                        String key, value;
                        if (keyValuePair.contains(", ")) {
                            key = keyValuePair.substring(0, keyValuePair.indexOf(", "));
                            value = keyValuePair.substring(keyValuePair.indexOf(", "));
                        } else if (keyValuePair.contains(",")) {
                            key = keyValuePair.substring(0, keyValuePair.indexOf(","));
                            value = keyValuePair.substring(keyValuePair.indexOf(","));
                        } else {
                            System.err.println("SUBMIT command must contain 2 arguments.");
                            continue;
                        }
                        valueMap.put(key, value);
                        outputStream.println(Constants.OK);
                        outputStream.flush();
                        break;
                    }
                    default:
                        outputStream.println("Command " + command + " not found!");
                        outputStream.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
