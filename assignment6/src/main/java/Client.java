import Communications.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private static final int DEFAULT_PORT = 3333;
  private static final String DEFAULT_HOST = "localhost";
  private ObjectOutputStream messageOutStream;

  public Client(String host, int port) {
    try {
      this.socket = new Socket(host, port);
      this.messageOutStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Socket getClientSocket() {
    return this.socket;
  }

  public static void main(String[] args) throws IOException, InvalidMessageException {
    Client client = new Client(DEFAULT_HOST, DEFAULT_PORT);
    ServerConnection serverConnectionThread = new ServerConnection(client.getClientSocket());
    new Thread(serverConnectionThread).start();
    client.listenForUserCommands();
  }

  public void listenForUserCommands() throws IOException, InvalidMessageException {
    Scanner console = new Scanner(System.in);

    // listen for client input on the console
    try {
      while (true) {
        // if client is not typing anything, sleep for a bit
        while (!console.hasNextLine()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        // take whatever the client is writing
        String input = console.nextLine();

        // if client command is logoff, then break while true and close socket
        // this will be overwritten by the real logoff, which will send disconnect message
        // that will then wait for a disconnect response from the server
        if (input.toLowerCase().equals("logoff")) {
          break;
        } else if (input.equals("?")) {
          displayCommands();
        } else {

          try {
            // construct a message from client input, and send to server
            Communication outboundMessage = Communication.communicationFactory(input);
            messageOutStream.writeObject(outboundMessage);
          } catch (InvalidMessageException | IOException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        messageOutStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void displayCommands() {
    System.out.println("logoff -> sends a message to disconnect from the server");
    System.out.println("who -> sends a query for all users connected to the server");
    System.out.println("@user -> sends a message directly to a specific user");
    System.out.println("@all -> sends a message to all connected users");
    System.out.println("!user -> sends a random insult to a specific user");
  }



}
