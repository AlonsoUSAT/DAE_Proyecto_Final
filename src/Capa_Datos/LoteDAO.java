package Capa_Datos;

import Capa_Negocio.clsLote;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Tiznado Leon
 */
public class LoteDAO {

    private final clsJDBC objConectar = new clsJDBC();

    
    public ArrayList<clsLote> listarLotes() throws Exception {
    ArrayList<clsLote> lotes = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

   
    String sql = "SELECT l.idLote, l.nroLote, l.fechaFabricacion, l.fechaVencimiento, " +
                 "l.cantidadRecibida, l.stockActual, l.estado, l.idPresentacion, l.idProducto, pr.nombre AS nombreproducto, " + 
                 "CONCAT(tp.nombretipopresentacion, ' x ', p.cantidad, ' ', u.nombreunidad) AS descripcionPresentacion " +
                 "FROM LOTE l " +
                 "INNER JOIN PRODUCTO pr ON l.idProducto = pr.idProducto " +
                 "INNER JOIN PRESENTACION p ON l.idPresentacion = p.idPresentacion " +
                 "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idtipopresentacion " +
                 "INNER JOIN UNIDAD u ON p.idunidad = u.idunidad";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            clsLote dao = new clsLote(
                rs.getInt("idLote"),
                rs.getString("nroLote"),
                rs.getDate("fechaFabricacion"),
                rs.getDate("fechaVencimiento"),
                rs.getInt("cantidadRecibida"),
                rs.getInt("stockActual"),
                rs.getBoolean("estado"),
                rs.getInt("idPresentacion"),
                rs.getInt("idProducto"), 
                rs.getString("nombreproducto"),
                rs.getString("descripcionPresentacion")
            );
            lotes.add(dao);
        }
    } catch (Exception e) {
        throw new Exception("Error al listar lotes: " + e.getMessage());
    } finally {
     
    }
    return lotes;
}

  
  public ArrayList<clsLote> listarLotesPorPresentacion(int idProducto, int idPresentacion) throws Exception {
    ArrayList<clsLote> lotes = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    
    String sql = "SELECT l.idLote, l.nroLote, l.fechaFabricacion, l.fechaVencimiento, " +
                 "l.cantidadRecibida, l.stockActual, l.estado, l.idPresentacion, l.idProducto, pr.nombre AS nombreproducto, " + 
                 "CONCAT(tp.nombreTipoPresentacion, ' x ', p.cantidad, ' ', u.nombreUnidad) AS descripcionPresentacion " +
                 "FROM LOTE l " +
                 "INNER JOIN PRODUCTO pr ON l.idProducto = pr.idProducto " +
                 "INNER JOIN PRESENTACION p ON l.idPresentacion = p.idPresentacion " +
                 "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idTipoPresentacion " +
                 "INNER JOIN UNIDAD u ON p.idUnidad = u.idUnidad " +
                 "WHERE l.idProducto = ? AND l.idPresentacion = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.setInt(2, idPresentacion);
        rs = ps.executeQuery();

        while (rs.next()) {
            clsLote dao = new clsLote(
                rs.getInt("idLote"),
                rs.getString("nroLote"),
                rs.getDate("fechaFabricacion"),
                rs.getDate("fechaVencimiento"),
                rs.getInt("cantidadRecibida"),
                rs.getInt("stockActual"),
                rs.getBoolean("estado"),
                rs.getInt("idPresentacion"),
                rs.getInt("idProducto"), 
                rs.getString("nombreproducto"),
                rs.getString("descripcionPresentacion")
            );
            lotes.add(dao);
        }
    } catch (Exception e) {
        throw new Exception("Error al listar lotes por presentación: " + e.getMessage());
    } finally {
       
    }
    return lotes;
}

    
   public void registrarLote(int idLote, String nroLote, Date fechaFab, Date fechaVen, int cantRecibida, int idPresentacion, int idProducto, boolean estado) throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    
   
    String sql = "INSERT INTO LOTE (idLote, nroLote, fechaFabricacion, fechaVencimiento, cantidadRecibida, stockActual, idPresentacion, idProducto, estado) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        
        ps.setInt(1, idLote);
        ps.setString(2, nroLote);
        
       
        if (fechaFab != null) {
            ps.setDate(3, new java.sql.Date(fechaFab.getTime()));
        } else {
            ps.setNull(3, java.sql.Types.DATE);
        }
        
        ps.setDate(4, new java.sql.Date(fechaVen.getTime()));
        ps.setInt(5, cantRecibida);
        ps.setInt(6, cantRecibida); 
        ps.setInt(7, idPresentacion);
        ps.setInt(8, idProducto);
        
       
        ps.setBoolean(9, estado); 
        
        ps.executeUpdate();
    } catch (Exception e) {
        throw new Exception("Error al registrar lote: " + e.getMessage());
    } finally {
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
}
    
    public void modificarLote(int idLote, Date fechaFab, Date fechaVen, int nuevaCantidadRecibida, boolean estado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = objConectar.conectar();
            
           
            clsLote loteActual = this.buscarLote(idLote);
            if (loteActual == null) {
                throw new Exception("El lote con ID " + idLote + " no existe.");
            }

           
            int unidadesMovidas = loteActual.getCantidadRecibida() - loteActual.getStockActual();

         
            int nuevoStockActual = nuevaCantidadRecibida - unidadesMovidas;

            
            if (nuevoStockActual < 0) {
                throw new Exception("La nueva cantidad es inconsistente con las unidades ya vendidas. El stock no puede ser negativo.");
            }

            String sql = "UPDATE LOTE SET fechaFabricacion = ?, fechaVencimiento = ?, cantidadRecibida = ?, stockActual = ?, estado = ? WHERE idLote = ?";
            ps = conn.prepareStatement(sql);
            
            if (fechaFab != null) {
                ps.setDate(1, new java.sql.Date(fechaFab.getTime()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }
            ps.setDate(2, new java.sql.Date(fechaVen.getTime()));
            ps.setInt(3, nuevaCantidadRecibida); 
            ps.setInt(4, nuevoStockActual);    
            ps.setBoolean(5, estado);
            ps.setInt(6, idLote);
            
            ps.executeUpdate();
            
        } catch (Exception e) {
           
            throw new Exception("Error al modificar lote: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
   
  public clsLote buscarLote(int idLote) throws Exception {
    clsLote loteEncontrado = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
  
    String sql = "SELECT l.idLote, l.nroLote, l.fechaFabricacion, l.fechaVencimiento, " +
                 "l.cantidadRecibida, l.stockActual, l.estado, l.idPresentacion, l.idProducto, " +
                 "pr.nombre AS nombreproducto, " +
                 "CONCAT(tp.nombretipopresentacion, ' x ', p.cantidad, ' ', u.nombreunidad) AS descripcionPresentacion " +
                 "FROM LOTE l " +
                 "INNER JOIN PRODUCTO pr ON l.idProducto = pr.idProducto " +
                 "INNER JOIN PRESENTACION p ON l.idPresentacion = p.idPresentacion " +
                 "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idtipopresentacion " +
                 "INNER JOIN UNIDAD u ON p.idunidad = u.idunidad " +
                 "WHERE l.idLote = ?";
    
    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idLote);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            loteEncontrado = new clsLote(
                rs.getInt("idLote"),
                rs.getString("nroLote"),
                rs.getDate("fechaFabricacion"),
                rs.getDate("fechaVencimiento"),
                rs.getInt("cantidadRecibida"),
                rs.getInt("stockActual"),
                rs.getBoolean("estado"),
                rs.getInt("idPresentacion"),
                rs.getInt("idProducto"), 
                rs.getString("nombreproducto"),
                rs.getString("descripcionPresentacion")
            );
        }
    } catch (Exception e) {
        throw new Exception("Error al buscar lote: " + e.getMessage());
    } finally {
     
    }
    return loteEncontrado;
}
   
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
    
    
   public String generarNumeroLote(int idProducto, int idPresentacion, int cantidadRecibida) throws Exception { // ✅ CAMBIO 1: Añadido idProducto
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int correlativo = 1;
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
    
    String correlativoFormateado = String.format("%02d", correlativo);
    
  
    return "P" + idProducto + "-" + correlativoFormateado + "-" + cantidadRecibida;
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