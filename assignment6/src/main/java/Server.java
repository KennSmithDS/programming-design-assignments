import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardProtocolFamily;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to represent a server object which manages the incoming client requests to join a chat application
 * Its primary function is to instantiate a thread pool to manage multiple ClientSession
 * Properties:
 * - serverSocket
 * - serverPort
 * - clientSessions
 * - threadPool
 * Methods:
 * - main entry point for the server application
 * - get server socket
 * - get server port
 * - open server socket
 * - get client count
 * - add client session
 * - drop client session
 * - get client sessions
 */
public class Server {
    private ServerSocket serverSocket;
    private int serverPort;
    private static final int DEFAULT_PORT = 3333;
    private static final int THREAD_LIMIT = 10;
    private ConcurrentHashMap<String, ClientSession> clientSessions;
    private boolean serverRunning = true;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_LIMIT);

    /**
     * Constructor method without any parameters
     * Assigns the default server port, server socket and instantiate concurrenthashmap
     * @throws IOException
     */
    Server() throws IOException {
        this.serverPort = DEFAULT_PORT;
        openServerSocketOnPort(DEFAULT_PORT);
        this.clientSessions = new ConcurrentHashMap<>();
    }

    /**
     * Constructor method with port parameters passed
     * Assigns the server port to port passed, server socket and instantiate concurrenthashmap
     * @param port
     * @throws IOException
     */
    Server(int port) throws IOException {
        this.serverPort = port;
        openServerSocketOnPort(port);
        this.clientSessions = new ConcurrentHashMap<>();
    }

    /**
     * Main method to drive the execution of server instantiation, and accepting client requests to connect over socket
     * @param args String array from command-line terminal
     */
    public static void main(String[] args) throws IOException {
        try {
            if (args.length==0) {
                Scanner serverConsole = new Scanner(System.in);

                Server server = new Server();
                ServerSocket serverSocket = server.getServerSocket();

                System.out.println("Chat server is running on port " + server.serverPort);
                System.out.println("Server is waiting for clients to connect");

                while (true) {
                    if (server.getClientCount() < THREAD_LIMIT) {
                        acceptClientRequest(server, serverSocket);
                    } else {
                        System.out.println("More than " + THREAD_LIMIT + " users attempted to connect to server.");
                    }

                }
            }
//            } else if (args[0].equals("poison")) {
//                Server server = new Server();
//                ServerSocket serverSocket = server.getServerSocket();
//                acceptClientRequest(server, serverSocket);
//                serverSocket.close();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Method to encapsulate the process of accepting inbound client requests to join server
     * @param server Server object instantiated in main
     * @param serverSocket ServerSocket for Server
     * @throws IOException default exception for IO error
     */
    private static void acceptClientRequest(Server server, ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connection from: " + clientSocket);
        ClientSession clientThread = new ClientSession(clientSocket, server, server.getServerPort());
        threadPool.execute(clientThread);
    }

    /**
     * Method to get the server socket from ClientSession
     * @return ServerSocket object
     */
    protected ServerSocket getServerSocket() { return this.serverSocket; }

    /**
     * Method to get the server port from ClientSession
     * @return int for server port
     */
    protected int getServerPort() { return this.serverPort; }

    /**
     * Method to open ServerSocket on server port in Server construction
     * @param port int
     */
    private void openServerSocketOnPort(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open connection on port " + this.serverPort, e);
        }
    }

    /**
     * Method to get the count of clients connected to server
     * @return int count of clients
     */
    protected int getClientCount() { return this.clientSessions.size(); }

    /**
     * Method to add clientName and ClientSession to thread safe hashmap
     * @param clientName String name of client connecting to server
     * @param session ClientSession of client connecting to server
     */
    protected boolean addClientSession(String clientName, ClientSession session) {
        if (!this.clientSessions.containsKey(clientName)) {
            clientSessions.putIfAbsent(clientName, session);
            return true;
        }
        return false;
    }

    /**
     * Method to drop a client from the thread safe hashmap
     * @param clientName String name of client disconnecting from server
     */
    protected void dropClientSession(String clientName) {
        clientSessions.remove(clientName);
    }

    /**
     * Method to get the ClientSession thread safe hashmap from within ClientSession
     * @return ConcurrentHashMap of client sessions
     */
    protected ConcurrentHashMap<String, ClientSession> getClientSessions() {
        return this.clientSessions;
    }

    /**
     * Override method of default equals()
     * @param o object
     * @return boolean
     */
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

    /**
     * Override method of default hashCode()
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(serverSocket, serverPort, clientSessions, serverRunning);
    }

    /**
     * Override method of default toString()
     * @return
     */
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
