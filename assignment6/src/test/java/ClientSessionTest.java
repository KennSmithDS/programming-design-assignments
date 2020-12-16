import Communications.*;
import com.sun.java.swing.ui.CommonUI;
import org.junit.After;
import org.junit.Assert;
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
    private Socket testServerSocketConnected1;
    private Socket testServerSocketConnected2;
    private int testPort;
    private ClientSession testClientSession1;
    private String testClient1;
    private ClientSession testClientSession2;
    private String testClient2;
    private Communication testConnectMessage1;
    private Communication testConnectMessage2;
    private Communication testDisconnectMessage;
    private Communication testQueryUsers;
    private Communication testBroadcastMessage;
    private Communication testDirectMessage;
    private Communication testInsultMessage;
    private ObjectOutputStream testClient1OutputStream;
    private ObjectInputStream testClient1InputStream;
    private ObjectOutputStream testClient2OutputStream;
    private ObjectInputStream testClient2InputStream;
    private static final String DEFAULT_HOST = "localhost";

    @After
    public void tearDown() {
        this.testServer.stop();
    }

    @Before
    public void setUp() throws IOException, InvalidMessageException {
        // server and client setup over socket
        this.testPort = ServerTest.getRandomPortMod();
        this.testServer = new Server(testPort);
        this.testServerSocket = testServer.getServerSocket();

        // first client connection
        this.testClientSocket1 = new Socket(DEFAULT_HOST, testPort);
        this.testServerSocketConnected1 = testServerSocket.accept();
        this.testClientSession1 = new ClientSession(testServerSocketConnected1, testServer, testPort);
//        this.testClientSession1.setConnected(true);
        this.testClient1 = "me";
        this.testServer.addClientSession(testClient1, testClientSession1);
        Thread sessionThread1 = new Thread(testClientSession1);
        sessionThread1.start();
        this.testClient1OutputStream = new ObjectOutputStream(testClientSocket1.getOutputStream());
        this.testClient1InputStream = new ObjectInputStream(testClientSocket1.getInputStream());

        // second client connection
        this.testClientSocket2 = new Socket(DEFAULT_HOST, testPort);
        this.testServerSocketConnected2 = testServerSocket.accept();
        this.testClientSession2 = new ClientSession(testServerSocketConnected2, testServer, testPort);
//        this.testClientSession1.setConnected(true);
        this.testClient2 = "you";
        this.testServer.addClientSession(testClient2, testClientSession2);
        Thread sessionThread2 = new Thread(testClientSession2);
        sessionThread2.start();
        this.testClient2OutputStream = new ObjectOutputStream(testClientSocket2.getOutputStream());
        this.testClient2InputStream = new ObjectInputStream(testClientSocket2.getInputStream());

        // create test messages
        this.testConnectMessage1 = Communication.communicationFactory("19 2 me");
        this.testConnectMessage2 = Communication.communicationFactory("19 3 you");
        this.testDisconnectMessage = Communication.communicationFactory("21 2 me");
        this.testQueryUsers = Communication.communicationFactory("23 2 me");
        this.testBroadcastMessage = Communication.communicationFactory("25 2 me 11 hello world");
        this.testDirectMessage = Communication.communicationFactory("26 2 me 3 you 8 hi there");
        this.testInsultMessage = Communication.communicationFactory("28 2 me 3 you");
    }

    @Test
    public void receiveConnectMessage_Pass() throws IOException, ClassNotFoundException {
        testClient1OutputStream.writeObject(testConnectMessage1);
//        for some reason, unable to fetch the servers input stream for a given session, throws a thread corruption error
//        Object inboundMessage = this.testServer.getClientSessions().get(testClient1).getMessageInputStream().readObject();
//        boolean correctMessage = inboundMessage instanceof ConnectMessage;
        Object serverResponse = this.testClient1InputStream.readObject();
        boolean correctResponse = serverResponse instanceof ConnectResponse;
        Assert.assertTrue(correctResponse);
    }

    @Test
    public void receiveConnectMessage_Fail() throws IOException, ClassNotFoundException {
        testClient1OutputStream.writeObject(testConnectMessage1);
        testClient1OutputStream.writeObject(testConnectMessage1);
        Object serverResponse = this.testClient1InputStream.readObject();
        boolean correctResponse = serverResponse instanceof ConnectResponse;
        Assert.assertTrue(correctResponse);
        boolean connected = ((ConnectResponse) serverResponse).isSuccess();
        Assert.assertFalse(connected);
    }

    @Test
    public void receiveDisconnectMessage() throws IOException, ClassNotFoundException, InterruptedException {
//        testClient1OutputStream.writeObject(testDisconnectMessage);
//        Object serverResponse2 = this.testClient1InputStream.readObject();
//        boolean correctResponse = serverResponse2 instanceof DisconnectMessage;
//        Assert.assertTrue(correctResponse);
    }

    @Test
    public void receiveQueryUsersMessage() {

    }

    @Test
    public void receiveBroadcastMessage() {

    }

    @Test
    public void receiveDirectMessage_Pass() {

    }

    @Test
    public void receiveDirectMessage_Fail() {

    }

    @Test
    public void receiveInsultMessage() {

    }

    @Test
    public void testEquals() {
        Assert.assertEquals(this.testClientSession1, this.testClientSession1);
        Assert.assertNotSame(this.testClientSession1, this.testClientSession2);
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(this.testClientSession1.hashCode(), this.testClientSession1.hashCode());
        Assert.assertNotSame(this.testClientSession1.hashCode(), this.testClientSession2.hashCode());
    }

    @Test
    public void testToString() {
        String toStringCalled = testClientSession1.toString();
        System.out.println(toStringCalled);
        String testToString = "ClientSession{" +
                "socket=" + this.testServerSocketConnected1 +
                ", server=" + this.testServer +
                ", port=" + this.testPort +
                '}';
        System.out.println(testToString);
        Assert.assertEquals(testToString, toStringCalled);
    }
}