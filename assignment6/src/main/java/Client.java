import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private static final int DEFAULT_PORT = 3333;
  private static final String DEFAULT_HOST = "localhost";
  private ObjectInputStream dataIn;
  private ObjectOutputStream dataOut;

  public Client(String host, int port) {
    try {
      this.socket = new Socket(host, port);
      this.dataIn = new ObjectInputStream(socket.getInputStream());
      this.dataOut = new ObjectOutputStream(socket.getOutputStream());
      listenToChatServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    new Client(DEFAULT_HOST, DEFAULT_PORT);
  }

  public void listenToChatServer() {
    Scanner console = new Scanner(System.in);
    while (true) {
      while (!console.hasNextLine()) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      String input = console.nextLine();
      if (input.toLowerCase().equals("@quit")) {
        break;
      }
    }
    try {
      dataIn.close();
      dataOut.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
