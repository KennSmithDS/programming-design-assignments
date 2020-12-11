import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientSession implements Runnable {

    private Socket socket;
    private Server server;
    private int port;
    private ObjectInputStream messageIn;
    private ObjectOutputStream messageOut;

    ClientSession (Socket socket, Server server, int port) {
        this.socket = socket;
        this.server = server;
        this.port = port;
    }

//    public void sendDirectMessage(Communications.Message message) {
//
//    }
//
//    public void sendGlobalMessage(Communications.Message message) {
//
//    }

    @Override
    public void run() {
        try {
            messageOut = new ObjectOutputStream(socket.getOutputStream());
            messageIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
