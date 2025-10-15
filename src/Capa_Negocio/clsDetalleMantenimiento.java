package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 * @author Mechan Vidaurre 
 */
public class clsDetalleMantenimiento {
    
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    
    public ResultSet listarDetallesPorVenta(Integer idVenta) throws Exception {
        // Hacemos un JOIN con PRODUCTO para obtener el nombre del producto.
        // Nota: Asegúrate que la columna de nombre en tu tabla PRODUCTO se llame 'descripcion'.
        strSQL = "SELECT D.*, P.descripcion as nombreProducto " +
                 "FROM DETALLE_VENTA D " +
                 "INNER JOIN PRODUCTO P ON D.idProducto = P.idProducto " +
                 "WHERE D.idVenta = " + idVenta;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar detalles de venta: " + e.getMessage());
        }
    }
    
    
    public void registrarDetalle(Integer idVenta, Integer idProducto, int cantidad, float precio, float subtotal) throws Exception {
        // ADVERTENCIA: Gravemente vulnerable a Inyección SQL.
        strSQL = "INSERT INTO DETALLE_VENTA (idVenta, idProducto, cantidad, precio, subtotal) " +
                 "VALUES (" + idVenta + ", " + idProducto + ", " + cantidad + ", " + precio + ", " + subtotal + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar detalle de venta: " + e.getMessage());
        }
    }
    
    
    public void eliminarDetalle(Integer idVenta, Integer idProducto) throws Exception {
        // ADVERTENCIA: Vulnerable a Inyección SQL.
        strSQL = "DELETE FROM DETALLE_VENTA WHERE idVenta = " + idVenta + " AND idProducto = " + idProducto;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar detalle de venta: " + e.getMessage());
        }
    }

   
    public void eliminarTodosLosDetalles(Integer idVenta) throws Exception {
        strSQL = "DELETE FROM DETALLE_VENTA WHERE idVenta = " + idVenta;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar todos los detalles de la venta: " + e.getMessage());
        }
    }
}