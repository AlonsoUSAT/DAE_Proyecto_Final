package Capa_Negocio;

import Capa_Datos.clsJDBC;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement; // Necesario importar
import java.sql.SQLException;      // Necesario importar

/**
 *
 * @author Nicole
 */
public class Cliente {

    clsJDBC objConectar = new clsJDBC();
    String strSQL;
    ResultSet rs = null;

    // ATRIBUTOS DE LA ENTIDAD CLIENTE
    private int idCliente;
    private String nroDoc;
    private String direccion;
    private String telefono;
    private String correo;
    private int id_tipoDoc;

    // ATRIBUTOS EXTRAS PARA INTERFAZ
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String razonSocial;
    private String sexo; // Para persona
    private Date fechaNacimiento; // Para persona

    // CONSTRUCTOR VACÍO
    public Cliente() {
    }

    // GETTERS Y SETTERS
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNroDoc() { return nroDoc; }
    public void setNroDoc(String nroDoc) { this.nroDoc = nroDoc; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getId_tipoDoc() { return id_tipoDoc; }
    public void setId_tipoDoc(int id_tipoDoc) { this.id_tipoDoc = id_tipoDoc; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    // MÉTODO PARA LISTAR TIPOS DE DOCUMENTOS
    public ResultSet listarTiposClientes() throws Exception {
        this.strSQL = "SELECT id_tipodoc, nom_tipodoc FROM tipo_documento ORDER BY id_tipodoc";
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar(); 
            this.rs = objConectar.consultarBD(strSQL);
            return this.rs;
        } catch (Exception e) {
            throw new Exception("Error al listar tipos de clientes (documentos): " + e.getMessage());
        }
    }

    // MÉTODO FILTRADO POR TIPO (PERSONA O EMPRESA)
    public ResultSet listarTipoDocumentosFiltrado(String tipoCliente) throws Exception {
        if (tipoCliente.equals("PERSONA")) {
            strSQL = "SELECT id_tipodoc, nom_tipodoc FROM tipo_documento WHERE nom_tipodoc IN ('DNI', 'Carné de Extranjería', 'Pasaporte')";
        } else if (tipoCliente.equals("EMPRESA")) {
            strSQL = "SELECT id_tipodoc, nom_tipodoc FROM tipo_documento WHERE nom_tipodoc = 'RUC'";
        } else {
            strSQL = "SELECT id_tipodoc, nom_tipodoc FROM tipo_documento WHERE 1=0";
        }
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar();
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar tipos de documento filtrado: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA INSERTAR UN CLIENTE
    public boolean insertarCliente(boolean esPersona) throws Exception {
        Connection localCon = null;
        try {
            // ✅ CORRECCIÓN: Abrir conexión explícitamente
            objConectar.conectar();
            localCon = objConectar.getCon();
            localCon.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar en tabla CLIENTE
            strSQL = "INSERT INTO cliente (nrodoc, direccion, telefono, correo, id_tipodoc, estado) "
                    + "VALUES (?, ?, ?, ?, ?, TRUE) RETURNING idcliente";

            java.sql.PreparedStatement ps = localCon.prepareStatement(strSQL);
            ps.setString(1, this.nroDoc);
            ps.setString(2, this.direccion != null ? this.direccion : "");
            ps.setString(3, this.telefono != null ? this.telefono : "");
            ps.setString(4, this.correo != null ? this.correo : "");
            ps.setInt(5, this.id_tipoDoc);

            ResultSet rsId = ps.executeQuery();
            int idGenerado = -1;
            if (rsId.next()) {
                idGenerado = rsId.getInt(1);
            }
            this.idCliente = idGenerado; // Guardamos el ID generado

            // 2. Insertar en PERSONA o EMPRESA según el tipo
            if (esPersona) {
                strSQL = "INSERT INTO persona (idpersona, nombres, apellidopaterno, apellidomaterno, sexo, fecha_nacimiento, idcliente) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = localCon.prepareStatement(strSQL);
                ps.setInt(1, idGenerado); // idpersona = idcliente
                ps.setString(2, this.nombres);
                ps.setString(3, this.apellidoPaterno);
                ps.setString(4, this.apellidoMaterno != null ? this.apellidoMaterno : "");
                ps.setString(5, this.sexo != null ? this.sexo.substring(0, 1) : "M");
                if (this.fechaNacimiento != null) {
                    ps.setDate(6, new java.sql.Date(this.fechaNacimiento.getTime()));
                } else {
                    ps.setNull(6, java.sql.Types.DATE);
                }
                ps.setInt(7, idGenerado); // idcliente
                ps.executeUpdate();

            } else { // Es Empresa
                strSQL = "INSERT INTO empresa (idempresa, razonsocial, idcliente) "
                        + "VALUES (?, ?, ?)";
                ps = localCon.prepareStatement(strSQL);
                ps.setInt(1, idGenerado); 
                ps.setString(2, this.razonSocial);
                ps.setInt(3, idGenerado);
                ps.executeUpdate();
            }

            localCon.commit(); // Confirmar transacción
            return true;

        } catch (Exception e) {
            if (localCon != null) {
                localCon.rollback(); // Revertir si hay error
            }
            throw new Exception("Error al insertar cliente: " + e.getMessage());
        } finally {
            if (localCon != null) {
                localCon.setAutoCommit(true); // Restaurar auto-commit
                // ✅ CORRECCIÓN: NO cerramos la conexión aquí para permitir que la interfaz siga usándola
                // objConectar.desconectar(); 
            }
        }
    }

    // ✅ MÉTODO PARA ACTUALIZAR UN CLIENTE
    public boolean actualizarCliente(boolean esPersona) throws Exception {
        Connection localCon = null;
        try {
            // ✅ CORRECCIÓN: Abrir conexión
            objConectar.conectar();
            localCon = objConectar.getCon();
            localCon.setAutoCommit(false);

            // 1. Actualizar tabla CLIENTE
            strSQL = "UPDATE cliente SET nrodoc=?, direccion=?, telefono=?, correo=?, id_tipodoc=? WHERE idcliente=?";
            java.sql.PreparedStatement ps = localCon.prepareStatement(strSQL);
            ps.setString(1, this.nroDoc);
            ps.setString(2, this.direccion != null ? this.direccion : "");
            ps.setString(3, this.telefono != null ? this.telefono : "");
            ps.setString(4, this.correo != null ? this.correo : "");
            ps.setInt(5, this.id_tipoDoc);
            ps.setInt(6, this.idCliente);
            ps.executeUpdate();

            // 2. Actualizar PERSONA o EMPRESA
            if (esPersona) {
                strSQL = "UPDATE persona SET nombres=?, apellidopaterno=?, apellidomaterno=?, sexo=?, fecha_nacimiento=? WHERE idcliente=?";
                ps = localCon.prepareStatement(strSQL);
                ps.setString(1, this.nombres);
                ps.setString(2, this.apellidoPaterno);
                ps.setString(3, this.apellidoMaterno != null ? this.apellidoMaterno : "");
                ps.setString(4, this.sexo != null ? this.sexo.substring(0, 1) : "M");
                if (this.fechaNacimiento != null) {
                    ps.setDate(5, new java.sql.Date(this.fechaNacimiento.getTime()));
                } else {
                    ps.setNull(5, java.sql.Types.DATE);
                }
                ps.setInt(6, this.idCliente);
                ps.executeUpdate();

            } else { // Empresa
                strSQL = "UPDATE empresa SET razonsocial=? WHERE idcliente=?";
                ps = localCon.prepareStatement(strSQL);
                ps.setString(1, this.razonSocial);
                ps.setInt(2, this.idCliente);
                ps.executeUpdate();
            }

            localCon.commit();
            return true;

        } catch (Exception e) {
            if (localCon != null) {
                localCon.rollback();
            }
            throw new Exception("Error al actualizar cliente: " + e.getMessage());
        } finally {
            if (localCon != null) {
                localCon.setAutoCommit(true);
                // ✅ CORRECCIÓN: No desconectar
                // objConectar.desconectar();
            }
        }
    }

    // ✅ MÉTODO PARA ELIMINAR UN CLIENTE
    public boolean eliminarCliente() throws Exception {
        Connection localCon = null;
        try {
            // ✅ CORRECCIÓN: Abrir conexión
            objConectar.conectar();
            localCon = objConectar.getCon();
            localCon.setAutoCommit(false);

            // Primero, verificar si existe en persona o empresa
            String tipo = obtenerTipoCliente(this.idCliente);
            if (tipo == null) {
                throw new Exception("Cliente no encontrado.");
            }

            // Eliminar de la tabla secundaria
            if ("PERSONA".equals(tipo)) {
                strSQL = "DELETE FROM persona WHERE idcliente = ?";
            } else {
                strSQL = "DELETE FROM empresa WHERE idcliente = ?";
            }
            java.sql.PreparedStatement ps = localCon.prepareStatement(strSQL);
            ps.setInt(1, this.idCliente);
            ps.executeUpdate();

            // Luego, eliminar de la tabla cliente
            strSQL = "DELETE FROM cliente WHERE idcliente = ?";
            ps = localCon.prepareStatement(strSQL);
            ps.setInt(1, this.idCliente);
            ps.executeUpdate();

            localCon.commit();
            return true;

        } catch (Exception e) {
            if (localCon != null) {
                localCon.rollback();
            }
            throw new Exception("Error al eliminar cliente: " + e.getMessage());
        } finally {
            if (localCon != null) {
                localCon.setAutoCommit(true);
                // ✅ CORRECCIÓN: No desconectar
                // objConectar.desconectar();
            }
        }
    }

    // ✅ MÉTODO AUXILIAR: Obtener tipo de cliente
    private String obtenerTipoCliente(int idCliente) throws Exception {
        strSQL = "SELECT CASE WHEN p.idcliente IS NOT NULL THEN 'PERSONA' ELSE 'EMPRESA' END AS tipo "
                + "FROM cliente c LEFT JOIN persona p ON c.idcliente = p.idcliente "
                + "LEFT JOIN empresa e ON c.idcliente = e.idcliente WHERE c.idcliente = ?";
        try {
            // Asegurar conexión (puede llamarse internamente)
            if(objConectar.getCon() == null || objConectar.getCon().isClosed()) {
                objConectar.conectar();
            }
            java.sql.PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tipo");
            }
            return null;
        } catch (Exception e) {
            throw new Exception("Error al obtener tipo de cliente: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA BUSCAR CLIENTE POR NRO DOC
    public ResultSet buscarClientePorNroDoc(String nroDoc) throws Exception {
        strSQL = "SELECT c.*, p.nombres, p.apellidopaterno, p.apellidomaterno, p.sexo, p.fecha_nacimiento, "
                + "e.razonsocial "
                + "FROM cliente c "
                + "LEFT JOIN persona p ON c.idcliente = p.idcliente "
                + "LEFT JOIN empresa e ON c.idcliente = e.idcliente "
                + "WHERE c.nrodoc = ?";
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar();
            java.sql.PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setString(1, nroDoc);
            return ps.executeQuery();
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente por número de documento: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA CARGAR TODOS LOS CLIENTES
    public ResultSet listarTodosClientes() throws Exception {
        strSQL = "SELECT c.idcliente, c.nrodoc, c.direccion, c.telefono, c.correo, td.nom_tipodoc, "
                + "CASE WHEN p.idcliente IS NOT NULL THEN 'PERSONA' ELSE 'EMPRESA' END AS tipo_cliente, "
                + "COALESCE(p.nombres || ' ' || p.apellidopaterno, e.razonsocial) AS nombre_completo "
                + "FROM cliente c "
                + "JOIN tipo_documento td ON c.id_tipodoc = td.id_tipodoc "
                + "LEFT JOIN persona p ON c.idcliente = p.idcliente "
                + "LEFT JOIN empresa e ON c.idcliente = e.idcliente "
                + "ORDER BY c.idcliente DESC";
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar();
            return objConectar.consultarBD(strSQL);
        } catch (Exception e) {
            throw new Exception("Error al listar clientes: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA BUSCAR CLIENTE POR ID
    public ResultSet buscarClientePorId(int idCliente) throws Exception {
        strSQL = "SELECT c.*, p.nombres, p.apellidopaterno, p.apellidomaterno, p.sexo, p.fecha_nacimiento, "
                + "e.razonsocial "
                + "FROM cliente c "
                + "LEFT JOIN persona p ON c.idcliente = p.idcliente "
                + "LEFT JOIN empresa e ON c.idcliente = e.idcliente "
                + "WHERE c.idcliente = ?";
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar();
            java.sql.PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setInt(1, idCliente);
            return ps.executeQuery();
        } catch (Exception e) {
            throw new Exception("Error al buscar cliente por ID: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA VERIFICAR SI EL NÚMERO DE DOCUMENTO YA EXISTE
    // --- AQUÍ ESTABA EL ERROR ORIGINAL ---
    public boolean existeNroDoc(String nroDoc, int idClienteActual) throws Exception {
        strSQL = "SELECT COUNT(*) FROM cliente WHERE nrodoc = ? AND idcliente != ?";
        try {
            // ✅ CORRECCIÓN: ¡Abrir la conexión explícitamente!
            objConectar.conectar(); 
            
            java.sql.PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setString(1, nroDoc);
            ps.setInt(2, idClienteActual);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("Error al verificar número de documento: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA HABILITAR/DeshabilitAR CLIENTE
    public boolean cambiarEstadoCliente(int idCliente, boolean nuevoEstado) throws Exception {
        strSQL = "UPDATE cliente SET estado = ? WHERE idcliente = ?";
        try {
            // ✅ CORRECCIÓN: Asegurar conexión
            objConectar.conectar();
            java.sql.PreparedStatement ps = objConectar.getCon().prepareStatement(strSQL);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idCliente);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (Exception e) {
            throw new Exception("Error al cambiar estado del cliente: " + e.getMessage());
        }
    }
}