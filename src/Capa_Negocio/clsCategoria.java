/*
 * Basado en la plantilla de capa_negocios
 */
package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

public class clsCategoria {
    
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent;

    public ResultSet listarCategorias() throws Exception {
        strSQL = "SELECT idcategoria, nombrecategoria, estado FROM categoria ORDER BY nombrecategoria";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar Categorías: " + e.getMessage());
        }
    }

    public ResultSet listarCategoriasActivas() throws Exception {
        strSQL = "SELECT idcategoria, nombrecategoria, estado FROM categoria WHERE estado = true ORDER BY nombrecategoria";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar categorías activas: " + e.getMessage());
        }
    }
    
    public Integer obtenerCodigoCategoria(String nom)throws Exception{
        strSQL = "select codcategoria from categoria where nomcategoria= '" +nom + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) return rs.getInt("codcategoria");

        } catch (Exception e) {
            throw new Exception("Error al buscar la categoria");
        }
        
        return 0;
    }

    public Integer generarCodigoCategoria() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idcategoria), 0) + 1 AS codigo FROM categoria";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) { 
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar codigo de categoria: " + e.getMessage());
        }
        return 1;
    }

    public void registrar(String nom, Boolean vig) throws Exception {
        String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";

        strSQL = "INSERT INTO CATEGORIA(nombreCategoria, estado) VALUES ('" + nomSeguro + "', " + vig + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
             if (e.getMessage().contains("23505")) { // Error de llave única
                 throw new Exception("Error: El nombre de la categoría ya existe.");
             }
            throw new Exception("Error al registrar la categoria: " + e.getMessage());
        }
    }
    
    public ResultSet buscarCategoria(Integer cod) throws Exception {
        strSQL = "SELECT idCategoria, nombreCategoria, estado FROM CATEGORIA WHERE idCategoria = " + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar categoria: " + e.getMessage());
        }
    }

    public void eliminarCategoria(Integer cod) throws Exception {
        strSQL = "DELETE FROM categoria WHERE idcategoria = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23503")) { // Error de llave foránea
                 throw new Exception("Error: No se puede eliminar, la categoría está en uso.");
             }
            throw new Exception("Error al eliminar la categoría: " + e.getMessage());
        }
    }
    
    public void darBaja(Integer cod) throws Exception {
        strSQL = "UPDATE categoria SET estado = false WHERE idcategoria = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja la categoría: " + e.getMessage());
        }
    }

    public void reactivar(Integer cod) throws Exception {
        strSQL = "UPDATE categoria SET estado = true WHERE idcategoria = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al reactivar la categoría: " + e.getMessage());
        }
    }
    
    public void modificar(Integer cod, String nom, Boolean vig) throws Exception {
        String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";

        strSQL = "UPDATE categoria SET nombrecategoria = '" + nomSeguro + "', estado = " + vig + 
                 " WHERE idcategoria = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
             if (e.getMessage().contains("23505")) { // Error de llave única
                 throw new Exception("Error: El nombre de la categoría ya existe.");
             }
            throw new Exception("Error al modificar la categoría: " + e.getMessage());
        }
    }
}