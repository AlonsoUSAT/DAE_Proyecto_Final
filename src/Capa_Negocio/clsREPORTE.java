package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class clsREPORTE {
    
    // Ruta donde tienes tus archivos .jasper
    // Nota: Usa "/" en lugar de "\\" para mayor compatibilidad
    public static final String RUTA_REPORTES = "src/Reportes/"; 
    
    /**
     * Genera un reporte para ser incrustado en un JPanel o JInternalFrame.
     * Retorna un objeto visual (JRViewer).
     */
    public JRViewer reporteInterno(String archivoReporte, Map<String, Object> parametros) {
        try {
            clsJDBC objConexion = new clsJDBC();
            Connection conn = objConexion.conectar(); // Obtenemos la conexión
            
            JasperPrint reporte = JasperFillManager.fillReport(
                    RUTA_REPORTES + archivoReporte,
                    parametros,
                    conn
            );
            
            // Retornamos el panel visual del reporte
            return new JRViewer(reporte);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte interno: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Genera un reporte listo para ser mostrado en una ventana emergente (JasperViewer).
     * Retorna el objeto de impresión (JasperPrint).
     */
    public JasperPrint reporteExterno(String archivoReporte, Map<String, Object> parametros) {
        try {
            clsJDBC objConexion = new clsJDBC();
            Connection conn = objConexion.conectar(); // Obtenemos la conexión
            
            JasperPrint reporte = JasperFillManager.fillReport(
                    RUTA_REPORTES + archivoReporte,
                    parametros,
                    conn
            );
            
            return reporte;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte externo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}