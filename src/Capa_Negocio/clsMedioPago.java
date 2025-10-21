package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Carlos Otoya
 */
public class clsMedioPago {
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    
    public ResultSet listarMediosPago() throws Exception {
        strSQL = """
            SELECT idMedioPago, nombre
            FROM medio_pago
            ORDER BY idmediopago;
        """;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar medios de pago: " + e.getMessage());
        }
    }
    
    // Generar código correlativo
    public Integer generarCodigoMedioPago() throws Exception {
        strSQL = "select coalesce(max(idmediopago),0)+1 as codigo from medio_pago";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de medio de pago, "+e.getMessage());
        }
        return 0;
    }

    // Insertar medio de pago
    public void insertarMedioPago(int id, String nombre) throws Exception {
        strSQL = "insert into medio_pago(idMedioPago, nombre) values(" + id + ", '" + nombre + "');";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar medio de pago, " + e.getMessage());
        }
    }

    // Modificar medio de pago
    public void modificarMedioPago(int id, String nombre) throws Exception {
        strSQL = "update medio_pago set nombre='" + nombre + "' where idmediopago=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar medio de pago");
        }
    }

    // Eliminar medio de pago
    public void eliminarMedioPago(int id) throws Exception {
        strSQL = "delete from medio_pago where idmediopago=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar medio de pago");
        }
    }
    
    public Integer obtenerIdPorNombre(String nombre) throws Exception {
        strSQL = "SELECT idmediopago AS id FROM medio_pago WHERE nombre = '" + nombre + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener id por nombre: " + e.getMessage());
        }
        return 0;
    }
    
}