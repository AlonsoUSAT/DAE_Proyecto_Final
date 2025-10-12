/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

/**
 *
 * @author USER
 */
public class clsPresentacionProducto {

    private int idProducto;
    private int idPresentacion;
    private int stock;
    private float precio;
     private boolean estado; // <-- 1. ATRIBUTO AÑADIDO

    // Constructor vacío
    public clsPresentacionProducto() {
    }

   // --- CONSTRUCTOR CORREGIDO ---
public clsPresentacionProducto(int idProducto, int idPresentacion, int stock, float precio, boolean estado) {
    this.idProducto = idProducto;
    this.idPresentacion = idPresentacion;
    this.stock = stock;
    this.precio = precio;
    this.estado = estado; // <-- Parámetro añadido
}

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdPresentacion() {
        return idPresentacion;
    }

    public void setIdPresentacion(int idPresentacion) {
        this.idPresentacion = idPresentacion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
    
      // --- 3. MÉTODOS GETTER Y SETTER AÑADIDOS ---
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
    
    public int obtenerStockTotalDeLotes(int idProducto, int idPresentacion) throws Exception {
    int stockTotal = 0;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    // Consulta que suma el stockActual de la tabla LOTE
    String sql = "SELECT SUM(stockActual) AS stock_total FROM LOTE WHERE idProducto = ? AND idPresentacion = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.setInt(2, idPresentacion);
        rs = ps.executeQuery();

        if (rs.next()) {
            // Obtenemos el resultado de la suma. Si no hay lotes, devolverá 0.
            stockTotal = rs.getInt("stock_total");
        }
    } catch (Exception e) {
        throw new Exception("Error al calcular el stock total de lotes: " + e.getMessage());
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
    
    return stockTotal;
}
    
    public void actualizarStock(int idProducto, int idPresentacion, int nuevoStock) throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    String sql = "UPDATE PRESENTACION_PRODUCTO SET stock = ? WHERE idProducto = ? AND idPresentacion = ?";

    try {
        conn = objConectar.conectar();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, nuevoStock);
        ps.setInt(2, idProducto);
        ps.setInt(3, idPresentacion);
        ps.executeUpdate();
    } catch (Exception e) {
        throw new Exception("Error al actualizar el stock en la presentación: " + e.getMessage());
    } finally {
        if (ps != null) ps.close();
        if (conn != null) objConectar.desconectar();
    }
}
    
}