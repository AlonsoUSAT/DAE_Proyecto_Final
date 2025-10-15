
package Capa_Datos;


import Capa_Negocio.clsCategoria;
import Capa_Negocio.clsLaboratorio;
import Capa_Negocio.clsMarca;
import Capa_Negocio.clsProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// autor: Fernando Hernández
public class ProductoDAO {

    private final clsJDBC objConexion = new clsJDBC();

    
    public List<clsProducto> listar() throws Exception {
        List<clsProducto> productos = new ArrayList<>();
        
        String sql = "SELECT p.idProducto, p.nombre, p.descripcion, p.estado, "
                + "m.idMarca, m.nombre AS nombreMarca, "
                + "c.idCategoria, c.nombreCategoria, "
                + "l.idLaboratorio, l.nombreLaboratorio "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio "
                + "ORDER BY p.nombre";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setNombre(rs.getString("nombreMarca"));

                clsCategoria categoria = new clsCategoria();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNombreCategoria(rs.getString("nombreCategoria"));

                clsLaboratorio distribuidor = new clsLaboratorio();
                distribuidor.setIdLaboratorio(rs.getInt("idLaboratorio"));
                distribuidor.setNombreLaboratorio(rs.getString("nombreLaboratorio"));

                clsProducto producto = new clsProducto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getBoolean("estado"));
                producto.setMarca(marca);
                producto.setCategoria(categoria);
                producto.setDistribuidor(distribuidor);

                productos.add(producto);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    
    public void insertar(clsProducto producto) throws Exception {
        
        String sql = "INSERT INTO PRODUCTO(idProducto, nombre, descripcion, estado, idMarca, idCategoria, idDistribuidor) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            
            ps.setString(3, producto.getDescripcion());
            ps.setBoolean(4, producto.isEstado());
            ps.setInt(5, producto.getMarca().getIdMarca());
            ps.setInt(6, producto.getCategoria().getIdCategoria());
            ps.setInt(7, producto.getDistribuidor().getIdLaboratorio());

            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException) e).getSQLState().equals("23505")) {
                throw new Exception("Error: El ID o el nombre del producto ya existe.");
            } else {
                throw new Exception("Error al registrar el producto: " + e.getMessage());
            }
        }
    }

    
    public void modificar(clsProducto producto) throws Exception {
        
        String sql = "UPDATE PRODUCTO SET nombre=?, descripcion=?, estado=?, idMarca=?, idCategoria=?, idDistribuidor=? "
                + "WHERE idProducto=?";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            
            ps.setString(2, producto.getDescripcion());
            ps.setBoolean(3, producto.isEstado());
            ps.setInt(4, producto.getMarca().getIdMarca());
            ps.setInt(5, producto.getCategoria().getIdCategoria());
            ps.setInt(6, producto.getDistribuidor().getIdLaboratorio());
            ps.setInt(7, producto.getIdProducto());

            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al actualizar el producto: " + e.getMessage());
        }
    }

    
    public void eliminar(int idProducto) throws Exception {
        String sql = "DELETE FROM PRODUCTO WHERE idProducto = ?";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException) e).getSQLState().equals("23503")) {
                throw new Exception("Error: No se puede eliminar. El producto está en uso (ej: en una venta).");
            } else {
                throw new Exception("Error al eliminar el producto: " + e.getMessage());
            }
        }
    }

    
    public void darDeBaja(int idProducto) throws Exception {
        String sql = "UPDATE PRODUCTO SET estado = false WHERE idProducto = ?";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al dar de baja el producto: " + e.getMessage());
        }
    }
    
    public clsProducto buscarPorId(int idProducto) throws Exception {
    clsProducto producto = null; 
    String sql = "SELECT p.idProducto, p.nombre, p.descripcion, p.estado, "
            + "m.idMarca, m.nombre AS nombreMarca, "
            + "c.idCategoria, c.nombreCategoria, "
            + "l.idLaboratorio, l.nombreLaboratorio "
            + "FROM PRODUCTO p "
            + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
            + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
            + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio "
            + "WHERE p.idProducto = ?"; // La condición de búsqueda

    try (Connection con = objConexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idProducto); 
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) { 
                producto = new clsProducto(); 
                
                
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setNombre(rs.getString("nombreMarca"));

                clsCategoria categoria = new clsCategoria();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNombreCategoria(rs.getString("nombreCategoria"));

                clsLaboratorio distribuidor = new clsLaboratorio();
                distribuidor.setIdLaboratorio(rs.getInt("idLaboratorio"));
                distribuidor.setNombreLaboratorio(rs.getString("nombreLaboratorio"));

                
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setEstado(rs.getBoolean("estado"));
                producto.setMarca(marca);
                producto.setCategoria(categoria);
                producto.setDistribuidor(distribuidor);
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        throw new Exception("Error al buscar el producto: " + e.getMessage());
    }
    return producto; 
}
    
     public Integer generarCodigo() throws Exception {
        Integer codigo = 1; 
      
        String sql = "SELECT COALESCE(MAX(idProducto), 0) + 1 AS codigo FROM PRODUCTO";

       
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

          
            if (rs.next()) {
                codigo = rs.getInt("codigo");
            }
        } catch (SQLException | ClassNotFoundException e) {
            
            throw new Exception("Error al generar el código del producto: " + e.getMessage());
        }

        
        return codigo;
    }
}