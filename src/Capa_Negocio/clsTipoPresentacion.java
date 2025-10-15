/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

/**
 *
 * @author Tiznado Leon
 */
public class clsTipoPresentacion {
      private int id;
    private String nombre;
    private boolean estado; 

   
    public clsTipoPresentacion(int id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
    
   
    public clsTipoPresentacion() {}

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isEstado() { return estado; } 
    public void setEstado(boolean estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return this.nombre;
    }
}
