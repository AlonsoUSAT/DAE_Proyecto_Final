/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

import java.util.Date;

/**
 *
 * @author USER
 */
public class clsLote {
     
     // Campos directos de la tabla LOTE
    private int idLote;
    private String nroLote;
    private Date fechaFabricacion;
    private Date fechaVencimiento;
    private int cantidadRecibida;
    private int stockActual;
    private boolean estado;
    private int idPresentacion;
    private int idProducto; // <-- 1. CAMPO AÑADIDO
    
    // Campos que vendrán de tablas relacionadas (JOINs)
    private String nombreProducto;
    private String descripcionPresentacion;

    // Se añade 'idProducto' al constructor
    public clsLote(int idLote, String nroLote, Date fechaFabricacion, Date fechaVencimiento, 
                   int cantidadRecibida, int stockActual, boolean estado, int idPresentacion, 
                   int idProducto, // <-- 2. PARÁMETRO AÑADIDO
                   String nombreProducto, String descripcionPresentacion) {
        
        this.idLote = idLote;
        this.nroLote = nroLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadRecibida = cantidadRecibida;
        this.stockActual = stockActual;
        this.estado = estado;
        this.idPresentacion = idPresentacion;
        this.idProducto = idProducto; // <-- 3. ASIGNACIÓN AÑADIDA
        this.nombreProducto = nombreProducto;
        this.descripcionPresentacion = descripcionPresentacion;
    }

    // --- Getters ---
    public int getIdLote() { return idLote; }
    public String getNroLote() { return nroLote; }
    public Date getFechaFabricacion() { return fechaFabricacion; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public int getCantidadRecibida() { return cantidadRecibida; }
    public int getStockActual() { return stockActual; }
    public boolean isEstado() { return estado; }
    public int getIdPresentacion() { return idPresentacion; }
    public String getNombreProducto() { return nombreProducto; }
    public String getDescripcionPresentacion() { return descripcionPresentacion; }

    // --- MÉTODO GETTER FALTANTE ---
    public int getIdProducto() { // <-- 4. MÉTODO AÑADIDO
        return idProducto;
    }
}
