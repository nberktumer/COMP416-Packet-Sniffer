package server.socket;

import config.Constants;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

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


public class SSLServerThread extends Thread {

    private SSLSocket socket;
    private BufferedReader inputStream;
    private BufferedWriter outputStream;

    private Map<String, String> valueMap = new HashMap<>();

    public SSLServerThread(SSLSocket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line;

            while ((line = inputStream.readLine()) != null) {
                String[] commandArr = line.split("\\|");
                String command = commandArr[0];

                switch (command) {
                    case Constants.GET:
                        break;
                    case Constants.SUBMIT:
                        String keyValPair = line.substring(line.indexOf(" "));
                        System.out.println(keyValPair);
                        break;
                    default:
                        outputStream.write("Command " + command + " not found!");
                        outputStream.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}