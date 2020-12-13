import Communications.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private static final int DEFAULT_PORT = 3333;
  private static final String DEFAULT_HOST = "localhost";
  private String userName;

  public Client(String host, int port) {
    try {
      this.userName = null;
      this.socket = new Socket(host, port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Socket getClientSocket() {
    return this.socket;
  }

  public static void main(String[] args) throws IOException, InvalidMessageException {
    Client client = new Client(DEFAULT_HOST, DEFAULT_PORT);
    ObjectOutputStream messageOutStream = new ObjectOutputStream(client.getClientSocket().getOutputStream());
    ObjectInputStream messageInStream = new ObjectInputStream(client.getClientSocket().getInputStream());

    ServerConnection serverConnectionThread = new ServerConnection(client.getClientSocket(), messageInStream);
    new Thread(serverConnectionThread).start();
    client.listenForUserCommands(messageOutStream);
  }

  public void listenForUserCommands(ObjectOutputStream messageOutStream) throws IOException, InvalidMessageException {
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

          // construct logon message
          String[] logonSplit = input.split("\\s+");
          userName = logonSplit[1];
          System.out.println("Attempting to login to server as " + userName);
          String connectString = Identifier.CONNECT_MESSAGE.getIdentifierValue() + " " + userName.length() + " " + userName;

          // send connect message
          Communication logonMessage = Communication.communicationFactory(connectString);
          messageOutStream.writeObject(logonMessage);

          // await connect response

        } else if (input.toLowerCase().equals("logoff")) {
          // send disconnect message
          String disconnectString = Identifier.DISCONNECT_MESSAGE.getIdentifierValue() + " " + userName.length() + " " + userName;
          Communication logoffMessage = Communication.communicationFactory(disconnectString);
          messageOutStream.writeObject(logoffMessage);

          // await disconnect response
          break;
        } else if (input.equals("?")) {
          // show the available commands to user
          displayCommands();
        } else {
          try {
            // construct other message/requests from client input and send to server
            Communication outboundMessage = Communication.communicationFactory(input);
            messageOutStream.writeObject(outboundMessage);
          } catch (InvalidMessageException e) {
            System.out.println("The command you entered is invalid, please try again...");
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("Logging off of chat app");
      System.exit(0);
    }
  }

  private static void displayCommands() {
    System.out.println("logon <username> -> sends a message to login to the chat server");
    System.out.println("logoff -> sends a message to logoff from the chat server");
    System.out.println("who -> sends a query for all users connected to the server");
    System.out.println("@<user> <message> -> sends a message directly to a specific user");
    System.out.println("@all <message> -> sends a message to all connected users");
    System.out.println("!<user> -> sends a random insult to a specific user");
  }

}
