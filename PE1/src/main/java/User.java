import java.io.*;
import java.util.LinkedList;

public class User {

    private String nombre;
    private String apellidos;
    private String email;

    public User(String nombre, String apellidos, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public boolean removeUser(String email, LinkedList<User> listaUsuariosJava) {
        int comprobador = listaUsuariosJava.size();
        for (int i = 0; i < listaUsuariosJava.size(); i++) {
            if (listaUsuariosJava.get(i).getEmail().equals(email)) {
                listaUsuariosJava.remove(listaUsuariosJava.indexOf(listaUsuariosJava.get(i)));
            }
        }
        if (comprobador == listaUsuariosJava.size()) {
            return false;
        } else {
            return true;
        }
    }

    public void guardarUsuario(String listaUsuariosJson) throws IOException {
        File file = new File("users.json");

        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writerFile = null;
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write("");
        bw.close();
        writerFile = new FileWriter(file.getAbsoluteFile(), true);
        PrintWriter printer = new PrintWriter(writerFile);
        printer.println(listaUsuariosJson);
        printer.close();
    }
}
