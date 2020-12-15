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
 * Class to represent the ClientSession, which are threads to be executed in the thread pool
 * ClientSessions are threads and therefore need to implement Runnable and override run()
 * The main responsibility of ClientSession is to route messages between clients and generate responses
 * Properties:
 * - client socket
 * - server instance
 * - socket port
 * - session connected status
 * - message input stream
 * - message output stream
 * Methods:
 * - handle the client session communications
 * - send direct message
 * - send broadcast message
 * - send connection response
 * - send disconnect response
 * - send user query response
 * - get all connected users
 */
public class ClientSession implements Runnable {

    private Socket socket;
    private Server server;
    private int port;
    private boolean isConnected;
    private ObjectInputStream messageInStream;
    private ObjectOutputStream messageOutStream;

    /**
     * Constructor for ClientSession object
     * Takes socket, server object and port as required parameters
     * Also instantiates connected status as false, and input/output streams as null
     * @param socket client socket
     * @param server instance of server
     * @param port server port
     * @throws IOException default exception for IO errors
     */
    ClientSession (Socket socket, Server server, int port) throws IOException {
        this.socket = socket;
        this.server = server;
        this.port = port;
        this.isConnected = false;
        this.messageInStream = null;
        this.messageOutStream = null;
    }

    /**
     * Override of run() method for Runnable interface
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
     * Method to handle the communications between clients and sending responses to clients
     * @throws InterruptedException default exception for thread interrupted
     * @throws IOException default exception for IO errors
     * @throws ClassNotFoundException default exception for class not found
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
        } catch (InvalidMessageException | ClassNotFoundException e) {
            e.printStackTrace();
            socket.close();
        }
    }

    /**
     * Method for sending a direct message to another client
     * Can be either DirectMessage or InsultMessage
     * @param message DirectMessage sent from client to another client
     * @throws IOException default exception for IO error
     */
    private void sendDirectMessage(DirectMessage message) throws IOException {
        try {
            String recipientUser = message.getRecipStringName();
            if (this.server.getClientSessions().containsKey(recipientUser)) {
                this.server.getClientSessions().get(recipientUser).messageOutStream.writeObject(message);
            } else {
                sendFailedMessage(message.getRecipStringName(), message.getStringName());
            }
        } catch (IOException | InvalidMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for sending an insult message to another client
     * @param message InsultMessage sent from client to another client
     * @throws IOException default exception for IO error
     */
    private void sendInsult(InsultMessage message) throws IOException {
        try {
            String recipientUser = message.getRecipStringName();
            if (this.server.getClientSessions().containsKey(recipientUser)) {
                this.server.getClientSessions().get(recipientUser).messageOutStream.writeObject(message);
            } else {
                sendFailedMessage(message.getRecipStringName(), message.getStringName());
            }
        } catch (IOException | InvalidMessageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send a failed message back to client when a user is not active on the chat server
     * @param recipStringName String of recipient client
     * @param stringName String of sender client
     * @throws InvalidMessageException custom InvalidMessageException error
     * @throws IOException default exception for IO error
     */
    private void sendFailedMessage(String recipStringName, String stringName) throws InvalidMessageException, IOException {
        String msg = "Cannot send direct message because user @" + recipStringName + " does not exist.";
        int msgLength = msg.length();
        String failMsg = Identifier.FAILED_MESSAGE.getIdentifierValue() + " " + msgLength + " " + msg;
        Communication fail = Communication.communicationFactory(failMsg);
        this.server.getClientSessions().get(stringName).messageOutStream.writeObject(fail);
    }

    /**
     * Method to send a broadcast message to all users except the sender
     * @param message BroadCast message object
     * @throws IOException default exception for IO error
     */
    private void sendBroadcastMessage(BroadcastMessage message) throws IOException {
        try {
            ConcurrentHashMap<String, ClientSession> sessions = this.server.getClientSessions();
            for (String clientName : sessions.keySet()) {
                if (!clientName.equals(message.getStringMsg())) {
                    sessions.get(clientName).messageOutStream.writeObject(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to generate and send a connection response to client
     * @param inboundMessage connection message inbound from client
     * @param status boolean status of client in the thread pool
     * @throws InvalidMessageException custom InvalidMessageException error
     * @throws IOException default exception for IO error
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
     * Method to generate and send a disconnect response to client
     * @param inboundMessage disconnect message inbound from client
     * @throws InvalidMessageException custom InvalidMessageException error
     * @throws IOException default exception for IO error
     */
    private void sendDisconnectResponse(Message inboundMessage) throws InvalidMessageException, IOException {
        Communication commProtocol;
        String disconnectString = "@" + inboundMessage.getStringName() + ", you are disconnecting from the chat server";
        String disconnectResponse = Identifier.DISCONNECT_RESPONSE.getIdentifierValue() + " " + disconnectString.length() + " " + disconnectString;
        commProtocol = Communication.communicationFactory(disconnectResponse);
        this.messageOutStream.writeObject(commProtocol);
    }

    /**
     * Method to generate a query response and send back to requesting client
     */
    private void sendUserQueryResponse() throws InvalidMessageException, IOException {
        Communication commProtocol;
        String userQueryString = Identifier.QUERY_USER_RESPONSE.getIdentifierValue() + " " + this.server.getClientCount() + " " + getAllConnectedUsers();
        commProtocol = Communication.communicationFactory(userQueryString);
        messageOutStream.writeObject(commProtocol);
    }

    /**
     * Method to get a String of all users and the lengths of their user names
     * @return String from StringBuilder of all users
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
     * Override method for default equals()
     * @param o object
     * @return boolean
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
     * Override method for default hashCode()
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(socket, server, port, messageInStream, messageOutStream);
    }

    /**
     * Override method for default toString()
     * @return String
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
