/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.PresentacionProductoDAO;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Clase de negocio para gestionar la tabla intermedia PRESENTACION_PRODUCTO.
 * @author USER
 */
public class clsPresentacionProducto {
    
    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Lista todas las relaciones entre productos y sus presentaciones.
     * Para ser más útil, esta consulta une varias tablas para obtener nombres descriptivos.
     * @return Un ArrayList con los datos de la relación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public ArrayList<PresentacionProductoDAO> listar() throws Exception {
        ArrayList<PresentacionProductoDAO> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Nota: Esta consulta es un ejemplo. Puedes adaptarla para obtener más datos descriptivos.
        String sql = "SELECT idProducto, idPresentacion, precio, stock FROM PRESENTACION_PRODUCTO ORDER BY idProducto, idPresentacion";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PresentacionProductoDAO ppDAO = new PresentacionProductoDAO();
                ppDAO.setIdProducto(rs.getInt("idProducto"));
                ppDAO.setIdPresentacion(rs.getInt("idPresentacion"));
                ppDAO.setPrecio(rs.getFloat("precio"));
                ppDAO.setStock(rs.getInt("stock"));
                lista.add(ppDAO);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar Presentacion_Producto: " + e.getMessage());
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
    public void registrar(int idProd, int idPres, float precio, int stock) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO PRESENTACION_PRODUCTO (idProducto, idPresentacion, precio, stock) VALUES (?, ?, ?, ?)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            ps.setFloat(3, precio);
            ps.setInt(4, stock);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar Presentacion_Producto: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Modifica el precio o stock de una presentación de producto existente.
     * La identificación se hace usando la llave compuesta (idProducto y idPresentacion).
     * @param idProd El ID del producto (parte de la llave).
     * @param idPres El ID de la presentación (parte de la llave).
     * @param nuevoPrecio El nuevo precio a actualizar.
     * @param nuevoStock El nuevo stock a actualizar.
     * @throws Exception Si ocurre un error de base de datos.
     */
    public void modificar(int idProd, int idPres, float nuevoPrecio, int nuevoStock) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "UPDATE PRESENTACION_PRODUCTO SET precio = ?, stock = ? WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setFloat(1, nuevoPrecio);
            ps.setInt(2, nuevoStock);
            ps.setInt(3, idProd);
            ps.setInt(4, idPres);
            
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
    public PresentacionProductoDAO buscar(int idProd, int idPres) throws Exception {
        PresentacionProductoDAO ppDAO = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT idProducto, idPresentacion, precio, stock FROM PRESENTACION_PRODUCTO WHERE idProducto = ? AND idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idProd);
            ps.setInt(2, idPres);
            rs = ps.executeQuery();

            if (rs.next()) {
                ppDAO = new PresentacionProductoDAO();
                ppDAO.setIdProducto(rs.getInt("idProducto"));
                ppDAO.setIdPresentacion(rs.getInt("idPresentacion"));
                ppDAO.setPrecio(rs.getFloat("precio"));
                ppDAO.setStock(rs.getInt("stock"));
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
}