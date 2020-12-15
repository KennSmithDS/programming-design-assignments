import Communications.BroadcastMessage;
import Communications.Communication;
import Communications.ConnectResponse;
import Communications.DirectMessage;
import Communications.DisconnectResponse;
import Communications.FailedResponse;
import Communications.Identifier;
import Communications.InsultMessage;
import Communications.InvalidMessageException;
import Communications.Message;
import Communications.QueryResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class to represent the ServerConnection, which are single threads to be executed by each client
 * ServerConnections are threads and therefore need to implement Runnable and override run()
 * The main responsibility of ServerConnection is to receive message from other users and display responses from the server
 * Properties:
 * - client socket
 * - message input stream
 * - server connection status
 * - if client is allowed to logoff
 * Methods:
 * - get connected status
 * - check if allowed to logoff
 * - handle server connection
 */
public class ServerConnection implements Runnable {

    private Socket socket;
    private ObjectInputStream messageInStream;
    private boolean connected;
    private boolean allowLogoff;
    private static final String insultJson = "./lib/insult_grammar.json";

    /**
     * Constructor method for ServerConnection that takes socket and message input stream (from client)
     * @param socket Socket of client socket for connection to server
     * @param messageInStream ObjectInputStream to receive messages/responses from server
     * @throws IOException default exception for IO errors
     */
    ServerConnection(Socket socket, ObjectInputStream messageInStream) throws IOException {
        this.socket = socket;
        this.messageInStream = messageInStream;
        this.connected = false;
        this.allowLogoff = false;
    }

    /**
     * Method to check if a client is connected to server or not via logon
     * @return boolean
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Method to check if client is allowed to logoff, has to be logged in first
     * @return boolean
     */
    public boolean isAllowLogoff() {
        return allowLogoff;
    }

    /**
     * Override of run() method for Runnable interface
     */
    @Override
    public void run() {
        try {
            handleServerConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle all inbound object stream messages/responses over server connection
     * @throws IOException default exception for IO errors
     */
    private void handleServerConnection() throws IOException {
        try {

            while (true) {
                Object serverInbound = messageInStream.readObject();
                //System.out.println("The message is: " + serverInbound.toString());

                if(serverInbound instanceof Communications.ConnectResponse) {
                    this.connected = ((ConnectResponse) serverInbound).isSuccess();
                    System.out.println(((ConnectResponse) serverInbound).getStringMsg());
                }

                else if(serverInbound instanceof Communications.DisconnectResponse) {
                    System.out.println(((DisconnectResponse) serverInbound).getStringMsg());
                    this.allowLogoff = true;
                    break;
                }

                else if(serverInbound instanceof Communications.QueryResponse) {
                    System.out.println("There are " + ((QueryResponse) serverInbound).getNumUsers() +
                        " users connected.");
                    ((QueryResponse) serverInbound).printUsers();
                }

                else if(serverInbound instanceof Communications.BroadcastMessage) {
                    System.out.println("Broadcast message from : @" + ((BroadcastMessage) serverInbound).getStringName());
                    System.out.println("MESSAGE: " + ((BroadcastMessage) serverInbound).getStringMsg());
                }

                else if(serverInbound instanceof Communications.DirectMessage) {
                    System.out.println("Direct message from : @" + ((DirectMessage) serverInbound).getStringName());
                    System.out.println("MESSAGE: " + ((DirectMessage) serverInbound).getStringMsg());
                }

                else if(serverInbound instanceof Communications.FailedResponse) {
                    System.out.println("FAILURE.");
                    System.out.println(((FailedResponse) serverInbound).getStringMsg());
                }

                else if(serverInbound instanceof Communications.InsultMessage) {
                    System.out.println("INSULT from @" + ((InsultMessage) serverInbound).getStringName());
                    String insult = new SentenceGenerator(new Grammar(new JSONFileParser(insultJson)), null).buildSentence();
                    System.out.println(insult);
                }

            }
            messageInStream.close();
            System.out.println("Closed stream.");

        } catch (Exception e) {
            e.printStackTrace();
            messageInStream.close();
            System.out.println("Caught error and closed stream.");
        }

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
        ServerConnection that = (ServerConnection) o;
        return connected == that.connected &&
                allowLogoff == that.allowLogoff &&
                Objects.equals(socket, that.socket) &&
                Objects.equals(messageInStream, that.messageInStream);
    }

    /**
     * Override method for default hashCode()
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(socket, messageInStream, connected, allowLogoff);
    }

    /**
     * Override method for default toString()
     * @return String
     */
    @Override
    public String toString() {
        return "ServerConnection{" +
                "socket=" + socket +
                ", messageInStream=" + messageInStream +
                ", connected=" + connected +
                ", allowLogoff=" + allowLogoff +
                '}';
    }
}
