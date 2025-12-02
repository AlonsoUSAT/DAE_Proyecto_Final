/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiznado Leon
 */
public class clsTipoPresentacion {
      private int id;
    private String nombre;
    private boolean estado; 

   
    public clsTipoPresentacion(int id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
    
   
    public clsTipoPresentacion() {}

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isEstado() { return estado; } 
    public void setEstado(boolean estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return this.nombre;
    }
    
     private final clsJDBC objConectar = new clsJDBC();

    public void registrar(clsTipoPresentacion tipo) throws Exception {
        String sql = "INSERT INTO TIPO_PRESENTACION (idTipoPresentacion, nombreTipoPresentacion, estado) VALUES (?, ?, ?)";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tipo.getId());
            ps.setString(2, tipo.getNombre());
            ps.setBoolean(3, tipo.isEstado());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar: " + e.getMessage());
        }
    }

    public void modificar(clsTipoPresentacion tipo) throws Exception {
        String sql = "UPDATE TIPO_PRESENTACION SET nombreTipoPresentacion = ?, estado = ? WHERE idTipoPresentacion = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setBoolean(2, tipo.isEstado());
            ps.setInt(3, tipo.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar: " + e.getMessage());
        }
    }

    public void darDeBaja(int id) throws Exception {
        String sql = "UPDATE TIPO_PRESENTACION SET estado = false WHERE idTipoPresentacion = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al dar de baja: " + e.getMessage());
        }
    }

    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM TIPO_PRESENTACION WHERE idTipoPresentacion = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar: " + e.getMessage());
        }
    }

    public clsTipoPresentacion buscarPorId(int id) throws Exception {
        clsTipoPresentacion tipo = null;
        String sql = "SELECT * FROM TIPO_PRESENTACION WHERE idTipoPresentacion = ?";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tipo = new clsTipoPresentacion(
                        rs.getInt("idTipoPresentacion"),
                        rs.getString("nombreTipoPresentacion"),
                        rs.getBoolean("estado")
                    );
                }
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar: " + e.getMessage());
        }
        return tipo;
    }
    
    public List<clsTipoPresentacion> listarTodos() throws Exception {
        List<clsTipoPresentacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM TIPO_PRESENTACION ORDER BY idTipoPresentacion";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clsTipoPresentacion tipo = new clsTipoPresentacion(
                    rs.getInt("idTipoPresentacion"),
                    rs.getString("nombreTipoPresentacion"),
                    rs.getBoolean("estado")
                );
                lista.add(tipo);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public int generarNuevoId() throws Exception {
        int nuevoId = 1;
        String sql = "SELECT COALESCE(MAX(idTipoPresentacion), 0) + 1 AS nuevoId FROM TIPO_PRESENTACION";
        try (Connection conn = objConectar.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nuevoId = rs.getInt("nuevoId");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar ID: " + e.getMessage());
        }
        return nuevoId;
    }
}
