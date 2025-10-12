package Capa_Datos;

import Capa_Negocio.clsLaboratorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class laboratorioDAO {
    
    private clsJDBC objConexion = new clsJDBC();

    public List<clsLaboratorio> listarLaboratorios() throws Exception {
        List<clsLaboratorio> laboratorios = new ArrayList<>();
        // 1. AÑADIMOS 'estado' A LA CONSULTA
        String sql = "SELECT idLaboratorio, nombreLaboratorio, direccion, telefono, estado FROM LABORATORIO ORDER BY nombreLaboratorio";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsLaboratorio lab = new clsLaboratorio();
                lab.setIdLaboratorio(rs.getInt("idLaboratorio"));
                lab.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                lab.setDireccion(rs.getString("direccion"));
                lab.setTelefono(rs.getString("telefono"));
                // 2. LEEMOS EL ESTADO
                lab.setEstado(rs.getBoolean("estado")); 
                laboratorios.add(lab);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar laboratorios: " + e.getMessage());
        }
        return laboratorios;
    }
    
    public clsLaboratorio buscarPorId(int id) throws Exception {
        clsLaboratorio lab = null;
        // 1. AÑADIMOS 'estado' A LA CONSULTA
        String sql = "SELECT idLaboratorio, nombreLaboratorio, direccion, telefono, estado FROM LABORATORIO WHERE idLaboratorio = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lab = new clsLaboratorio();
                    lab.setIdLaboratorio(rs.getInt("idLaboratorio"));
                    lab.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                    lab.setDireccion(rs.getString("direccion"));
                    lab.setTelefono(rs.getString("telefono"));
                    // 2. LEEMOS EL ESTADO
                    lab.setEstado(rs.getBoolean("estado"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al buscar laboratorio: " + e.getMessage());
        }
        return lab;
    }

    public void registrarLaboratorio(clsLaboratorio lab) throws Exception {
        // 1. AÑADIMOS 'estado' A LA CONSULTA
        String sql = "INSERT INTO LABORATORIO (idLaboratorio, nombreLaboratorio, direccion, telefono, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, lab.getIdLaboratorio());
            ps.setString(2, lab.getNombreLaboratorio());
            ps.setString(3, lab.getDireccion());
            ps.setString(4, lab.getTelefono());
            // 2. GUARDAMOS EL ESTADO
            ps.setBoolean(5, lab.isEstado());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) { 
                throw new Exception("Error: El ID o el Nombre del laboratorio ya existe.");
            } else {
                throw new Exception("Error al registrar laboratorio: " + e.getMessage());
            }
        }
    }

    public void modificarLaboratorio(clsLaboratorio lab) throws Exception {
        // 1. AÑADIMOS 'estado' A LA CONSULTA
        String sql = "UPDATE LABORATORIO SET nombreLaboratorio = ?, direccion = ?, telefono = ?, estado = ? WHERE idLaboratorio = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, lab.getNombreLaboratorio());
            ps.setString(2, lab.getDireccion());
            ps.setString(3, lab.getTelefono());
            // 2. GUARDAMOS EL ESTADO
            ps.setBoolean(4, lab.isEstado());
            ps.setInt(5, lab.getIdLaboratorio());
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23505")) {
                 throw new Exception("Error: El nombre del laboratorio ya existe.");
            } else {
                throw new Exception("Error al modificar laboratorio: " + e.getMessage());
            }
        }
    }

    public void darDeBaja(int idLaboratorio) throws Exception {
        String sql = "UPDATE LABORATORIO SET estado = false WHERE idLaboratorio = ?";
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLaboratorio);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al dar de baja el laboratorio: " + e.getMessage());
        }
    }

    public void eliminarLaboratorio(int id) throws Exception {
        String sql = "DELETE FROM LABORATORIO WHERE idLaboratorio = ?";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException)e).getSQLState().equals("23503")) {
                throw new Exception("Error: No se puede eliminar. El laboratorio está asignado a una marca o producto.");
            } else {
                throw new Exception("Error al eliminar laboratorio: " + e.getMessage());
            }
        }
    }
    
    // ---- (OPCIONAL PERO RECOMENDADO) NUEVO MÉTODO PARA LLENAR LA TABLA ----
    public List<clsLaboratorio> listarLaboratoriosActivos() throws Exception {
        List<clsLaboratorio> laboratorios = new ArrayList<>();
        // Consulta que solo trae los laboratorios vigentes
        String sql = "SELECT idLaboratorio, nombreLaboratorio, direccion, telefono, estado FROM LABORATORIO WHERE estado = true ORDER BY nombreLaboratorio";
        
        try (Connection con = objConexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                clsLaboratorio lab = new clsLaboratorio();
                lab.setIdLaboratorio(rs.getInt("idLaboratorio"));
                lab.setNombreLaboratorio(rs.getString("nombreLaboratorio"));
                lab.setDireccion(rs.getString("direccion"));
                lab.setTelefono(rs.getString("telefono"));
                lab.setEstado(rs.getBoolean("estado")); 
                laboratorios.add(lab);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error al listar laboratorios activos: " + e.getMessage());
        }
        return laboratorios;
    }
}