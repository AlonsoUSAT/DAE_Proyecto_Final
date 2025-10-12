package Capa_Negocio;

// NO hay import de BigDecimal, ya no se necesita aquí.

public class clsProducto {

    private int idProducto;
    private String nombre;
    private String descripcion;
    private boolean estado;
    
    // Atributos para las llaves foráneas como objetos
    private clsMarca marca;
    private clsCategoria categoria;
    private clsLaboratorio distribuidor;

    // Constructor vacío
    public clsProducto() {
    }

    // --- Getters y Setters (solo los necesarios) ---
    
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public clsMarca getMarca() { return marca; }
    public void setMarca(clsMarca marca) { this.marca = marca; }

    public clsCategoria getCategoria() { return categoria; }
    public void setCategoria(clsCategoria categoria) { this.categoria = categoria; }

    public clsLaboratorio getDistribuidor() { return distribuidor; }
    public void setDistribuidor(clsLaboratorio distribuidor) { this.distribuidor = distribuidor; }
    
    @Override
    public String toString() {
        return this.nombre; // Mostrar solo el nombre es más amigable en listas.
    }
}