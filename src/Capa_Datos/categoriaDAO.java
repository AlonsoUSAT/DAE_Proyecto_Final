package Capa_Datos;

import Capa_Negocio.clsCategoria; // O como hayas llamado a tu clase de negocio
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class categoriaDAO {

    // Instancia de nuestra clase de conexión
    private clsJDBC objConexion = new clsJDBC();

    public List<clsCategoria> listarCategorias() throws Exception {
        List<clsCategoria> categorias = new ArrayList<>();
        String sql = "SELECT idCategoria, nombreCategoria FROM CATEGORIA ORDER BY nombreCategoria";
        
        // try-with-resources se encarga de cerrar la conexión y el statement automáticamente
        try (Connection con = objConexion.conectar(); // <--- ASÍ SE OBTIENE LA CONEXIÓN AHORA
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsCategoria cat = new clsCategoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombreCategoria(rs.getString("nombreCategoria"));
                categorias.add(cat);
            }
        } catch (SQLException | ClassNotFoundException e) { // Captura ambos tipos de error
            throw new Exception("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }

    public clsCategoria buscarPorId(int id) throws Exception {
        clsCategoria cat = null;
        String sql = "SELECT idCategoria, nombreCategoria FROM CATEGORIA WHERE idCategoria = ?";
        
        try (Connection con = objConexion.conectar(); // <--- ASÍ SE OBTIENE LA CONEXIÓN
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cat = new clsCategoria();
                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    cat.setNombreCategoria(rs.getString("nombreCategoria"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar categoría: " + e.getMessage());
        }
        return cat;
    }

    public void registrarCategoria(clsCategoria cat) throws Exception {
        String sql = "INSERT INTO CATEGORIA (idCategoria, nombreCategoria) VALUES (?, ?)";
        
        try (Connection con = objConexion.conectar(); // <--- ASÍ SE OBTIENE LA CONEXIÓN
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, cat.getIdCategoria());
            ps.setString(2, cat.getNombreCategoria());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
                throw new Exception("Error: El ID o el Nombre de la categoría ya existe.");
            } else {
                throw new Exception("Error al registrar categoría: " + e.getMessage());
            }
        }
    }

    public void modificarCategoria(clsCategoria cat) throws Exception {
        String sql = "UPDATE CATEGORIA SET nombreCategoria = ? WHERE idCategoria = ?";
        
        try (Connection con = objConexion.conectar(); // <--- ASÍ SE OBTIENE LA CONEXIÓN
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cat.getNombreCategoria());
            ps.setInt(2, cat.getIdCategoria());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) {
                 throw new Exception("Error: El nombre de la categoría ya existe.");
            } else {
                throw new Exception("Error al modificar categoría: " + e.getMessage());
            }
        }
    }

    public void eliminarCategoria(int id) throws Exception {
        String sql = "DELETE FROM CATEGORIA WHERE idCategoria = ?";
        
        try (Connection con = objConexion.conectar(); // <--- ASÍ SE OBTIENE LA CONEXIÓN
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23503")) {
                throw new Exception("Error: No se puede eliminar la categoría porque está siendo usada por un producto.");
            } else {
                throw new Exception("Error al eliminar categoría: " + e.getMessage());
            }
        }
    }
}