package Capa_Negocio;

// autor: Fernando Hernández

public class clsProducto {

    private int idProducto;
    private String nombre;
    private String descripcion;
    private boolean estado;
    
    
    private clsMarca marca;
    private clsCategoria categoria;
    private clsLaboratorio distribuidor;

   
    public clsProducto() {
    }

    
    
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
        return this.nombre; 
    }
}