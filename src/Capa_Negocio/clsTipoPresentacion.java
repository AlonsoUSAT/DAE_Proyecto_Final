/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import Capa_Datos.TipoPresentacionDAO;
import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class clsTipoPresentacion {
     private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todos los tipos de presentación.
     * @return ArrayList<TipoPresentacionDTO>
     * @throws Exception
     */
    public ArrayList<TipoPresentacionDAO> listarTiposPresentacion() throws Exception {
        ArrayList<TipoPresentacionDAO> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT idtipopresentacion, nombrepresentacion FROM tipo_presentacion ORDER BY nombrepresentacion";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                TipoPresentacionDAO dto = new TipoPresentacionDAO(
                    rs.getInt("idtipopresentacion"),
                    rs.getString("nombrepresentacion")
                );
                lista.add(dto);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar los tipos de presentación: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return lista;
    }

    /**
     * Obtiene el ID de un tipo de presentación a partir de su nombre.
     * @param nom El nombre del tipo de presentación a buscar.
     * @return El ID correspondiente, o 0 si no se encuentra.
     * @throws Exception
     */
    public Integer obtenerCodigoTipoPresentacion(String nom) throws Exception {
        Integer codigo = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT idtipopresentacion FROM tipo_presentacion WHERE nombrepresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nom); // Parámetro seguro
            rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt("idtipopresentacion");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener código de tipo de presentación: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return codigo;
    }
}
