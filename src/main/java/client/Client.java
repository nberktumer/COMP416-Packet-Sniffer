package client;

import client.socket.ConnectionFactory;
import client.socket.ConnectionType;
import client.socket.IClientConnection;
import config.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private IClientConnection connection;

    private ConnectionType connectionType;
    private String ipAddress = "";
    private int port = -1;

    public Client() {
        readConnectionInformationFromUser();
        connectToServer();
    }

    /**
     * Gets the server ip address and port number from the user using scanner
     */
    private void readConnectionInformationFromUser() {
        Scanner scanner = new Scanner(System.in);

        boolean isValidIpAddress = false;
        boolean isValidPort = false;
        boolean isValidConnectionType = false;

        while (!isValidConnectionType) {
            System.out.println("Please enter the connection type [1-SSL, 2-TCP]:");
            try {
                int connectionTypeRaw = scanner.nextInt();

                if (connectionTypeRaw != 1 && connectionTypeRaw != 2) {
                    throw new InputMismatchException();
                }
                isValidConnectionType = true;
                connectionType = connectionTypeRaw == 1 ? ConnectionType.SSL : ConnectionType.TCP;
            } catch (InputMismatchException e) {
                System.err.println("Invalid connection type. Connection type must be either 1 or 2.");
                scanner.next();
            }
        }

        while (!isValidIpAddress) {
            System.out.println("Please enter the ip address:");
            ipAddress = scanner.next();

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

                if (port < 0 || port > 65535) {
                    throw new InputMismatchException();
                }
                isValidPort = true;
            } catch (InputMismatchException e) {
                System.err.println("Invalid port number. Port number must be between 0 and 65535.");
                scanner.next();
            }
        }
    }

    private void connectToServer() {
        connection = ConnectionFactory.getInstance().createConnection(connectionType, ipAddress, port);
        connection.connect();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            input = scanner.nextLine();

            try {
                String[] commandArr = input.split(" ");
                String command = commandArr[0];
                String[] arguments = input.substring(command.length()).split(",");

                switch (command.toLowerCase()) {
                    case Constants.GET: {
                        if (arguments.length != 1) {
                            System.err.println("GET command can have only 1 argument.");
                            continue;
                        }
                        System.out.println(connection.send(input));
                        break;
                    }
                    case Constants.SUBMIT: {
                        if (arguments.length != 2) {
                            System.err.println("SUBMIT command must contain 2 arguments.");
                            continue;
                        }

                        System.out.println(connection.send(input));
                        break;
                    }
                    default:
                        System.err.println("Command " + commandArr[0] + " not found.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Invalid command");
            }
        } while (!input.equals("exit"));
    }
}
