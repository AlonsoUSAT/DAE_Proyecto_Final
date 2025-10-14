package Capa_Datos;

import Capa_Negocio.clsUnidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UnidadDAO {
    
    private final clsJDBC objConectar = new clsJDBC();

    public void registrar(clsUnidad unidad) throws Exception {
        String sql = "INSERT INTO UNIDAD (idUnidad, nombreUnidad, estado) VALUES (?, ?, ?)";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, unidad.getId());
            ps.setString(2, unidad.getNombre());
            ps.setBoolean(3, unidad.isEstado());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar unidad: " + e.getMessage());
        }
    }

    public void modificar(clsUnidad unidad) throws Exception {
        String sql = "UPDATE UNIDAD SET nombreUnidad = ?, estado = ? WHERE idUnidad = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unidad.getNombre());
            ps.setBoolean(2, unidad.isEstado());
            ps.setInt(3, unidad.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar unidad: " + e.getMessage());
        }
    }

    public void darDeBaja(int id) throws Exception {
        String sql = "UPDATE UNIDAD SET estado = false WHERE idUnidad = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al dar de baja unidad: " + e.getMessage());
        }
    }

    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM UNIDAD WHERE idUnidad = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar unidad: " + e.getMessage());
        }
    }

    public clsUnidad buscarPorId(int id) throws Exception {
        clsUnidad unidad = null;
        String sql = "SELECT * FROM UNIDAD WHERE idUnidad = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    unidad = new clsUnidad(
                        rs.getInt("idUnidad"),
                        rs.getString("nombreUnidad"),
                        rs.getBoolean("estado")
                    );
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar unidad: " + e.getMessage());
        }
        return unidad;
    }
    
    public List<clsUnidad> listarTodas() throws Exception {
        List<clsUnidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM UNIDAD ORDER BY idUnidad";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsUnidad unidad = new clsUnidad(
                    rs.getInt("idUnidad"),
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado")
                );
                lista.add(unidad);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar unidades: " + e.getMessage());
        }
        return lista;
    }

    public int generarNuevoId() throws Exception {
        int nuevoId = 1;
        String sql = "SELECT COALESCE(MAX(idUnidad), 0) + 1 AS nuevoId FROM UNIDAD";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nuevoId = rs.getInt("nuevoId");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar ID de unidad: " + e.getMessage());
        }
        return nuevoId;
    }
    
       public List<clsUnidad> listarActivas() throws Exception {
        List<clsUnidad> lista = new ArrayList<>();
        // Esta consulta filtra para traer solo las unidades con estado = true
        String sql = "SELECT * FROM UNIDAD WHERE estado = true ORDER BY nombreUnidad";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsUnidad unidad = new clsUnidad(
                    rs.getInt("idUnidad"),
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado")
                );
                lista.add(unidad);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar unidades activas: " + e.getMessage());
        }
        return lista;
    }
       
   
}