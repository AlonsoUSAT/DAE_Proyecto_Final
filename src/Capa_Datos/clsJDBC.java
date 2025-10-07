/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Mechan Vidaurre Mia
 */
public class clsJDBC {
    private String driver, url, user, password;
    private Connection con;
    private Statement sent = null;
    
    public Connection getCon() {
        return con;
    }
    
    public clsJDBC(){
        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/DAE_PROYECTO_FINAL";
        this.user = "postgres";
        this.password = "postgres";
        this.con = null;
    }
    
    //Conectamos
    public Connection conectar() throws ClassNotFoundException, SQLException {
        // Carga el driver
        Class.forName(driver);
        // Establece y devuelve la conexión
        return DriverManager.getConnection(url, user, password);
    }
    
    
    
    //Desconectamos
   public void desconectar() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    
    //Ejecutar ua consulta select
    public ResultSet consultarBD(String strSQL) throws Exception {
        ResultSet rs = null;
        try {
            conectar();
            sent = con.createStatement();
            rs = sent.executeQuery(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al ejecutar consulta"+e.getMessage());
        } finally {
            if (con != null) {
                desconectar();
            }
        }
    }
    public void ejecutarBD(String strSQL) throws Exception {
        try {
            conectar();
            sent = con.createStatement();
            sent.executeUpdate(strSQL);

        } catch (Exception e) {
            throw new Exception("Error al ejecutar Update --> " + e.getMessage());

            //Parte de la estructura de try, el finally siempre se ejecuta pase el try o catch
        } finally {
            if (con != null) {
                desconectar();
            }
        }
    }
}
