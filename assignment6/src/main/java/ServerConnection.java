import Communications.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Socket socket;
    private ObjectInputStream messageInStream;

    ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.messageInStream = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {
                // if the server is not sending anything, sleep for a bit
                if (!socket.isConnected()) break;

                while (messageInStream.available() == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // receive the communication from server and interpret
//                Communication commProtocol = (Communication) messageInStream.readObject();
//                if (commProtocol instanceof DirectMessage) {
//                    DirectMessage inboundMessage = (DirectMessage) commProtocol;
//                    displayMessage(inboundMessage);
//                } else if (commProtocol instanceof BroadcastMessage) {
//                    BroadcastMessage inboundMessage = (BroadcastMessage) commProtocol;
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert messageInStream != null;
                messageInStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(Message inboundMessage) {
//    String sendingUsername = Arrays.toString(inboundMessage.getUsername());
//    String messageSent = Arrays.toString(inboundMessage.getMsg());
//    String directMessageContents = sendingUsername +": " + messageSent;
//    System.out.println(directMessageContents);
    }

}
