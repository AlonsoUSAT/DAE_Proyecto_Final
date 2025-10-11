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

    // Se recomienda tener un solo objeto de conexión que se reutiliza en los métodos.
    private final clsJDBC objConectar = new clsJDBC();

    /**
     * Devuelve una lista de todas las presentaciones activas en la base de datos.
     * @return ArrayList<PresentacionDTO> Lista de objetos de presentación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public ArrayList<PresentacionDAO> listarPresentaciones() throws Exception {
        ArrayList<PresentacionDAO> presentaciones = new ArrayList<>();
        Connection conn = null; // Variable para la conexión
        PreparedStatement ps = null; // Variable para la consulta segura
        ResultSet rs = null; // Variable para los resultados

        String sql = "SELECT p.idpresentacion, p.cantidad, u.nombreunidad, tp.nombrepresentacion " +
                     "FROM presentacion p " +
                     "INNER JOIN unidad u ON p.idunidad = u.idunidad " +
                     "INNER JOIN tipo_presentacion tp ON p.idtipopresentacion = tp.idtipopresentacion " +
                     "ORDER BY p.idpresentacion";
        
        try {
            conn = objConectar.conectar(); // 1. Abrir la conexión
            ps = conn.prepareStatement(sql); // 2. Preparar la consulta
            rs = ps.executeQuery(); // 3. Ejecutar la consulta

            // 4. Procesar los resultados
            while (rs.next()) {
                PresentacionDAO dto = new PresentacionDAO(
                    rs.getInt("idpresentacion"),
                    rs.getString("nombrepresentacion"),
                    rs.getInt("cantidad"),
                    rs.getString("nombreunidad")
                );
                presentaciones.add(dto);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar presentaciones: " + e.getMessage());
        } finally {
            // 5. Cerrar todos los recursos en orden inverso
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar(); // Usar el método de tu clase clsJDBC
        }
        
        return presentaciones;
    }

    /**
     * Registra una nueva presentación en la base de datos.
     * @param can Cantidad de la presentación.
     * @param codUni Código de la unidad.
     * @param codPre Código del tipo de presentación.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public void registrarPresentacion(int can, int codUni, int codPre) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // El ID se omite si es autoincremental en la BD. Si no lo es, debes incluirlo.
        String sql = "INSERT INTO presentacion (cantidad, idunidad, idtipopresentacion) VALUES (?, ?, ?)";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            // Asignar los valores a los parámetros (?) de forma segura
            ps.setInt(1, can);
            ps.setInt(2, codUni);
            ps.setInt(3, codPre);
            
            ps.executeUpdate(); // Ejecutar la inserción
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
    public void modificarPresentacion(int cod, int can, int codUni, int codPre) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "UPDATE presentacion SET cantidad = ?, idunidad = ?, idtipopresentacion = ? WHERE idpresentacion = ?";
        
        try {
            conn = objConectar.conectar();
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, can);
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
     * Elimina una presentación de la base de datos.
     * @param cod El ID de la presentación a eliminar.
     * @throws Exception Si ocurre un error en la base de datos.
     */
    public void eliminarPresentacion(int cod) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "DELETE FROM presentacion WHERE idpresentacion = ?";
        
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
     * Lógica para dar de baja (desactivar) una presentación.
     * NOTA: Esto asume que tienes una columna 'estado' de tipo booleano en tu tabla 'presentacion'.
     * @param cod El ID de la presentación a dar de baja.
     * @throws Exception Si ocurre un error.
     */
    public void darBajaPresentacion(int cod) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        // Corregido: La consulta debe afectar a la tabla 'presentacion' y no 'producto'.
        String sql = "UPDATE presentacion SET estado = false WHERE idpresentacion = ?";
        
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

        String sql = "SELECT COALESCE(MAX(idpresentacion), 0) + 1 AS codigo FROM presentacion";

        try {
            conn = objConectar.conectar(); // 1. Abrir la conexión
            ps = conn.prepareStatement(sql); // 2. Preparar la consulta
            rs = ps.executeQuery(); // 3. Ejecutar

            // 4. Leer el resultado
            if (rs.next()) {
                codigo = rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de presentación: " + e.getMessage());
        } finally {
            // 5. Cerrar todos los recursos
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }

        return codigo;
    }
      
        public PresentacionDAO buscarPresentacion(int id) throws Exception {
        PresentacionDAO presentacionEncontrada = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT p.idpresentacion, p.cantidad, u.nombreunidad, tp.nombrepresentacion " +
                     "FROM presentacion p " +
                     "INNER JOIN unidad u ON p.idunidad = u.idunidad " +
                     "INNER JOIN tipo_presentacion tp ON p.idtipopresentacion = tp.idtipopresentacion " +
                     "WHERE p.idpresentacion = ?";
        
        try {
            conn = objConectar.conectar(); // 1. Abrir la conexión
            ps = conn.prepareStatement(sql); // 2. Preparar la consulta
            ps.setInt(1, id); // 3. Asignar el ID de forma segura
            
            rs = ps.executeQuery(); // 4. Ejecutar

            // 5. Si se encontró un resultado, crear el objeto
            if (rs.next()) {
                presentacionEncontrada = new PresentacionDAO(
                    rs.getInt("idpresentacion"),
                    rs.getString("nombrepresentacion"),
                    rs.getInt("cantidad"),
                    rs.getString("nombreunidad")
                );
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar la presentación: " + e.getMessage());
        } finally {
            // 6. Cerrar todos los recursos
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) objConectar.desconectar();
        }
        
        return presentacionEncontrada; // Devuelve el objeto encontrado o null
    }
}