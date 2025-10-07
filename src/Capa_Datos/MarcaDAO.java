package Capa_Datos;

import Capa_Negocio.clsMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    private clsJDBC objConexion = new clsJDBC();

    // Nota el JOIN para traer el nombre del laboratorio
    public List<clsMarca> listarMarcas() throws Exception {
        List<clsMarca> marcas = new ArrayList<>();
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.idLaboratorio, l.nombreLaboratorio " +
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
                marcas.add(marca);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar marcas: " + e.getMessage());
        }
        return marcas;
    }

    // El buscar también necesita el JOIN
    public clsMarca buscarPorId(int id) throws Exception {
        clsMarca marca = null;
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.idLaboratorio, l.nombreLaboratorio " +
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
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar marca: " + e.getMessage());
        }
        return marca;
    }
    
    // Registrar, modificar y eliminar no necesitan JOIN
    public void registrarMarca(clsMarca marca) throws Exception {
        String sql = "INSERT INTO MARCA (idMarca, nombre, descripcion, idLaboratorio) VALUES (?, ?, ?, ?)";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, marca.getIdMarca());
            ps.setString(2, marca.getNombre());
            ps.setString(3, marca.getDescripcion());
            ps.setInt(4, marca.getIdLaboratorio());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
                throw new Exception("Error: El ID de la marca ya existe.");
            } else {
                throw new Exception("Error al registrar marca: " + e.getMessage());
            }
        }
    }

    public void modificarMarca(clsMarca marca) throws Exception {
        String sql = "UPDATE MARCA SET nombre = ?, descripcion = ?, idLaboratorio = ? WHERE idMarca = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setInt(3, marca.getIdLaboratorio());
            ps.setInt(4, marca.getIdMarca());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al modificar marca: " + e.getMessage());
        }
    }

    public void eliminarMarca(int id) throws Exception {
        String sql = "DELETE FROM MARCA WHERE idMarca = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23503")) {
                throw new Exception("Error: No se puede eliminar. La marca está asignada a un producto.");
            } else {
                throw new Exception("Error al eliminar marca: " + e.getMessage());
            }
        }
    }
}