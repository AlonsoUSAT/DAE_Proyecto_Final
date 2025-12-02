package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Carlos Otoya
 */
public class clsCliente {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public ResultSet consultarClientes() throws Exception {
        strSQL = """
                select 
                    idcliente,
                    nrodoc,
                    to_char(fecha_registro, 'YYYY-MM-DD HH24:MI:SS') as fecha_registro,
                    direccion,
                    telefono,
                    correo,
                    id_tipoDoc
                from cliente
                order by idcliente;
                """;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar clientes: " + e.getMessage());
        }
    }

    public ResultSet buscarClienteDniRuc(String nroDoc, Boolean esDni) throws Exception {
        strSQL = "SELECT C.idcliente, C.nrodoc, C.direccion, C.telefono, C.correo, "
               + "P.nombres, P.apellidopaterno, P.apellidomaterno, "
               + "E.razonsocial, "
               + "TD.nom_tipodoc "
               + "FROM cliente C "
               + "INNER JOIN tipo_documento TD ON C.id_tipodoc = TD.id_tipodoc "
               + "LEFT JOIN persona P ON C.idcliente = P.idcliente "
               + "LEFT JOIN empresa E ON C.idcliente = E.idcliente "
               + "WHERE C.nrodoc = '" + nroDoc + "'";

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente por DNI/RUC: " + e.getMessage());
        }
    }
    
     
    public int obtenerCodigoCliente(String nroDoc) throws Exception {
        // Usamos TRIM() para limpiar espacios en blanco por si acaso
        strSQL = "SELECT idcliente FROM cliente WHERE nrodoc = '" + nroDoc.trim() + "'";
        
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("idcliente");
            }
        } catch (Exception e) {
            throw new Exception("Error al buscar el cliente: " + e.getMessage());
        }
        return 0; // Retorna 0 si no encuentra al cliente
    }


    public ResultSet buscarClientePorId(int idCliente) throws Exception {
        // Usamos la misma lógica de JOINs que ya tienes, pero filtrando por ID
        strSQL = "SELECT C.idcliente, C.nrodoc, C.direccion, "
            + "P.nombres, P.apellidopaterno, P.apellidomaterno, "
            + "E.razonsocial "
            + "FROM cliente C "
            + "LEFT JOIN persona P ON C.idcliente = P.idcliente "
            + "LEFT JOIN empresa E ON C.idcliente = E.idcliente "
            + "WHERE C.idcliente = " + idCliente;

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente por ID: " + e.getMessage());
        }
    }



}
