package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;

/**
 *
 * @author Carlos Otoya
 */
public class clsSerieVenta {
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    public ResultSet listarSeries() throws Exception {
        strSQL = """
            SELECT 
                sc.idSerie,
                sc.serie,
                tc.nombre_tipoComprobante AS tipo_comprobante,
                sc.ultimoNumero,
                sc.limiteNumerico,
                CASE 
                    WHEN sc.estado = true THEN 'Activo' 
                    ELSE 'Inactivo' 
                END AS estado
            FROM serie_comprobante sc
            INNER JOIN tipo_comprobante tc ON sc.id_tipoComprobante = tc.id_tipoComprobante
            ORDER BY sc.serie;
        """;
        try {
            rs = objConectar.consultarBD(strSQL);
            return rs;
        } catch (Exception e) {
            throw new Exception("Error al listar series de comprobante: " + e.getMessage());
        }
    }
    
    public Integer obtenerIdPorSerie(String serie) throws Exception {
        strSQL = "SELECT idSerie AS id FROM serie_comprobante WHERE serie = '" + serie + "'";
        try {
            rs = objConectar.consultarBD(strSQL);
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener id de serie: " + e.getMessage());
        }
        return 0;
    }

}