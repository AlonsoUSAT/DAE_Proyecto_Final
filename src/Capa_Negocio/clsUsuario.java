package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Mia Mechan
 */
public class clsUsuario {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public String[] login(String usu, String con) throws Exception {
        strSQL = "SELECT nombres, codusuario FROM usuario "
                + "WHERE nomusuario='" + usu + "' AND clave ='" + con + "' AND estado=true";

        String[] datos = new String[2];

        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                String hahGuardado = rs.getString("clave");
                if (BCrypt.checkpw(con, hahGuardado)) {
                    datos[0] = rs.getString("nombres");
                    datos[1] = rs.getString("codusuario"); // Recuperamos el ID
                    return datos;
                }

            }
        } catch (Exception e) {
            throw new Exception("Error al iniciar sesión completo: " + e.getMessage());
        }
        return null; // Retorna null si no encuentra usuario
    }

    public Boolean validarVigencia(String usu) throws Exception {
        strSQL = "SELECT estado FROM usuario WHERE nomusuario = '" + usu + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getBoolean("estado");
            }
        } catch (Exception e) {
            throw new Exception("Error al validar usuario -->" + e.getMessage());
        }
        return false;
    }

    public ResultSet listarRoles() throws Exception {
        strSQL = "SELECT * FROM ROL";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar roles: " + e.getMessage());
        }
    }

    public ResultSet listarUsuarios() throws Exception {
        // Asegúrate de usar el nombre correcto de la columna de la tabla ROL
        strSQL = "SELECT u.*, r.nombre_rol as nombre_rol FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol ORDER BY codusuario";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios -->" + e.getMessage());
        }
    }

    public Integer obtenerIdRol(String nombreRol) throws Exception {
        strSQL = "SELECT id_rol FROM rol WHERE nombre_rol = '" + nombreRol + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("id_rol");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener el ID del Rol: " + e.getMessage());
        }
        return 0;
    }

    public Integer generarCodigoUsuario() throws Exception {
        strSQL = "SELECT COALESCE(MAX(codusuario),0)+1 AS codigo FROM usuario";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de usuario");
        }
        return 0;
    }

    // 2. REGISTRAR CORREGIDO: Eliminé la columna vacía que sobraba
    public void registrarUsuario(int cod, String nombres, String apePaterno, String apeMaterno, String correo, String sexo, String clave, Boolean estado, Integer idRol, String nomusuario, String pregunta, String respuesta) throws Exception {
        String claveEncriptada = BCrypt.hashpw(clave, BCrypt.gensalt());
        strSQL = "INSERT INTO usuario(codusuario, nombres, apellidopaterno, apellidomaterno, correo, sexo, clave, estado, id_rol, nomusuario, pregunta, respuesta) "
                + "VALUES(" + cod + ",'" + nombres + "','" + apePaterno + "','" + apeMaterno + "','" + correo + "','" + sexo + "','" + claveEncriptada + "'," + estado + "," + idRol + ",'" + nomusuario + "', '" + pregunta + "', '" + respuesta + "')";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar al usuario: " + e.getMessage());
        }
    }

    public ResultSet buscarUsuario(Integer cod) throws Exception {
        // También traemos el nombre del rol aquí
        strSQL = "SELECT u.*, r.nombre as nombre_rol FROM usuario u INNER JOIN rol r ON u.id_rol=r.id_rol WHERE codusuario=" + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario");
        }
    }

    public void eliminarUsuario(Integer cod) throws Exception {
        strSQL = "DELETE FROM usuario WHERE codusuario=" + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar al usuario");
        }
    }

    public void darBaja(Integer cod) throws Exception {
        strSQL = "UPDATE usuario SET estado=false WHERE codusuario=" + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja al usuario");
        }
    }

    // 3. MODIFICAR CORREGIDO: Usamos la sintaxis correcta UPDATE SET
    public void modificarUsuario(int cod, String nombres, String apePaterno, String apeMaterno, String correo, String sexo, String clave, Boolean estado, Integer idRol, String nomusuario, String pregunta, String respuesta) throws Exception {
        if (clave != null && !clave.trim().isEmpty()) {
            String claveEncriptada = BCrypt.hashpw(clave, BCrypt.gensalt());
            strSQL = "UPDATE usuario SET "
                + "nombres='" + nombres + "', "
                + "apellidopaterno='" + apePaterno + "', "
                + "apellidomaterno='" + apeMaterno + "', "
                + "correo='" + correo + "', "
                + "sexo='" + sexo + "', "
                + "clave='" + claveEncriptada + "', "
                + "estado=" + estado + ", "
                + "id_rol=" + idRol + ", "
                + "nomusuario='" + nomusuario + "', "
                + "pregunta='" + pregunta + "', "
                + "respuesta='" + respuesta + "' "
                + "WHERE codusuario=" + cod;
        }else{
            strSQL = "UPDATE usuario SET "
                + "nombres='" + nombres + "', "
                + "apellidopaterno='" + apePaterno + "', "
                + "apellidomaterno='" + apeMaterno + "', "
                + "correo='" + correo + "', "
                + "sexo='" + sexo + "', "
                + "estado=" + estado + ", "
                + "id_rol=" + idRol + ", "
                + "nomusuario='" + nomusuario + "', "
                + "pregunta='" + pregunta + "', "
                + "respuesta='" + respuesta + "' "
                + "WHERE codusuario=" + cod;
        }
        
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar al usuario: " + e.getMessage());
        }
    }

    public ResultSet buscarPorCodigo(Integer cod) throws Exception {
        strSQL = "SELECT * FROM usuario WHERE codusuario= " + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar usuario");
        }
    }

}
