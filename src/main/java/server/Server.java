package server;

import java.util.InputMismatchException;
import java.util.Scanner;


public class Server {
    private int connectionType = -1;
    private int port = -1;

    public Server() {
        getServerInfo();
    }

    private void getServerInfo() {
        Scanner scanner = new Scanner(System.in);

        boolean isValidConnectionType = false;
        boolean isValidPort = false;

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
}


