/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Presentacion;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author Usuario
 */
public class HashPrueba {
    public static void main(String[] args) {
        // Generar el hash para "123456"
        String password = "123456";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("COPIA ESTE CÓDIGO PARA TU INSERT SQL:");
        System.out.println(hash);
    }
}
