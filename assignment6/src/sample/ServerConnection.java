package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {
    private Socket socket;
    private ServerMulti server;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private boolean shouldRun = true;

    public ServerConnection(Socket socket, ServerMulti server) {
        super("ServerConnectionThread");
        this.socket = socket;
        this.server = server;
    }

    public void sendStringToClient(String text) throws IOException {
        try {
            dataOut.writeUTF(text);
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sentStringToAllClients(String text) throws IOException {
        for (int index=0; index<server.connections.size(); index++) {
            ServerConnection conn = server.connections.get(index);
            conn.sendStringToClient(text);
        }
    }

    public void run() {
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());

            while (shouldRun) {
                    while (dataIn.available() == 0) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                String textIn = dataIn.readUTF();
                sentStringToAllClients(textIn);
            }

            dataOut.close();
            dataIn.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
