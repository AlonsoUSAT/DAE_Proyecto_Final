package Capa_Datos;

import Capa_Negocio.clsMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// autor: Fernando Hernández
public class MarcaDAO {

    private clsJDBC objConexion = new clsJDBC();

    
    public List<clsMarca> listarMarcas() throws Exception {
        List<clsMarca> marcas = new ArrayList<>();
        
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                     "FROM MARCA m " +
                     "JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                     "ORDER BY m.nombre";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setNombre(rs.getString("nombre"));
                marca.setDescripcion(rs.getString("descripcion"));
                marca.setIdLaboratorio(rs.getInt("idLaboratorio"));
                marca.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                
                marca.setEstado(rs.getBoolean("estado")); 
                marcas.add(marca);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar marcas: " + e.getMessage());
        }
        return marcas;
    }

    
    public clsMarca buscarPorId(int id) throws Exception {
        clsMarca marca = null;
        
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                     "FROM MARCA m " +
                     "JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                     "WHERE m.idMarca = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    marca = new clsMarca();
                    marca.setIdMarca(rs.getInt("idMarca"));
                    marca.setNombre(rs.getString("nombre"));
                    marca.setDescripcion(rs.getString("descripcion"));
                    marca.setIdLaboratorio(rs.getInt("idLaboratorio"));
                    marca.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                    
                    marca.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar marca: " + e.getMessage());
        }
        return marca;
    }
    
    
    public void registrarMarca(clsMarca marca) throws Exception {
    
    String sql = "INSERT INTO MARCA (nombre, descripcion, idLaboratorio, estado) VALUES (?, ?, ?, ?)";
    
    try (Connection con = objConexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        
        ps.setString(1, marca.getNombre());
        ps.setString(2, marca.getDescripcion());
        ps.setInt(3, marca.getIdLaboratorio());
        ps.setBoolean(4, marca.isEstado());
        ps.executeUpdate();
        
    } catch (SQLException | ClassNotFoundException e) {
        if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
           
            throw new Exception("Error: El nombre de la marca ya existe.");
        } else {
            throw new Exception("Error al registrar marca: " + e.getMessage());
        }
    }
}

   
    public void modificarMarca(clsMarca marca) throws Exception {
        
        String sql = "UPDATE MARCA SET nombre = ?, descripcion = ?, idLaboratorio = ?, estado = ? WHERE idMarca = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setInt(3, marca.getIdLaboratorio());
            
            ps.setBoolean(4, marca.isEstado());
            ps.setInt(5, marca.getIdMarca());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al modificar marca: " + e.getMessage());
        }
    }
    
    
    public void darDeBaja(int idMarca) throws Exception {
        String sql = "UPDATE MARCA SET estado = false WHERE idMarca = ?";

        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMarca);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al dar de baja la marca: " + e.getMessage());
        }
    }

   
    public void eliminarMarca(int idMarca) throws Exception {
    String sql = "DELETE FROM MARCA WHERE idMarca = ?";
    
    try (Connection con = objConexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, idMarca);
        
        
        int filasAfectadas = ps.executeUpdate();
        if (filasAfectadas == 0) {
            throw new Exception("No se encontró ninguna marca con el ID " + idMarca + " para eliminar.");
        }
        
    } catch (SQLException | ClassNotFoundException e) {
       
        if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23503")) {
            throw new Exception("Error: No se puede eliminar la marca porque está asignada a uno o más productos.");
        } else {
            throw new Exception("Error al eliminar la marca: " + e.getMessage());
        }
    }
}
    
    
    public List<clsMarca> listarMarcasActivas() throws Exception {
        List<clsMarca> marcas = new ArrayList<>();
       
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                     "FROM MARCA m " +
                     "JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                     "WHERE m.estado = true " +
                     "ORDER BY m.nombre";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsMarca marca = new clsMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setNombre(rs.getString("nombre"));
                marca.setDescripcion(rs.getString("descripcion"));
                marca.setIdLaboratorio(rs.getInt("idLaboratorio"));
                marca.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                marca.setEstado(rs.getBoolean("estado")); 
                marcas.add(marca);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar marcas activas: " + e.getMessage());
        }
        return marcas;
    }
    
}