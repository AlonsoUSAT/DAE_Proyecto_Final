/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

/**
 *
 * @author Tiznado Leon
 */
public class clsPresentacionProducto {

    private int idProducto;
    private int idPresentacion;
    private int stock;
    private float precio;
     private boolean estado; 

  
    public clsPresentacionProducto() {
    }

   
public clsPresentacionProducto(int idProducto, int idPresentacion, int stock, float precio, boolean estado) {
    this.idProducto = idProducto;
    this.idPresentacion = idPresentacion;
    this.stock = stock;
    this.precio = precio;
    this.estado = estado; 
}

    
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
    
    
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
    
   
}