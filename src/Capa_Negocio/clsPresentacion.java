package Capa_Negocio;

/**

 * @author Tiznado Leon
 */
public class clsPresentacion {
   
    private final int id;
    private final String nombreTipoPresentacion;
    private final float cantidad;
    private final String nombreUnidad;
    private final boolean activo; 

    
    public clsPresentacion(int id, String nombreTipoPresentacion, float cantidad, String nombreUnidad, boolean activo) {
        this.id = id;
        this.nombreTipoPresentacion = nombreTipoPresentacion;
        this.cantidad = cantidad;
        this.nombreUnidad = nombreUnidad;
        this.activo = activo; 
    }

  
    public int getId() {
        return id;
    }

    public String getNombreTipoPresentacion() {
        return nombreTipoPresentacion;
    }

    public float getCantidad() {
        return cantidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    
    public boolean isActivo() {
        return activo;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s x %.2f %s (%s)", 
            this.nombreTipoPresentacion, 
            this.cantidad, 
            this.nombreUnidad,
            this.activo ? "Activo" : "Inactivo"
        );
    }
}