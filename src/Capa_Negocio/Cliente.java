
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Capa_Negocio;


import Capa_Datos.clsJDBC;
import java.security.Timestamp;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Nicole
 */
public class Cliente {
    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;
    
// 2. ATRIBUTOS DE LA ENTIDAD CLIENTE (Tabla CLIENTE)
    // Usamos Integer y String para que el método de BÚSQUEDA pueda cargar los datos
    private int idCliente;
    private String nroDoc;
    // private Timestamp fecha_registro; // Por ahora lo omitiremos si no lo usas en el formulario
    private String direccion;
    private String telefono;
    private String correo;
    private int id_tipoDoc; // FK (DNI=1, RUC=2, etc.)

    // 3. ATRIBUTOS EXTRAS para la Interfaz (de Persona o Empresa)
    // Nota: Es mejor crear clases Persona y Empresa, pero para seguir tu lógica simple:
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String razonSocial;
    

    public ResultSet listarClientes() throws Exception {

        // Consulta SQL mejorada: Junta CLIENTE, TIPO_DOCUMENTO, PERSONA y EMPRESA
        // Muestra: ID, Nro. Doc, Tipo Doc, Nombre Completo/Razón Social, Teléfono, correo.
        this.strSQL = 
            "SELECT " +
            "    C.idCliente, " +
            "    C.nroDoc, " +
            "    TD.nom_tipoDoc, " +
            "    CASE " +
            "        WHEN C.id_tipoDoc = 1 THEN CONCAT(P.nombres, ' ', P.apellidoPaterno, ' ', P.apellidoMaterno) " + // DNI = Persona (Asumiendo id_tipoDoc=1 para DNI)
            "        WHEN C.id_tipoDoc = 2 THEN E.razonSocial " + // RUC = Empresa (Asumiendo id_tipoDoc=2 para RUC)
            "        ELSE 'Cliente Vario' " +
            "    END AS nombre_completo_o_razon_social, " +
            "    C.telefono, " +
            "    C.correo " +
            "FROM CLIENTE C " +
            "JOIN TIPO_DOCUMENTO TD ON C.id_tipoDoc = TD.id_tipoDoc " +
            "LEFT JOIN PERSONA P ON C.idCliente = P.idCliente " +
            "LEFT JOIN EMPRESA E ON C.idCliente = E.idCliente " +
            "ORDER BY C.idCliente DESC"; // Últimos registrados primero (Opcional)

        try {
            // Ejecuta la consulta usando el método consultarBD de clsJDBC
            // Nota: consultarBD() abre la conexión, ejecuta, y devuelve el ResultSet.
            this.rs = objConectar.consultarBD(strSQL);
            return this.rs;
        } catch (Exception e) {
            throw new Exception("Error al listar clientes: " + e.getMessage());
        }
    }
    
    public ResultSet listarTiposClientes() throws Exception {

        // Consulta simple a la tabla TIPO_DOCUMENTO
        this.strSQL = "SELECT id_tipoDoc, nom_tipoDoc FROM TIPO_DOCUMENTO ORDER BY id_tipoDoc";

        try {
            // Ejecuta la consulta usando el método consultarBD de clsJDBC
            // Nota: consultarBD() abre la conexión, ejecuta y devuelve el ResultSet.
            this.rs = objConectar.consultarBD(strSQL);
            return this.rs;

        } catch (Exception e) {
            throw new Exception("Error al listar tipos de clientes (documentos): " + e.getMessage());
        }
    }
    
    public ResultSet listarTipoDocumentosFiltrado(String tipoCliente) throws Exception{
        // Lógica para filtrar por nombre (ajusta los nombres de documentos si son diferentes)
        if (tipoCliente.equals("PERSONA")) {
            // Documentos que aplican a PERSONAS
            strSQL = "SELECT id_tipoDoc, nom_tipoDoc FROM TIPO_DOCUMENTO WHERE nom_tipoDoc IN ('DNI', 'Carné de Extranjería', 'Pasaporte')";
        } else if (tipoCliente.equals("EMPRESA")) {
            // Documentos que aplican a EMPRESAS
            strSQL = "SELECT id_tipoDoc, nom_tipoDoc FROM TIPO_DOCUMENTO WHERE nom_tipoDoc = 'RUC'";
        } else {
            // Por defecto, no mostrar nada o todos, según tu preferencia
            strSQL = "SELECT id_tipoDoc, nom_tipoDoc FROM TIPO_DOCUMENTO WHERE 1=0"; // No devuelve resultados
        }

        try{
            return objConectar.consultarBD(strSQL);
        }catch (Exception e){
            throw new Exception ("Error al listar tipos de documento filtrado: " + e.getMessage());
        }
    }
    
