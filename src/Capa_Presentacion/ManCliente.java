/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;
/*/
import capaNegocio.Cliente;
import capaNegocio.TipoDocumento;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
*/



import Capa_Negocio.Cliente;
import Capa_Negocio.TipoDocumento;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;

import java.awt.Color;

/**
 *
 * @author Nicole
 */
public class ManCliente extends javax.swing.JDialog {
    Cliente objCliente = new Cliente();

    public ManCliente(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         if (grupoCliente != null) { 
            grupoCliente.clearSelection();
        }
        // 2. Establecer el estado visual inicial
        cargarSexos();
        cargarListaClientes();
        txtCodigo.setEditable(false);
        
    }
 
    private void cargarTiposDocumento(String tipoCliente) {
        try {
            cboTipoDocumento.removeAllItems();

            ResultSet rs = objCliente.listarTipoDocumentosFiltrado(tipoCliente); 

            while (rs.next()) {
                int id = rs.getInt("id_tipoDoc");
                String nombre = rs.getString("nom_tipoDoc");

                cboTipoDocumento.addItem(new TipoDocumento(id, nombre)); 
            }

            if (cboTipoDocumento.getItemCount() > 0) {
                cboTipoDocumento.setSelectedIndex(0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void seleccionarTipoDocumento(int idTipoDoc) {
        for (int i = 0; i < cboTipoDocumento.getItemCount(); i++) {
            TipoDocumento td = (TipoDocumento) cboTipoDocumento.getItemAt(i);
            if (td.getId() == idTipoDoc) {
                cboTipoDocumento.setSelectedIndex(i);
                break;
            }
        }
    }
 
      /* MÉTODO CLAVE: Habilitar/Deshabilitar campos según tipo
    private void actualizarCamposSegunTipo() {
        boolean esPersona = rbPersona.isSelected();
        String tipoCliente = esPersona ? "PERSONA" : "EMPRESA";
        
        cargarTiposDocumento(tipoCliente);
        
        // Habilitar/Deshabilitar campos PERSONA
        txtNombres.setEnabled(esPersona);
        txtApellidoPaterno.setEnabled(esPersona);
        txtApellidoMaterno.setEnabled(esPersona);
        cboSexo.setEnabled(esPersona);
        txtFechaNacimiento.setEnabled(esPersona);
        
        // Habilitar/Deshabilitar campos EMPRESA
        txtRazonSocial.setEnabled(!esPersona);
        
        // Cambiar color de fondo para indicar visualmente
        Color colorDeshabilitado = new Color(204,224,250);
        Color colorHabilitado = Color.WHITE;
        
        txtNombres.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoPaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoMaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtFechaNacimiento.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
    }*/
    
    private void actualizarCamposSegunTipo() {
        boolean esPersona = rbPersona.isSelected();
        String tipoCliente = esPersona ? "PERSONA" : "EMPRESA";

        // 🔥 AQUÍ ESTÁ LA CLAVE: CARGAR LOS TIPOS DE DOCUMENTO FILTRADOS
        cargarTiposDocumento(tipoCliente);

        // Habilitar/Deshabilitar campos PERSONA
        txtNombres.setEnabled(esPersona);
        txtApellidoPaterno.setEnabled(esPersona);
        txtApellidoMaterno.setEnabled(esPersona);
        cboSexo.setEnabled(esPersona);
        txtFechaNacimiento1.setDate(null); // Opcional: limpiar fecha al cambiar
        txtFechaNacimiento1.setEnabled(esPersona);

        // Habilitar/Deshabilitar campos EMPRESA
        txtRazonSocial.setEnabled(!esPersona);

        // Cambiar color de fondo para indicar visualmente
        Color colorDeshabilitado = new Color(204, 224, 250);
        Color colorHabilitado = Color.WHITE;

        txtNombres.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoPaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoMaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtFechaNacimiento1.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtRazonSocial.setBackground(!esPersona ? colorHabilitado : colorDeshabilitado);
    }
    
    private void cargarSexos() {
        // Limpiar el ComboBox antes de llenarlo
        cboSexo.removeAllItems();

        // Se añaden los valores que el usuario verá
        cboSexo.addItem("Masculino");
        cboSexo.addItem("Femenino");

        // Seleccionar el primer elemento por defecto
        cboSexo.setSelectedIndex(0);
    }
  
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNroDoc.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtNombres.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtRazonSocial.setText("");
        cboSexo.setSelectedIndex(0);
        txtFechaNacimiento1.setDate(null);
        rbPersona.setSelected(true);
        actualizarCamposSegunTipo();
    }
    
    // Método para cargar todos los clientes en la tabla
    private void cargarListaClientes() {
        DefaultTableModel modelo = new DefaultTableModel();
        // Definir las columnas
        modelo.addColumn("ID");
        modelo.addColumn("N° Doc");
        modelo.addColumn("Tipo");
        modelo.addColumn("Nombre / Razón Social");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");

        try {
            ResultSet rs = objCliente.listarTodosClientes();
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("idcliente");
                fila[1] = rs.getString("nrodoc");
                fila[2] = rs.getString("tipo_cliente"); // PERSONA o EMPRESA
                fila[3] = rs.getString("nombre_completo"); // Nombres o razón social
                fila[4] = rs.getString("telefono");
                fila[5] = rs.getString("correo");
                modelo.addRow(fila);
            }
            tblClientes.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar lista de clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoCliente = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        rbPersona = new javax.swing.JRadioButton();
        rbEmpresa = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        cboTipoDocumento = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtNroDoc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cboSexo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtApellidoMaterno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtApellidoPaterno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFechaNacimiento1 = new com.toedter.calendar.JDateChooser();
        txtTelefono = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtRazonSocial = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        txtNumDocBuscar = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Mantenimiento de Clientes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(530, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N

        jLabel2.setText("Tipo de Cliente *");

        rbPersona.setBackground(new java.awt.Color(204, 204, 255));
        grupoCliente.add(rbPersona);
        rbPersona.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbPersona.setText("Persona");
        rbPersona.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rbPersona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPersonaActionPerformed(evt);
            }
        });

        grupoCliente.add(rbEmpresa);
        rbEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbEmpresa.setText("Empresa");
        rbEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEmpresaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel1.setText("Tipo de Documento *");

        cboTipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoDocumentoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel3.setText("Número de Documento *");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel9.setText("Dirección");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel10.setText("Teléfono");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel13.setText("Correo Electronico");

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Persona", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel8.setText("Fecha de Nacimiento");

        cboSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel7.setText("Sexo *");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setText("Apellido Materno *");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText("Apellido Paterno *");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setText("Nombres *");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(65, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtFechaNacimiento1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtApellidoPaterno, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombres, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSexo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtApellidoMaterno, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addGap(0, 0, 0)
                .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtApellidoPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel6)
                .addGap(0, 0, 0)
                .addComponent(txtApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel7)
                .addGap(0, 0, 0)
                .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtFechaNacimiento1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Empresa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N
        jPanel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel18.setText("Razón Social *");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRazonSocial)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 168, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRazonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N

        btnEliminar.setBackground(new java.awt.Color(204, 224, 250));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminar-usuario.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(204, 224, 250));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/actualizacion-de-usuario.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(204, 224, 250));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar-usuario.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(204, 224, 250));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/agregar-usuario.png"))); // NOI18N
        btnGuardar.setText("Nuevo");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCerrar.setBackground(new java.awt.Color(204, 224, 250));
        btnCerrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrar-sesion (1).png"))); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(204, 224, 250));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ver-usuario.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista Clientes"));

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel14.setText("N° doc:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtNumDocBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumDocBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        txtCodigo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        jLabel12.setText("Código:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rbPersona)
                                .addGap(90, 90, 90)
                                .addComponent(rbEmpresa))
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNroDoc, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cboTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel13)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbPersona)
                            .addComponent(rbEmpresa))
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(cboTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addComponent(txtNroDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel9)
                        .addGap(10, 10, 10)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10)
                        .addGap(10, 10, 10)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
            try {
           // Validaciones básicas
           if (txtNroDoc.getText().trim().isEmpty()) {
               JOptionPane.showMessageDialog(this, "El número de documento es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }

           TipoDocumento td = (TipoDocumento) cboTipoDocumento.getSelectedItem();
           if (td == null) {
               JOptionPane.showMessageDialog(this, "Seleccione un tipo de documento.", "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }

           boolean esPersona = rbPersona.isSelected();

           // Validaciones específicas
           if (esPersona) {
               if (txtNombres.getText().trim().isEmpty() || txtApellidoPaterno.getText().trim().isEmpty()) {
                   JOptionPane.showMessageDialog(this, "Los nombres y apellido paterno son obligatorios para personas.", "Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
           } else {
               if (txtRazonSocial.getText().trim().isEmpty()) {
                   JOptionPane.showMessageDialog(this, "La razón social es obligatoria para empresas.", "Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
           }

           // Verificar duplicado
           if (objCliente.existeNroDoc(txtNroDoc.getText().trim(), 0)) {
               JOptionPane.showMessageDialog(this, "El número de documento ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }

           // Cargar datos en objeto Cliente
           objCliente.setNroDoc(txtNroDoc.getText().trim());
           objCliente.setDireccion(txtDireccion.getText().trim());
           objCliente.setTelefono(txtTelefono.getText().trim());
           objCliente.setCorreo(txtCorreo.getText().trim());
           objCliente.setId_tipoDoc(td.getId());

           if (esPersona) {
               objCliente.setNombres(txtNombres.getText().trim());
               objCliente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
               objCliente.setApellidoMaterno(txtApellidoMaterno.getText().trim());
               objCliente.setSexo((String) cboSexo.getSelectedItem());
               objCliente.setFechaNacimiento(txtFechaNacimiento1.getDate());
           } else {
               objCliente.setRazonSocial(txtRazonSocial.getText().trim());
           }

           // Insertar en BD
           if (objCliente.insertarCliente(esPersona)) {
               JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
               limpiarCampos();
               cargarListaClientes();
               // Opcional: dejarlo listo para otro nuevo
           } else {
               JOptionPane.showMessageDialog(this, "Error al registrar cliente.", "Error", JOptionPane.ERROR_MESSAGE);
           }

       } catch (Exception e) {
           JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           e.printStackTrace();
       }
    
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
         if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar un cliente para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validaciones (igual que en Nuevo)
            if (txtNroDoc.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El número de documento es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoDocumento td = (TipoDocumento) cboTipoDocumento.getSelectedItem();
            if (td == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo de documento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean esPersona = rbPersona.isSelected();

            // Validaciones específicas
            if (esPersona) {
                if (txtNombres.getText().trim().isEmpty() || txtApellidoPaterno.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Los nombres y apellido paterno son obligatorios para personas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                if (txtRazonSocial.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La razón social es obligatoria para empresas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Verificar duplicado (excluyendo el actual)
            int idActual = Integer.parseInt(txtCodigo.getText().trim());
            if (objCliente.existeNroDoc(txtNroDoc.getText().trim(), idActual)) {
                JOptionPane.showMessageDialog(this, "El número de documento ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar datos en objeto Cliente
            objCliente.setNroDoc(txtNroDoc.getText().trim());
            objCliente.setDireccion(txtDireccion.getText().trim());
            objCliente.setTelefono(txtTelefono.getText().trim());
            objCliente.setCorreo(txtCorreo.getText().trim());
            objCliente.setId_tipoDoc(td.getId());

            if (esPersona) {
                objCliente.setNombres(txtNombres.getText().trim());
                objCliente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
                objCliente.setApellidoMaterno(txtApellidoMaterno.getText().trim());
                objCliente.setSexo((String) cboSexo.getSelectedItem());
                objCliente.setFechaNacimiento(txtFechaNacimiento1.getDate());
            } else {
                objCliente.setRazonSocial(txtRazonSocial.getText().trim());
            }

            // Actualizar en BD
            objCliente.setIdCliente(idActual);
            if (objCliente.actualizarCliente(esPersona)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarListaClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Código de cliente inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
         limpiarCampos();
        // Dejarlo listo para un nuevo registro
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar un cliente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este cliente?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                objCliente.setIdCliente(Integer.parseInt(txtCodigo.getText().trim()));
                if (objCliente.eliminarCliente()) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    cargarListaClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cboTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTipoDocumentoActionPerformed

    private void rbEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEmpresaActionPerformed
        // TODO add your handling code here:
        actualizarCamposSegunTipo();
    }//GEN-LAST:event_rbEmpresaActionPerformed

    private void rbPersonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPersonaActionPerformed
        // TODO add your handling code here:
        actualizarCamposSegunTipo();
    }//GEN-LAST:event_rbPersonaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
         String nroDoc = txtNumDocBuscar.getText().trim(); 
         String nrodoc = txtNroDoc.getText().trim();
            if (nroDoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de documento para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

        try {
            ResultSet rs = objCliente.buscarClientePorNroDoc(nroDoc);
            if (rs.next()) {
                txtCodigo.setText(String.valueOf(rs.getInt("idcliente")));
                txtDireccion.setText(rs.getString("direccion"));
                txtTelefono.setText(rs.getString("telefono"));
                txtCorreo.setText(rs.getString("correo"));
                seleccionarTipoDocumento(rs.getInt("id_tipodoc"));
                txtNroDoc.setText(rs.getString("nrodoc")); 
                
                // Determinar si es persona o empresa
                if (rs.getString("nombres") != null) {
                    rbPersona.setSelected(true);
                    actualizarCamposSegunTipo();
                    txtNombres.setText(rs.getString("nombres"));
                    txtApellidoPaterno.setText(rs.getString("apellidopaterno"));
                    txtApellidoMaterno.setText(rs.getString("apellidomaterno"));
                    cboSexo.setSelectedItem(rs.getString("sexo").equals("F") ? "Femenino" : "Masculino");
                    txtFechaNacimiento1.setDate(rs.getDate("fecha_nacimiento"));
                } else {
                    rbEmpresa.setSelected(true);
                    actualizarCamposSegunTipo();
                    txtRazonSocial.setText(rs.getString("razonsocial"));
                }

                // Habilitar botones de edición
                btnModificar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnGuardar.setEnabled(false); // No se puede crear nuevo mientras editas

            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
    
        int filaSeleccionada = tblClientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Obtener el ID del cliente desde la primera columna (ID)
            int idCliente = Integer.parseInt(tblClientes.getValueAt(filaSeleccionada, 0).toString());

            try {
                ResultSet rs = objCliente.buscarClientePorId(idCliente);
                if (rs.next()) {
                    // Cargar datos en el formulario
                    txtCodigo.setText(String.valueOf(rs.getInt("idcliente")));
                    txtNroDoc.setText(rs.getString("nrodoc"));
                    txtDireccion.setText(rs.getString("direccion"));
                    txtTelefono.setText(rs.getString("telefono"));
                    txtCorreo.setText(rs.getString("correo"));
                    seleccionarTipoDocumento(rs.getInt("id_tipodoc"));

                    // Determinar si es persona o empresa
                    if (rs.getString("nombres") != null) {
                        rbPersona.setSelected(true);
                        actualizarCamposSegunTipo();
                        txtNombres.setText(rs.getString("nombres"));
                        txtApellidoPaterno.setText(rs.getString("apellidopaterno"));
                        txtApellidoMaterno.setText(rs.getString("apellidomaterno"));
                        cboSexo.setSelectedItem(rs.getString("sexo").equals("F") ? "Femenino" : "Masculino");
                        txtFechaNacimiento1.setDate(rs.getDate("fecha_nacimiento"));
                    } else {
                        rbEmpresa.setSelected(true);
                        actualizarCamposSegunTipo();
                        txtRazonSocial.setText(rs.getString("razonsocial"));
                    }

                    // Habilitar botones de edición
                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    btnGuardar.setEnabled(false);

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    

        
    }//GEN-LAST:event_tblClientesMouseClicked

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cboSexo;
    private javax.swing.JComboBox<TipoDocumento> cboTipoDocumento;
    private javax.swing.ButtonGroup grupoCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbEmpresa;
    private javax.swing.JRadioButton rbPersona;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtApellidoMaterno;
    private javax.swing.JTextField txtApellidoPaterno;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private com.toedter.calendar.JDateChooser txtFechaNacimiento1;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtNroDoc;
    private javax.swing.JTextField txtNumDocBuscar;
    private javax.swing.JTextField txtRazonSocial;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
