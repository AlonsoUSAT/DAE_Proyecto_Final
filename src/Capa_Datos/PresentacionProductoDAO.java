/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;

/**
 *
 * @author USER
 */
public class PresentacionProductoDAO {

    private int idProducto;
    private int idPresentacion;
    private int stock;
    private float precio;

    // Constructor vacío
    public PresentacionProductoDAO() {
    }

    // Constructor con todos los parámetros
    public PresentacionProductoDAO(int idProducto, int idPresentacion, int stock, float precio) {
        this.idProducto = idProducto;
        this.idPresentacion = idPresentacion;
        this.stock = stock;
        this.precio = precio;
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
}