   public int generarCodigoCliente() throws Exception {
    
    // Consulta para obtener el valor máximo (o el último valor de la secuencia si fuera necesario)
    this.strSQL = "SELECT MAX(idCliente) FROM CLIENTE";
    int nuevoId = 1; // Por defecto, si la tabla está vacía, será 1.

    // Usamos un bloque try-catch-finally para asegurar el cierre de recursos
    try {
        // Ejecuta la consulta (esto abre la conexión y devuelve el ResultSet)
        this.rs = objConectar.consultarBD(strSQL);

        if (this.rs.next()) {
            // 1. OBTENER EL VALOR MÁXIMO
            int maxId = rs.getInt(1); 
            
            // 2. CALCULAR EL NUEVO ID
            if (maxId > 0) { // Si ya hay clientes
                nuevoId = maxId + 1;
            } 
            // Si maxId es 0 o NULL (tabla vacía), nuevoId sigue siendo 1
        }
        
        // CERRAR RECURSOS: IMPORTANTE para liberar la conexión
        this.rs.close(); 
        

        return nuevoId;

    } catch (Exception e) {
        throw new Exception("Error al generar el código del cliente: " + e.getMessage());
    } finally {
        // Asegurarse de cerrar el ResultSet y la conexión en el caso de que tu clsJDBC lo requiera
        try {
            if (this.rs != null && !this.rs.isClosed()) {
                this.rs.close();
            }
            // Si tu clsJDBC tiene un método para forzar la desconexión, ponlo aquí
            // objConectar.desconectar(); 
        } catch (Exception ex) {
            // Ignorar errores al cerrar
        }
    }
}
    
    public Cliente buscarCliente(String nroDoc) throws Exception {
        Cliente clienteEncontrado = null;
        ResultSet rs = null;

        // Consulta SQL que combina CLIENTE con PERSONA y EMPRESA (usando LEFT JOIN)
        String strSQL = String.format(
            "SELECT C.*, P.nombres, P.apellidoPaterno, P.apellidoMaterno, E.razonSocial " +
            "FROM CLIENTE C " +
            "LEFT JOIN PERSONA P ON C.idCliente = P.idCliente " +
            "LEFT JOIN EMPRESA E ON C.idCliente = E.idCliente " +
            "WHERE C.nroDoc = '%s'", nroDoc
        );

        try {
            // Usa tu método consultarBD() de clsJDBC que abre, ejecuta y cierra la conexión
            rs = objConectar.consultarBD(strSQL);

            if (rs.next()) {
                // 1. Se encontró el cliente: Creamos un nuevo objeto
                clienteEncontrado = new Cliente();

                // 2. Cargar los atributos del NUEVO objeto (REQUIERE SETTERS)
                clienteEncontrado.setIdCliente(rs.getInt("idCliente"));
                clienteEncontrado.setNroDoc(rs.getString("nroDoc"));
                clienteEncontrado.setDireccion(rs.getString("direccion"));
                clienteEncontrado.setTelefono(rs.getString("telefono"));
                clienteEncontrado.setCorreo(rs.getString("correo"));
                clienteEncontrado.setId_tipoDoc(rs.getInt("id_tipoDoc"));
                // clienteEncontrado.setEstado(rs.getBoolean("estado")); // Si tienes este campo

                // Cargar datos de PERSONA/EMPRESA
                clienteEncontrado.setNombres(rs.getString("nombres"));
                clienteEncontrado.setApellidoPaterno(rs.getString("apellidoPaterno"));
                clienteEncontrado.setApellidoMaterno(rs.getString("apellidoMaterno"));
                clienteEncontrado.setRazonSocial(rs.getString("razonSocial"));
            }

            return clienteEncontrado; // Devuelve el objeto cargado o null

        } catch (Exception e) {
            throw new Exception("Error al ejecutar la búsqueda: " + e.getMessage());
        }
    }
    
