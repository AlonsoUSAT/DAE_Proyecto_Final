package Capa_Negocio;

import Capa_Datos.LoteDAO;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase de negocio para gestionar las operaciones de la entidad Lote.
 * @author USER
 */
public class clsLote {

    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todos los lotes de la base de datos.
     * @return ArrayList<LoteDAO> con los datos de los lotes.
     * @throws Exception Si ocurre un error de base de datos.
     */
    public ArrayList<LoteDAO> listarLotes() throws Exception {
        ArrayList<LoteDAO> lotes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT l.idLote, l.nroLote, l.fechaFabricacion, l.fechaVencimiento, " +
                     "l.cantidadRecibida, l.stockActual, l.estado, pr.nombreproducto, " +
                     "CONCAT(tp.nombrepresentacion, ' x ', p.cantidad, ' ', u.nombreunidad) AS descripcionPresentacion " +
                     "FROM LOTE l " +
                     "INNER JOIN PRODUCTO pr ON l.idProducto = pr.idProducto " +
                     "INNER JOIN PRESENTACION p ON l.idPresentacion = p.idPresentacion " +
                     "INNER JOIN TIPO_PRESENTACION tp ON p.idtipopresentacion = tp.idtipopresentacion " +
                     "INNER JOIN UNIDAD u ON p.idunidad = u.idunidad " +
                     "ORDER BY l.idLote";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                LoteDAO dao = new LoteDAO(
                    rs.getInt("idLote"),
                    rs.getString("nroLote"),
                    rs.getDate("fechaFabricacion"),
                    rs.getDate("fechaVencimiento"),
                    rs.getInt("cantidadRecibida"),
                    rs.getInt("stockActual"),
                    rs.getBoolean("estado"),
                    rs.getString("nombreproducto"),
                    rs.getString("descripcionPresentacion")
                );
                lotes.add(dao);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar lotes: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return lotes;
    }

    /**
     * Registra un nuevo lote en la base de datos, generando su nroLote automáticamente.
     * @param fechaFab Fecha de fabricación.
     * @param fechaVen Fecha de vencimiento.
     * @param cantRecibida Cantidad total del lote.
     * @param idPresentacion ID de la presentación del producto.
     * @param idProducto ID del producto.
     * @throws Exception Si ocurre un error.
     */
    public void registrarLote(Date fechaFab, Date fechaVen, int cantRecibida, int idPresentacion, int idProducto) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        // 1. Generamos el número de lote antes de insertarlo
        String nroLote = this.generarNumeroLote(idPresentacion, cantRecibida);

