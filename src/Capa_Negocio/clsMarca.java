package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Clase de servicio (Transaction Script) para gestionar Marcas.
 * NO es una entidad. NO tiene getters ni setters.
 */
public class clsMarca {
    
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    Connection con = null;
    Statement sent;

    public ResultSet listarMarcas() throws Exception {
        strSQL = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                 "FROM MARCA m " +
                 "INNER JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                 "ORDER BY m.nombre";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar Marcas: " + e.getMessage());
        }
    }

    public ResultSet listarMarcasActivas() throws Exception {
        strSQL = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                 "FROM MARCA m " +
                 "INNER JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                 "WHERE m.estado = true ORDER BY m.nombre";
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar Marcas activas: " + e.getMessage());
        }
    }
    
    public Integer obtenerCodigoMarca(String nom)throws Exception{
        strSQL = "select * from marca where nommarca= '" +nom + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) return rs.getInt("idmarca");

        } catch (Exception e) {
            throw new Exception("Error al buscar la marca por nombre");
        }
        
        return 0;
    }

    public Integer generarCodigoMarca() throws Exception {
        strSQL = "SELECT COALESCE(MAX(idMarca), 0) + 1 AS codigo FROM MARCA";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (Exception e) {
            throw new Exception("Error al generar codigo de marca");
        }
        return 1;
    }

    public void registrarMarca(String nom, String desc, Integer idLab, Boolean estado) throws Exception {
    String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";
    String descSeguro = (desc != null) ? desc.replace("'", "''") : "";
    
    // 2. Modificamos el INSERT para NO incluir la columna idMarca
    strSQL = "INSERT INTO MARCA(nombre, descripcion, idLaboratorio, estado) VALUES('" +
             nomSeguro + "', '" + descSeguro + "', " + idLab + ", " + estado + ")";
             
    try {
        objConectar.ejecutarBD(strSQL);
    } catch (Exception e) {
        throw new Exception("Error al registrar la marca: " + e.getMessage());
    }
}

    public ResultSet buscarMarca(Integer cod) throws Exception {
        strSQL = "SELECT m.idMarca, m.nombre, m.descripcion, m.estado, m.idLaboratorio, l.nombreLaboratorio " +
                 "FROM MARCA m " +
                 "INNER JOIN LABORATORIO l ON m.idLaboratorio = l.idLaboratorio " +
                 "WHERE m.idMarca = " + cod;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar marca: " + e.getMessage());
        }
    }

    public void eliminarMarca(Integer cod) throws Exception {
        strSQL = "DELETE FROM MARCA WHERE idMarca=" + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al eliminar la marca: " + e.getMessage());
        }
    }

    public void darBajaMarca(Integer cod) throws Exception {
        strSQL = "UPDATE MARCA SET estado = false WHERE idMarca=" + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al dar de baja la marca: " + e.getMessage());
        }
    }
    
    public void reactivarMarca(Integer cod) throws Exception {
         strSQL = "UPDATE MARCA SET estado = true WHERE idMarca=" + cod;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al reactivar la marca: " + e.getMessage());
        }
    }


    public void modificarMarca(Integer id, String nom, String desc, Integer idLab, Boolean estado) throws Exception {
        String nomSeguro = (nom != null) ? nom.replace("'", "''") : "";
        String descSeguro = (desc != null) ? desc.replace("'", "''") : "";

        strSQL = "UPDATE MARCA SET nombre='" + nomSeguro + "', descripcion='" + descSeguro + 
                 "', idLaboratorio=" + idLab + ", estado=" + estado + 
                 " WHERE idMarca=" + id;
        try {
            objConectar.ejecutarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al modificar la marca: " + e.getMessage());
        }
    }
    
    public int getCodigo(String nom) throws Exception {
    // Asumo que la tabla de Marca usa 'codmarca' y 'nombre' o 'nomarca'
    // Según tu código, usa 'codmarca' y 'nomarca'
    strSQL = "SELECT idmarca FROM marca WHERE nombre = '" + nom + "';"; 
    try {
        rs = objConectar.consultarBD(strSQL);
        if (rs.next()) {
            return rs.getInt("idmarca"); // CORREGIDO: Usamos el nombre de columna correcto
        }
    } catch (Exception e) {
        throw new Exception("Error al obtener el código de la marca: " + e.getMessage());
    }
    return 0;
}
}