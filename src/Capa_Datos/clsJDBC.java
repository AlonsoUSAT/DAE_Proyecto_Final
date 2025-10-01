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
    
    public clsJDBC(){
        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/MechanMia";
        this.user = "postgres";
        this.password = "Us@t2025";
        this.con = null;
    }
    
    //Conectamos
    public void conectar() throws Exception {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new Exception("Error al conectar a la BD!");
        }
    }
    
    //Desconectamos
    public void desconectar() throws Exception {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new Exception("Error al desconectar de la BD!" + ex.getMessage());
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
}
