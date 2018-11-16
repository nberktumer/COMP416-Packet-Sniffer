package server.socket;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private ServerSocket serverSocket;

    /**
     * Initiates a server socket on the input port, listens to the line, on
     * receiving an incoming connection creates and starts a ServerThread on the
     * client
     *
     * @param port port number to start listening on
     */
    public TCPServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);

            System.out.println("TCP Server started on " + Inet4Address.getLocalHost());

            while (true) {
                listenConnections();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Server: Constructor exception on opening a server socket");
        }
    }

    /**
     * Listens to the line and starts a connection on receiving a request from the
     * client The connection is started and initiated as a ServerThread object
     */
    private void listenConnections() {
        try {
            Socket socket = serverSocket.accept();

            System.out.println("A connection was established with a client on the address of " + socket.getRemoteSocketAddress());

            ServerThread serverThread = new ServerThread(socket);
            serverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Server Class: Connection establishment error inside listenConnections function");
        }
    }

}
