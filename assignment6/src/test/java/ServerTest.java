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
    private ClientSession testSession;
    private static final String testClientName1 = "sally";
    private static final String testClientName2 = "harry";

    @Before
    public void setUp() throws Exception {
        this.testServerPort = DEFAULT_PORT;
        this.testClientSessions = new ConcurrentHashMap<>();
    }

    @Test
    public void main() throws IOException, InterruptedException {
//        String[] args = new String[]{"poison"};
//        Server.main(args);
//        Thread.sleep(15000);
//        Client testClient = new Client(DEFAULT_HOST, 3333);
////        System.exit(0);
//        testClient.getClientSocket().close();
//        Thread.sleep(3000);
    }

    @Test
    public void getServerSocket() throws IOException, InterruptedException {
        System.out.println("testing server socket get method");
        Server testServer = new Server(DEFAULT_PORT);
        ServerSocket testServerSocket = testServer.getServerSocket();
        int serverPort = testServerSocket.getLocalPort();
        Assert.assertEquals(serverPort, this.testServerPort, 0);
        testServerSocket.close();
        Thread.sleep(3000);
    }

    @Test (expected = RuntimeException.class)
    public void serverSocketInUse_Fail() throws RuntimeException, IOException, InterruptedException {
        System.out.println("testing server socket in use");
        Server newServer = new Server(DEFAULT_PORT);
        Server newServer2 = new Server(DEFAULT_PORT);
        newServer.getServerSocket().close();
        newServer2.getServerSocket().close();
        Thread.sleep(3000);
    }

    @Test
    public void getServerPort() throws IOException, InterruptedException {
        System.out.println("testing server port get method");
        Server testServer = new Server(DEFAULT_PORT);
        int serverPortCalled = testServer.getServerPort();
        Assert.assertEquals(testServerPort, serverPortCalled, 0);
        testServer.getServerSocket().close();
        Thread.sleep(3000);
    }

    @Test
    public void getClientCount_Default() throws IOException, InterruptedException {
        System.out.println("testing get client count method");
        Server testServer = new Server(DEFAULT_PORT);
        int noClients = testServer.getClientCount();
        Assert.assertEquals(0, noClients,0);
        testServer.getServerSocket().close();
        Thread.sleep(3000);
    }

    @Test
    public void addClientSession_Pass() throws IOException, InterruptedException {
        System.out.println("testing add client session method");
        Server testServer = new Server(DEFAULT_PORT);
        Client testClient = new Client(DEFAULT_HOST, DEFAULT_PORT);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName1, testSession);
        Assert.assertEquals(1, testServer.getClientCount(), 0);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(3000);
    }

    @Test
    public void addClientSession_Fail() throws IOException, InterruptedException {
        System.out.println("testing add client session fail method when same user");
        Server testServer = new Server(DEFAULT_PORT);
        Client testClient = new Client(DEFAULT_HOST, DEFAULT_PORT);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        boolean result1 = testServer.addClientSession(testClientName1, testSession);
        boolean result2 = testServer.addClientSession(testClientName1, testSession);
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(3000);
    }
//
//    @Test
//    public void acceptClientRequest_Test() throws IOException, InterruptedException {
//        System.out.println("testing accept client request method");
//        Server testServer = new Server(DEFAULT_PORT);
//        ServerSocket serverSocket = testServer.getServerSocket();
//        Client testClient = new Client(DEFAULT_HOST, DEFAULT_PORT);
//        Server.acceptClientRequest(testServer, serverSocket);
//        Assert.assertFalse(serverSocket.isClosed());
//        serverSocket.close();
//        Thread.sleep(3000);
//    }

    @Test
    public void dropClientSession() throws IOException, InterruptedException {
        Server testServer = new Server(DEFAULT_PORT);
        Client testClient = new Client(DEFAULT_HOST, DEFAULT_PORT);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName1, testSession);
        testServer.dropClientSession(testClientName1);
        Assert.assertEquals(0, testServer.getClientCount(), 0);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(3000);
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

    }
}