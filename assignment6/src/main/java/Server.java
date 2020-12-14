import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Server {
    private ServerSocket serverSocket;
    private int serverPort;
    private static final int DEFAULT_PORT = 3333;
    private static final int THREAD_LIMIT = 10;
    private ConcurrentHashMap<byte[], ClientSession> clientSessions;
    private int threadCount;
    private boolean serverRunning = true;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_LIMIT);

    /**
     *
     * @throws IOException
     */
    Server() throws IOException {
        this.serverPort = DEFAULT_PORT;
        openServerSocketOnPort(DEFAULT_PORT);
        this.clientSessions = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param port
     * @throws IOException
     */
    Server(int port) throws IOException {
        this.serverPort = port;
        openServerSocketOnPort(port);
        this.clientSessions = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        try {
            Scanner serverConsole = new Scanner(System.in);

            Server server = new Server();
            ServerSocket serverSocket = server.getServerSocket();

            System.out.println("Chat server is running on port " + server.serverPort);
            System.out.println("Server is waiting for clients to connect");

            while (true) {
                // unable to get the main logic to be able to handle both shutdown command as well as listen on port
//                String consoleInput = serverConsole.nextLine();
//                while (!consoleInput.equals("shutdown")) {
                // accept inbound connections from clients, and add them to the thread pool executor

                if (server.threadCount < THREAD_LIMIT) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connection from: " + clientSocket);
                    ClientSession clientThread = new ClientSession(clientSocket, server, server.getServerPort());
                    threadPool.execute(clientThread);
                    System.out.println("There are " + server.getClientCount() + " clients connected");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
//            server.shutdownServer();
        }
    }

    /**
     *
     * @return
     */
    private ServerSocket getServerSocket() { return this.serverSocket; }

    /**
     *
     * @return
     */
    protected int getServerPort() { return this.serverPort; }

    /**
     *
     */
    protected void countIncrement() { this.threadCount++; }

    /**
     *
     */
    protected void countDecrement() { this.threadCount--; }

    /**
     *
     * @param port
     */
    private void openServerSocketOnPort(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open connection on port " + this.serverPort, e);
        }
    }

    protected int getClientCount() {
        return this.threadCount;
//        return this.clientSessions.size();
    }

    /**
     *
     * @param clientName
     * @param session
     */
    protected void addClientSession(byte[] clientName, ClientSession session) { clientSessions.putIfAbsent(clientName, session); }

    /**
     *
     * @param clientName
     * @param session
     */
    protected void dropClientSession(byte[] clientName, ClientSession session) { clientSessions.remove(clientName, session); }

    /**
     *
     * @return
     */
    protected ConcurrentHashMap<byte[], ClientSession> getClientSessions() {
        return this.clientSessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return serverPort == server.serverPort &&
                serverRunning == server.serverRunning &&
                Objects.equals(serverSocket, server.serverSocket) &&
                Objects.equals(clientSessions, server.clientSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverSocket, serverPort, clientSessions, serverRunning);
    }

    @Override
    public String toString() {
        return "Server{" +
                "serverSocket=" + serverSocket +
                ", serverPort=" + serverPort +
                ", clientSessions=" + clientSessions +
                ", serverRunning=" + serverRunning +
                '}';
    }
}
