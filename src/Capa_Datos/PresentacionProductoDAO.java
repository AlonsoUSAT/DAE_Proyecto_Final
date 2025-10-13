/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;

import Capa_Negocio.clsPresentacionProducto;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List; // Asegúrate de importar List
import java.math.BigDecimal; // Importa BigDecimal
/**
 * Clase de negocio para gestionar la tabla intermedia PRESENTACION_PRODUCTO.
 * @author USER
 */
public class PresentacionProductoDAO {
    
    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Lista todas las relaciones entre productos y sus presentaciones.
     * Para ser más útil, esta consulta une varias tablas para obtener nombres descriptivos.
     * @return Un ArrayList con los datos de la relación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
  /**
 * Lista todos los formatos/presentaciones de un producto específico con datos detallados
 * para ser mostrados en una tabla.
 * @param idProducto El ID del producto a buscar.
 * @return Una lista de arrays de objetos, donde cada array es una fila para la tabla.
 * @throws Exception Si ocurre un error de base de datos.
 */
public List<Object[]> listarFormatosParaTabla(int idProducto) throws Exception {
    List<Object[]> lista = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // --- CONSULTA SQL CORREGIDA ---
    String sql = "SELECT " +
                 "    pp.idPresentacion, " +
                 "    prod.idProducto, " +
                 "    CONCAT(tp.nombretipopresentacion, ' x ', pres.cantidad, ' ', u.nombreunidad) AS Presentacion, " +
                 "    pp.precio, " +
                 "    pp.stock, " +
                 //  AQUÍ ESTÁ LA CORRECCIÓN: Se cambió 'prod.estado' por 'pp.estado'
                 "    pp.estado AS Vigencia, " +
                 "    cat.nombreCategoria, " +
                 "    mar.nombre AS nombreMarca, " +
                 "    lab.nombreLaboratorio " +
                 "FROM " +
                 "    PRESENTACION_PRODUCTO pp " +
                 "JOIN " +
                 "    PRODUCTO prod ON pp.idProducto = prod.idProducto " +
                 "JOIN " +
                 "    PRESENTACION pres ON pp.idPresentacion = pres.idPresentacion " +
                 "JOIN " +
                 "    TIPO_PRESENTACION tp ON pres.tipoPresentacion = tp.idTipoPresentacion " +
                 "JOIN " +
                 "    UNIDAD u ON pres.idUnidad = u.idUnidad " +
                 "JOIN " +
                 "    CATEGORIA cat ON prod.idCategoria = cat.idCategoria " +
                 "JOIN " +
                 "    MARCA mar ON prod.idMarca = mar.idMarca " +
                 "JOIN " +
                 "    LABORATORIO lab ON prod.idDistribuidor = lab.idLaboratorio " +
                 "WHERE " +
                 "    pp.idProducto = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idProducto);
        rs = ps.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[9];
            fila[0] = rs.getInt("idPresentacion");
            fila[1] = rs.getInt("idProducto");
            fila[2] = rs.getString("Presentacion");
            fila[3] = rs.getBigDecimal("precio");
            fila[4] = rs.getInt("stock");
            // AQUÍ TAMBIÉN SE CORRIGE: Se usa el nuevo alias "Vigencia"
            fila[5] = rs.getBoolean("Vigencia");
            fila[6] = rs.getString("nombreCategoria");
            fila[7] = rs.getString("nombreMarca");
            fila[8] = rs.getString("nombreLaboratorio");
            lista.add(fila);
        }
    } catch (Exception e) {
        throw new Exception("Error al listar formatos para la tabla: " + e.getMessage());
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
    return lista;
}

    /**
     * Registra una nueva asociación entre un producto y una presentación.
     * @param idProd El ID del producto.
     * @param idPres El ID de la presentación.
     * @param precio El precio de venta para esta combinación.
     * @param stock El stock inicial.
     * @throws Exception Si ocurre un error de base de datos.
     */
   public void registrar(int idProd, int idPres, float precio, int stock, boolean estado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // CORRECCIÓN: Se añade el campo 'estado'
        String sql = "INSERT INTO PRESENTACION_PRODUCTO (idProducto, idPresentacion, precio, stock, estado) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            ps.setFloat(3, precio);
            ps.setInt(4, stock);
            ps.setBoolean(5, estado); // Se añade el estado
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar Presentacion_Producto: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Modifica una presentación de producto existente.
     * @param idProd El ID del producto.
     * @param idPres El ID de la presentación.
     * @param nuevoPrecio El nuevo precio a actualizar.
     * @param nuevoStock El nuevo stock a actualizar.
     * @param nuevoEstado El nuevo estado de vigencia.
     * @throws Exception Si ocurre un error.
     */
    public void modificar(int idProd, int idPres, float nuevoPrecio, int nuevoStock, boolean nuevoEstado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // CORRECCIÓN: Se añade el campo 'estado' a la actualización
        String sql = "UPDATE PRESENTACION_PRODUCTO SET precio = ?, stock = ?, estado = ? WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setFloat(1, nuevoPrecio);
            ps.setInt(2, nuevoStock);
            ps.setBoolean(3, nuevoEstado); // Se añade el nuevo estado
            ps.setInt(4, idProd);
            ps.setInt(5, idPres);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar Presentacion_Producto: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
    
    
    /**
     * Elimina una asociación entre un producto y una presentación.
     * @param idProd El ID del producto a eliminar.
     * @param idPres El ID de la presentación a eliminar.
     * @throws Exception Si ocurre un error de base de datos.
     */
    public void eliminar(int idProd, int idPres) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "DELETE FROM PRESENTACION_PRODUCTO WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar Presentacion_Producto: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Busca una relación específica de producto-presentación.
     * @param idProd El ID del producto.
     * @param idPres El ID de la presentación.
     * @return El objeto Presentacion_productoDAO encontrado, o null si no existe.
     * @throws Exception Si ocurre un error de base de datos.
     */
    public clsPresentacionProducto buscar(int idProd, int idPres) throws Exception {
        clsPresentacionProducto ppDAO = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // CORRECCIÓN: Se añade el campo 'estado' a la consulta
        String sql = "SELECT idProducto, idPresentacion, precio, stock, estado FROM PRESENTACION_PRODUCTO WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            rs = ps.executeQuery();

            if (rs.next()) {
                ppDAO = new clsPresentacionProducto();
                ppDAO.setIdProducto(rs.getInt("idProducto"));
                ppDAO.setIdPresentacion(rs.getInt("idPresentacion"));
                ppDAO.setPrecio(rs.getFloat("precio"));
                ppDAO.setStock(rs.getInt("stock"));
                ppDAO.setEstado(rs.getBoolean("estado")); // Se obtiene el estado
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar Presentacion_Producto: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return ppDAO;
    }

    public int obtenerStockTotalDeLotes(int idProducto, int idPresentacion) throws Exception {
    int stockTotal = 0;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    // Consulta que suma el stockActual de la tabla LOTE
    String sql = "SELECT SUM(stockActual) AS stock_total FROM LOTE WHERE idProducto = ? AND idPresentacion = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.setInt(2, idPresentacion);
        rs = ps.executeQuery();

        if (rs.next()) {
            // Obtenemos el resultado de la suma. Si no hay lotes, devolverá 0.
            stockTotal = rs.getInt("stock_total");
        }
    } catch (Exception e) {
        throw new Exception("Error al calcular el stock total de lotes: " + e.getMessage());
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
    
    return stockTotal;
}


   public void actualizarStock(int idProducto, int idPresentacion, int nuevoStock) throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    String sql = "UPDATE PRESENTACION_PRODUCTO SET stock = ? WHERE idProducto = ? AND idPresentacion = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, nuevoStock);
        ps.setInt(2, idProducto);
        ps.setInt(3, idPresentacion);
        ps.executeUpdate();
    } catch (Exception e) {
        throw new Exception("Error al actualizar el stock en la presentación: " + e.getMessage());
    } finally {
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
}
    
    public void darBaja(int idProd, int idPres) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE PRESENTACION_PRODUCTO SET estado = false WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al dar de baja la presentación del producto: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
    
}