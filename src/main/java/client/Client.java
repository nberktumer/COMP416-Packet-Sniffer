package client;

import client.socket.ClientConnection;
import client.socket.SSLConnection;
import client.socket.TCPConnection;
import config.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private ClientConnection connection;

    private int connectionType = -1;
    private String ipAddress = "";
    private int port = -1;

    public Client() {
        getMasterInformationFromUser();
        connectToServer();
        commandLine();
    }

    /**
     * Gets the server ip address and port number from the user using scanner
     */
    private void getMasterInformationFromUser() {
        Scanner scanner = new Scanner(System.in);

        boolean isValidIpAddress = false;
        boolean isValidPort = false;
        boolean isValidConnectionType = false;

        while (!isValidConnectionType) {
            System.out.println("Please enter the connection type [1-SSL, 2-TCP]:");
            try {
                connectionType = scanner.nextInt();

                if (connectionType == 1 || connectionType == 2)
                    isValidConnectionType = true;
                else
                    System.err.println("Invalid connection type. Connection type must be either 1 or 2.");
            } catch (InputMismatchException e) {
                System.err.println("Invalid connection type. Connection type must be either 1 or 2.");
                scanner.next();
            }
        }

        while (!isValidIpAddress) {
            System.out.println("Please enter the ip address:");
            ipAddress = scanner.nextLine();

            try {
                InetAddress.getByName(ipAddress);
                isValidIpAddress = true;
            } catch (UnknownHostException e) {
                System.err.println("Invalid ip address.");
            }
        }

        while (!isValidPort) {
            System.out.println("Please enter the port number:");
            try {
                port = scanner.nextInt();

                if (port >= 0 && port <= 65535)
                    isValidPort = true;
                else
                    System.err.println("Invalid port number. Port number must be between 0 and 65535.");
            } catch (InputMismatchException e) {
                System.err.println("Invalid port number. Port number must be between 0 and 65535.");
                scanner.next();
            }
        }
    }

    private void connectToServer() {
        switch (connectionType) {
            case 1:
                connection = new SSLConnection(ipAddress, port);
                break;
            case 2:
                connection = new TCPConnection(ipAddress, port);
                break;
        }
    }

    private void commandLine() {
        Scanner scanner = new Scanner(System.in);
        String command;

        do {
            command = scanner.nextLine();

            try {
                String[] commandArr = command.split(" ");
                if(!Constants.COMMANDS.contains(commandArr[0]))
                    System.err.println("Command " + commandArr[0] + " not found.");

                switch (commandArr[0]) {
                    case "get":
                        if(commandArr.length != 2) {
                            System.err.println("Get command can have only 1 argument.");
                            continue;
                        }
                        connection.send(command);
                        break;
                    case "send":
                        break;
                }
            } catch (Exception e) {
                System.err.println("Invalid command");
                continue;
            }
        } while (!command.equals("exit"));
    }
}