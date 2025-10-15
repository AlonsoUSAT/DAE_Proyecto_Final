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

    // ---- MÉTODO LISTAR MEJORADO ----
    public List<clsMarca> listarMarcas() throws Exception {
        List<clsMarca> marcas = new ArrayList<>();
        // 1. AÑADIMOS m.estado A LA CONSULTA
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
                // 2. LEEMOS EL ESTADO DE LA BASE DE DATOS
                marca.setEstado(rs.getBoolean("estado")); 
                marcas.add(marca);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar marcas: " + e.getMessage());
        }
        return marcas;
    }

    // ---- MÉTODO BUSCAR MEJORADO ----
    public clsMarca buscarPorId(int id) throws Exception {
        clsMarca marca = null;
        // 1. AÑADIMOS m.estado A LA CONSULTA
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
                    // 2. LEEMOS EL ESTADO DE LA BASE DE DATOS
                    marca.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar marca: " + e.getMessage());
        }
        return marca;
    }
    
    // ---- MÉTODO REGISTRAR MEJORADO ----
    public void registrarMarca(clsMarca marca) throws Exception {
    // 1. Se quita 'idMarca' de la consulta INSERT
    String sql = "INSERT INTO MARCA (nombre, descripcion, idLaboratorio, estado) VALUES (?, ?, ?, ?)";
    
    try (Connection con = objConexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        // 2. Ya no se envía el ID. Los parámetros se reordenan.
        ps.setString(1, marca.getNombre());
        ps.setString(2, marca.getDescripcion());
        ps.setInt(3, marca.getIdLaboratorio());
        ps.setBoolean(4, marca.isEstado());
        ps.executeUpdate();
        
    } catch (SQLException | ClassNotFoundException e) {
        if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
            // El error de duplicado ahora se refiere al nombre o a otra restricción UNIQUE
            throw new Exception("Error: El nombre de la marca ya existe.");
        } else {
            throw new Exception("Error al registrar marca: " + e.getMessage());
        }
    }
}

    // ---- MÉTODO MODIFICAR MEJORADO ----
    public void modificarMarca(clsMarca marca) throws Exception {
        // 1. AÑADIMOS estado A LA CONSULTA
        String sql = "UPDATE MARCA SET nombre = ?, descripcion = ?, idLaboratorio = ?, estado = ? WHERE idMarca = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setInt(3, marca.getIdLaboratorio());
            // 2. GUARDAMOS EL ESTADO QUE VENGA DEL CHECKBOX
            ps.setBoolean(4, marca.isEstado());
            ps.setInt(5, marca.getIdMarca());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al modificar marca: " + e.getMessage());
        }
    }
    
    // ---- MÉTODO DAR DE BAJA (YA LO TENÍAS, ESTÁ CORRECTO) ----
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

    // ---- MÉTODO ELIMINAR (SIN CAMBIOS, YA ERA CORRECTO) ----
    public void eliminarMarca(int id) throws Exception {
        // ... (tu código de eliminar no necesita cambios)
    }
    
    // ---- (OPCIONAL PERO RECOMENDADO) NUEVO MÉTODO PARA LLENAR LA TABLA ----
    public List<clsMarca> listarMarcasActivas() throws Exception {
        List<clsMarca> marcas = new ArrayList<>();
        // Consulta que solo trae las marcas vigentes
        String sql = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                     "FROM MARCA m " +
                     "JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                     "WHERE m.estado = true " +
                     "ORDER BY m.nombre";
        // El resto del método es igual a listarMarcas()
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