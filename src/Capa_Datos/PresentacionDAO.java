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
    private int id;
    private String tipoPresentacion;
    private int cantidad;
    private String unidad;

    // Constructor
    public PresentacionDAO(int id, String tipo, int cantidad, String unidad) {
        this.id = id;
        this.tipoPresentacion = tipo;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    // Getters
    public int getId() { return id; }
    public String getTipoPresentacion() { return tipoPresentacion; }
    public int getCantidad() { return cantidad; }
    public String getUnidad() { return unidad; }
}
