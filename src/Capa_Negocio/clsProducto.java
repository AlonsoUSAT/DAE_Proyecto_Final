
package Capa_Negocio;

import java.math.BigDecimal;

public class clsProducto {

    private int idProducto;
    private String nombre;
    private BigDecimal precio;
    private int stock;
    private String unidad;
    private String descripcion;
    private boolean estado;
    
    // Atributos para las llaves foráneas como objetos
    private clsMarca marca;
    private clsCategoria categoria;
    private clsLaboratorio distribuidor; // Usamos clsLaboratorio como distribuidor

    // Constructor vacío
    public clsProducto() {
    }

    // Constructor con todos los campos
    public clsProducto(int idProducto, String nombre, BigDecimal precio, int stock, String unidad, String descripcion, boolean estado, clsMarca marca, clsCategoria categoria, clsLaboratorio distribuidor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.unidad = unidad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.marca = marca;
        this.categoria = categoria;
        this.distribuidor = distribuidor;
    }

    // --- Getters y Setters ---
    
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public clsMarca getMarca() {
        return marca;
    }

    public void setMarca(clsMarca marca) {
        this.marca = marca;
    }

    public clsCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(clsCategoria categoria) {
        this.categoria = categoria;
    }

    public clsLaboratorio getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(clsLaboratorio distribuidor) {
        this.distribuidor = distribuidor;
    }
    
     /**
     * Devuelve una representación en String del objeto.
     * Esto es lo que se mostrará en los JComboBox.
     * @return El ID y el nombre del producto.
     */
    @Override
    public String toString() {
        return this.idProducto + " - " + this.nombre;
    }
}