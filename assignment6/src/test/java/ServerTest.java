import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ServerTest {

    private ServerSocket testServerSocket;
    private int testServerPort;
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_LIMIT = 2;
    private ConcurrentHashMap<String, ClientSession> testClientSessions;
    private boolean testServerRunning = true;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_LIMIT);
    private Server testServer1;
    private Server testServer2;
    private ClientSession testSession;
    private String testClientName;
    private Client testClient;
    private Socket testClientSocket;

    @Before
    public void setUp() throws Exception {
        this.testServerPort = DEFAULT_PORT;
        this.testClientSessions = new ConcurrentHashMap<>();
        this.testServer1 = new Server(DEFAULT_PORT);
        this.testServer2 = new Server(DEFAULT_PORT+1);
        this.testClientName = "sally";
        this.testClient = new Client(DEFAULT_HOST, DEFAULT_PORT);
        this.testClientSocket = testServer1.getServerSocket().accept();
    }

    @Test
    public void main() throws IOException {
//        String[] args = new String[0];
//        Server.main(args);
    }

    @Test
    public void getServerSocket() throws IOException {
        ServerSocket testServerSocket = testServer1.getServerSocket();
        int serverPort = testServerSocket.getLocalPort();
        Assert.assertEquals(serverPort, this.testServerPort, 0);
        testServerSocket.close();
    }

    @Test (expected = RuntimeException.class)
    public void serverSocketInUse_Fail() throws RuntimeException, IOException {
        Server newServer = new Server(DEFAULT_PORT);
        newServer.getServerSocket().close();
    }

    @Test
    public void getServerPort() throws IOException {
        int serverPortCalled = testServer1.getServerPort();
        Assert.assertEquals(testServerPort, serverPortCalled, 0);
        testServer1.getServerSocket().close();
    }

    @Test
    public void getClientCount_Default() throws IOException {
        int noClients = testServer1.getClientCount();
        Assert.assertEquals(0, noClients,0);
        testServer1.getServerSocket().close();
    }

    @Test
    public void addClientSession_Pass() throws IOException {
        testSession = new ClientSession(testClientSocket, testServer1, testServer1.getServerPort());
        testServer1.addClientSession(testClientName, testSession);
        Assert.assertEquals(1, testServer1.getClientCount(), 0);
        testServer1.getServerSocket().close();
        testClientSocket.close();
    }

    @Test
    public void addClientSession_Fail() throws IOException {
        testSession = new ClientSession(testClientSocket, testServer1, testServer1.getServerPort());
        boolean result1 = testServer1.addClientSession(testClientName, testSession);
        boolean result2 = testServer1.addClientSession(testClientName, testSession);
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        testServer1.getServerSocket().close();
        testClientSocket.close();
    }

    @Test
    public void dropClientSession() throws IOException {
        testSession = new ClientSession(testClientSocket, testServer1, testServer1.getServerPort());
        testServer1.addClientSession(testClientName, testSession);
        testServer1.dropClientSession(testClientName);
        Assert.assertEquals(0, testServer1.getClientCount(), 0);
        testServer1.getServerSocket().close();
        testClientSocket.close();
    }

    @Test
    public void getClientSessions() {
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

    @After
    public void takeDown() throws IOException {
        testServer1.getServerSocket().close();
        testClientSocket.close();
    }
}