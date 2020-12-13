import Communications.Communication;
import Communications.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Socket socket;
    private ObjectInputStream messageInStream;

    ServerConnection(Socket socket, ObjectInputStream messageInStream) throws IOException {
        this.socket = socket;
        this.messageInStream = messageInStream;
    }

    @Override
    public void run() {
        try {
            handleServerConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleServerConnection() throws IOException {
        try {

            while (true) {
                Object serverInbound = messageInStream.readObject();
                System.out.println(serverInbound.toString());

                // receive the communication from server and interpret
    //                Communication commProtocol = (Communication) messageInStream.readObject();
    //                if (commProtocol instanceof DirectMessage) {
    //                    DirectMessage inboundMessage = (DirectMessage) commProtocol;
    //                    displayMessage(inboundMessage);
    //                } else if (commProtocol instanceof BroadcastMessage) {
    //                    BroadcastMessage inboundMessage = (BroadcastMessage) commProtocol;
    //                }
                }

        } catch (Exception e) {
                e.printStackTrace();
                messageInStream.close();
        }
    }

    public void displayMessage(Communication inboundCommunication) {
//    String sendingUsername = Arrays.toString(inboundMessage.getUsername());
//    String messageSent = Arrays.toString(inboundMessage.getMsg());
//    String directMessageContents = sendingUsername +": " + messageSent;
//    System.out.println(directMessageContents);
    }

}
