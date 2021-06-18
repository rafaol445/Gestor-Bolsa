import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final int SERVER_PORT = 9876;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    Socket client;
    ServerSocket server = null;

    public void run() {

        try {
            server = new ServerSocket(SERVER_PORT);
            while (true) {
                try {
                    client = server.accept();
                    Worker worker = new Worker(client);
                    executorService.execute(worker);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
