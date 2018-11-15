import client.Client;
import server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainProgram {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isValidType = false;
        int type = -1;

        while (!isValidType) {
            System.out.println("Please select the application type (1-ServerConnection, 2-ClientConnection):");
            try {
                type = scanner.nextInt();
                if (type == 1 || type == 2)
                    isValidType = true;
                else
                    System.err.println("Invalid application type. Please select either 1 (ServerConnection) or 2 (ClientConnection)");
            } catch (InputMismatchException e) {
                System.err.println("Invalid application type. Please select either 1 (ServerConnection) or 2 (ClientConnection)");
                scanner.next();
            }
        }

        if (type == 1)
            new Server();
        else if (type == 2)
            new Client();
        else
            System.err.println("Unexpected application type.");
    }
}
