package Capa_Negocio;

import Capa_Datos.PresentacionDAO;
import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Clase de negocio para gestionar las operaciones de la entidad Presentacion.
 * @author USER
 */
public class clsPresentacion {

    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todas las presentaciones ACTIVAS en la base de datos.
     * @return ArrayList<PresentacionDAO> Lista de objetos de presentación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public ArrayList<PresentacionDAO> listarPresentaciones() throws Exception {
        ArrayList<PresentacionDAO> presentaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT p.idPresentacion, p.cantidad, u.nombreUnidad, tp.nombreTipoPresentacion " +
                     "FROM PRESENTACION p " +
                     "INNER JOIN UNIDAD u ON p.idUnidad = u.idUnidad " +
                     "INNER JOIN TIPO_PRESENTACION tp ON p.tipoPresentacion = tp.idTipoPresentacion " +
                     "WHERE p.estado = true " + // Filtra solo los activos
                     "ORDER BY p.idPresentacion";

        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PresentacionDAO dto = new PresentacionDAO(
                    rs.getInt("idPresentacion"),
                    rs.getString("nombreTipoPresentacion"),
                    rs.getInt("cantidad"), // Corregido a getFloat
                    rs.getString("nombreUnidad")
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

    /**
     * Registra una nueva presentación en la base de datos, asignándole un estado activo.
     * @param can Cantidad de la presentación (puede tener decimales).
     * @param codUni Código de la unidad.
     * @param codPre Código del tipo de presentación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public void registrarPresentacion(float can, int codUni, int codPre) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // Corregido: Nombres de columnas y se añade el campo 'estado' por defecto en 'true'
        String sql = "INSERT INTO PRESENTACION (cantidad, idUnidad, tipoPresentacion, estado) VALUES (?, ?, ?, true)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setFloat(1, can); // Corregido a setFloat
            ps.setInt(2, codUni);
            ps.setInt(3, codPre);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Modifica los datos de una presentación existente.
     * @param cod El ID de la presentación a modificar.
     * @param can La nueva cantidad.
     * @param codUni El nuevo código de unidad.
     * @param codPre El nuevo código de tipo de presentación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public void modificarPresentacion(int cod, float can, int codUni, int codPre) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // Corregido: Nombres de columnas actualizados
        String sql = "UPDATE PRESENTACION SET cantidad = ?, idUnidad = ?, tipoPresentacion = ? WHERE idPresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setFloat(1, can); // Corregido a setFloat
            ps.setInt(2, codUni);
            ps.setInt(3, codPre);
            ps.setInt(4, cod);
            
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Elimina permanentemente una presentación de la base de datos.
     * @param cod El ID de la presentación a eliminar.
     * @throws Exception Si ocurre un error en la base de datos.
     */
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
    
    /**
     * Realiza una eliminación lógica (soft delete) cambiando el estado a 'false'.
     * @param cod El ID de la presentación a dar de baja.
     * @throws Exception Si ocurre un error.
     */
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
    
    /**
     * Genera el siguiente código disponible para una nueva presentación.
     * @return El siguiente ID a utilizar.
     * @throws Exception Si ocurre un error.
     */
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
      
    /**
     * Busca una presentación específica por su ID.
     * @param id El ID de la presentación a buscar.
     * @return Un objeto PresentacionDAO si se encuentra, de lo contrario null.
     * @throws Exception Si ocurre un error.
     */
    public PresentacionDAO buscarPresentacion(int id) throws Exception {
        PresentacionDAO presentacionEncontrada = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT p.idPresentacion, p.cantidad, u.nombreUnidad, tp.nombreTipoPresentacion " +
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
                presentacionEncontrada = new PresentacionDAO(
                    rs.getInt("idPresentacion"),
                    rs.getString("nombretipopresentacion"), // Corregido
                    rs.getInt("cantidad"),             // Corregido
                    rs.getString("nombreUnidad")
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
}