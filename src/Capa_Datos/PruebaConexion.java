/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;
import javax.swing.JOptionPane;
import java.sql.SQLException;
/**
 *
 * @author Usuario
 */
public class PruebaConexion {
    public static void main(String[] args) {
        clsJDBC objConexion = new clsJDBC();

        try {
            objConexion.conectar();
            
            JOptionPane.showMessageDialog(null, "¡Conexión Exitosa a la Base de Datos!", "Prueba de Conexión", JOptionPane.INFORMATION_MESSAGE);
            
            objConexion.desconectar();
            System.out.println("Desconexión exitosa.");

        // --- MANEJO DE ERRORES ESPECÍFICOS ---

        } catch (ClassNotFoundException e) {
            // Este error ocurre si el DRIVER (el archivo .jar) no está en el proyecto
            String mensaje = "Error: Driver de base de datos no encontrado.\n\n"
                           + "Asegúrate de que el archivo 'postgresql-XX.X.X.jar' esté agregado a las librerías de tu proyecto.";
            JOptionPane.showMessageDialog(null, mensaje, "Error de Driver", JOptionPane.ERROR_MESSAGE);
            
        } catch (SQLException e) {
            // Este error ocurre por problemas con la BD (URL, usuario, pass, etc.)
            String mensaje = "Error de Conexión a la Base de Datos:\n\n"
                           + "SQLState: " + e.getSQLState() + "\n"
                           + "Mensaje: " + e.getMessage();
            JOptionPane.showMessageDialog(null, mensaje, "Error de SQL", JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            String mensaje = "Ocurrió un error inesperado:\n\n" + e.getMessage();
            JOptionPane.showMessageDialog(null, mensaje, "Error General", JOptionPane.ERROR_MESSAGE);
        }
    }
}
