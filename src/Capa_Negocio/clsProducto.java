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
                // Concatenamos para que se vea bonito en la tabla: "Caja x 20 Tabletas"
                + "CONCAT(tp.nombreTipoPresentacion, ' x ', pres.cantidad, ' ', u.nombreUnidad) AS presentacion, "
                + "pp.precio, "
                // Subconsulta para calcular el Stock sumando los lotes de ESA presentación específica
                + "(SELECT COALESCE(SUM(stockActual), 0) FROM LOTE l WHERE l.idProducto = p.idProducto AND l.idPresentacion = pp.idPresentacion) AS stock "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                // JOINs para llegar al precio y la presentación
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

        strSQL = "SELECT p.idProducto, p.nombre, p.descripcion, m.nombre AS nombreMarca, "
                + "l.nombreLaboratorio, c.nombreCategoria, " // <-- Nombre de Laboratorio y Categoría
                + "CONCAT(tp.nombreTipoPresentacion, ' x ', pres.cantidad, ' ', u.nombreUnidad) AS presentacion, "
                + "pp.precio, "
                // Cálculo del STOCK
                + "(SELECT COALESCE(SUM(lt.stockActual), 0) FROM LOTE lt WHERE lt.idProducto = p.idProducto AND lt.idPresentacion = pp.idPresentacion) AS stock "
                + "FROM PRODUCTO p "
                + "INNER JOIN MARCA m ON p.idMarca = m.idMarca "
                + "INNER JOIN CATEGORIA c ON p.idCategoria = c.idCategoria "
                + "INNER JOIN LABORATORIO l ON p.idDistribuidor = l.idLaboratorio " // <-- JOIN a LABORATORIO
                + "INNER JOIN PRESENTACION_PRODUCTO pp ON p.idProducto = pp.idProducto "
                + "INNER JOIN PRESENTACION pres ON pp.idPresentacion = pres.idPresentacion "
                + "INNER JOIN TIPO_PRESENTACION tp ON pres.tipoPresentacion = tp.idTipoPresentacion "
                + "INNER JOIN UNIDAD u ON pres.idUnidad = u.idUnidad "
                + "WHERE p.estado = true AND pp.estado = true ";

        // --- FILTROS DINÁMICOS ---
        if (!nombre.isEmpty()) {
            strSQL += " AND UPPER(p.nombre) LIKE UPPER('%" + nombre + "%')";
        }
        if (!marca.isEmpty()) {
            strSQL += " AND UPPER(m.nombre) = UPPER('" + marca + "')";
        }
        if (!laboratorio.isEmpty()) { // <-- FILTRO DE LABORATORIO
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
            throw new Exception("Error en búsqueda avanzada con stock y laboratorio: " + e.getMessage());
        }
    }

    // En Capa_Negocio/clsProducto.java
    public int getStockPresentacion(int idProducto, int idPresentacion) throws Exception {
        strSQL = "SELECT COALESCE(SUM(stockActual), 0) as total FROM LOTE WHERE idProducto = " + idProducto + " AND idPresentacion = " + idPresentacion;
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("total");
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

        // --- CORRECCIÓN CLAVE: SI EL ID ES 0, GENERAMOS UNO NUEVO ---
        if (idProducto == 0) {
            idProducto = generarCodigoProducto(); // Calculamos el ID real aquí mismo
        }

        String nombreSeguro = nombre.replace("'", "''");
        String descSegura = (descripcion != null) ? descripcion.replace("'", "''") : "";

        strSQL = "INSERT INTO PRODUCTO(idProducto, nombre, descripcion, estado, idMarca, idCategoria, idDistribuidor) "
                + "VALUES(" + idProducto + ", '" + nombreSeguro + "', '" + descSegura + "', "
                + estado + ", " + idMarca + ", " + idCategoria + ", " + idDistribuidor + ")";

        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23505")) {
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
            if (e.getMessage().contains("23503")) {
                throw new Exception("Error: No se puede eliminar. El producto está en uso.");
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
        // Consultamos la tabla PRESENTACION_PRODUCTO que es la que tiene el campo 'precio'
        strSQL = "SELECT COALESCE(MAX(precio), 0) FROM PRESENTACION_PRODUCTO";

        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                // Usamos getDouble porque el precio puede tener decimales
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener precio máximo: " + e.getMessage());
        }
        return 0.0;
    }

    public ResultSet filtrar(String nom, int min, int max) throws Exception {
        String strSQL = "SELECT p.idproducto, p.nombre, p.descripcion, p.estado, m.nombre AS nomMarca, c.nombrecategoria AS nomCategoria, "
                + "pp.precio, pp.stock "
                + "FROM producto p "
                + "INNER JOIN marca m ON p.idmarca = m.idmarca " // <--- CORREGIDO: p.idmarca
                + "INNER JOIN categoria c ON p.idcategoria = c.idcategoria " // <--- CORREGIDO: p.idcategoria
                + "INNER JOIN presentacion_producto pp ON p.idproducto = pp.idproducto "
                + "WHERE UPPER(p.nombre) LIKE UPPER('%" + nom + "%') AND "
                + "pp.precio BETWEEN " + min + " AND " + max + ";";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al filtrar productos (General): " + e.getMessage());
        }
    }

    public String getNombreProducto(int cod) throws Exception {
        String nombre = null;
        try {
            String sql = "SELECT nombre FROM producto WHERE idproducto = " + cod;
            rs = objConectar.consultarBD(sql);

            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener nombre del producto: " + e.getMessage());
        }
        return nombre;
    }

    public ResultSet filtrarMarca(int marca, String nom, int min, int max) throws Exception {
        String strSQL = "SELECT p.idproducto, p.nombre, p.descripcion, p.estado, m.nombre AS nomMarca, c.nombrecategoria AS nomCategoria, "
                + "pp.precio, pp.stock "
                + "FROM producto p "
                + "INNER JOIN marca m ON p.idmarca = m.idmarca " // <--- CORREGIDO
                + "INNER JOIN categoria c ON p.idcategoria = c.idcategoria " // <--- CORREGIDO
                + "INNER JOIN presentacion_producto pp ON p.idproducto = pp.idproducto "
                + "WHERE p.idmarca = " + marca // <--- CORREGIDO
                + " AND UPPER(p.nombre) LIKE UPPER('%" + nom + "%')"
                + " AND pp.precio BETWEEN " + min + " AND " + max + ";";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al filtrar por marca: " + e.getMessage());
        }
    }

    public ResultSet filtrar(int marca, int categoria, String nom, int min, int max) throws Exception {
        String strSQL = "SELECT p.idproducto, p.nombre, p.descripcion, p.estado, m.nombre AS nomMarca, c.nombrecategoria AS nomCategoria, "
                + "pp.precio, pp.stock "
                + "FROM producto p "
                + "INNER JOIN marca m ON p.idmarca = m.idmarca " // <--- CORREGIDO
                + "INNER JOIN categoria c ON p.idcategoria = c.idcategoria " // <--- CORREGIDO
                + "INNER JOIN presentacion_producto pp ON p.idproducto = pp.idproducto "
                + "WHERE p.idmarca = " + marca
                + " AND p.idcategoria = " + categoria
                + " AND UPPER(p.nombre) LIKE UPPER('%" + nom + "%')"
                + " AND pp.precio BETWEEN " + min + " AND " + max + ";";

        try {
            ResultSet rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al filtrar productos (Completo): " + e.getMessage());
        }
    }

    public ResultSet filtrarCategoria(int categoria, String nom, int min, int max) throws Exception {
        String strSQL = "SELECT p.idproducto, p.nombre, p.descripcion, p.estado, m.nombre AS nomMarca, c.nombrecategoria AS nomCategoria, "
                + "pp.precio, pp.stock "
                + "FROM producto p "
                + "INNER JOIN marca m ON p.idmarca = m.idmarca " // <--- CORREGIDO
                + "INNER JOIN categoria c ON p.idcategoria = c.idcategoria " // <--- CORREGIDO
                + "INNER JOIN presentacion_producto pp ON p.idproducto = pp.idproducto "
                + "WHERE p.idcategoria = " + categoria // <--- CORREGIDO
                + " AND UPPER(p.nombre) LIKE UPPER('%" + nom + "%')"
                + " AND pp.precio BETWEEN " + min + " AND " + max + ";";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al filtrar por categoría: " + e.getMessage());
        }
    }

    // --- NUEVO MÉTODO MAESTRO PARA FILTRAR TODO ---
    public ResultSet filtrarAvanzadoLotes(int idMarca, int idCategoria, int idLaboratorio, String nom, double min, double max) throws Exception {

        String strSQL = "SELECT p.idproducto, p.nombre, p.descripcion, p.estado, "
                + "m.nombre AS nomMarca, "
                + "c.nombrecategoria AS nomCategoria, "
                + "l.nombreLaboratorio, " // <--- 1. TRAMOS EL NOMBRE DEL LAB
                + "pp.precio, pp.stock "
                + "FROM producto p "
                + "INNER JOIN marca m ON p.idmarca = m.idmarca "
                + "INNER JOIN categoria c ON p.idcategoria = c.idcategoria "
                + "INNER JOIN laboratorio l ON p.iddistribuidor = l.idlaboratorio " // <--- 2. JOIN CON LABORATORIO
                + "INNER JOIN presentacion_producto pp ON p.idproducto = pp.idproducto "
                + "WHERE UPPER(p.nombre) LIKE UPPER('%" + nom + "%') "
                + "AND pp.precio BETWEEN " + min + " AND " + max;

        // --- LÓGICA INTELIGENTE ---
        // 3. Si idMarca es mayor a 0, agregamos el filtro.
        if (idMarca > 0) {
            strSQL += " AND p.idmarca = " + idMarca;
        }

        // 4. Si idCategoria es mayor a 0, agregamos el filtro.
        if (idCategoria > 0) {
            strSQL += " AND p.idcategoria = " + idCategoria;
        }

        // 5. Si idLaboratorio es mayor a 0, filtramos por distribuidor (así se llama en tu BD)
        if (idLaboratorio > 0) {
            strSQL += " AND p.iddistribuidor = " + idLaboratorio;
        }

        strSQL += " ORDER BY p.nombre";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al filtrar avanzado: " + e.getMessage());
        }
    }

}
