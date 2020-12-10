import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

  private Socket socket;
  private DataInputStream dataIn;
  private DataOutputStream dataOut;

  public Client(int port, String host) {
    try {
      this.socket = new Socket(host, port);
      this.dataIn = new DataInputStream(socket.getInputStream());
      this.dataOut = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
