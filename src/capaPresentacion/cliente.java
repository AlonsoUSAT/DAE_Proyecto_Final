/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package capaPresentacion;

import capaNegocio.Cliente;
import capaNegocio.TipoDocumento;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nicole
 */
public class cliente extends javax.swing.JDialog {
    Cliente objCliente = new Cliente();

      private Cliente clienteActual = null; // Guarda el cliente encontrado para Modificar/Dar Baja
    private boolean esModoNuevo = false; 
    
    public cliente(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         if (grupoCliente != null) { 
            grupoCliente.clearSelection();
        }
        // 2. Establecer el estado visual inicial
        cargarSexos();
        
        listarClientesEnTabla();

    }
    
  // Método que se llama al iniciar o al hacer clic en un botón de "Listar/Actualizar"
    public void listarClientesEnTabla() {
        // Define el modelo de la tabla (las columnas)
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nro. Doc", "Tipo Doc", "Nombres / Razón Social", "Teléfono", "Correo"}, 0);
        
        try {
            // 1. Obtener el ResultSet
            ResultSet rs = objCliente.listarClientes();
            
            // 2. Iterar sobre el ResultSet y llenar el modelo
            while (rs.next()) {
                // Obtener los datos del cliente
                int id = rs.getInt("idCliente");
                String nroDoc = rs.getString("nroDoc");
                String tipoDoc = rs.getString("nom_tipoDoc");
                String nombreCompleto = rs.getString("nombre_completo_o_razon_social");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                
                // Agregar una fila al modelo
                modelo.addRow(new Object[]{id, nroDoc, tipoDoc, nombreCompleto, telefono, correo});
            }
            
            // 3. Asignar el modelo a tu JTable
            tblClientes.setModel(modelo);
            
            // Opcional: Ajustar el ancho de las columnas, si lo requiere el profesor.
            
        } catch (Exception e) {
            // Muestra un mensaje de error si algo falla en la conexión o consulta
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error al cargar la lista de clientes: " + e.getMessage(), 
                "Error de Base de Datos", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
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
 
    private void limpiarFormulario() {
        txtNumeroDocumento.setText("");
        txtNombres.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtRazonSocial.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtFechaNacimiento.setText("");
        
        cboSexo.setSelectedIndex(0);
        
        rbPersona.setSelected(true);        
        //int clienteSeleccionadoId = -1;
        tblClientes.clearSelection();
    }
     
    public void restablecerModoInicial() {
        limpiarFormulario(); // Limpiamos los datos de entrada

        // Restablecer el campo ID
        txtCodigo.setText(""); 
        txtCodigo.setEnabled(false); // El ID nunca debe ser editable

        // Restablecer el estado de los botones (Modo Búsqueda/Inactivo)
        esModoNuevo = false;
        btnGuardar.setText("Nuevo");
        btnGuardar.setEnabled(true); // Deshabilitar Guardar hasta que se presione "Nuevo" o "Modificar"
        btnBuscar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true); 

        // Deshabilitar campos comunes (Modo Lectura)
        
        txtNumeroDocumento.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtCorreo.setEnabled(true);
        cboTipoDocumento.setEnabled(true);
        rbPersona.setEnabled(true);
        rbEmpresa.setEnabled(true);
        

        // Forzar la actualización para deshabilitar los campos Persona/Empresa
        actualizarCamposSegunTipo(); 

        listarClientesEnTabla();
}
    
      // MÉTODO CLAVE: Habilitar/Deshabilitar campos según tipo
    private void actualizarCamposSegunTipo() {
        boolean esPersona = rbPersona.isSelected();
        String tipoCliente = esPersona ? "PERSONA" : "EMPRESA";
        
        cargarTiposDocumento(tipoCliente);
        
        // Habilitar/Deshabilitar campos PERSONA
        txtNombres.setEnabled(esPersona);
        txtApellidoPaterno.setEnabled(esPersona);
        txtApellidoMaterno.setEnabled(esPersona);
        cboSexo.setEnabled(esPersona);
        //txtFechaNacimiento.setEnabled(esPersona);
        
        // Habilitar/Deshabilitar campos EMPRESA
        txtRazonSocial.setEnabled(!esPersona);
        
        // Cambiar color de fondo para indicar visualmente
        Color colorDeshabilitado = new Color(204,224,250);
        Color colorHabilitado = Color.WHITE;
        
        txtNombres.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoPaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtApellidoMaterno.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
        txtFechaNacimiento.setBackground(esPersona ? colorHabilitado : colorDeshabilitado);
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
        txtNumeroDocumento = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtFechaNacimiento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cboSexo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtApellidoMaterno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtApellidoPaterno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jdFechaNacimiento = new com.toedter.calendar.JDateChooser();
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
        txtNumeroDocumento1 = new javax.swing.JTextField();
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
                .addContainerGap(539, Short.MAX_VALUE))
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtApellidoPaterno, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombres, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSexo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtApellidoMaterno, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jdFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(61, Short.MAX_VALUE))))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, 0)
                        .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8)
                        .addGap(0, 0, 0)
                        .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar-usuario.png"))); // NOI18N
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
                        .addComponent(txtNumeroDocumento1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDocumento1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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
                                .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addGap(0, 19, Short.MAX_VALUE))
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
                        .addComponent(txtNumeroDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    
        if (btnGuardar.getText().equals("Nuevo")) {
        // --- FLUJO 1: INICIAR NUEVO REGISTRO ---

        try {
            esModoNuevo = true; // Establece el modo a INSERCIÓN

            int codigoGenerado = objCliente.generarCodigoCliente();
            txtCodigo.setText(String.valueOf(codigoGenerado));
            txtCodigo.setEnabled(false); // ID no editable

            limpiarFormulario(); 

            txtNumeroDocumento.setEnabled(true);
            txtDireccion.setEnabled(true);
            txtTelefono.setEnabled(true);
            txtCorreo.setEnabled(true);
            cboTipoDocumento.setEnabled(true);
            rbPersona.setEnabled(true);
            rbEmpresa.setEnabled(true);

            btnGuardar.setText("Guardar");
            btnBuscar.setEnabled(false);

            rbPersona.setSelected(true); 
            actualizarCamposSegunTipo(); 
            txtNumeroDocumento.requestFocus();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al generar código: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    } else if (btnGuardar.getText().equals("Guardar")) {
        // --- FLUJO 2: EJECUTAR GUARDAR (INSERTAR O MODIFICAR) ---

        int idCliente = esModoNuevo ? 0 : Integer.parseInt(txtCodigo.getText()); 
        String nroDoc = txtNumeroDocumento.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        TipoDocumento tipoSeleccionado = (TipoDocumento) cboTipoDocumento.getSelectedItem();
        int id_tipoDoc = tipoSeleccionado.getId();

        String nombres = null, apellidoPaterno = null, apellidoMaterno = null, razonSocial = null;
        String sexo = null;
        java.util.Date fechaNacimiento = null; 
        boolean esPersona = rbPersona.isSelected();

        // 🔹 Validaciones generales
        if (nroDoc.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar el número de documento.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔹 Validar longitud según tipo de documento
        switch (id_tipoDoc) {
            case 1: // DNI
                if (nroDoc.length() != 8) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                break;
            case 3: // Carné de Extranjería
                if (nroDoc.length() < 9 || nroDoc.length() > 12) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El Carné de Extranjería debe tener entre 9 y 12 caracteres.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                break;
            case 4: // Pasaporte
                if (nroDoc.length() < 6 || nroDoc.length() > 12) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El Pasaporte debe tener entre 6 y 12 caracteres.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                break; 
            case 2: // RUC
                if (nroDoc.length() != 11) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El RUC debe tener exactamente 11 dígitos.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                break;
        }

        if (esPersona) {
            nombres = txtNombres.getText().trim();
            apellidoPaterno = txtApellidoPaterno.getText().trim();
            apellidoMaterno = txtApellidoMaterno.getText().trim();
            sexo = cboSexo.getSelectedItem().toString().substring(0, 1); 

            // 🔹 Validar nombres y apellidos
            if (nombres.isEmpty() || apellidoPaterno.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar los Nombres y Apellido Paterno para Personas.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 🔹 Validar fecha de nacimiento
            try {
                String fechaTexto = txtFechaNacimiento.getText().trim();
                if (fechaTexto.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ingrese su fecha de nacimiento.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy");
                fechaNacimiento = format.parse(fechaTexto);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Formato de Fecha de Nacimiento inválido (debe ser dd/MM/yyyy).", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

        } else {
            razonSocial = txtRazonSocial.getText().trim();

            // 🔹 Validar Razón Social obligatoria si es Empresa
            if (razonSocial.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar la Razón Social para Empresas.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            boolean exito = false;
            java.sql.Date sqlFechaNacimiento = null;
            if (fechaNacimiento != null) {
                sqlFechaNacimiento = new java.sql.Date(fechaNacimiento.getTime());
            }

            if (esModoNuevo) {
                exito = objCliente.registrarCliente(
                    nroDoc, direccion, telefono, correo, id_tipoDoc,
                    nombres, apellidoPaterno, apellidoMaterno, sexo, 
                    sqlFechaNacimiento, razonSocial
                ); 
            } else {
                exito = objCliente.modificarCliente(
                    idCliente, nroDoc, direccion, telefono, correo, id_tipoDoc,
                    nombres, apellidoPaterno, apellidoMaterno, sexo, 
                    sqlFechaNacimiento, razonSocial
                );
            }

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this, "Operación completada exitosamente.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de base de datos al guardar: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        txtNumeroDocumento.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtCorreo.setEnabled(false);
        actualizarCamposSegunTipo(); 

        btnGuardar.setText("Nuevo");
        btnBuscar.setEnabled(true);
        btnModificar.setEnabled(false);
        limpiarFormulario();
        listarClientesEnTabla();
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
            // 1. Validar que estemos listos para modificar
    if (clienteActual == null || esModoNuevo) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Debe buscar un cliente existente y estar en modo Modificar.", 
            "Advertencia", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    // 2. HABILITAR todos los campos para la edición
    txtNumeroDocumento.setEnabled(true);
    txtDireccion.setEnabled(true);
    txtTelefono.setEnabled(true);
    txtCorreo.setEnabled(true);
    cboTipoDocumento.setEnabled(true);
    rbPersona.setEnabled(true);
    rbEmpresa.setEnabled(true);
    
    // Habilitar campos específicos (llama a tu método)
    actualizarCamposSegunTipo();

    // 3. AJUSTAR BOTONES: Entrar en modo edición/Guardar
    btnGuardar.setText("Guardar");
    btnGuardar.setEnabled(true); // Habilitar Guardar para ejecutar la modificación
    
    btnModificar.setEnabled(false); // Deshabilitar Modificar mientras editamos
    btnBuscar.setEnabled(false); // Deshabilitar Buscar mientras editamos
    //btnDarBaja.setEnabled(false); // Deshabilitar Dar Baja mientras editamos
    
    txtNumeroDocumento.requestFocus();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        if (txtCodigo.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente de la tabla primero.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idClienteAEliminar = Integer.parseInt(txtCodigo.getText());

        // 2. Mensaje de Confirmación
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar al Cliente con ID: " + idClienteAEliminar + "?\nEsta acción es irreversible.", 
            "Confirmar Eliminación", 
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            try {
                // 3. Ejecutar la Eliminación
                // Nota: Se asume que objCliente.eliminarCliente(id) existe en tu capa de negocio.
                boolean exito = objCliente.eliminarCliente(idClienteAEliminar); 

                if (exito) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    // 4. Resetear la Interfaz completamente
                    restablecerModoInicial(); // <-- Llama al método que limpia todo (ID, campos, botones y recarga tabla)
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error de base de datos al eliminar: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String nroDocBusqueda = txtNumeroDocumento1.getText().trim();  
    
    if (nroDocBusqueda.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el Nro. de Documento a buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        clienteActual = objCliente.buscarCliente(nroDocBusqueda);
        
        if (clienteActual != null) {
            // --- CLIENTE ENCONTRADO (MODO LECTURA/LISTO PARA MODIFICAR) ---
            JOptionPane.showMessageDialog(this, "Cliente encontrado. Presione Modificar para editar.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            esModoNuevo = false; // El modo es Modificar
            
            // 3. Llenar campos comunes
            txtCodigo.setText(String.valueOf(clienteActual.getIdCliente()));
            txtCodigo.setEnabled(false); 
            txtNumeroDocumento.setText(clienteActual.getNroDoc());
            txtDireccion.setText(clienteActual.getDireccion());
            txtTelefono.setText(clienteActual.getTelefono());
            txtCorreo.setText(clienteActual.getCorreo());
            
            // 4. DESHABILITAR CAMPOS (Modo Lectura)
            txtNumeroDocumento.setEnabled(false);
            txtDireccion.setEnabled(false);
            txtTelefono.setEnabled(false);
            txtCorreo.setEnabled(false);
            cboTipoDocumento.setEnabled(false);
            rbPersona.setEnabled(false);
            rbEmpresa.setEnabled(false);
            
            // 5. Lógica de Persona/Empresa y llenado
            seleccionarTipoDocumento(clienteActual.getId_tipoDoc());
            
            if (clienteActual.getRazonSocial() != null && !clienteActual.getRazonSocial().isEmpty()) {
                rbEmpresa.setSelected(true);
                txtRazonSocial.setText(clienteActual.getRazonSocial());
            } else {
                rbPersona.setSelected(true);
                txtNombres.setText(clienteActual.getNombres());
                txtApellidoPaterno.setText(clienteActual.getApellidoPaterno());
                txtApellidoMaterno.setText(clienteActual.getApellidoMaterno());
            }
            
            // 6. AJUSTAR BOTONES para el Modo Lectura
            actualizarCamposSegunTipo(); // Deshabilita los campos Persona/Empresa
            
            btnGuardar.setText("Nuevo"); // Aseguramos que el Guardar esté en modo "Nuevo" inactivo
            btnGuardar.setEnabled(false); 
            btnBuscar.setEnabled(true); // Se puede buscar otro sin modificar este
            btnModificar.setEnabled(true); // HABILITAR MODIFICAR
            btnEliminar.setEnabled(true); // HABILITAR DAR BAJA
            
        } else {
            // --- CLIENTE NO ENCONTRADO (Llamar a Nuevo) ---
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Iniciando nuevo registro.", "Información", JOptionPane.INFORMATION_MESSAGE);
            
            String tempNroDoc = txtNumeroDocumento.getText();
            limpiarFormulario();
            txtNumeroDocumento.setText(tempNroDoc);

            // Llama a la acción del botón Guardar (que está en modo "Nuevo")
            btnGuardarActionPerformed(null); 
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error en la búsqueda: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // TODO add your handling code here:
         int fila = tblClientes.getSelectedRow();
    
        if (fila < 0) {
            return;
        }

        // Obtener el ID del cliente de la columna "ID" (columna 0)
        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        int idClienteSeleccionado = (int) modelo.getValueAt(fila, 0); 

        // Cargar el ID en el campo de texto (aunque esté deshabilitado)
        txtCodigo.setText(String.valueOf(idClienteSeleccionado));

        // Habilitar los botones de acción para este cliente
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true); // Asumimos que tienes un btnEliminar

        // Deshabilitar el botón "Guardar" mientras se trabaja sobre un cliente existente
        btnGuardar.setEnabled(false); 
        btnGuardar.setText("Guardar"); // Esto evita que diga "Nuevo" si está habilitado

        // Si quieres que los campos se carguen para Modificar, aquí deberías llamar a:
        // clienteActual = objCliente.buscarCliente(idClienteSeleccionado);
        // mostrarCliente(clienteActual);
    }//GEN-LAST:event_tblClientesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cliente dialog = new cliente(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

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
    private com.toedter.calendar.JDateChooser jdFechaNacimiento;
    private javax.swing.JRadioButton rbEmpresa;
    private javax.swing.JRadioButton rbPersona;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtApellidoMaterno;
    private javax.swing.JTextField txtApellidoPaterno;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFechaNacimiento;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtNumeroDocumento;
    private javax.swing.JTextField txtNumeroDocumento1;
    private javax.swing.JTextField txtRazonSocial;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