        String sql = "INSERT INTO LOTE (nroLote, fechaFabricacion, fechaVencimiento, cantidadRecibida, stockActual, idPresentacion, idProducto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, nroLote); // 2. Usamos el nroLote generado
            ps.setDate(2, new java.sql.Date(fechaFab.getTime()));
            ps.setDate(3, new java.sql.Date(fechaVen.getTime()));
            ps.setInt(4, cantRecibida);
            ps.setInt(5, cantRecibida); // El stock actual es igual a la cantidad recibida al registrar
            ps.setInt(6, idPresentacion);
            ps.setInt(7, idProducto);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar lote: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Modifica un lote existente.
     * @param idLote ID del lote a modificar.
     * @param nroLote Nuevo número de lote.
     * @param fechaFab Nueva fecha de fabricación.
     * @param fechaVen Nueva fecha de vencimiento.
     * @param stockActual Nuevo stock actual.
     * @param idPresentacion Nuevo ID de presentación.
     * @param idProducto Nuevo ID de producto.
     * @param estado Nuevo estado del lote.
     * @throws Exception Si ocurre un error.
     */
    public void modificarLote(int idLote, String nroLote, Date fechaFab, Date fechaVen, int stockActual, int idPresentacion, int idProducto, boolean estado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;

        String sql = "UPDATE LOTE SET nroLote = ?, fechaFabricacion = ?, fechaVencimiento = ?, " +
                     "stockActual = ?, idPresentacion = ?, idProducto = ?, estado = ? " +
                     "WHERE idLote = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, nroLote);
            ps.setDate(2, new java.sql.Date(fechaFab.getTime()));
            ps.setDate(3, new java.sql.Date(fechaVen.getTime()));
            ps.setInt(4, stockActual);
            ps.setInt(5, idPresentacion);
            ps.setInt(6, idProducto);
            ps.setBoolean(7, estado);
            ps.setInt(8, idLote);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar lote: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Busca un lote por su ID.
     * @param idLote El ID del lote a buscar.
     * @return Un objeto LoteDAO si se encuentra, o null si no existe.
     * @throws Exception Si ocurre un error.
     */
    public LoteDAO buscarLote(int idLote) throws Exception {
        LoteDAO loteEncontrado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT l.idLote, l.nroLote, l.fechaFabricacion, l.fechaVencimiento, " +
                     "l.cantidadRecibida, l.stockActual, l.estado, pr.nombreproducto, " +
                     "CONCAT(tp.nombrepresentacion, ' x ', p.cantidad, ' ', u.nombreunidad) AS descripcionPresentacion " +
                     "FROM LOTE l " +
                     "INNER JOIN PRODUCTO pr ON l.idProducto = pr.idProducto " +
                     "INNER JOIN PRESENTACION p ON l.idPresentacion = p.idPresentacion " +
                     "INNER JOIN TIPO_PRESENTACION tp ON p.idtipopresentacion = tp.idtipopresentacion " +
                     "INNER JOIN UNIDAD u ON p.idunidad = u.idunidad " +
                     "WHERE l.idLote = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idLote);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                loteEncontrado = new LoteDAO(
                    rs.getInt("idLote"),
                    rs.getString("nroLote"),
                    rs.getDate("fechaFabricacion"),
                    rs.getDate("fechaVencimiento"),
                    rs.getInt("cantidadRecibida"),
                    rs.getInt("stockActual"),
                    rs.getBoolean("estado"),
                    rs.getString("nombreproducto"),
                    rs.getString("descripcionPresentacion")
                );
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar lote: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return loteEncontrado;
    }
    
     /**
     * Genera un nuevo código para un lote, calculando el máximo ID existente y sumándole 1.
     * @return Integer El siguiente código disponible.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public Integer generarCodeLote() throws Exception {
        Integer codigo = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT COALESCE(MAX(idLote), 0) + 1 AS codigo FROM LOTE";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                codigo = rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de lote: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return codigo;
    }
    
    /**
     * Genera un número de lote informativo basado en la presentación, un correlativo y la cantidad.
     * Formato: P[idPresentacion]-[Correlativo]-[Cantidad] ej: "P1-02-50"
     * @param idPresentacion El ID de la presentación para la que se crea el lote.
     * @param cantidadRecibida La cantidad de unidades que llegan en este lote.
     * @return El String con el número de lote generado.
     * @throws Exception Si ocurre un error de base de datos.
     */
    public String generarNumeroLote(int idPresentacion, int cantidadRecibida) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int correlativo = 0;

        // 1. Contamos cuántos lotes existen ya para esta presentación y le sumamos 1
        String sql = "SELECT COUNT(*) + 1 AS siguiente FROM LOTE WHERE idPresentacion = ?";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPresentacion);
            rs = ps.executeQuery();

            if (rs.next()) {
                correlativo = rs.getInt("siguiente");
            }
        } catch (Exception e) {
            throw new Exception("Error al calcular el correlativo del lote: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }

        // 2. Formateamos el número para que tenga un 0 a la izquierda si es menor a 10 (ej: 01, 02, etc.)
        String correlativoFormateado = String.format("%02d", correlativo);

        // 3. Construimos y devolvemos el número de lote final
        return "P" + idPresentacion + "-" + correlativoFormateado + "-" + cantidadRecibida;
    }
    
     public void eliminarLote(int idLote) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM LOTE WHERE idLote = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idLote);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar el lote: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Cambia el estado de un lote a 'false' (inactivo).
     * @param idLote El ID del lote a dar de baja.
     * @throws Exception Si ocurre un error.
     */
    public void darBajaLote(int idLote) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE LOTE SET estado = false WHERE idLote = ?";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idLote);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el lote: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
}