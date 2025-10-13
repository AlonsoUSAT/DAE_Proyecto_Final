package Capa_Negocio;

/**
 * Representa una presentación de producto con sus atributos.
 * Esta clase sirve como un Objeto de Transferencia de Datos (DTO).
 * @author USER
 */
public class clsPresentacion {
    // --- Atributos ---
    private final int id;
    private final String nombreTipoPresentacion;
    private final float cantidad;
    private final String nombreUnidad;
    private final boolean activo; // Atributo para el estado

    /**
     * Constructor para inicializar un objeto de presentación.
     * @param id El identificador único de la presentación.
     * @param nombreTipoPresentacion El nombre del tipo de presentación (ej: "Caja", "Frasco").
     * @param cantidad La cantidad numérica (ej: 50.5).
     * @param nombreUnidad El nombre de la unidad (ej: "Mililitros").
     * @param activo El estado de la presentación (true para activo, false para inactivo).
     */
    public clsPresentacion(int id, String nombreTipoPresentacion, float cantidad, String nombreUnidad, boolean activo) {
        this.id = id;
        this.nombreTipoPresentacion = nombreTipoPresentacion;
        this.cantidad = cantidad;
        this.nombreUnidad = nombreUnidad;
        this.activo = activo; // Se inicializa el nuevo atributo
    }

    // --- Métodos Getter ---
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

    /**
     * Devuelve el estado de la presentación.
     * El prefijo "is" es la convención estándar para getters de tipo boolean.
     * @return true si la presentación está activa, false en caso contrario.
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Devuelve una representación en texto del objeto, útil para depuración o logs.
     * Muestra los datos principales de la presentación.
     */
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