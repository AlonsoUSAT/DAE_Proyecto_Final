/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.Connection;
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

    public boolean validarRespuesta(String usu, String respuesta) throws Exception {
        strSQL = "SELECT respuesta FROM usuario WHERE nomusuario = '" + usu + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getString("respuesta").equalsIgnoreCase(respuesta.trim());
            }
        } catch (Exception e) {
            throw new Exception("Error al validar la respuesta: " + e.getMessage());
        }
        return false;
    }

    public String obtenerPregunta(String usu) throws Exception {
        strSQL = "SELECT pregunta FROM usuario WHERE nomusuario = '" + usu + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getString("pregunta");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener la pregunta: " + e.getMessage());
        }
        return "";
    }

    public void modificarClave(String nombreUsuario, String nuevaClave) throws Exception {
        Connection con = null;
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false);

            String strSQL = "UPDATE usuario SET clave = '" + nuevaClave + "' WHERE nomusuario = '" + nombreUsuario + "'";
            objConectar.ejecutarBD(strSQL);

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                con.rollback();
            }
            throw new Exception("Error al modificar la contraseña: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }

    public String obtenerUsuarioPorPregunta(String pregunta) throws Exception {
        String strSQL = "SELECT nomusuario FROM usuario WHERE pregunta = '" + pregunta + "'";
        try {
            ResultSet rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getString("nomusuario");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener el usuario: " + e.getMessage());
        }
        return "";
    }

    public boolean validarUsuario(String usu) throws Exception {
        strSQL = "SELECT 1 FROM usuario WHERE nomusuario = '" + usu + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs.next(); // Si devuelve al menos una fila, el usuario existe
        } catch (Exception e) {
            throw new Exception("Error al validar existencia de usuario: " + e.getMessage());
        }
    }

}
