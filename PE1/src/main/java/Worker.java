import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import es.florida.EmailService;
import org.apache.commons.mail.EmailException;
import org.jasypt.util.text.StrongTextEncryptor;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker implements Runnable {

    private final Socket client;
    private final LinkedList<User> listaUsuarios = new LinkedList<>();
    private final User user = new User();
    private final ExecutorService senderThreads = Executors.newFixedThreadPool(2);

    public Worker(Socket client) {
        this.client = client;
    }

    public void run() {
        File file = new File("block.txt");

        try {
            System.out.println("Nuevo cliente Conectado en el puerto: " + client.getPort());
            BufferedReader reader = createBuffer(client);
            PrintWriter writer = createWriter(client);
            if (file.exists()) {
                writer.println("Servidor Bloqueado única opción UNBLOCK.");
            }else {writer.println("Inserta comando o -HELP.");}

            String linea;
            while((linea= reader.readLine())!=null){
                System.out.println(linea);
                linea = linea.toUpperCase();

                if (file.exists()) {
                    switch (linea) {
                        case "HELP" : unBlockMenu(writer);
                            break;
                        case "EXIT": shutDown(client,writer);
                            break;
                        case "ADDUSER" : addUser(writer,reader);
                            break;
                        case "DELUSER" : deleteUser(writer,reader);
                            break;
                        case "UNBLOCK" : unblockServer(writer,reader);
                            break;
                        default: unBlockMenu(writer);
                    }
                }else {
                    switch (linea) {
                        case "" : break;
                        case "HELP" : menu(writer);
                            break;
                        case "EXIT": shutDown(client,writer);
                            break;
                        case "ADDUSER" : addUser(writer,reader);
                            break;
                        case "DELUSER" : deleteUser(writer,reader);
                            break;
                        case "BUY" : buy(writer,reader);
                            break;
                        case "SELL" : sell(writer,reader);
                            break;
                        case "BLOCK" : blockServer(writer,reader);
                            break;
                        case "UNBLOCK" : unblockServer(writer,reader);
                            break;
                        default: writer.println("Comando no encontrado.");
                    }
                }
            }
        } catch (IOException | EmailException e) {
            System.out.println(e);
        }
    }

    private void unblockServer(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("Introduzca la contraseña.");
        String password = reader.readLine();

        if (checkPassword(password)) {
            File file = new File("block.txt");
            file.delete();
            writer.println("Server UnBlock - Introduzca comando o HELP. ");
            System.out.println(new Date().toString() +"- Servidor Desbloqueado");

        }else {writer.println("La contraseña no es correcta."); }
    }

    private void blockServer(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("Introduzca la contraseña.");
        String password = reader.readLine();

        if (checkPassword(password)) {
            File file = new File("block.txt");
            file.createNewFile();
            writer.println("Server Block - Unico comando disponible UNBLOCK.");
            System.out.println(new Date().toString() +"- Servidor Bloqueado");

        }else {writer.println("La contraseña no es correcta."); }
    }

    private void sell(PrintWriter writer, BufferedReader reader) throws IOException, EmailException {
        EmailService emailService = new EmailService();
        writer.println("Escriba Operacion.");
        String operacion = reader.readLine();
        String text = "SELL-"+ operacion;
        LinkedList<User> listaUsuarios = jsonToJava(leerFicheroJson());

        if (listaUsuarios == null) {
            writer.println("No hay usuarios que notificar");
            System.out.println(new Date().toString() +"- No hay usuarios que notificar");

        }else {
            for (User listaUsuario : listaUsuarios) {
                senderThreads.execute(new SendMail(listaUsuario.getEmail(),"SELL", text));
                writer.println("notificado correo: "+ listaUsuario.getEmail());
            }
            System.out.println(new Date().toString() +" Se envia operacion de venta de " + operacion.toUpperCase());
        }
        writer.println("Notificados a todos los correos.");
    }

    private void buy(PrintWriter writer, BufferedReader reader) throws IOException, EmailException {
        EmailService emailService = new EmailService();
        writer.println("Escriba Operacion.");
        String operacion = reader.readLine();
        String text = "BUY-"+ operacion;
        LinkedList<User> listaUsuarios = jsonToJava(leerFicheroJson());

        if (listaUsuarios == null) {
            writer.println("No hay usuarios que notificar.");
            System.out.println(new Date().toString() +"- No hay usuarios que notificar");

        }else {
            for (User listaUsuario : listaUsuarios) {
                senderThreads.execute(new SendMail(listaUsuario.getEmail(),"BUY", text));
                writer.println("notificado correo: "+ listaUsuario.getEmail());
            }
            System.out.println(new Date().toString() +" Se envia operacion de compra de " + operacion.toUpperCase());
        }
        writer.println("Notificados a todos los correos.");
    }

    private void deleteUser(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("Introduzca el email que desea eliminar");
        String email = reader.readLine();
        String listaUsuariosJson = leerFicheroJson();
        LinkedList<User> listaUsuariosJava = jsonToJava(listaUsuariosJson);

        if (user.removeUser(email, listaUsuariosJava)) {
            user.guardarUsuario(javaToJson(listaUsuariosJava));
            writer.println("Email borrado correctamente.");
            System.out.println(new Date().toString() +"- Usuario borrado: " + email);

        }else {writer.println("El email no existe en la base de datos.");}
    }

    private void addUser(PrintWriter writer , BufferedReader reader) throws IOException {
        String nombre;
        String apellidos;
        String email;

        writer.println("Escriba el nombre.");
        nombre = reader.readLine();
        writer.println("Escriba el apellido.");
        apellidos = reader.readLine();
        writer.println("Escriba el email.");
        email = reader.readLine();
        User user = new User(nombre,apellidos,email);
        String listaUsuariosJson = leerFicheroJson();
        LinkedList<User> listaUsuariosJava = jsonToJava(listaUsuariosJson);

        if (listaUsuariosJson.equals("null")) {
            listaUsuarios.add(user);
            user.guardarUsuario(javaToJson(listaUsuarios));
            System.out.println(new Date().toString() +"- Usuario creado: " + user.getEmail());
            writer.println("Usuario creado correctamente.");

        }else {listaUsuariosJava.add(user);
            user.guardarUsuario(javaToJson(listaUsuariosJava));
            System.out.println(new Date().toString() +"- Usuario creado: " + user.getEmail());
            writer.println("Usuario creado correctamente.");
        }
    }

    private LinkedList<User> jsonToJava(String ficheroJson) {
        LinkedList<User> users;
        Gson gson = new Gson();
        final Type tipoListaEmpleados = new TypeToken<LinkedList<User>>(){}.getType();
        users = gson.fromJson(ficheroJson, tipoListaEmpleados);
        return users;
    }

    private String javaToJson(LinkedList<User> listaUsuarios) {
        Gson gson = new Gson();
        String listaUsuariosJson = gson.toJson(listaUsuarios);
        return listaUsuariosJson;
    }

    private String leerFicheroJson() throws IOException {
        JsonParser parser = new JsonParser();
        File file = new File("users.json");
        FileReader fileReader = new FileReader(file);
        JsonElement datos = parser.parse(fileReader);
        return datos.toString();
    }

    private void menu(PrintWriter writer) {
        writer.println("-HELP - (LISTA DE COMANDOS)");
        writer.println("-ADDUSER - (AGREGAR USUARIO)");
        writer.println("-DELUSER - (ELIMINAR USUARIO)");
        writer.println("-BUY - (LANZAR ORDEN DE COMPRA)");
        writer.println("-SELL - (LANZAR ORDEN DE VENTA)");
        writer.println("-BLOCK - (BLOQUEAR SERVIDOR)");
        writer.println("-UNBLOCK - (DESBLOQUEAR SERVIDOR)");
        writer.println("-EXIT - (DESCONECTAR).");
    }

    private void unBlockMenu(PrintWriter writer) {
        writer.println("SERVER BLOCK");
        writer.println("-HELP - (LISTA DE COMANDOS)");
        writer.println("-ADDUSER - (AGREGAR USUARIO)");
        writer.println("-DELUSER - (ELIMINAR USUARIO)");
        writer.println("-UNBLOCK - (DESBLOQUEAR SERVIDOR)");
        writer.println("-EXIT - (DESCONECTAR).");
    }

    private void shutDown(Socket client, PrintWriter writer){
        try {
            writer.println("Bye Bye.");
            client.close();
        } catch (IOException e) {
            System.out.println("User Disconnected");
        }
    }

    private PrintWriter createWriter(Socket client) throws IOException {
        OutputStream outputStream = client.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        PrintWriter printWriter = new PrintWriter(outputStreamWriter, true);
        return printWriter;
    }

    private BufferedReader createBuffer(Socket client) throws IOException {
        InputStream inputStream = client.getInputStream();
        InputStreamReader inputStreamReader  = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader;
    }

    private String readPassword() throws IOException {
        File file = new File("password.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader.readLine();
    }

    private boolean checkPassword(String password) throws IOException {
        StrongTextEncryptor desEncryptor = new StrongTextEncryptor();
        desEncryptor.setPassword("algo");
        String decryptedPasswordClient = desEncryptor.decrypt(password);
        String decryptedPasswordServer = desEncryptor.decrypt(readPassword());
        System.out.println("a continuacion van");
        System.out.println(decryptedPasswordClient);
        System.out.println(decryptedPasswordServer);
        return decryptedPasswordClient.equals(decryptedPasswordServer);
    }
}
