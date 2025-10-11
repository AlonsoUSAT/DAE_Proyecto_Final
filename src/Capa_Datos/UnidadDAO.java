/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Datos;

/**
 *
 * @author USER
 */
public class UnidadDAO {
    private int id;
    private String nombre;

    // Constructor para crear el objeto
    public UnidadDAO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Métodos para obtener los valores
    public int getId() { return id; }
    public String getNombre() { return nombre; }

    /**
     * Este método es VITAL. Permite que el JComboBox muestre el nombre
     * de la unidad en lugar de un texto sin sentido.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}
