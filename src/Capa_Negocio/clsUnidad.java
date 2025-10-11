/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.UnidadDAO;
import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 *
 * @author USER
 */
public class clsUnidad {
      private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todas las unidades.
     * @return ArrayList<UnidadDAO>
     * @throws Exception
     */
    public ArrayList<UnidadDAO> listarUnidades() throws Exception {
        // 2. La lista debe ser de tipo UnidadDAO
        ArrayList<UnidadDAO> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT idunidad, nombreunidad FROM unidad ORDER BY nombreunidad";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                // 3. Crear un nuevo objeto UnidadDAO
                UnidadDAO dao = new UnidadDAO(
                    rs.getInt("idunidad"),
                    rs.getString("nombreunidad")
                );
                lista.add(dao);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar las unidades: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return lista;
    }
    /**
     * Obtiene el ID de una unidad a partir de su nombre.
     * @param nom El nombre de la unidad a buscar.
     * @return El ID correspondiente, o 0 si no se encuentra.
     * @throws Exception
     */
    public Integer obtenerCodigoUnidad(String nom) throws Exception {
        Integer codigo = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT idunidad FROM unidad WHERE nombreunidad = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nom); // Parámetro seguro
            rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt("idunidad");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener código de unidad: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return codigo;
    }
     
}
