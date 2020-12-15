import Communications.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

// start server
// open socket
// define port
// start mock client
// create client session (server, socket, port)
// add session to client sessions
// create each message type
// from mock client, test each message inbound

public class ClientSessionTest {

    private Server testServer;
    private ServerSocket testServerSocket;
    private Socket testClientSocket1;
    private Socket testClientSocket2;
    private int testPort;
    private ClientSession testClientSession1;
    private String testClient1;
    private ClientSession testClientSession2;
    private String testClient2;
    private ConnectMessage testConnectMessage;
    private DirectMessage testDirectMessage;
    private BroadcastMessage testBroadcastMessage;
    private InsultMessage testInsultMessage;
    private QueryUsers testUserQuery;
    private DisconnectMessage testDisconnectMessage;
    private ObjectOutputStream testClient1OutputStream;
    private ObjectInputStream testClient1InputStream;
    private ObjectOutputStream testClient2OutputStream;
    private ObjectInputStream testClient2InputStream;
    private static final String DEFAULT_HOST = "localhost";

    @Before
    public void setUp() throws IOException {
        // server and client setup over socket
        this.testPort = ServerTest.getRandomPortMod();
        this.testServer = new Server(testPort);
        testServerSocket = testServer.getServerSocket();

        // first client connection
        testClientSocket1 = new Socket(DEFAULT_HOST, testPort);
        Socket testServerSocketConnected1 = testServerSocket.accept();
        testClientSession1 = new ClientSession(testServerSocketConnected1, testServer, testPort);
        testServer.addClientSession(testClient1, testClientSession1);
        Thread sessionThread1 = new Thread(testClientSession1);
        sessionThread1.start();
        testClient1OutputStream = new ObjectOutputStream(testClientSocket1.getOutputStream());
        testClient1InputStream = new ObjectInputStream(testClientSocket1.getInputStream());

        // second client connection
        testClientSocket2 = new Socket(DEFAULT_HOST, testPort);
        Socket testServerSocketConnected2 = testServerSocket.accept();
        testClientSession2 = new ClientSession(testServerSocketConnected2, testServer, testPort);
        testServer.addClientSession(testClient2, testClientSession2);
        Thread sessionThread2 = new Thread(testClientSession2);
        sessionThread2.start();
        testClient1OutputStream = new ObjectOutputStream(testClientSocket2.getOutputStream());
        testClient1InputStream = new ObjectInputStream(testClientSocket2.getInputStream());

        // create test messages
        
    }

    @Test
    public void run() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testToString() {
    }
}