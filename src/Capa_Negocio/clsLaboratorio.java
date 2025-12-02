/*
 * Basado en la plantilla de capa_negocios (clsCategoria)
 */
package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Fernando Hernández 
 */
public class clsLaboratorio {
    
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent;


    public ResultSet listarLaboratorios() throws Exception {
        strSQL = "SELECT idlaboratorio, nombrelaboratorio, direccion, telefono, estado FROM laboratorio ORDER BY nombrelaboratorio";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar laboratorios: " + e.getMessage());
        }
    }

    public ResultSet listarLaboratoriosActivos() throws Exception {
        strSQL = "SELECT idlaboratorio, nombrelaboratorio, direccion, telefono, estado FROM laboratorio WHERE estado = true ORDER BY nombrelaboratorio";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar laboratorios activos: " + e.getMessage());
        }
    }
    
    public Integer obtenerCodigoLaboratorio(String nom)throws Exception{
        strSQL = "select * from laboratorio where nombrelaboratorio= '" +nom + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) return rs.getInt("idlaboratorio");

        } catch (Exception e) {
            throw new Exception("Error al buscar laboratorio por nombre");
        }
        
        return 0;
    }

    public Integer generarCodigoLaboratorio() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idlaboratorio), 0) + 1 AS codigo FROM laboratorio";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar codigo de laboratorio");
        }
        return 1;
    }

    public void registrar(String nom, String dir, String tel, Boolean est) throws Exception {
        String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";
        String dirSeguro = (dir != null) ? dir.replace("'", "''") : "";
        String telSeguro = (tel != null) ? tel.replace("'", "''") : "";

        strSQL = "INSERT INTO laboratorio(nombrelaboratorio, direccion, telefono, estado) VALUES ('" + 
                 nomSeguro + "', '" + dirSeguro + "', '" + telSeguro + "', " + est + ")";
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23505")) { // Error de llave única
                throw new Exception("Error: El nombre del laboratorio ya existe.");
            }
            throw new Exception("Error al registrar el laboratorio: " + e.getMessage());
        }
    }

    public ResultSet buscarLaboratorio(Integer cod) throws Exception {
        strSQL = "SELECT idlaboratorio, nombrelaboratorio, direccion, telefono, estado FROM laboratorio WHERE idlaboratorio = " + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar laboratorio: " + e.getMessage());
        }
    }


    public void eliminarLaboratorio(Integer cod) throws Exception {
        strSQL = "DELETE FROM laboratorio WHERE idlaboratorio = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23503")) { // Error de llave foránea
                throw new Exception("Error: No se puede eliminar. El laboratorio está en uso.");
            }
            throw new Exception("Error al eliminar el laboratorio: " + e.getMessage());
        }
    }

    public void darBaja(Integer cod) throws Exception {
        strSQL = "UPDATE laboratorio SET estado = false WHERE idLaboratorio = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el laboratorio: " + e.getMessage());
        }
    }

    public void reactivar(Integer cod) throws Exception {
        strSQL = "UPDATE laboratorio SET estado = true WHERE idlaboratorio = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al reactivar el laboratorio: " + e.getMessage());
        }
    }

    public void modificar(Integer cod, String nom, String dir, String tel, Boolean est) throws Exception {
        String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";
        String dirSeguro = (dir != null) ? dir.replace("'", "''") : "";
        String telSeguro = (tel != null) ? tel.replace("'", "''") : "";

        strSQL = "UPDATE laboratorio SET nombrelaboratorio = '" + nomSeguro + "', direccion = '" + dirSeguro + 
                 "', telefono = '" + telSeguro + "', estado = " + est + 
                 " WHERE idLaboratorio = " + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            if (e.getMessage().contains("23505")) { // Error de llave única
                throw new Exception("Error: El nombre del laboratorio ya existe.");
            }
            throw new Exception("Error al modificar el laboratorio: " + e.getMessage());
        }
    }
    
    public int getCodigo(String nombre) throws Exception {
        String strSQL = "SELECT idlaboratorio FROM laboratorio WHERE nombrelaboratorio = '" + nombre + "'";
        try {
            ResultSet rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("idlaboratorio");
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar código de laboratorio: " + e.getMessage());
        }
        return 0;
    }
    
    public ResultSet listarLaboratorio() throws Exception {
        // Seleccionamos nombrelaboratorio de la tabla laboratorio
        strSQL = "SELECT nombrelaboratorio FROM laboratorio WHERE estado = true ORDER BY nombrelaboratorio";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar laboratorios: " + e.getMessage());
        }
    }
}