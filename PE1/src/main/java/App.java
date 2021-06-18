import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {



    public static void main(String[] args) throws IOException {
        Server server = new Server();
        Thread thread1 = new Thread(server);
        thread1.start();
    }

}
