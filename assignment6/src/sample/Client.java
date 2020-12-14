package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    
    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {
        try {
            socket = new Socket("localhost", 3333); // don't use a port below 1024
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
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
            try {
                dataOut.writeUTF(input);
                dataOut.flush();

                while (dataIn.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String reply = dataIn.readUTF();
                System.out.println(reply);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            dataIn.close();
            dataOut.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
