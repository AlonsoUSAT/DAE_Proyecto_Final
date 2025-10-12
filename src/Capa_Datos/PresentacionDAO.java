/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;

/**
 *
 * @author USER
 */
public class PresentacionDAO {
      // --- Atributos ---
    private int id;
    private String nombreTipoPresentacion; // El nombre completo es importante
    private float cantidad;
    private String nombreUnidad;           // El nombre completo es importante

    // --- Constructor ---
    public PresentacionDAO(int id, String nombreTipoPresentacion, float cantidad, String nombreUnidad) {
        this.id = id;
        this.nombreTipoPresentacion = nombreTipoPresentacion;
        this.cantidad = cantidad;
        this.nombreUnidad = nombreUnidad;
    }

    // --- Métodos Getter (estos son los que usas para obtener los datos) ---
    public int getId() {
        return id;
    }

    public String getNombreTipoPresentacion() { // ✔️ Este es el nombre correcto del método
        return nombreTipoPresentacion;
    }

    public float getCantidad() {
        return cantidad;
    }

    public String getNombreUnidad() { // ✔️ Este es el nombre correcto del método
        return nombreUnidad;
    }
    
    // (Opcional) Sobreescribes toString para que se vea bien en los ComboBox
    @Override
    public String toString() {
        return String.format("%s x %.0f %s", this.nombreTipoPresentacion, this.cantidad, this.nombreUnidad);
    }
}
