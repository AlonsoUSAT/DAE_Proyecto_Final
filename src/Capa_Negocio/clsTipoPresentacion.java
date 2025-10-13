/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

/**
 *
 * @author USER
 */
public class clsTipoPresentacion {
      private int id;
    private String nombre;
    private boolean estado; // <-- 1. AÑADIR CAMPO

    // 2. ACTUALIZAR CONSTRUCTOR
    public clsTipoPresentacion(int id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
    
    // Constructor vacío por si lo necesitas
    public clsTipoPresentacion() {}

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isEstado() { return estado; } // <-- 3. AÑADIR GETTER
    public void setEstado(boolean estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return this.nombre;
    }
}
