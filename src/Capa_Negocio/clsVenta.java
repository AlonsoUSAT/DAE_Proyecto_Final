package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 
 * @author Mechan Vidaurre ¿
 */
public class clsVenta {
    
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    
    
    public ResultSet listarVentas() throws Exception {
        // Hacemos un JOIN para obtener datos más completos para la tabla de visualización.
        // Nota: Asegúrate que los nombres de las columnas (nombres, nomUsuario) sean correctos en tus tablas CLIENTE y USUARIO.
        strSQL = "SELECT V.*, C.nombres as nombreCliente, U.nomUsuario " +
                 "FROM VENTA V " +
                 "INNER JOIN CLIENTE C ON V.idCliente = C.idcliente " +
                 "INNER JOIN USUARIO U ON V.idUsuario = U.idUsuario";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar ventas: " + e.getMessage());
        }
    }

    public ResultSet buscarVenta(Integer idVenta) throws Exception {
        strSQL = "SELECT V.*, C.nombres as nombreCliente, U.nomUsuario " +
                 "FROM VENTA V " +
                 "INNER JOIN CLIENTE C ON V.idCliente = C.idcliente " +
                 "INNER JOIN USUARIO U ON V.idUsuario = U.idUsuario " +
                 "WHERE V.idVenta = " + idVenta;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar venta: " + e.getMessage());
        }
    }

    public Integer generarIdVenta() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idVenta), 0) + 1 AS codigo FROM VENTA";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar ID de venta: " + e.getMessage());
        }
        return 0;
    }

    public void registrar(Integer idVenta, String fecha, Integer idCliente, Integer idUsuario, float total, boolean estado) throws Exception {
        // El formato de fecha debe ser 'YYYY-MM-DD' para la mayoría de bases de datos.
        strSQL = "INSERT INTO VENTA (idVenta, fecha, idCliente, idUsuario, total, estado) " +
                 "VALUES(" + idVenta + ", '" + fecha + "', " + idCliente + ", " + idUsuario + ", " + total + ", " + estado + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar venta: " + e.getMessage());
        }
    }
    
    public void modificar(Integer idVenta, String fecha, Integer idCliente, Integer idUsuario, float total, boolean estado) throws Exception {
        strSQL = "UPDATE VENTA SET fecha='" + fecha + "', idCliente=" + idCliente + ", idUsuario=" + idUsuario + 
                 ", total=" + total + ", estado=" + estado + " WHERE idVenta=" + idVenta;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar venta: " + e.getMessage());
        }
    }
    

    public void anularVenta(Integer idVenta) throws Exception {
        strSQL = "UPDATE VENTA SET estado = false WHERE idVenta = " + idVenta;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al anular la venta: " + e.getMessage());
        }
    }


    public void eliminarVenta(Integer idVenta) throws Exception {
        String strSQLDetalles = "DELETE FROM DETALLE_VENTA WHERE idVenta = " + idVenta;
        // Luego, se elimina la venta principal.
        String strSQLVenta = "DELETE FROM VENTA WHERE idVenta = " + idVenta;
        
        try {
            // Se recomienda manejar esto como una transacción, pero siguiendo el estilo actual, lo hacemos en dos pasos.
            objConectar.ejecutarBD(strSQLDetalles);
            objConectar.ejecutarBD(strSQLVenta);
        } catch (Exception e) {
            throw new Exception("Error al eliminar venta: " + e.getMessage());
        }
    }
}