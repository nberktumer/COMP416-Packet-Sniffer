package server.socket;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;

/**
 * Copyright [2017] [Yahya Hassanzadeh-Nazarabadi]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SSLServer extends Thread {
    private final String SERVER_KEYSTORE_FILE = "server.jks";
    private final String SERVER_KEYSTORE_PASSWORD = "kocuniv";
    private final String SERVER_KEY_PASSWORD = "kocuniv";

    private SSLServerSocket sslServerSocket;

    public SSLServer(int port) {
        try {
            // Instance of SSL protocol with TLS variance
            SSLContext sc = SSLContext.getInstance("TLS");

            // Key management of the server
            char ksPass[] = SERVER_KEYSTORE_PASSWORD.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(SERVER_KEYSTORE_FILE), ksPass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_KEY_PASSWORD.toCharArray());
            sc.init(kmf.getKeyManagers(), null, null);

            // SSL socket factory which creates SSLSockets
            SSLServerSocketFactory sslServerSocketFactory = sc.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

            System.out.println("SSL server is up and running on port " + port);
            while (true) {
                listenAndAccept();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Listens to the line and starts a connection on receiving a request with the client
     */
    private void listenAndAccept() {
        try {
            Socket socket = sslServerSocket.accept();
            System.out.println("An SSL connection was established with a client on the address of " + socket.getRemoteSocketAddress());
            ServerThread sslServerThread = new ServerThread(socket);
            sslServerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server Class.Connection establishment error inside listen and accept function");
        }
    }
}
