package server.socket;

import config.Constants;
import server.data.IKeyStorage;
import server.data.StorageManager;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {
    private final IKeyStorage storage;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private Socket socket;

    /**
     * Creates a server thread on the input socket
     *
     * @param socket input socket to create a thread on
     */
    public ServerThread(Socket socket) {
        this.socket = socket;
        this.storage = StorageManager.getInstance().getStorage();
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
                System.out.println("Got: " + line);
                int receivedAck = Character.getNumericValue(line.charAt(0));
                line = line.substring(1);
                String[] commandArr = line.split(" ");
                String command = commandArr[0].toLowerCase();
                String[] arguments = line.substring(command.length()).trim().split(",");

                switch (command) {
                    case Constants.GET: {
                        if (arguments.length != 1) {
                            outputStream.println(receivedAck + Constants.OK + "GET command must have only 1 argument");
                            outputStream.flush();
                            continue;
                        }
                        String key = arguments[0];
                        if (storage.containsKey(key)) {
                            outputStream.println(receivedAck + Constants.OK + storage.getKey(key));
                        } else {
                            outputStream.println(receivedAck + Constants.OK + "No stored value for " + key);
                        }
                        outputStream.flush();
                        break;
                    }
                    case Constants.SUBMIT: {
                        if (arguments.length != 2) {
                            outputStream.println(receivedAck + Constants.OK + "SUBMIT command must contain 2 arguments.");
                            outputStream.flush();
                            continue;
                        }

                        String key = arguments[0].trim();
                        String value = arguments[1].trim();

                        storage.setKey(key, value);
                        outputStream.println(receivedAck + Constants.OK);
                        outputStream.flush();
                        break;
                    }
                    default:
                        outputStream.println(receivedAck + Constants.OK + "Command " + command + " not found!");
                        outputStream.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
