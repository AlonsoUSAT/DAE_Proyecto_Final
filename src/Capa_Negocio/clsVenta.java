package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.*;

public class clsVenta {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public ResultSet buscarVenta(Integer idVenta) throws Exception {
        strSQL = "SELECT V.*, C.nroDoc, C.direccion, C.id_tipoDoc, TD.nom_tipoDoc "
                + "FROM VENTA V "
                + "INNER JOIN CLIENTE C ON V.idCliente = C.idCliente "
                + "INNER JOIN TIPO_DOCUMENTO TD ON C.id_tipoDoc = TD.id_tipoDoc "
                + "WHERE V.idVenta = " + idVenta; // Vulnerable a SQL Injection
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar venta: " + e.getMessage());
        }
    }

    public ResultSet listarDetalleVenta(Integer idVenta) throws Exception {
        strSQL = "SELECT DV.*, P.nombre AS nombreProducto, "
                + "CONCAT(TP.nombreTipoPresentacion, ' x ', PR.cantidad, ' ', U.nombreUnidad) AS presentacion, "
                + "DV.precio AS precioUnitarioVenta " // El precio unitario de la venta
                + "FROM DETALLE_VENTA DV "
                + "INNER JOIN PRODUCTO P ON DV.idProducto = P.idProducto "
                // JOINs para obtener la descripción de la presentación
                + "INNER JOIN PRESENTACION PRES ON DV.idPresentacion = PRES.idPresentacion "
                + "INNER JOIN TIPO_PRESENTACION TP ON PRES.tipoPresentacion = TP.idTipoPresentacion "
                + "INNER JOIN UNIDAD U ON PRES.idUnidad = U.idUnidad "
                + "WHERE DV.idVenta = " + idVenta; // Vulnerable a SQL Injection
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar detalle: " + e.getMessage());
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

    public void registrar(String codVenta, String total, String subtotal, String igv,
            boolean estado, String idCliente, javax.swing.JTable tblDetalle) throws Exception {

        Connection con = null;
        Statement sent = null;

        try {
           objConectar.conectar();
            con = objConectar.getCon(); // O la forma en que obtengas la conexión java.sql.Connection de tu clase JDBC
            con.setAutoCommit(false); // Desactivamos el guardado automático (Inicio de transacción)

            sent = con.createStatement();

            strSQL = "INSERT INTO VENTA (idVenta, fecha, hora, idCliente, estado) "
                    + "VALUES (" + codVenta + ", CURRENT_DATE, CURRENT_TIME, " + idCliente + ", " + estado + ")";
            sent.executeUpdate(strSQL);

            for (int i = 0; i < tblDetalle.getRowCount(); i++) {

                String idProd = String.valueOf(tblDetalle.getValueAt(i, 0));
                String idPres = String.valueOf(tblDetalle.getValueAt(i, 1)); // Importante: ID Presentación
                String precio = String.valueOf(tblDetalle.getValueAt(i, 3));
                String cant = String.valueOf(tblDetalle.getValueAt(i, 4));

               strSQL = "INSERT INTO DETALLE_VENTA (iddetalle, precio, cantidad, idventa, idproducto, idpresentacion) "
                        + "VALUES ((SELECT COALESCE(MAX(iddetalle),0)+1 FROM detalle_venta), "
                        + precio + ", " + cant + ", " + codVenta + ", " + idProd + ", " + idPres + ")";
                sent.executeUpdate(strSQL);

               strSQL = "UPDATE LOTE SET stockActual = stockActual - " + cant + " "
                        + "WHERE idLote = (SELECT idLote FROM LOTE "
                        + "WHERE idProducto=" + idProd + " AND idPresentacion=" + idPres
                        + " AND stockActual >= " + cant + " LIMIT 1)";
                sent.executeUpdate(strSQL);
            }

            con.commit();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new Exception("Error al guardar Venta: " + e.getMessage());
        } finally {
            if (con != null) {
                con.setAutoCommit(true); // Restaurar estado por defecto
            }
            objConectar.desconectar();
        }
    }
    
    public ResultSet consultarVentas(){
        
        return null;
        
    }

}
