import Communications.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSession implements Runnable {

    private Socket socket;
    private Server server;
    private int port;
    private boolean serverRun = true;
    private ObjectInputStream messageInStream;
    private ObjectOutputStream messageOutStream;

    ClientSession (Socket socket, Server server, int port) throws IOException {
        this.socket = socket;
        this.server = server;
        this.port = port;
        messageInStream = new ObjectInputStream(socket.getInputStream());
        messageOutStream = new ObjectOutputStream(socket.getOutputStream());
    }

    protected Socket getClientSocket() { return this.socket; }

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
            while (serverRun) {
                while (messageInStream.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // decide what kind of message to send
                Message inboundMessage = (Message) messageInStream.readObject();
                messageHandler(inboundMessage);
            }

        } catch (IOException | ClassNotFoundException | InvalidMessageException e) {
            e.printStackTrace();

        } finally {
            try {
                messageInStream.close();
                messageOutStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void messageHandler(Message inboundMessage) throws InvalidMessageException {
        Communication commProtocol = null;
        int type;

        try {
            type = inboundMessage.getMessageIdValue();
        } catch (Exception e) {
            throw new InvalidMessageException("The received a message type from client that is invalid.");
        }
        switch(type) {
            case 19:
                Communication.communicationFactory("19 5 hello");
        }
    }

}
