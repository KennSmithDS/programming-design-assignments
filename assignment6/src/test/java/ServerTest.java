import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
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
    private ClientSession testSession2;
    private static final String testClientName1 = "sally";
    private static final String testClientName2 = "harry";
    private Random rand = new Random();
    private static final int min = 1;
    private static final int max = 1000;

    private int getRandomPortMod() {
        return (int) ((Math.random() * (max - min)) + min);
    }

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
//        Thread.sleep(1000);
    }

    @Test
    public void getServerSocket() throws IOException, InterruptedException {
        System.out.println("testing server socket get method");
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        ServerSocket testServerSocket = testServer.getServerSocket();
        Assert.assertFalse(testServerSocket.isClosed());
        int serverPort = testServerSocket.getLocalPort();
        Assert.assertEquals(serverPort, newPort, 0);
        testServerSocket.close();
        Assert.assertTrue(testServerSocket.isClosed());
        Thread.sleep(1000);
    }

    @Test (expected = RuntimeException.class)
    public void serverSocketInUse_Fail() throws RuntimeException, IOException, InterruptedException {
        System.out.println("testing server socket in use");
        Server newServer = new Server(DEFAULT_PORT);
        Server newServer2 = new Server(DEFAULT_PORT);
        newServer.getServerSocket().close();
        newServer2.getServerSocket().close();
        Thread.sleep(1000);
    }

    @Test
    public void getServerPort() throws IOException, InterruptedException {
        System.out.println("testing server port get method");
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        int serverPortCalled = testServer.getServerPort();
        Assert.assertEquals(newPort, serverPortCalled, 0);
        testServer.getServerSocket().close();
        Thread.sleep(1000);
    }

    @Test
    public void getClientCount_Default() throws IOException, InterruptedException {
        System.out.println("testing get client count method");
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        int noClients = testServer.getClientCount();
        Assert.assertEquals(0, noClients,0);
        testServer.getServerSocket().close();
        Thread.sleep(1000);
    }

    @Test
    public void addClientSession_Pass() throws IOException, InterruptedException {
        System.out.println("testing add client session method");
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        Client testClient = new Client(DEFAULT_HOST, newPort);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName1, testSession);
        Assert.assertEquals(1, testServer.getClientCount(), 0);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(1000);
    }

    @Test
    public void addClientSession_Fail() throws IOException, InterruptedException {
        System.out.println("testing add client session fail method when same user");
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        Client testClient = new Client(DEFAULT_HOST, newPort);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        boolean result1 = testServer.addClientSession(testClientName1, testSession);
        boolean result2 = testServer.addClientSession(testClientName1, testSession);
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(1000);
    }

    @Test
    public void acceptClientRequest_Test() throws IOException, InterruptedException {
//        System.out.println("testing accept client request method");
//        Server server = new Server();
//        ServerSocket serverSocket = server.getServerSocket();
//        Server.acceptClientRequest(server, serverSocket);
//        serverSocket.close();
    }

    @Test
    public void dropClientSession() throws IOException, InterruptedException {
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        Client testClient = new Client(DEFAULT_HOST, newPort);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName1, testSession);
        testServer.dropClientSession(testClientName1);
        Assert.assertEquals(0, testServer.getClientCount(), 0);
        testServer.getServerSocket().close();
        testClientSocket.close();
        Thread.sleep(1000);
    }

    @Test
    public void getClientSessions() throws IOException {
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);

        Client testClient = new Client(DEFAULT_HOST, newPort);
        Socket testClientSocket = testServer.getServerSocket().accept();
        testSession = new ClientSession(testClientSocket, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName1, testSession);

        Client testClient2 = new Client(DEFAULT_HOST, newPort);
        Socket testClientSocket2 = testServer.getServerSocket().accept();
        testSession2 = new ClientSession(testClientSocket2, testServer, testServer.getServerPort());
        testServer.addClientSession(testClientName2, testSession2);

        ConcurrentHashMap<String, ClientSession> sessions = testServer.getClientSessions();
        Assert.assertEquals(2, sessions.size());
    }

    @Test
    public void testEquals() throws IOException, InterruptedException {
        Server testServer = new Server(DEFAULT_PORT+getRandomPortMod());
        Assert.assertEquals(testServer, testServer);
        Thread.sleep(1000);
    }

    @Test
    public void testNotEquals() throws IOException, InterruptedException {
        Server testServer1 = new Server(DEFAULT_PORT+getRandomPortMod());
        Server testServer2 = new Server(DEFAULT_PORT+getRandomPortMod());
        assertFalse(testServer1.equals(testServer2));
        Thread.sleep(1000);
    }

    @Test
    public void testHashCode() throws IOException, InterruptedException {
        Server testServer = new Server(DEFAULT_PORT+getRandomPortMod());
        Assert.assertEquals(testServer.hashCode(), testServer.hashCode());
        Thread.sleep(1000);
    }

    @Test
    public void testToString() throws InterruptedException, IOException {
        int newPort = DEFAULT_PORT+getRandomPortMod();
        Server testServer = new Server(newPort);
        String serverToString = testServer.toString();
//        System.out.println(serverToString);
        ServerSocket socket = testServer.getServerSocket();
        String testString = "Server{serverSocket=" + socket + ", serverPort=" + newPort +
                ", clientSessions={}, serverRunning=true}";
//        System.out.println(testString);
        Assert.assertEquals(testString, serverToString);
        Thread.sleep(1000);
    }

}