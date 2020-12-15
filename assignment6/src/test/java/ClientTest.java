import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ClientTest {

  private Client c1;
  private Client c2;
  private Client c3;
  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 3333;
  private Server server;
  private Server serverTest;
  private ServerSocket serverSocket;
  private ServerSocket serverSocketTest;
  private ObjectOutputStream messageOutStream;
  private ObjectInputStream messageInStream;
  private InputOutput inputOutput = new InputOutput();
  private InputStream in;


  @Before
  public void setUp() throws Exception {
    server = new Server();
    serverSocket = server.getServerSocket();
    serverTest = new Server(8080);
    serverSocketTest = serverTest.getServerSocket();
    c1 = new Client(DEFAULT_HOST, DEFAULT_PORT);
    c2 = new Client(DEFAULT_HOST, DEFAULT_PORT);
    c3 = new Client(DEFAULT_HOST, 8080);
    //messageOutStream = new ObjectOutputStream(c1.getClientSocket().getOutputStream());
    //messageInStream = new ObjectInputStream(c1.getClientSocket().getInputStream());
    //ServerConnection serverConnectionThread = new ServerConnection(c1.getClientSocket(), messageInStream);
    //new Thread(serverConnectionThread).start();
  }

  @After
  public void tearDown() throws IOException {
    messageOutStream.close();
    messageInStream.close();
    serverSocket.close();
    server.stop();
    serverSocketTest.close();
    serverTest.stop();
  }

  @Test
  public void getClientSocket() {
    String input = "add 5";
    in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    String out = Main.readShit();
    Assert.assertEquals("add 5", out);
  }

  /*
  @Test
  public void listenForUserCommandsLogon() {
    String input = "logon isi";
    in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);

  }

   */

  @Test
  public void testEquals() throws IOException {

  }

  @Test
  public void testHashCode() {
  }

  @Test
  public void testToString() {
  }



}