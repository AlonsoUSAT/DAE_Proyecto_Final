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
     
    
    private int idLote;
    private String nroLote;
    private Date fechaFabricacion;
    private Date fechaVencimiento;
    private int cantidadRecibida;
    private int stockActual;
    private boolean estado;
    private int idPresentacion;
    private int idProducto; 
    
   
    private String nombreProducto;
    private String descripcionPresentacion;

    
    public clsLote(int idLote, String nroLote, Date fechaFabricacion, Date fechaVencimiento, 
                   int cantidadRecibida, int stockActual, boolean estado, int idPresentacion, 
                   int idProducto, 
                   String nombreProducto, String descripcionPresentacion) {
        
        this.idLote = idLote;
        this.nroLote = nroLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadRecibida = cantidadRecibida;
        this.stockActual = stockActual;
        this.estado = estado;
        this.idPresentacion = idPresentacion;
        this.idProducto = idProducto; 
        this.nombreProducto = nombreProducto;
        this.descripcionPresentacion = descripcionPresentacion;
    }

   
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

    
    public int getIdProducto() { 
        return idProducto;
    }
}
