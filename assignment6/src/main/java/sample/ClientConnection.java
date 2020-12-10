package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {

    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private boolean shouldRun = true;

    public ClientConnection(Socket socket, ClientMulti client) {
        this.socket = socket;
    }

    public void sendStringToServer(String text) throws IOException {
        try {
            dataOut.writeUTF(text);
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void run() {
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());

            while (shouldRun) {
                try {
                    while (dataIn.available() == 0) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close() {
        try {
            dataIn.close();
            dataOut.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//    String reply = dataIn.readUTF();
//        System.out.println(reply);
