import Communications.Communication;
import Communications.InvalidMessageException;
import Communications.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class ClientSession implements Runnable {

    private Socket socket;
    private Server server;
    private int port;
    private boolean serverRun = true;
    private ObjectInputStream messageInStream;
    private ObjectOutputStream messageOutStream;

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
            messageOutStream = new ObjectOutputStream(socket.getOutputStream());
            messageInStream = new ObjectInputStream(socket.getInputStream());

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
            
            messageInStream.close();
            messageOutStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException | InvalidMessageException e) {
            e.printStackTrace();
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
