/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;

import java.util.Date;

/**
 *
 * @author USER
 */
public class LoteDAO {
     
    // Campos directos de la tabla LOTE
    private int idLote;
    private String nroLote;
    private Date fechaFabricacion;
    private Date fechaVencimiento;
    private int cantidadRecibida;
    private int stockActual;
    private boolean estado;
    private int idPresentacion; // ✅ 1. AÑADE ESTE CAMPO
    
    // Campos que vendrán de tablas relacionadas (JOINs)
    private String nombreProducto;
    private String descripcionPresentacion; // Para mostrar ej: "Caja x 24 Unidades"

   // ✅ 2. AÑADE el parámetro 'idPresentacion' al constructor
    public LoteDAO(int idLote, String nroLote, Date fechaFabricacion, Date fechaVencimiento, 
                   int cantidadRecibida, int stockActual, boolean estado, int idPresentacion, 
                   String nombreProducto, String descripcionPresentacion) {
        
        this.idLote = idLote;
        this.nroLote = nroLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadRecibida = cantidadRecibida;
        this.stockActual = stockActual;
        this.estado = estado;
        this.idPresentacion = idPresentacion; // ✅ 3. ASIGNA EL VALOR
        this.nombreProducto = nombreProducto;
        this.descripcionPresentacion = descripcionPresentacion;
    }

    // Getters para acceder a los valores
    public int getIdLote() {
        return idLote;
    }

    public String getNroLote() {
        return nroLote;
    }

    public Date getFechaFabricacion() {
        return fechaFabricacion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public int getStockActual() {
        return stockActual;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getDescripcionPresentacion() {
        return descripcionPresentacion;
    }
    
     // ✅ 4. AÑADE este método para poder leer el ID
    public int getIdPresentacion() {
        return idPresentacion;
    }

    
    // etc.
    
}
