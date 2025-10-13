package Capa_Datos;

import Capa_Negocio.clsPresentacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Clase de Acceso a Datos (DAO) para la entidad Presentacion.
 * Gestiona todas las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos.
 * @author USER
 */
public class PresentacionDAO {

    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todas las presentaciones de la base de datos.
     * @return Un ArrayList de objetos clsPresentacion.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public ArrayList<clsPresentacion> listarPresentaciones() throws Exception {
        ArrayList<clsPresentacion> presentaciones = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Se incluye el campo 'p.estado' en la consulta
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
                    rs.getFloat("cantidad"), // Correcto: leer como float
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado") // Se lee el estado
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
     * Registra una nueva presentación en la base de datos.
     * @param id El ID de la nueva presentación.
     * @param can La cantidad.
     * @param codUni El código de la unidad.
     * @param codPre El código del tipo de presentación.
     * @param estado El estado (activo/inactivo).
     * @throws Exception Si ocurre un error.
     */
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
            ps.setBoolean(5, estado); // Se añade el estado
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Modifica una presentación existente.
     * @param cod El ID de la presentación a modificar.
     * @param can La nueva cantidad.
     * @param codUni El nuevo código de unidad.
     * @param codPre El nuevo código de tipo de presentación.
     * @param estado El nuevo estado.
     * @throws Exception Si ocurre un error.
     */
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
            ps.setBoolean(4, estado); // Se actualiza el estado
            ps.setInt(5, cod);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al modificar la presentación: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
    }

    /**
     * Busca una presentación por su ID.
     * @param id El ID a buscar.
     * @return Un objeto clsPresentacion con los datos, o null si no se encuentra.
     * @throws Exception Si ocurre un error.
     */
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
                    rs.getFloat("cantidad"), // Correcto: leer como float
                    rs.getString("nombreUnidad"),
                    rs.getBoolean("estado") // Se lee el estado
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

    // ... (Aquí irían tus otros métodos: eliminarPresentacion, darBajaPresentacion, generarCodePresentacion, que no necesitan cambios)
    
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
    
    
    
    
}