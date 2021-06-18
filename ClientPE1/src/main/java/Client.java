import org.jasypt.util.text.StrongTextEncryptor;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT_HTTP = 9876;
    private static final String HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(HOSTNAME, PORT_HTTP);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        BufferedReader entrada = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter salida = new PrintWriter(outputStream, true);
        String linea;
        String comnadoEntrada = "";
        System.out.println(entrada.readLine());

        while (!comnadoEntrada.equals("salir")) {
            Scanner entradaCliente = new Scanner(System.in);
            comnadoEntrada = entradaCliente.nextLine();
            if (comnadoEntrada.equals("")) {
                System.out.println("Introduzca un comando valido");
            } else {
                salida.println(comnadoEntrada);

                while ((linea = entrada.readLine()) != null) {
                    System.out.println("Respuesta servidor: " + linea);

                    if (linea.equals("Introduzca la contraseña.")) {
                        String password = entradaCliente.nextLine();
                        String passwordEncrypted = encryptor(password);
                        salida.println(passwordEncrypted);
                        System.out.println(entrada.readLine());
                    }
                    if (linea.endsWith(".") || linea.endsWith(":")) {
                        break;
                    }
                }
            }
        }
        System.out.println("Adios...");
    }
    private static String encryptor(String password) {
        StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword("algo");
        return encryptor.encrypt(password);
    }
}




