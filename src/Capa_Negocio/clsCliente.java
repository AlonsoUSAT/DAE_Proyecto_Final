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


    
    public ResultSet consultarClientes() throws Exception{
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
    
}