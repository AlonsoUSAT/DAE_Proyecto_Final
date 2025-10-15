package Capa_Negocio;

public class clsUnidad {
    private int id;
    private String nombre;
    private boolean estado;

    
    public clsUnidad(int id, String nombre, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }
    
    
    public clsUnidad() {}

    
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