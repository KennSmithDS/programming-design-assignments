import Communications.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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
//                    if (inboundMessage.getIdentifier() != Identifier.CONNECT_MESSAGE) {
                    if (!(inboundMessage instanceof ConnectMessage)) {
                        System.out.println("@" + inboundMessage.getStringName() + " attempted to send message while not logged in.");
                        // return failed message to user
                        sendConnectionResponse(inboundMessage, false);
//                    } else if (inboundMessage.getIdentifier() == Identifier.CONNECT_MESSAGE) {
                    } else {
                        // check if session in pool - not waiting for available spot
                        // add to client sessions
                        System.out.println("Inbound request from @" + inboundMessage.getStringName() + " to login to the chat server.");
                        server.addClientSession(inboundMessage.getStringName(), this);
                        System.out.println("There are " + server.getClientCount() + " clients connected");
                        // send connect response with boolean == true
                        //what if there are already max amount of clients?
                        sendConnectionResponse(inboundMessage, true);
                        isConnected = true;
                    }
                // let the user do other normal things once connected
                } else {
//                    if (inboundMessage.getIdentifier() == Identifier.DISCONNECT_MESSAGE) {
                    if (inboundMessage instanceof DisconnectMessage) {
                        System.out.println("Inbound request from @" + inboundMessage.getStringName() + " to logoff the chat server.");
                        // send disconnect message
                        sendDisconnectResponse(inboundMessage);

                        // remove user from sessions
                        isConnected = false;
                        server.dropClientSession(inboundMessage.getStringName()); // here
                        server.countDecrement();
                        System.out.println("There are " + server.getClientCount() + " clients connected");
                        socket.close();
                        break;
                    // handle other message types, i.e. direct, broadcast, insult and user query
                    } else if (inboundMessage instanceof DirectMessage){
                        sendDirectMessage((DirectMessage) inboundMessage);
                    } else if (inboundMessage instanceof BroadcastMessage) {
                        sendBroadcastMessage((BroadcastMessage) inboundMessage);
                    } else if (inboundMessage instanceof InsultMessage) {
                        sendInsult((InsultMessage) inboundMessage);
                    } else if (inboundMessage instanceof QueryUsers) {
                        sendUserQueryResponse();
                        // use function to query all client names byte[] from clientSessions hashmap
                    } else {
                        // failed message due to unknown type - base case handle
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
    private void sendDirectMessage(DirectMessage message) throws IOException {
        try {
            String recipientUser = message.getRecipStringName();
            if (this.server.getClientSessions().containsKey(recipientUser)) {
                this.server.getClientSessions().get(recipientUser).messageOutStream.writeObject(message);
            } else {
                String msg = "Cannot send direct message because user @" + message.getRecipStringName() + " does not exist.";
                int msgLength = msg.length();
                String failMsg = Identifier.FAILED_MESSAGE.getIdentifierValue() + " " + msgLength + " " + msg;
                Communication fail = Communication.communicationFactory(failMsg);
                this.server.getClientSessions().get(message.getStringName()).messageOutStream.writeObject(fail);
            }
        } catch (IOException | InvalidMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    private void sendInsult(InsultMessage message) throws IOException {
        try {
            String recipientUser = message.getRecipStringName();
            if (this.server.getClientSessions().containsKey(recipientUser)) {
                this.server.getClientSessions().get(recipientUser).messageOutStream.writeObject(message);
            } else {
                // logic to send failed message
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
    private void sendBroadcastMessage(BroadcastMessage message) throws IOException {
        try {
            ConcurrentHashMap<String, ClientSession> sessions = this.server.getClientSessions();
            for (String clientName : sessions.keySet()) {
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
        String responseString = "@" + inboundMessage.getStringName() + ", you are connected to chat server!";
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
        String disconnectString = "@" + inboundMessage.getStringName() + ", you are disconnecting from the chat server";
        String disconnectResponse = Identifier.DISCONNECT_RESPONSE.getIdentifierValue() + " " + disconnectString.length() + " " + disconnectString;
        commProtocol = Communication.communicationFactory(disconnectResponse);
        this.messageOutStream.writeObject(commProtocol);
    }

    /**
     *
     */
    private void sendUserQueryResponse() throws InvalidMessageException, IOException {
        Communication commProtocol;
        String userQueryString = Identifier.QUERY_USER_RESPONSE.getIdentifierValue() + " " + this.server.getClientCount() + " " + getAllConnectedUsers();
        commProtocol = Communication.communicationFactory(userQueryString);
        messageOutStream.writeObject(commProtocol);
    }

    /**
     *
     * @return
     */
    private String getAllConnectedUsers() {
        ConcurrentHashMap<String, ClientSession> sessions = this.server.getClientSessions();
        StringBuilder userQuery = new StringBuilder();
        for (String stringName : sessions.keySet()) {
//            String stringName = new String(name, StandardCharsets.UTF_8);
            userQuery.append(stringName.length());
            userQuery.append(" ");
            userQuery.append(stringName);
            userQuery.append(" ");
        }
        return userQuery.toString();
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
