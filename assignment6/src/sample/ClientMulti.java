package sample;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMulti {

    private ClientConnection clientConn;

    public static void main(String[] args) throws IOException {
        new ClientMulti();
    }

    public ClientMulti() throws IOException {
        try {
            Socket socket = new Socket("localhost", 3333); // don't use a port below 1024
            clientConn = new ClientConnection(socket, this);
            clientConn.start();

            listenForServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForServer() throws IOException {
        Scanner console = new Scanner(System.in);
        while (true) {
            while (!console.hasNextLine()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String input = console.nextLine();
            if (input.toLowerCase().equals("@quit")) {
                break;
            }
            clientConn.sendStringToServer(input);
        }
        clientConn.close();
    }
}
