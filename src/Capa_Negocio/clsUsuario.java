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
        strSQL = "select usuario,tipo_usuario from usuarios where nomusuario = '" + usu + "' and clave = '" + con + "'";
        String[] valores = new String[2];
        try {
            rs = objConectar.consultarBD((strSQL));
            while (rs.next()) {
                valores[0] = rs.getString("nomusuario");
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
    /*

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
    }*/
    public ResultSet listarUsuarios() throws Exception {
        // ADVERTENCIA: Se recomienda especificar columnas en lugar de usar '*'
        strSQL = "SELECT U.*, R.nombre as nombreRol FROM USUARIO U INNER JOIN ROL R ON U.idRol = R.idRol";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios: " + e.getMessage());
        }
    }

    /**
     * Devuelve un ResultSet con todos los roles disponibles.
     */
    public ResultSet listarRoles() throws Exception {
        strSQL = "SELECT * FROM ROL";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar roles: " + e.getMessage());
        }
    }

    public Integer obtenerIdRol(String nombreRol) throws Exception {
        strSQL = "SELECT idRol FROM ROL WHERE nombre = '" + nombreRol + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("idRol");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener ID de rol: " + e.getMessage());
        }
        return null;
    }

    public Integer generarIdUsuario() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idUsuario), 0) + 1 AS codigo FROM USUARIO";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar ID de usuario: " + e.getMessage());
        }
        return 0;
    }

    public ResultSet buscarUsuario(Integer idUsuario) throws Exception {
        strSQL = "SELECT U.*, R.nombre as nombreRol FROM USUARIO U INNER JOIN ROL R ON U.idRol = R.idRol WHERE idUsuario = " + idUsuario;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario: " + e.getMessage());
        }
    }

    public void registrar(Integer id, String nomUsuario, String clave, String nombre, String apePaterno, String apeMaterno, String correo, char sexo, Boolean estado, Integer idRol) throws Exception {
        strSQL = "INSERT INTO USUARIO VALUES(" + id + ", '" + nomUsuario + "', '" + clave + "', " + estado + ", '" + nombre + "', '" + apePaterno + "', '" + apeMaterno + "', '" + correo + "', '" + sexo + "', " + idRol + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar usuario: " + e.getMessage());
        }
    }

    public void modificar(Integer id, String nomUsuario, String clave, String nombre, String apePaterno, String apeMaterno, String correo, char sexo, Boolean estado, Integer idRol) throws Exception {
        strSQL = "UPDATE USUARIO SET nomUsuario='" + nomUsuario + "', clave='" + clave + "', estado=" + estado + ", nombre='" + nombre + "', apellidoPaterno='" + apePaterno + "', apellidoMaterno='" + apeMaterno + "', correo='" + correo + "', sexo='" + sexo + "', idRol=" + idRol + " WHERE idUsuario=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar usuario: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) throws Exception {

        strSQL = "DELETE FROM USUARIO WHERE idUsuario = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public void darDeBaja(Integer id) throws Exception {
        strSQL = "UPDATE USUARIO SET estado = false WHERE idUsuario = " + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja al usuario: " + e.getMessage());
        }
    }

}
