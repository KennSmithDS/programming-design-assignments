import Communications.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ClientSession implements Runnable {

    private Socket socket;
    private Server server;
    private int port;
    private boolean isConnected;
    private ObjectInputStream messageInStream;
    private ObjectOutputStream messageOutStream;

    /**
     *
     * @param socket
     * @param server
     * @param port
     * @throws IOException
     */
    ClientSession (Socket socket, Server server, int port) throws IOException {
        this.socket = socket;
        this.server = server;
        this.port = port;
        this.isConnected = false;
        this.messageInStream = null;
        this.messageOutStream = null;
        this.server.countIncrement();
    }

    public ObjectInputStream getMessageInStream() {
        return this.messageInStream;
    }

    public ObjectOutputStream getMessageOutStream() {
        return this.messageOutStream;
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            handleClientSession();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    private void handleClientSession() throws InterruptedException, IOException {
        try {
            System.out.println("New client session created.");
            this.messageOutStream = new ObjectOutputStream(socket.getOutputStream());
            this.messageInStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                // decide what kind of message to send
                Message inboundMessage = (Message) messageInStream.readObject();
                // block user from doing anything until connected
                if (!isConnected) {
                    if (inboundMessage.getIdentifier() != Identifier.CONNECT_MESSAGE) {
                        System.out.println("@" + inboundMessage.getStringName() + " attempted to send message while not logged in.");
                        // return failed message to user
                        sendConnectionResponse(inboundMessage, false);
                        continue;
                    } else if (inboundMessage.getIdentifier() == Identifier.CONNECT_MESSAGE) {
                        // check if session in pool - not waiting for available spot
                        // add to client sessions
                        System.out.println("Inbound request from @" + inboundMessage.getStringName() + " to login to chat.");
                        server.addClientSession(inboundMessage.getUsername(), this);
                        // send connect response with boolean == true
                        //what if there are already max amount of clients?
                        sendConnectionResponse(inboundMessage, true);
                        isConnected = true;

                    }
                // let the user do other normal things once connected
                } else {
                    if (inboundMessage.getIdentifier() == Identifier.DISCONNECT_MESSAGE) {
                        System.out.println("@" + inboundMessage.getStringName() + " requested to logoff the chat server.");
                        // send disconnect message
                        sendDisconnectResponse(inboundMessage);

                        // remove user from sessions
                        isConnected = false;
                        server.dropClientSession(inboundMessage.getUsername(), this);
                        server.countDecrement();
                        server.showClientCount();
                        socket.close();
                        break;
                    } else {
                        // handle other message types, i.e. direct, broadcast, insult and user query
                    }
                }
            }
        } catch (ClassNotFoundException | InvalidMessageException e) {
            e.printStackTrace();
            socket.close();
        }
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    public void sendDirectMessage(Communications.DirectMessage message) throws IOException {
        try {
            byte[] recipientUser = message.getUsername();
            if (this.server.getClientSessions().containsKey(recipientUser)) {
                this.server.getClientSessions().get(recipientUser).messageOutStream.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    public void sendBroadcastMessage(Communications.BroadcastMessage message) throws IOException {
        try {
            HashMap<byte[], ClientSession> sessions = this.server.getClientSessions();
            for (byte[] clientName : sessions.keySet()) {
                sessions.get(clientName).messageOutStream.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param inboundMessage
     * @param status
     * @throws InvalidMessageException
     * @throws IOException
     */
    private void sendConnectionResponse(Message inboundMessage, boolean status) throws InvalidMessageException, IOException {
        Communication commProtocol;
        byte[] userName = inboundMessage.getUsername();
        String responseString = "User @" + inboundMessage.getStringName() + " connection status to server on port: " + this.server.getServerPort();
        int responseSize = responseString.length();
        String connectResponse = Identifier.CONNECT_RESPONSE.getIdentifierValue() + " " + responseSize + " " + responseString +" " + status;
        commProtocol = Communication.communicationFactory(connectResponse);
        this.messageOutStream.writeObject(commProtocol);
    }

    /**
     *
     * @param inboundMessage
     * @throws InvalidMessageException
     * @throws IOException
     */
    private void sendDisconnectResponse(Message inboundMessage) throws InvalidMessageException, IOException {
        Communication commProtocol;
        String disconnectString = "@" + inboundMessage.getStringName() + ", you are no longer connected to the chat server";
        String disconnectResponse = Identifier.DISCONNECT_RESPONSE.getIdentifierValue() + " " + disconnectString.length() + " " + disconnectString;
        commProtocol = Communication.communicationFactory(disconnectResponse);
        this.messageOutStream.writeObject(commProtocol);
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSession that = (ClientSession) o;
        return port == that.port &&
                Objects.equals(socket, that.socket) &&
                Objects.equals(server, that.server) &&
                Objects.equals(messageInStream, that.messageInStream) &&
                Objects.equals(messageOutStream, that.messageOutStream);
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(socket, server, port, messageInStream, messageOutStream);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "ClientSession{" +
                "socket=" + socket +
                ", server=" + server +
                ", port=" + port +
                ", messageInStream=" + messageInStream +
                ", messageOutStream=" + messageOutStream +
                '}';
    }
}
