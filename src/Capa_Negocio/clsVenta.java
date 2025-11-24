package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class clsVenta {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent = null;

    // Busca la venta para mostrarla luego (opcional)
    public ResultSet buscarVenta(Integer idVenta) throws Exception {
        strSQL = "SELECT V.*, C.nroDoc, C.direccion, C.id_tipodoc "
                + "FROM venta V "
                + "INNER JOIN cliente C ON V.idcliente = C.idcliente "
                + "WHERE V.idventa = " + idVenta;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar venta: " + e.getMessage());
        }
    }

    public ResultSet listarDetalleVenta(Integer idVenta) throws Exception {
        // Corregido para usar los nombres reales de tu BD
        strSQL = "SELECT DV.*, P.nombre AS nombreProducto, "
                + "DV.precio AS precioUnitarioVenta "
                + "FROM detalle_venta DV "
                + "INNER JOIN producto P ON DV.idproducto = P.idproducto "
                + "WHERE DV.idventa = " + idVenta;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar detalle: " + e.getMessage());
        }
    }

    // Generadores de ID
    public Integer generarIdVenta() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idventa), 0) + 1 AS codigo FROM venta";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error ID Venta: " + e.getMessage());
        }
        return 1;
    }

    public Integer generarIdComprobante() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idcomprobante), 0) + 1 AS codigo FROM comprobante_venta";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error ID Comprobante");
        }
        return 1;
    }

    public void registrar(int codVenta, String total, String subtotal, String igv,
            boolean esBoleta, int idCliente, int idUsuario, JTable tblDetalle) throws Exception {

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false); // INICIO TRANSACCIÓN
            sent = con.createStatement();

            strSQL = "INSERT INTO venta (idventa, fecha, hora, idcliente, idusuario, estado) "
                    + "VALUES (" + codVenta + ", CURRENT_DATE, CURRENT_TIME, " + idCliente + ", " + idUsuario + ", true)";
            sent.executeUpdate(strSQL);

            DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();

            for (int i = 0; i < modelo.getRowCount(); i++) {

                String idProd = String.valueOf(modelo.getValueAt(i, 0));
                String idPres = String.valueOf(modelo.getValueAt(i, 1));
                String cant = String.valueOf(modelo.getValueAt(i, 4));
                String precioVenta = String.valueOf(modelo.getValueAt(i, 6)).replace(",", ".");
                strSQL = "INSERT INTO detalle_venta (iddetalle, precio, cantidad, idventa, idproducto, idpresentacion) "
                        + "VALUES ((SELECT COALESCE(MAX(iddetalle),0)+1 FROM detalle_venta), "
                        + precioVenta + ", " + cant + ", " + codVenta + ", " + idProd + ", " + idPres + ")";
                sent.executeUpdate(strSQL);
                strSQL = "UPDATE presentacion_producto SET stock = stock - " + cant + " "
                        + "WHERE idproducto=" + idProd + " AND idpresentacion=" + idPres;
                sent.executeUpdate(strSQL);
            }

            int idComprobante = generarIdComprobante();
            int idTipoComp = esBoleta ? 1 : 2;
            int idSerie = 1;
            ResultSet rsSerie = sent.executeQuery("SELECT idserie FROM serie_comprobante WHERE id_tipocomprobante=" + idTipoComp + " LIMIT 1");
            if (rsSerie.next()) {
                idSerie = rsSerie.getInt("idserie");
            }
            int correlativo = 1;
            ResultSet rsCorr = sent.executeQuery("SELECT ultimonumero + 1 as corre FROM serie_comprobante WHERE idserie=" + idSerie);
            if (rsCorr.next()) {
                correlativo = rsCorr.getInt("corre");
            }

            strSQL = "INSERT INTO comprobante_venta (idcomprobante, correlativo, fechhora, estado, subtotal, igv, total, idventa, idmediopago, idserie) "
                    + "VALUES (" + idComprobante + ", " + correlativo + ", CURRENT_TIMESTAMP, true, "
                    + subtotal.replace(",", ".") + ", " + igv.replace(",", ".") + ", " + total.replace(",", ".") + ", "
                    + codVenta + ", 1, " + idSerie + ")"; // 1 = Efectivo
            sent.executeUpdate(strSQL);

            sent.executeUpdate("UPDATE serie_comprobante SET ultimonumero = " + correlativo + " WHERE idserie=" + idSerie);

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
                con.setAutoCommit(true);
                objConectar.desconectar();
            }
        }
    }
}
