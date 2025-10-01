/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Mechan Vidaurre Mia
 */
public class clsUsuario {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public String[] login(String usu, String con) throws Exception {
        strSQL = "select usuario,tipo_usuario from usuarios where usuario = '" + usu + "' and contraseña = '" + con + "'";
        String[] valores = new String[2];
        try {
            rs = objConectar.consultarBD((strSQL));
            while (rs.next()) {
                valores[0] = rs.getString("usuario");
                valores[1] = rs.getString("tipo_usuario");
                return valores;
            }
        } catch (Exception e) {
            throw new Exception("Error al iniciar sesion -->" + e.getMessage());
        }
        valores[0] = "";
        return valores;
    }
    
    

}
