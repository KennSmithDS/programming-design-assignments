import Communications.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class to represent a client application that connects to the chat server
 * It's primary task is to provide a simple terminal UI for client to engage
 * With the chat server, e.g. logon, logoff, and message other clients
 * Properties:
 * - client socket
 * - string user name
 * - connected status
 * - able to logoff server (e.g. logged on)
 * Methods:
 * - main entry point for the client application
 * - get client socket for access from inside ServerConnection
 * - listen for commands from the user to send messages
 * - display the list of available commands
 */
public class Client {

  private Socket socket;
  private static final int DEFAULT_PORT = 3333;
  private static final String DEFAULT_HOST = "localhost";
  private String userName;
  private boolean connected;
  private boolean allowLogoff;
  private static final int CONNECT_TIMEOUT = 5000;
  private static final String MENU = "logon <username> -> sends a message to login to the chat server" +
      "\n" + "logoff -> sends a message to logoff from the chat server" + "\n" +
      "who -> sends a query for all users connected to the server" + "\n" +
      "@<user> <message> -> sends a message directly to a specific user" + "\n" +
      "@all <message> -> sends a message to all connected users" + "\n" +
      "!<user> -> sends a random insult to a specific user";

  /**
   * Constructor method for Client object that takes a host and port as parameters
   * @param host String of host location, either local or remote IP
   * @param port int of port to connect on
   */
  public Client(String host, int port) {
    try {
      this.userName = null;
      this.socket = new Socket(host, port);
      socket.setSoTimeout(10000);
//      this.socket = new Socket();
//      socket.connect(new InetSocketAddress(host,port),CONNECT_TIMEOUT);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.connected = false;
    this.allowLogoff = false;
  }

  /**
   * Method to get the client socket for access from ServerConnection
   * @return Socket client socket object connected to server
   */
  public Socket getClientSocket() {
    return this.socket;
  }

  /**
   * Main method to drive the execution of client instantiation
   * Allows user to logon, logoff, and create and send messages based on user input
   * @param args String array from command-line terminal
   * @throws IOException default exception for IO errors
   * @throws InvalidMessageException custom exception for invalid message type
   */
  public static void main(String[] args) throws IOException, InvalidMessageException {
    try {
      Client client = new Client(DEFAULT_HOST, DEFAULT_PORT);
      if (client.socket.isConnected()) {
        ObjectOutputStream messageOutStream = new ObjectOutputStream(client.getClientSocket().getOutputStream());
        ObjectInputStream messageInStream = new ObjectInputStream(client.getClientSocket().getInputStream());

        ServerConnection serverConnectionThread = new ServerConnection(client.getClientSocket(), messageInStream);
        new Thread(serverConnectionThread).start();
        client.listenForUserCommands(messageOutStream, serverConnectionThread);
      } else {
        System.out.println("Currently the chat server is full. Please try to join again later! :-)");
      }
    } catch (SocketTimeoutException e) {
      System.out.println("Currently the chat server is full. Please try to join again later! :-)");
    } finally {
      System.exit(1);
    }
  }

  /**
   * Method to handle user input from command-line in terminal
   * Primary method to handle the routing of user input to generate output messages and logon/logoff requests
   * @param messageOutStream message output stream
   * @param server ServerConnection listening to server
   * @throws IOException default exception for IO errors
   * @throws InvalidMessageException custom InvalidMessageException error
   */
  public void listenForUserCommands(ObjectOutputStream messageOutStream, ServerConnection server) throws IOException, InvalidMessageException {
    System.out.println("Welcome to the chat app, you can type '?' at any time to see list of available commands");
    Scanner console = new Scanner(System.in);

    // listen for client input on the console
    try {
      while (true) {

        // take whatever the client is writing
        String input = console.nextLine();

        // if client command is logoff, then break while true and close socket
        // this will be overwritten by the real logoff, which will send disconnect message
        // that will then wait for a disconnect response from the server
        if (input.toLowerCase().startsWith("logon")) {
          if(server.isConnected()) {
            System.out.println("You are already logged on and cannot log on again.");
          } else {
            // construct logon message
            String[] logonSplit = input.split("\\s+");
            this.userName = logonSplit[1];
            System.out.println("Attempting to login to server as @" + userName);
            String connectString = Identifier.CONNECT_MESSAGE.getIdentifierValue() + " " + userName.length() + " " + userName;

            // send connect message
            Communication logonMessage = Communication.communicationFactory(connectString);
            messageOutStream.writeObject(logonMessage);
          }

        } else if (input.toLowerCase().equals("logoff")) {
          if(!server.isConnected()) {
            System.out.println("You are not yet logged on. Please log on before trying to log off.");
          } else {
            String disconnectString = Identifier.DISCONNECT_MESSAGE.getIdentifierValue() + " " + userName.length() + " " + userName;
            Communication logoffMessage = Communication.communicationFactory(disconnectString);
            messageOutStream.writeObject(logoffMessage);

            while(!server.isAllowLogoff()) {
            }
            break;
          }

        } else if (input.equals("?")) {
          // show the available commands to user
          displayCommands();
        }

        else if(input.toLowerCase().startsWith("who")) {
          if(!server.isConnected()) {
            System.out.println("You are not yet logged on. Please log on before calling other commands.");
          } else {
            System.out.println("User query request from @" + userName + ".");
            String queryString = Identifier.QUERY_CONNECTED_USERS.getIdentifierValue() + " " + this.userName.length() + " " + this.userName;
            Communication queryMessage = Communication.communicationFactory(queryString);
            messageOutStream.writeObject(queryMessage);
          }
        }

        else if(input.toLowerCase().startsWith("@all")) {
          if(!server.isConnected()) {
            System.out.println("You are not yet logged on. Please log on before calling other commands.");
          } else {
            String[] allSplit = input.split("\\s+");
            String msg = "";
            for(int i = 1; i < allSplit.length; i++) {
              msg = msg + allSplit[i] + " ";
            }
            int msgLength = msg.length();
            String allString = Identifier.BROADCAST_MESSAGE.getIdentifierValue() + " " +
                this.userName.length() + " " + this.userName + " " + msgLength + " " + msg;
            Communication broadcastMessage = Communication.communicationFactory(allString);
            messageOutStream.writeObject(broadcastMessage);
          }
        }

        else if(input.toLowerCase().startsWith("@")) {
          if(!server.isConnected()) {
            System.out.println("You are not yet logged on. Please log on before calling other commands.");
          } else {
            String[] directSplit = input.split("\\s+");
            String recipient = directSplit[0].substring(1);
            int recpiSize = recipient.length();
            String msg = "";
            for(int i = 1; i < directSplit.length; i++) {
              msg = msg + directSplit[i] + " ";
            }
            int msgLength = msg.length();
            String directString = Identifier.DIRECT_MESSAGE.getIdentifierValue() + " " +
                this.userName.length() + " " + this.userName + " " + recpiSize + " " + recipient
                + " " + msgLength + " " + msg;
            Communication directMessage = Communication.communicationFactory(directString);
            messageOutStream.writeObject(directMessage);

          }
        }

        else if(input.toLowerCase().startsWith("!")) {
          if(!server.isConnected()) {
            System.out.println("You are not yet logged on. Please log on before calling other commands.");
          } else {
            String[] directSplit = input.split("\\s+");
            String recipient = directSplit[0].substring(1);
            int recpiSize = recipient.length();
            String insultString = Identifier.SEND_INSULT.getIdentifierValue() + " " +
                this.userName.length() + " " + this.userName + " " + recpiSize + " " + recipient;
            Communication insultMessage = Communication.communicationFactory(insultString);
            messageOutStream.writeObject(insultMessage);
          }

        }

        else {
          try {
            // construct other message/requests from client input and send to server
            Communication outboundMessage = Communication.communicationFactory(input);
            messageOutStream.writeObject(outboundMessage);
          } catch (InvalidMessageException e) {
            System.out.println("The command you entered is invalid, please try again. Do not include spaces before or after commands.");
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.exit(0);
    }
  }

  /**
   * Method to display the available commands on client application
   */
  private static void displayCommands() {
    System.out.println(MENU);
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
    Client client = (Client) o;
    return connected == client.connected &&
            allowLogoff == client.allowLogoff &&
            Objects.equals(socket, client.socket) &&
            Objects.equals(userName, client.userName);
  }

  /**
   * Override method for default hashCode()
   * @return int
   */
  @Override
  public int hashCode() {
    return Objects.hash(socket, userName, connected, allowLogoff);
  }

  /**
   * Override method for default toString()
   * @return String
   */
  @Override
  public String toString() {
    return "Client{" +
            "socket=" + socket +
            ", userName='" + userName + '\'' +
            ", connected=" + connected +
            ", allowLogoff=" + allowLogoff +
            '}';
  }

}
