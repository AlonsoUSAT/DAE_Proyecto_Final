package Capa_Datos;

import Capa_Negocio.clsCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
// autor: Fernando Hernández
public class categoriaDAO {

    private clsJDBC objConexion = new clsJDBC();

    public List<clsCategoria> listarCategorias() throws Exception {
        List<clsCategoria> categorias = new ArrayList<>();
        
        String sql = "SELECT idCategoria, nombreCategoria, estado FROM CATEGORIA ORDER BY nombreCategoria";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsCategoria cat = new clsCategoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombreCategoria(rs.getString("nombreCategoria"));
               
                cat.setEstado(rs.getBoolean("estado"));
                categorias.add(cat);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }

    public clsCategoria buscarPorId(int id) throws Exception {
        clsCategoria cat = null;
        
        String sql = "SELECT idCategoria, nombreCategoria, estado FROM CATEGORIA WHERE idCategoria = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cat = new clsCategoria();
                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    cat.setNombreCategoria(rs.getString("nombreCategoria"));
                    
                    cat.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar categoría: " + e.getMessage());
        }
        return cat;
    }

    public int registrarCategoria(clsCategoria cat) throws Exception {
        
        String sql = "INSERT INTO CATEGORIA (nombreCategoria, estado) VALUES (?, ?)";
        int idGenerado = -1; 
        
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, cat.getNombreCategoria());
            ps.setBoolean(2, cat.isEstado());
            
            ps.executeUpdate();
            
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID de la categoría creada.");
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
                throw new Exception("Error: El Nombre de la categoría ya existe.");
            } else {
                throw new Exception("Error al registrar categoría: " + e.getMessage());
            }
        }
        
        
        return idGenerado;
    }


    public void modificarCategoria(clsCategoria cat) throws Exception {
    String sql = "UPDATE CATEGORIA SET nombreCategoria = ?, estado = ? WHERE idCategoria = ?";
    
    try (Connection con = objConexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, cat.getNombreCategoria());
        ps.setBoolean(2, cat.isEstado());
        ps.setInt(3, cat.getIdCategoria());
        ps.executeUpdate();
        
    } catch (SQLException | ClassNotFoundException e) {
        
        if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) {
            
            try {
                
                boolean existeEnOtro = false;
                List<clsCategoria> todas = listarCategorias(); // Traemos todas
                for (clsCategoria c : todas) {
                    if (c.getNombreCategoria().equalsIgnoreCase(cat.getNombreCategoria()) && c.getIdCategoria() != cat.getIdCategoria()) {
                        existeEnOtro = true;
                        break;
                    }
                }
                
                if (existeEnOtro) {
                    throw new Exception("Error: El nombre '" + cat.getNombreCategoria() + "' ya está en uso por otra categoría.");
                } else {
                    
                    throw new Exception("Error: El nombre de la categoría ya existe.");
                }
            } catch (Exception checkEx) {
                throw checkEx; 
            }

        } else {
            
            throw new Exception("Error al modificar categoría: " + e.getMessage());
        }
    }
}

    public void darDeBaja(int idCategoria) throws Exception {
        String sql = "UPDATE CATEGORIA SET estado = false WHERE idCategoria = ?";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al dar de baja la categoría: " + e.getMessage());
        }
    }

    public void eliminarCategoria(int id) throws Exception {
        String sql = "DELETE FROM CATEGORIA WHERE idCategoria = ?";
        
        try (Connection con = objConexion.conectar();
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
    
    
    public List<clsCategoria> listarCategoriasActivas() throws Exception {
        List<clsCategoria> categorias = new ArrayList<>();
        // Consulta que solo trae las categorías vigentes
        String sql = "SELECT idCategoria, nombreCategoria, estado FROM CATEGORIA WHERE estado = true ORDER BY nombreCategoria";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsCategoria cat = new clsCategoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombreCategoria(rs.getString("nombreCategoria"));
                cat.setEstado(rs.getBoolean("estado"));
                categorias.add(cat);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar categorías activas: " + e.getMessage());
        }
        return categorias;
    }
}