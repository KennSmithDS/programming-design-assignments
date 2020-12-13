import Communications.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

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

    private void handleClientSession() throws InterruptedException, IOException {
        try {
            System.out.println("New client session created");
            this.messageOutStream = new ObjectOutputStream(socket.getOutputStream());
            this.messageInStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                // decide what kind of message to send
                if (messageInStream.available() > 0) {
                    Message inboundMessage = (Message) messageInStream.readObject();
                    if (!isConnected) {
                        if (inboundMessage.getIdentifier() != Identifier.CONNECT_MESSAGE) {
                            // return failed message to user
                            continue;
                        } else if (inboundMessage.getIdentifier() == Identifier.CONNECT_MESSAGE) {
                            // check if session in pool - not waiting for available spot
                            // add to client sessions
                            System.out.println("");
                            server.addClientSession(inboundMessage.getUsername(), this);
                            // send connect response with boolean == true
                            isConnected = true;
                        }
                    } else {
                        messageHandler(inboundMessage);
                    }
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException | InvalidMessageException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Closing object input/output streams and client socket");
                server.countDecrement();
                messageInStream.close();
                messageOutStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void sendGlobalMessage(Communications.BroadcastMessage message) throws IOException {
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
     * @throws InvalidMessageException
     */
    private void messageHandler(Message inboundMessage) throws InvalidMessageException, IOException {
        Communication commProtocol = null;
        Identifier messageType;

        try {
            messageType = inboundMessage.getIdentifier();
        } catch (Exception e) {
            throw new InvalidMessageException("The received a message type from client that is invalid.");
        }
        switch(messageType) {
            // make a response message if receive a connect
            case CONNECT_MESSAGE:
                byte[] userName = inboundMessage.getUsername();
                String responseString = "User " + Arrays.toString(userName) + " connected to server on port: " + this.server.getServerPort();
                int responseSize = responseString.length();
                String connectResponse = "20" + " " + responseSize + " " + responseString +" " + "true";
                commProtocol = Communication.communicationFactory(connectResponse);
                this.messageOutStream.writeObject(commProtocol);
                break;
            case DISCONNECT_MESSAGE:
                break;
            case QUERY_CONNECTED_USERS:
                break;
            case BROADCAST_MESSAGE:
                break;
            case DIRECT_MESSAGE:
                break;
            case SEND_INSULT:
                break;
        }
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
