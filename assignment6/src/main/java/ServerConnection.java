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

public class ServerConnection implements Runnable {

    private Socket socket;
    private ObjectInputStream messageInStream;
    private boolean connected;
    private boolean allowLogoff;

    ServerConnection(Socket socket, ObjectInputStream messageInStream) throws IOException {
        this.socket = socket;
        this.messageInStream = messageInStream;
        this.connected = false;
        this.allowLogoff = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAllowLogoff() {
        return allowLogoff;
    }

    @Override
    public void run() {
        try {
            handleServerConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Handles all responses
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
                    String insult = new SentenceGenerator(new Grammar(new JSONFileParser("./lib/insult_grammar.json")), null).buildSentence();
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


}