    public boolean registrarCliente(String nroDoc, String direccion, String telefono, String correo, 
                                      int id_tipoDoc, String nombres, String apellidoPaterno, 
                                      String apellidoMaterno, String sexo, Date fechaNacimiento, 
                                      String razonSocial) throws Exception {
        
        // 1. OBTENER EL PRÓXIMO ID (Necesario si la BD es PostgreSQL o no soporta GET GENERATED KEYS simple)
        int idClienteGenerado = generarCodigoCliente(); // Usamos tu método
        
        // 2. INSERTAR en CLIENTE
        String sqlCliente = String.format(
            "INSERT INTO CLIENTE (idCliente, nroDoc, direccion, telefono, correo, id_tipoDoc) " +
            "VALUES (%d, '%s', '%s', '%s', '%s', %d)", 
            idClienteGenerado, nroDoc, direccion, telefono, correo, id_tipoDoc
        );

        // 3. INSERTAR en PERSONA o EMPRESA
        String sqlEspecifica = "";
        
        if (razonSocial == null || razonSocial.isEmpty()) { 
            // Es PERSONA
            sqlEspecifica = String.format(
                "INSERT INTO PERSONA (idPersona, nombres, apellidoPaterno, apellidoMaterno, sexo, fecha_nacimiento, idCliente) " +
                "VALUES (%d, '%s', '%s', '%s', '%s', '%s', %d)",
                idClienteGenerado, // Asumiendo que idPersona = idCliente
                nombres, apellidoPaterno, apellidoMaterno, sexo, 
                fechaNacimiento != null ? fechaNacimiento.toString() : null, // Convierte Date a String (formato YYYY-MM-DD)
                idClienteGenerado
            );
        } else {
            // Es EMPRESA
            sqlEspecifica = String.format(
                "INSERT INTO EMPRESA (idEmpresa, razonSocial, idCliente) " +
                "VALUES (%d, '%s', %d)",
                idClienteGenerado, // Asumiendo que idEmpresa = idCliente
                razonSocial, 
                idClienteGenerado
            );
        }
        
        // 4. EJECUTAR TRANSACCIÓN (Ambas inserciones deben tener éxito)
        try {
            objConectar.conectar();
            objConectar.ejecutarBD(sqlCliente); // Ejecutar INSERT en CLIENTE
            objConectar.ejecutarBD(sqlEspecifica); // Ejecutar INSERT en PERSONA/EMPRESA
            return true;
        } catch (Exception e) {
            // Manejo de errores (por ejemplo, deshacer si solo falló el segundo insert)
            throw new Exception("Error al registrar el cliente: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }
    
    public boolean modificarCliente(int idCliente, String nroDoc, String direccion, String telefono, 
                                  String correo, int id_tipoDoc, String nombres, String apellidoPaterno, 
                                  String apellidoMaterno, String sexo, java.sql.Date fechaNacimiento, 
                                  String razonSocial) throws Exception {
    
    // 1. UPDATE en CLIENTE
        String sqlCliente = String.format(
            "UPDATE CLIENTE SET nroDoc = '%s', direccion = '%s', telefono = '%s', correo = '%s', id_tipoDoc = %d " +
            "WHERE idCliente = %d", 
            nroDoc, direccion, telefono, correo, id_tipoDoc, idCliente
        );

        // 2. UPDATE en PERSONA o EMPRESA
        String sqlEspecifica = "";

        if (razonSocial == null || razonSocial.isEmpty()) { 
            // Es PERSONA
            sqlEspecifica = String.format(
                "UPDATE PERSONA SET nombres = '%s', apellidoPaterno = '%s', apellidoMaterno = '%s', sexo = '%s', fecha_nacimiento = '%s' " +
                "WHERE idCliente = %d",
                nombres, apellidoPaterno, apellidoMaterno, sexo, 
                fechaNacimiento != null ? fechaNacimiento.toString() : null, // Convierte Date a String (YYYY-MM-DD)
                idCliente
            );
        } else {
            // Es EMPRESA
            sqlEspecifica = String.format(
                "UPDATE EMPRESA SET razonSocial = '%s' WHERE idCliente = %d",
                razonSocial, 
                idCliente
            );
        }

        // 3. EJECUTAR TRANSACCIÓN
        try {
            objConectar.conectar();
            objConectar.ejecutarBD(sqlCliente); // Ejecutar UPDATE en CLIENTE
            objConectar.ejecutarBD(sqlEspecifica); // Ejecutar UPDATE en PERSONA/EMPRESA
            return true;
        } catch (Exception e) {
            throw new Exception("Error al modificar el cliente: " + e.getMessage());
        } finally {
            objConectar.desconectar();
        }
    }
    
    public boolean eliminarCliente(int idCliente) throws Exception {
        String sqlCliente = String.format("UPDATE CLIENTE SET estado = FALSE WHERE idCliente = %d", idCliente);

        try {
            objConectar.conectar(); // Abrir conexión

            // Solo se actualiza el estado del cliente
            objConectar.ejecutarBD(sqlCliente);

            return true;
        } catch (Exception e) {
            throw new Exception("Error al dar de baja al cliente: " + e.getMessage());
        } finally {
            objConectar.desconectar(); // Cerrar conexión
        }
    }
    

    public clsJDBC getObjConectar() {
        return objConectar;
    }

    public void setObjConectar(clsJDBC objConectar) {
        this.objConectar = objConectar;
    }

    public String getStrSQL() {
        return strSQL;
    }

    public void setStrSQL(String strSQL) {
        this.strSQL = strSQL;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId_tipoDoc() {
        return id_tipoDoc;
    }

    public void setId_tipoDoc(int id_tipoDoc) {
        this.id_tipoDoc = id_tipoDoc;
    }

   

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    
}
