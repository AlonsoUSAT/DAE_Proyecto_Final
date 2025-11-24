package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.*;

public class clsProducto {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public ResultSet listarParaVenta(String texto) throws Exception {
        strSQL = "SELECT p.idProducto, pp.idPresentacion, p.nombre, p.descripcion, "
                + "m.nombre AS nombreMarca, c.nombreCategoria, "
                + "CONCAT(tp.nombreTipoPresentacion, ' x ', pres.cantidad, ' ', u.nombreUnidad) AS presentacion, "
                + "pp.precio, "
                + "pp.stock AS stock " // <--- CORREGIDO: Leemos el stock actualizado de aquí
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN PRESENTACION_PRODUCTO pp ON p.idProducto = pp.idProducto "
                + "INNER JOIN PRESENTACION pres ON pp.idPresentacion = pres.idPresentacion "
                + "INNER JOIN TIPO_PRESENTACION tp ON pres.tipoPresentacion = tp.idTipoPresentacion "
                + "INNER JOIN UNIDAD u ON pres.idUnidad = u.idUnidad "
                + "WHERE UPPER(p.nombre) LIKE UPPER('%" + texto + "%') "
                + "AND p.estado = true AND pp.estado = true "
                + "ORDER BY p.nombre";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar para venta: " + e.getMessage());
        }
    }

    public ResultSet filtrarAvanzado(String nombre, String marca, String laboratorio, String categoria, double minPrecio, double maxPrecio) throws Exception {

        strSQL = "SELECT p.idProducto, pp.idPresentacion, p.nombre, p.descripcion, m.nombre AS nombreMarca, "
                + "l.nombreLaboratorio, c.nombreCategoria, "
                + "CONCAT(tp.nombreTipoPresentacion, ' x ', pres.cantidad, ' ', u.nombreUnidad) AS presentacion, "
                + "pp.precio, "
                + "pp.stock AS stock "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio "
                + "INNER JOIN PRESENTACION_PRODUCTO pp ON p.idProducto = pp.idProducto "
                + "INNER JOIN PRESENTACION pres ON pp.idPresentacion = pres.idPresentacion "
                + "INNER JOIN TIPO_PRESENTACION tp ON pres.tipoPresentacion = tp.idTipoPresentacion "
                + "INNER JOIN UNIDAD u ON pres.idUnidad = u.idUnidad "
                + "WHERE p.estado = true AND pp.estado = true ";

        if (!nombre.isEmpty()) {
            strSQL += " AND UPPER(p.nombre) LIKE UPPER('%" + nombre + "%')";
        }
        if (!marca.isEmpty()) {
            strSQL += " AND UPPER(m.nombre) = UPPER('" + marca + "')";
        }
        if (!laboratorio.isEmpty()) {
            strSQL += " AND UPPER(l.nombreLaboratorio) = UPPER('" + laboratorio + "')";
        }
        if (!categoria.isEmpty()) {
            strSQL += " AND UPPER(c.nombreCategoria) = UPPER('" + categoria + "')";
        }

        if (maxPrecio > 0) {
            strSQL += " AND pp.precio BETWEEN " + minPrecio + " AND " + maxPrecio;
        }

        strSQL += " ORDER BY p.nombre";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error en búsqueda avanzada: " + e.getMessage());
        }
    }

    public int getStockPresentacion(int idProducto, int idPresentacion) throws Exception {
        strSQL = "SELECT stock FROM PRESENTACION_PRODUCTO WHERE idProducto = " + idProducto + " AND idPresentacion = " + idPresentacion;
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener stock de presentación.");
        }
        return 0;
    }

    public ResultSet listarProductos() throws Exception {
        strSQL = "SELECT p.idProducto, p.nombre, p.descripcion, p.estado, "
                + "m.idMarca, m.nombre AS nombreMarca, "
                + "c.idCategoria, c.nombreCategoria, "
                + "l.idLaboratorio, l.nombreLaboratorio "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio "
                + "ORDER BY p.nombre";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar Productos: " + e.getMessage());
        }
    }

    public Integer generarCodigoProducto() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idProducto), 0) + 1 AS codigo FROM PRODUCTO";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar código de producto: " + e.getMessage());
        }
        return 1;
    }

    public void registrarProducto(Integer idProducto, String nombre, String descripcion,
            Boolean estado, int idMarca, int idCategoria, int idDistribuidor) throws Exception {

        String nombreSeguro = nombre.replace("'", "''");
        String descSegura = (descripcion != null) ? descripcion.replace("'", "''") : "";

        strSQL = "INSERT INTO PRODUCTO(idProducto, nombre, descripcion, estado, idMarca, idCategoria, idDistribuidor) "
                + "VALUES(" + idProducto + ", '" + nombreSeguro + "', '" + descSegura + "', "
                + estado + ", " + idMarca + ", " + idCategoria + ", " + idDistribuidor + ")";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23505")) { // Código de error para duplicados
                throw new Exception("Error: El ID o el nombre del producto ya existe.");
            }
            throw new Exception("Error al registrar el producto: " + e.getMessage());
        }
    }

    public void modificarProducto(Integer idProducto, String nombre, String descripcion,
            Boolean estado, Integer idMarca, Integer idCategoria, Integer idDistribuidor) throws Exception {

        String nombreSeguro = nombre.replace("'", "''");
        String descSegura = (descripcion != null) ? descripcion.replace("'", "''") : "";

        strSQL = "UPDATE PRODUCTO SET "
                + "nombre = '" + nombreSeguro + "', "
                + "descripcion = '" + descSegura + "', "
                + "estado = " + estado + ", "
                + "idMarca = " + idMarca + ", "
                + "idCategoria = " + idCategoria + ", "
                + "idDistribuidor = " + idDistribuidor + " "
                + "WHERE idProducto = " + idProducto;

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar el producto: " + e.getMessage());
        }
    }

    public void eliminarProducto(Integer idProducto) throws Exception {
        strSQL = "DELETE FROM PRODUCTO WHERE idProducto = " + idProducto;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23503")) { // Violación de llave foránea
                throw new Exception("Error: No se puede eliminar. El producto tiene ventas o presentaciones asociadas.");
            }
            throw new Exception("Error al eliminar el producto: " + e.getMessage());
        }
    }

    public void darBajaProducto(Integer idProducto) throws Exception {
        strSQL = "UPDATE PRODUCTO SET estado = false WHERE idProducto = " + idProducto;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el producto: " + e.getMessage());
        }
    }

    public ResultSet buscarProducto(Integer idProducto) throws Exception {
        strSQL = "SELECT p.idProducto, p.nombre, p.descripcion, p.estado, "
                + "m.idMarca, m.nombre AS nombreMarca, "
                + "c.idCategoria, c.nombreCategoria, "
                + "l.idLaboratorio, l.nombreLaboratorio "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio "
                + "WHERE p.idProducto = " + idProducto;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar producto: " + e.getMessage());
        }
    }

    public double getPrecioMax() throws Exception {
        strSQL = "SELECT COALESCE(MAX(precio), 0) FROM PRESENTACION_PRODUCTO";

        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener precio máximo: " + e.getMessage());
        }
        return 0.0;
    }
}
