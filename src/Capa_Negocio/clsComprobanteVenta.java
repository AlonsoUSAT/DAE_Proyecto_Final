package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Carlos Otoya
 */
public class clsComprobanteVenta {
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    
    public ResultSet consultarComprobantes() throws Exception{
        strSQL="""
               select 
                   cv.idcomprobante,
                   cv.correlativo,
                   cv.fechhora as fecha_hora,
                   concat(p.nombres, ' ', p.apellidopaterno, ' ', coalesce(p.apellidomaterno, '')) as cliente,
                   c.nrodoc as documento,
                   mp.nombre as medio_pago,
                   sc.serie,
                   v.idventa,
                   to_char(v.fecha, 'YYYY-MM-DD') as fecha_venta,
                   to_char(v.hora, 'HH24:MI:SS') as hora_venta,
                   cv.subtotal,
                   cv.igv,
                   cv.total,
                   case when cv.estado = true then 'Cancelado' else 'Pendiente' end as estado
               from comprobante_venta cv
               inner join cliente c on cv.idcliente = c.idcliente
               inner join persona p on c.idcliente = p.idcliente
               inner join medio_pago mp on cv.idmediopago = mp.idmediopago
               inner join serie_comprobante sc on cv.idserie = sc.idserie
               inner join venta v on cv.idventa = v.idventa
               order by cv.idcomprobante;
               """;
        try{
            rs=objConectar.consultarBD(strSQL);
            return rs;
        }catch(Exception e){
            throw new Exception("Error: "+e.getMessage());
        }
    }
    
    public Integer generarCodigoComprobante() throws Exception {
        strSQL = "SELECT coalesce(max(idComprobante),0)+1 AS codigo FROM comprobante_venta";
        try {
            rs = objConectar.consultarBD(strSQL);
            while (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de comprobante: " + e.getMessage());
        }
        return 0;
    }
    
    public void insertarComprobante(int idComprobante, int correlativo, double subtotal, double igv, double total,
                                    int idVenta, int idCliente, int idMedioPago, int idSerie) throws Exception {
        strSQL = """
            INSERT INTO comprobante_venta
            (idComprobante, correlativo, subtotal, igv, total, idVenta, idCliente, idMedioPago, idSerie)
            VALUES (%d, %d, %.2f, %.2f, %.2f, %d, %d, %d, %d);
        """.formatted(idComprobante, correlativo, subtotal, igv, total, idVenta, idCliente, idMedioPago, idSerie);

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al registrar comprobante: " + e.getMessage());
        }
    }
    
    public void modificarComprobante(int idComprobante, int correlativo, double subtotal, double igv, double total,
                                     int idVenta, int idCliente, int idMedioPago, int idSerie, boolean estado) throws Exception {
        strSQL = """
            UPDATE comprobante_venta
            SET correlativo = %d,
                subtotal = %.2f,
                igv = %.2f,
                total = %.2f,
                idVenta = %d,
                idCliente = %d,
                idMedioPago = %d,
                idSerie = %d,
                estado = %b
            WHERE idComprobante = %d;
        """.formatted(correlativo, subtotal, igv, total, idVenta, idCliente, idMedioPago, idSerie, estado, idComprobante);

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar comprobante: " + e.getMessage());
        }
    }
    
    public void eliminarComprobante(int idComprobante) throws Exception {
        strSQL = """
            DELETE FROM comprobante_venta
            WHERE idComprobante = %d;
        """.formatted(idComprobante);
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar comprobante: " + e.getMessage());
        }
    }
    
    public void darDeBajaComprobante(int idComprobante) throws Exception {
        strSQL = "UPDATE comprobante_venta SET estado = false WHERE idComprobante = " + idComprobante;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el comprobante: " + e.getMessage());
        }
    }
    
    public ResultSet buscarComprobantePorId(int idComprobante) throws Exception {
        strSQL = """
            SELECT c.*, m.nombre AS nombre_medio_pago, s.serie||' - '||t.nombre_tipocomprobante as serie
                FROM comprobante_venta c
                INNER JOIN medio_pago m ON c.idmediopago = m.idmediopago
                INNER JOIN serie_comprobante s ON c.idserie = s.idserie
                INNER JOIN tipo_comprobante t ON s.id_tipocomprobante = t.id_tipocomprobante
                WHERE c.idcomprobante =""" + idComprobante;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar comprobante por ID: " + e.getMessage());
        }
    }
    
    public void darDeBajaComprobante(int idComprobante, boolean estado) throws Exception {
        strSQL = """
            UPDATE comprobante_venta
            SET estado = %b
            WHERE idComprobante = %d;
        """.formatted(estado, idComprobante);

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al cambiar estado del comprobante: " + e.getMessage());
        }
    }
    
}