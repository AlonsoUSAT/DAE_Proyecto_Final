package Capa_Datos;

import Capa_Negocio.clsTipoPresentacion;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TipoPresentacionDAO1 {
    
    private final clsJDBC objConectar = new clsJDBC();

    public ArrayList<clsTipoPresentacion> listarTiposPresentacion() throws Exception {
        ArrayList<clsTipoPresentacion> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Esta consulta ya estaba correcta
        String sql = "SELECT idTipoPresentacion, nombretipopresentacion FROM TIPO_PRESENTACION WHERE estado = true ORDER BY idTipoPresentacion";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                clsTipoPresentacion dao = new clsTipoPresentacion(
                    rs.getInt("idTipoPresentacion"),
                    rs.getString("nombreTipoPresentacion") 
                    
                );
                lista.add(dao);
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
     */
    public Integer obtenerCodigoTipoPresentacion(String nom) throws Exception {
        Integer codigo = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // ✔️ CORRECCIÓN AQUÍ: Se cambió "nombrepresentacion" por "nombreTipoPresentacion"
        String sql = "SELECT idtipopresentacion FROM tipo_presentacion WHERE nombreTipoPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nom);
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