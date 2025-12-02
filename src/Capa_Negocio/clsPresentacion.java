package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**

 * @author Tiznado Leon
 */
public class clsPresentacion {
   
    private final int id;
    private final String nombreTipoPresentacion;
    private final float cantidad;
    private final String nombreUnidad;
    private final boolean activo; 
    private final clsJDBC objConectar = new clsJDBC();
    
    public clsPresentacion(int id, String nombreTipoPresentacion, float cantidad, String nombreUnidad, boolean activo) {
        this.id = id;
        this.nombreTipoPresentacion = nombreTipoPresentacion;
        this.cantidad = cantidad;
        this.nombreUnidad = nombreUnidad;
        this.activo = activo; 
    }

  
    public int getId() {
        return id;
    }

    public String getNombreTipoPresentacion() {
        return nombreTipoPresentacion;
    }

    public float getCantidad() {
        return cantidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    
    public boolean isActivo() {
        return activo;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s x %.2f %s (%s)", 
            this.nombreTipoPresentacion, 
            this.cantidad, 
            this.nombreUnidad,
            this.activo ? "Activo" : "Inactivo"
        );
    }
    
    public ArrayList<clsPresentacion> listarPresentaciones() throws Exception {
        ArrayList<clsPresentacion> presentaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

       
        String sql = "SELECT p.idPresentacion, p.cantidad, u.nombreUnidad, tp.nombreTipoPresentacion, p.estado " +
                       "FROM PRESENTACION p " +
                       "INNER JOIN UNIDAD u ON p.idUnidad = u.idUnidad " +
                       "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idTipoPresentacion " +
                       "ORDER BY p.idPresentacion";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                clsPresentacion dto = new clsPresentacion(
                    rs.getInt("idPresentacion"),
                    rs.getString("nombreTipoPresentacion"),
                    rs.getFloat("cantidad"), 
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado") 
                );
                presentaciones.add(dto);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar presentaciones: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return presentaciones;
    }

    
    public void registrarPresentacion(int id, float can, int codUni, int codPre, boolean estado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO PRESENTACION (idPresentacion, cantidad, idUnidad, tipoPresentacion, estado) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setFloat(2, can);
            ps.setInt(3, codUni);
            ps.setInt(4, codPre);
            ps.setBoolean(5, estado);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    
    public void modificarPresentacion(int cod, float can, int codUni, int codPre, boolean estado) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE PRESENTACION SET cantidad = ?, idUnidad = ?, tipoPresentacion = ?, estado = ? WHERE idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setFloat(1, can);
            ps.setInt(2, codUni);
            ps.setInt(3, codPre);
            ps.setBoolean(4, estado); 
            ps.setInt(5, cod);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

   
    public clsPresentacion buscarPresentacion(int id) throws Exception {
        clsPresentacion presentacionEncontrada = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT p.idPresentacion, p.cantidad, u.nombreUnidad, tp.nombreTipoPresentacion, p.estado " +
                       "FROM PRESENTACION p " +
                       "INNER JOIN UNIDAD u ON p.idUnidad = u.idUnidad " +
                       "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idTipoPresentacion " +
                       "WHERE p.idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                presentacionEncontrada = new clsPresentacion(
                    rs.getInt("idPresentacion"),
                    rs.getString("nombreTipoPresentacion"),
                    rs.getFloat("cantidad"), 
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado") 
                );
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar la presentación: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return presentacionEncontrada;
    }

   
    public void eliminarPresentacion(int cod) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM PRESENTACION WHERE idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cod);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al eliminar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
    
    public void darBajaPresentacion(int cod) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE PRESENTACION SET estado = false WHERE idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cod);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al dar de baja la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }
    
    public Integer generarCodePresentacion() throws Exception {
        Integer codigo = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COALESCE(MAX(idPresentacion), 0) + 1 AS codigo FROM PRESENTACION";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigo = rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de presentación: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        return codigo;
    }
    
    
    public boolean verificarUsoTipoPresentacion(int idTipoPresentacion) throws Exception {
   
    java.sql.Connection cn = objConectar.conectar(); 
    
    
    String sql = "SELECT 1 FROM Presentacion WHERE tipoPresentacion = ? LIMIT 1";
    
   
    try (java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setInt(1, idTipoPresentacion);
        try (java.sql.ResultSet rs = ps.executeQuery()) {
           
            return rs.next(); 
        }
    } catch (Exception e) {
        throw new Exception("Error al verificar uso de TipoPresentacion: " + e.getMessage());
    } finally {
       
        if (cn != null) {
          
            objConectar.desconectar();
        }
    }
}
    
        public boolean verificarUsoUnidad(int idUnidad) throws Exception {
    java.sql.Connection cn = objConectar.conectar();
    
   
    String sql = "SELECT 1 FROM Presentacion WHERE idUnidad = ? LIMIT 1";
    
    try (java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setInt(1, idUnidad);
        try (java.sql.ResultSet rs = ps.executeQuery()) {
          
            return rs.next(); 
        }
    } catch (Exception e) {
        throw new Exception("Error al verificar uso de Unidad: " + e.getMessage());
    } finally {
        if (cn != null) {
            objConectar.desconectar();
        }
    }
}
}