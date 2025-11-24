package Capa_Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Mechan Vidaurre Mia
 */
public class clsJDBC {

    private String driver, url, user, password;
    private Connection con;
    private Statement sent = null;

    public clsJDBC() {
        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/DAE_PROYECTO_FINAL";
        this.user = "postgres";
        this.password = "72756176Mia"; // Nota: En producción, evita poner contraseñas en el código.
        this.con = null;
    }

    // Conectamos
    public Connection conectar() throws ClassNotFoundException, SQLException {
        // Carga el driver solo si no está cargada la conexión o está cerrada
        if (con == null || con.isClosed()) {
            Class.forName(driver);
            this.con = DriverManager.getConnection(url, user, password);
        }
        return this.con;
    }

    // Desconectamos
    public void desconectar() throws SQLException {
        // Es buena práctica cerrar también el Statement
        if (sent != null && !sent.isClosed()) {
            sent.close();
        }
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    // Ejecutar una consulta SELECT
    public ResultSet consultarBD(String strSQL) throws Exception {
        ResultSet rs = null;
        try {
            conectar();
            // Usamos createStatement con parámetros para permitir scroll si es necesario (opcional)
            sent = con.createStatement();
            rs = sent.executeQuery(strSQL);
            return rs;

        } catch (Exception e) {
            // Solo desconectamos si hubo un error al intentar consultar
            desconectar();
            throw new Exception("Error al ejecutar consulta: " + e.getMessage());
        }
        // OJO: AQUÍ NO PONEMOS EL FINALLY PARA DESCONECTAR
        // La conexión debe seguir viva para leer el ResultSet fuera de esta clase.
    }

    // Ejecutar INSERT, UPDATE, DELETE
    public void ejecutarBD(String strSQL) throws Exception {
        try {
            conectar();
            sent = con.createStatement();
            sent.executeUpdate(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al ejecutar Update --> " + e.getMessage());
        } finally {
            // Aquí SÍ desconectamos, porque la operación terminó y no devolvemos datos vivos
            desconectar();
        }
    }

    public Connection getCon() {
        return con;
    }
}
