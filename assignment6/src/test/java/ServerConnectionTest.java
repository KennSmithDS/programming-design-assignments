import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ServerConnectionTest {

    private Socket testCientSocket;
    private Server testServer;
    private ServerSocket testServerSocket;
    private ServerConnection testServerConnection;
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;
    private ObjectInputStream testInputStream;
    private ObjectOutputStream testOutputStream;

    @Before
    public void setUp() throws Exception {
        this.testServer = new Server(DEFAULT_PORT);
        System.out.println(testServer.toString());
        this.testServerSocket = this.testServer.getServerSocket();
        System.out.println(testServerSocket);
        this.testCientSocket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
        System.out.println(testCientSocket);
        Socket localServerSocket = testServerSocket.accept();
        System.out.println(localServerSocket);
        testOutputStream = new ObjectOutputStream(localServerSocket.getOutputStream());
        testInputStream = new ObjectInputStream(testCientSocket.getInputStream());
        this.testServerConnection = new ServerConnection(testCientSocket, testInputStream);
//        Thread serverConnThread = new Thread(testServerConnection);
//        serverConnThread.start();
    }

    @After
    public void tearDown() throws IOException {
        this.testCientSocket.close();
        this.testServerSocket.close();
        this.testServer.stop();
    }

    @Test
    public void isConnected() {
        Assert.assertFalse(this.testServerConnection.isConnected());
    }

    @Test
    public void isAllowLogoff() {
        Assert.assertFalse(this.testServerConnection.isAllowLogoff());
    }

    @Test
    public void run() {
//        Thread connThread = new Thread(testServerConnection);
//        connThread.start();
//        connThread.interrupt();
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(testServerConnection, testServerConnection);
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(testServerConnection.hashCode(), testServerConnection.hashCode());
    }

    @Test
    public void testToString() {
        String testToString = "ServerConnection{" +
                "socket=" + this.testCientSocket +
                ", connected=" + false +
                ", allowLogoff=" + false +
                '}';
        String toStringCalled = this.testServerConnection.toString();
        Assert.assertEquals(testToString, toStringCalled);
    }
}