import Communications.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private static final int DEFAULT_PORT = 3333;
  private static final String DEFAULT_HOST = "localhost";
  private String userName;
  private boolean connected;
  private boolean allowLogoff;

  public Client(String host, int port) {
    try {
      this.userName = null;
      this.socket = new Socket(host, port);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.connected = false;
    this.allowLogoff = false;
  }

  public Socket getClientSocket() {
    return this.socket;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public boolean isAllowLogoff() {
    return allowLogoff;
  }

  public void setAllowLogoff(boolean allowLogoff) {
    this.allowLogoff = allowLogoff;
  }

  public static void main(String[] args) throws IOException, InvalidMessageException {
    Client client = new Client(DEFAULT_HOST, DEFAULT_PORT);
    if (client.socket.isConnected()) {
      ObjectOutputStream messageOutStream = new ObjectOutputStream(client.getClientSocket().getOutputStream());
      ObjectInputStream messageInStream = new ObjectInputStream(client.getClientSocket().getInputStream());

      ServerConnection serverConnectionThread = new ServerConnection(client.getClientSocket(), messageInStream);
      new Thread(serverConnectionThread).start();
      client.listenForUserCommands(messageOutStream, messageInStream, serverConnectionThread);
    }
  }

  public void listenForUserCommands(ObjectOutputStream messageOutStream, ObjectInputStream messageInStream, ServerConnection server) throws IOException, InvalidMessageException {
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


  private static void displayCommands() {
    System.out.println("logon <username> -> sends a message to login to the chat server");
    System.out.println("logoff -> sends a message to logoff from the chat server");
    System.out.println("who -> sends a query for all users connected to the server");
    System.out.println("@<user> <message> -> sends a message directly to a specific user");
    System.out.println("@all <message> -> sends a message to all connected users");
    System.out.println("!<user> -> sends a random insult to a specific user");
  }

}
