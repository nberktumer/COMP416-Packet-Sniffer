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
                String[] commandArr = line.split(" ");
                String command = commandArr[0].toLowerCase();

                switch (command) {
                    case Constants.GET: {
                        String key = line.substring(Constants.GET.length()).trim();
                        if (valueMap.containsKey(key)) {
                            outputStream.println(valueMap.get(key));
                        } else {
                            outputStream.println("No stored value for " + key);
                        }
                        outputStream.flush();
                        break;
                    }
                    case Constants.SUBMIT: {
                        String keyValuePair = line.substring(Constants.SUBMIT.length());
                        String[] keyValueArr = keyValuePair.split(",");

                        if (keyValueArr.length != 2) {
                            System.err.println("SUBMIT command must contain 2 arguments.");
                            continue;
                        }

                        String key = keyValueArr[0].trim();
                        String value = keyValueArr[1].trim();

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
