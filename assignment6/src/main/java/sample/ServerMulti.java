package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMulti {

    private ServerSocket serverSocket;
    ArrayList<ServerConnection> connections = new ArrayList<>();
    private static final int DEFAULT_PORT = 3333;
    private boolean shouldRun = true;

    public static void main(String[] args) throws IOException {
        new ServerMulti();
    }

    /**
     * constructor for the sample.Server class, which will take
     * @throws IOException
     */
    public ServerMulti() throws IOException {
        try {
            this.serverSocket = new ServerSocket(DEFAULT_PORT);
            while (shouldRun) {
                Socket clientSocket = serverSocket.accept();
                ServerConnection serverConn = new ServerConnection(clientSocket, this);
                serverConn.start();
                connections.add(serverConn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
