package Capa_Presentacion;
import org.mindrot.jbcrypt.BCrypt;

public class HashPrueba {

    public static void main(String[] args) {
        String password = "123456";
        
        // Generar el Hash
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("------------------------------------------------");
        System.out.println("Contraseña plana: " + password);
        System.out.println("HASH GENERADO (Copia esto a tu BD):");
        System.out.println(hash);
        System.out.println("------------------------------------------------");
    }
}