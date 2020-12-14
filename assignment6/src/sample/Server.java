package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
//    private ObjectInputStream messageInput;
//    private ObjectOutputStream messageOutput;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 3333;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    /**
     * constructor for the sample.Server class, which will take
     * @throws IOException
     */
    public Server() throws IOException {
        try {
            this.serverSocket = new ServerSocket(DEFAULT_PORT);
            this.clientSocket = serverSocket.accept();
            this.dataIn = new DataInputStream(clientSocket.getInputStream());
            this.dataOut = new DataOutputStream(clientSocket.getOutputStream());
//            this.messageInput = new ObjectInputStream(clientSocket.getInputStream());
//            this.messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            listenForClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // this is what will become multithreaded
    public void listenForClient() {
        while (true) {
            try {
                while (dataIn.available() == 0)
//                while (messageInput.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                String input = dataIn.readUTF();
                dataOut.writeUTF(input);
//                String inboundMessage = messageInput.readUTF();
//                messageOutput.writeUTF(inboundMessage);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
//            messageInput.close();
//            messageOutput.close();
            this.clientSocket.close();
            this.serverSocket.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

}
