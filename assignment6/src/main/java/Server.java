import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private int serverPort;
    private static final int DEFAULT_PORT = 3333;
    private static final int THREAD_LIMIT = 10;
    private HashMap<String, ClientSession> clientConnections;
    private boolean serverRunning = true;
    protected ExecutorService threadExecutor;

    Server() throws IOException {
        this.serverPort = DEFAULT_PORT;
        openServerSocketOnPort(DEFAULT_PORT);
        this.threadExecutor = Executors.newFixedThreadPool(THREAD_LIMIT);
        this.clientConnections = new HashMap<>();
    }

    Server(int port) throws IOException {
        this.serverPort = port;
        openServerSocketOnPort(port);
        this.threadExecutor = Executors.newFixedThreadPool(THREAD_LIMIT);
        this.clientConnections = new HashMap<>();
    }

    public static void main(String[] args) {
        try {
            Scanner serverConsole = new Scanner(System.in);
            Server server = new Server();
            ServerSocket serverSocket = server.getServerSocket();
            System.out.println("Chat server is running on port " + server.serverPort);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                ClientSession clientThread = new ClientSession(clientSocket, server, server.serverPort);
                server.threadExecutor.execute(clientThread);
//                server.threadExecutor.execute(new ClientSession(server.serverSocket.accept(), server, server.serverPort));

                while (!serverConsole.hasNext()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String consoleInput = serverConsole.nextLine();
                if (consoleInput.toLowerCase().equals("@shutdown")) {
                    // add logic to send disconnect response to all clients
                    server.shutdownServer();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
//        finally {
//            try {
//                serverSocket.close();
//                /* unable to close the server connection here */
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private ServerSocket getServerSocket() { return this.serverSocket; }

    private void shutdownServer() throws IOException {
        for (String clientName : this.clientConnections.keySet()) {
            clientConnections.get(clientName).getClientSocket().close();
        }
        System.out.println("Shutting down client connections and server port");
        this.serverSocket.close();
    }

    private int getServerPort() { return this.serverPort; }

    private void openServerSocketOnPort(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open connection on port " + this.serverPort, e);
        }
    }

    protected void addClientConnection(String clientName, ClientSession session) {
        clientConnections.putIfAbsent(clientName, session);
    }

    protected ClientSession getClientConnection(String clientName) {
        if (clientConnections.containsKey(clientName)) {
            return clientConnections.get(clientName);
        }
        return null;
    }

}
