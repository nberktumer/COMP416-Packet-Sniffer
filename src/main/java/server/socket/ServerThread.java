package server.socket;


import config.Constants;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class ServerThread extends Thread {
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private Socket socket;

    private Map<String, String> valueMap = new HashMap<>();

    /**
     * Creates a server thread on the input socket
     *
     * @param socket input socket to create a thread on
     */
    public ServerThread(Socket socket) {
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
                String[] commandArr = line.split(" ");
                String command = commandArr[0].toLowerCase();
                String[] arguments = line.substring(command.length()).trim().split(",");

                switch (command) {
                    case Constants.GET: {
                        if (arguments.length != 1) {
                            System.err.println("GET command must have only 1 argument");
                        }
                        String key = arguments[0];
                        if (valueMap.containsKey(key)) {
                            outputStream.println(valueMap.get(key));
                        } else {
                            outputStream.println("No stored value for " + key);
                        }
                        outputStream.flush();
                        break;
                    }
                    case Constants.SUBMIT: {
                        if (arguments.length != 2) {
                            System.err.println("SUBMIT command must contain 2 arguments.");
                            continue;
                        }

                        String key = arguments[0].trim();
                        String value = arguments[1].trim();

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
