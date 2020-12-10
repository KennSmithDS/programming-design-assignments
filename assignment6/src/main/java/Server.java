import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private int serverPort = 8080;
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_LIMIT = 10;
    private boolean serverRunning = true;
    protected Executor threadExecutor;

    Server() throws IOException {
        this.serverSocket = new ServerSocket(DEFAULT_PORT);
        this.threadExecutor = Executors.newFixedThreadPool(THREAD_LIMIT);
    }

    Server(int port) throws IOException {
        this.serverPort = port;
        this.serverSocket = new ServerSocket(port);
        this.threadExecutor = Executors.newFixedThreadPool(THREAD_LIMIT);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.openServerSocketOnPort();
            System.out.println("Chat server is running on port " + server.serverPort);
            while (true) {
                server.threadExecutor.execute(new ClientSession(server.serverSocket.accept(), server, server.serverPort));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openServerSocketOnPort() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open connection on port " + this.serverPort, e);
        }
    }

}
