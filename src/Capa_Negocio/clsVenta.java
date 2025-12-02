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
            boolean esBoleta, int idCliente, int idUsuario,
            DefaultTableModel modeloPagos, // <--- NUEVO PARÁMETRO: La tabla de pagos
            JTable tblDetalle) throws Exception {

        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false); // INICIO TRANSACCIÓN
            sent = con.createStatement();

            // 1. INSERTAR VENTA
            strSQL = "INSERT INTO venta (idventa, fecha, hora, idcliente, idusuario, estado) "
                    + "VALUES (" + codVenta + ", CURRENT_DATE, CURRENT_TIME, " + idCliente + ", " + idUsuario + ", true)";
            sent.executeUpdate(strSQL);

            // 2. INSERTAR DETALLE DE PRODUCTOS
            DefaultTableModel modeloDetalle = (DefaultTableModel) tblDetalle.getModel();

            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                String idProd = String.valueOf(modeloDetalle.getValueAt(i, 0));
                String idPres = String.valueOf(modeloDetalle.getValueAt(i, 1));
                String cant = String.valueOf(modeloDetalle.getValueAt(i, 4));
                String precioVenta = String.valueOf(modeloDetalle.getValueAt(i, 6)).replace(",", ".");

                // Insertar detalle
                strSQL = "INSERT INTO detalle_venta (iddetalle, precio, cantidad, idventa, idproducto, idpresentacion) "
                        + "VALUES ((SELECT COALESCE(MAX(iddetalle),0)+1 FROM detalle_venta), "
                        + precioVenta + ", " + cant + ", " + codVenta + ", " + idProd + ", " + idPres + ")";
                sent.executeUpdate(strSQL);

                // Actualizar Stock
                strSQL = "UPDATE presentacion_producto SET stock = stock - " + cant + " "
                        + "WHERE idproducto=" + idProd + " AND idpresentacion=" + idPres;
                sent.executeUpdate(strSQL);
            }

            // 3. OBTENER DATOS PARA EL COMPROBANTE
            int idComprobante = generarIdComprobante();
            int idTipoComp = esBoleta ? 1 : 2;
            int idSerie = 0;

            // Obtener Serie activa
            ResultSet rsSerie = sent.executeQuery("SELECT idserie FROM serie_comprobante WHERE id_tipocomprobante=" + idTipoComp + " AND estado=true LIMIT 1");
            if (rsSerie.next()) {
                idSerie = rsSerie.getInt("idserie");
            } else {
                throw new Exception("No se encontró una serie activa para el tipo de comprobante seleccionado.");
            }

            // Obtener Correlativo
            int correlativo = 1;
            ResultSet rsCorr = sent.executeQuery("SELECT ultimonumero + 1 as corre FROM serie_comprobante WHERE idserie=" + idSerie);
            if (rsCorr.next()) {
                correlativo = rsCorr.getInt("corre");
            }

            // 4. INSERTAR COMPROBANTE DE VENTA
            // NOTA: Se eliminó el campo 'idmediopago' de este INSERT porque ahora está en otra tabla
            strSQL = "INSERT INTO comprobante_venta (idcomprobante, correlativo, fechhora, estado, subtotal, igv, total, idventa, idserie) "
                    + "VALUES (" + idComprobante + ", " + correlativo + ", CURRENT_TIMESTAMP, true, "
                    + subtotal.replace(",", ".") + ", " + igv.replace(",", ".") + ", " + total.replace(",", ".") + ", "
                    + codVenta + ", " + idSerie + ")";
            sent.executeUpdate(strSQL);

            // 5. INSERTAR PAGOS (NUEVO BLOQUE)
            // Recorremos el modelo de la tabla de pagos (tblPagos) que recibimos de la vista
            for (int k = 0; k < modeloPagos.getRowCount(); k++) {
                // Según tu diseño de tabla: Col 0 = ID Medio, Col 2 = Monto
                String idMedio = modeloPagos.getValueAt(k, 0).toString();
                String montoPago = modeloPagos.getValueAt(k, 2).toString().replace(",", ".");

                strSQL = "INSERT INTO pago_venta (idventa, idmediopago, monto) "
                        + "VALUES (" + codVenta + ", " + idMedio + ", " + montoPago + ")";
                sent.executeUpdate(strSQL);
            }

            // 6. ACTUALIZAR CORRELATIVO EN LA SERIE
            sent.executeUpdate("UPDATE serie_comprobante SET ultimonumero = " + correlativo + " WHERE idserie=" + idSerie);

            con.commit(); // CONFIRMAR TODOS LOS CAMBIOS

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback(); // DESHACER CAMBIOS SI HAY ERROR
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

    // Método para buscar ventas por rango de fechas y nombre/doc de cliente
    public ResultSet listarVentasPorFechas(java.util.Date fechaIni, java.util.Date fechaFin, String texto) throws Exception {
        // Convertir fechas de Java a formato SQL
        java.sql.Date sqlFechaIni = new java.sql.Date(fechaIni.getTime());
        java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

        strSQL = "SELECT v.idventa, v.fecha, v.hora, "
                + "CASE WHEN e.razonsocial IS NOT NULL THEN e.razonsocial "
                + "ELSE concat(p.nombres, ' ', p.apellidopaterno) END as cliente, "
                + "cv.total, "
                + "CASE WHEN v.estado = true THEN 'EMITIDO' ELSE 'ANULADO' END as estado_texto "
                + "FROM venta v "
                + "INNER JOIN comprobante_venta cv ON v.idventa = cv.idventa "
                + "INNER JOIN cliente c ON v.idcliente = c.idcliente "
                + "LEFT JOIN persona p ON c.idcliente = p.idcliente "
                + "LEFT JOIN empresa e ON c.idcliente = e.idcliente "
                + "WHERE v.fecha BETWEEN '" + sqlFechaIni + "' AND '" + sqlFechaFin + "' "
                + "AND (p.nombres ILIKE '%" + texto + "%' OR e.razonsocial ILIKE '%" + texto + "%' OR c.nrodoc LIKE '%" + texto + "%') "
                + "ORDER BY v.idventa DESC";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar historial de ventas: " + e.getMessage());
        }
    }

    // =========================================================================
    //                        MÉTODO PARA ANULAR VENTA
    // =========================================================================
    public void anularVenta(int idVenta) throws Exception {
        try {
            objConectar.conectar();
            con = objConectar.getCon();
            con.setAutoCommit(false); // INICIO DE TRANSACCIÓN (Bloqueo de seguridad)
            sent = con.createStatement();

            // 1. VERIFICAR ESTADO ACTUAL
            // Consultamos si la venta existe y si está activa (true)
            ResultSet rsEstado = sent.executeQuery("SELECT estado FROM venta WHERE idventa = " + idVenta);
            if (rsEstado.next()) {
                if (!rsEstado.getBoolean("estado")) {
                    throw new Exception("La venta ya se encuentra ANULADA.");
                }
            } else {
                throw new Exception("La venta no existe.");
            }

            // 2. RECUPERAR PRODUCTOS PARA DEVOLVER AL STOCK
            // Usamos un Statement diferente o una lista en memoria para no romper el cursor del ResultSet
            String sqlDetalle = "SELECT idproducto, idpresentacion, cantidad FROM detalle_venta WHERE idventa = " + idVenta;
            
            // Creamos un Statement auxiliar solo para recorrer el detalle (lectura)
            Statement sentLectura = con.createStatement();
            ResultSet rsDetalle = sentLectura.executeQuery(sqlDetalle);

            // Statement auxiliar para ejecutar los updates (escritura)
            Statement sentUpdate = con.createStatement();

            while (rsDetalle.next()) {
                int idProd = rsDetalle.getInt("idproducto");
                int idPres = rsDetalle.getInt("idpresentacion");
                int cant = rsDetalle.getInt("cantidad");

                // LÓGICA DE DEVOLUCIÓN: Stock = Stock + Cantidad
                String sqlStock = "UPDATE presentacion_producto SET stock = stock + " + cant + " "
                                + "WHERE idproducto=" + idProd + " AND idpresentacion=" + idPres;
                sentUpdate.executeUpdate(sqlStock);
            }
            
            // Cerrar recursos auxiliares
            rsDetalle.close();
            sentLectura.close();
            sentUpdate.close();

            // 3. CAMBIAR ESTADO A FALSE (ANULADO)
            // Anulamos la cabecera de la venta
            sent.executeUpdate("UPDATE venta SET estado = false WHERE idventa = " + idVenta);
            
            // Anulamos también el comprobante asociado para que no salga en reportes contables
            sent.executeUpdate("UPDATE comprobante_venta SET estado = false WHERE idventa = " + idVenta);

            con.commit(); // CONFIRMAR TRANSACCIÓN (Todo salió bien)

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback(); // ERROR: Deshacer todos los cambios (evita stock corrupto)
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }
            }
            throw new Exception("Error al anular la venta: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    objConectar.desconectar();
                } catch (SQLException ex) {
                    // Ignorar error de cierre
                }
            }
        }
    }
    
    
}
