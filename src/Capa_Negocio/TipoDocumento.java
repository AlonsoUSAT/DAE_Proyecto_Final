/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;

/**
 *
 * @author Nicole
 */
public class TipoDocumento {
    
  private int id;
    private String nombre;


    // CONSTRUCTOR CORREGIDO: Asigna los parámetros a los atributos de la clase
    public TipoDocumento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public int getId() {
        return id;
    }
    
    // Método setter para nombre (útil si lo requieres, aunque no es necesario aquí)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Crucial: Lo que el JComboBox mostrará al usuario
    @Override
    public String toString() {
        return nombre;
    }
}
