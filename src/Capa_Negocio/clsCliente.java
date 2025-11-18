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

    public ResultSet buscarClienteDniRuc(String cod, Boolean tipo) throws Exception {

        // NOTA: Mantenemos el estilo de concatenación que usas, pero es VULNERABLE a Inyección SQL.
        if (tipo) { // Tipo = true: Asumimos DNI (Persona)
            strSQL = "SELECT C.*, P.nombres, P.apellidoPaterno, TD.nom_tipoDoc "
                    + "FROM CLIENTE C "
                    + "INNER JOIN PERSONA P ON C.idCliente = P.idCliente "
                    + "INNER JOIN TIPO_DOCUMENTO TD ON C.id_tipoDoc = TD.id_tipoDoc "
                    + "WHERE C.nroDoc = '" + cod + "';";
        } else { // Tipo = false: Asumimos RUC (Empresa)
            strSQL = "SELECT C.*, E.razonSocial, TD.nom_tipoDoc "
                    + "FROM CLIENTE C "
                    + "INNER JOIN EMPRESA E ON C.idCliente = E.idCliente "
                    + "INNER JOIN TIPO_DOCUMENTO TD ON C.id_tipoDoc = TD.id_tipoDoc "
                    + "WHERE C.nroDoc = '" + cod + "';";
        }

        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente: " + e.getMessage());
        }
    }
}
