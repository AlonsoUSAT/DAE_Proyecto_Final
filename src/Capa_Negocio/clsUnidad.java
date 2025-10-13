package Capa_Negocio;

public class clsUnidad {
    private int id;
    private String nombre;
    private boolean estado; // <-- 1. AÑADIR CAMPO

    // 2. ACTUALIZAR CONSTRUCTOR
    public clsUnidad(int id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
    
    // Constructor vacío
    public clsUnidad() {}

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