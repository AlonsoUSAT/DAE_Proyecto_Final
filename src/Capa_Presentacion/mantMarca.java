package Capa_Presentacion;
import Capa_Negocio.clsMarca;
import Capa_Negocio.clsLaboratorio; 
import javax.swing.DefaultComboBoxModel;
import java.sql.*; // Necesario para ResultSet
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class mantMarca extends javax.swing.JDialog {
    private clsMarca objMarca = new clsMarca();
    private clsLaboratorio objLaboratorio = new clsLaboratorio();
   
    public mantMarca(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        llenarComboLaboratorio(); 
        actualizarTabla();
        limpiarCampos();
    }
    
    private void llenarComboLaboratorio() {
        ResultSet rsLab = null;
        DefaultComboBoxModel modeloMar = new DefaultComboBoxModel();
        cmbLaboratorio.setModel(modeloMar);
        try {
            rsLab = objMarca.listarMarcas();
            while (rsLab.next()) {
                modeloMar.addElement(rsLab.getString("nombrelaboratorio"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar laboratorio en el combobox");
        }

    }
   
   
    private void actualizarTabla() {
        String[] titulos = {"ID", "Nombre", "Descripción", "Laboratorio", "Vigencia"};
        DefaultTableModel model = new DefaultTableModel(null, titulos);

        ResultSet rs = null;
        try {
            rs = objMarca.listarMarcas(); 

            while (rs.next()) {
                boolean estado = rs.getBoolean("estado");
                String vigencia = estado ? "Vigente" : "No Vigente";

                Object[] row = {
                    rs.getInt("idmarca"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("nombrelaboratorio"), // El SQL ya lo trae
                    vigencia
                };
                model.addRow(row);
            }
            tblMarca.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } 

    }

    private void limpiarCampos() {

        txtID.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        if (cmbLaboratorio.getItemCount() > 0) {

            cmbLaboratorio.setSelectedIndex(0); 
        }
        chkVigencia.setSelected(true);


        txtID.setEnabled(true);
        btnBuscar.setEnabled(true);


        txtNombre.setEnabled(false);
        txtDescripcion.setEnabled(false);
        cmbLaboratorio.setEnabled(false);
        chkVigencia.setEnabled(false);


        btnNuevo.setText("Nuevo");
        btnNuevo.setEnabled(true);

        btnModificar.setText("Modificar");
        btnModificar.setEnabled(false);

        btnEliminar.setEnabled(false);
        btnDardeBaja.setEnabled(false);


        tblMarca.clearSelection();
        txtID.requestFocus(); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMarca = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnDardeBaja = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        cmbLaboratorio = new javax.swing.JComboBox<>();
        btnNuevoLaboratorio = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        chkVigencia = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        tblMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMarcaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMarca);

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setName(""); // NOI18N

        btnNuevo.setBackground(new java.awt.Color(204, 224, 250));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar-usuario.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(204, 224, 250));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiar-usuario.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(204, 224, 250));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminar-usuario.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCerrar.setBackground(new java.awt.Color(204, 224, 250));
        btnCerrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cerrar-sesion (1).png"))); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(204, 224, 250));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/editar-usuario.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnDardeBaja.setBackground(new java.awt.Color(204, 224, 250));
        btnDardeBaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDardeBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBaja-usuario.png"))); // NOI18N
        btnDardeBaja.setText("Dar de Baja");
        btnDardeBaja.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDardeBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDardeBajaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(btnDardeBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDardeBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Marca", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("ID:");

        btnBuscar.setBackground(new java.awt.Color(204, 224, 250));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar-usuario.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel1.setText("Descripción:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("Laboratorio:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        btnNuevoLaboratorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        btnNuevoLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoLaboratorioActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setText("Vigencia:");

        chkVigencia.setText("(Vigente)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbLaboratorio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(txtNombre)
                            .addComponent(txtID, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscar)
                            .addComponent(btnNuevoLaboratorio))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chkVigencia)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar))
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnNuevoLaboratorio))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(chkVigencia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tblMarca.getSelectedRow();
    
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una marca de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Seguro desea eliminar la marca seleccionada?", 
            "Confirmar Eliminación Física", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE 
        );
    
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int idMarca = (int) tblMarca.getValueAt(filaSeleccionada, 0);
                objMarca.eliminarMarca(idMarca);
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Marca eliminada permanentemente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }    
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
      
    
        try {
            if (btnNuevo.getText().equals("Nuevo")) {
                btnNuevo.setText("Guardar");
                limpiarCampos();
            }else{
                if (txtNombre.getText().trim().isEmpty() || cmbLaboratorio.getSelectedIndex() == -1) {
                  JOptionPane.showMessageDialog(this, "El Nombre y el Laboratorio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                  return;
                }else{
                        objMarca.registrarMarca(Integer.valueOf(txtID.getText()), 
                                        txtNombre.getText().trim(), 
                                        txtDescripcion.getText(), 
                                        objLaboratorio.obtenerCodigoLaboratorio(cmbLaboratorio.getSelectedItem().toString()),
                                        chkVigencia.isSelected());
                
                    JOptionPane.showMessageDialog(this, "Marca registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);      
                }
                actualizarTabla();
                limpiarCampos();
                
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnNuevoLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoLaboratorioActionPerformed
        mantLaboratorio formLab = new mantLaboratorio(null, true); 
        formLab.setVisible(true);
        llenarComboLaboratorio();       
    }//GEN-LAST:event_btnNuevoLaboratorioActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        if (txtNombre.getText().trim().isEmpty() || cmbLaboratorio.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "El Nombre y el Laboratorio son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        try {
            if (btnModificar.getText().equals("Guardar")) {
                objMarca.registrarMarca(Integer.valueOf(txtID.getText()), 
                                        txtNombre.getText().trim(), 
                                        txtDescripcion.getText(), 
                                        objLaboratorio.obtenerCodigoLaboratorio(cmbLaboratorio.getSelectedItem().toString()),
                                        chkVigencia.isSelected());
                
                JOptionPane.showMessageDialog(this, "Marca registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } else {
                objMarca.modificarMarca(Integer.valueOf(txtID.getText()), 
                                        txtNombre.getText().trim(), 
                                        txtDescripcion.getText(), 
                                        objLaboratorio.obtenerCodigoLaboratorio(cmbLaboratorio.getSelectedItem().toString()),
                                        chkVigencia.isSelected());
                JOptionPane.showMessageDialog(this, "Marca modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            actualizarTabla();
            limpiarCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tblMarcaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMarcaMouseClicked
        int fila = tblMarca.getSelectedRow();
        if (fila != -1) {
            int idMarca = (int) tblMarca.getValueAt(fila, 0);

            ResultSet rs = null;
            try {
                rs = objMarca.buscarMarca(idMarca);
                if (rs != null && rs.next()) {
                    txtID.setText(String.valueOf(rs.getInt("idmarca")));
                    txtNombre.setText(rs.getString("nombre"));
                    txtDescripcion.setText(rs.getString("descripcion"));
                    chkVigencia.setSelected(rs.getBoolean("estado"));

                    String nombreLabBuscado = rs.getString("nombrelaboratorio");

                    cmbLaboratorio.setSelectedItem(nombreLabBuscado);

                    txtID.setEnabled(false); 
                    txtNombre.setEnabled(true);
                    txtDescripcion.setEnabled(true);
                    cmbLaboratorio.setEnabled(true);
                    chkVigencia.setEnabled(true);
                    btnNuevo.setEnabled(false); 
                    btnModificar.setText("Modificar"); 
                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    btnDardeBaja.setEnabled(true);

                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos para la marca seleccionada.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar marca: " + e.getMessage());
            }
            // ADVERTENCIA: Fuga de recursos. rs no se cierra.
        }
    }//GEN-LAST:event_tblMarcaMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnDardeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDardeBajaActionPerformed
        int filaSeleccionada = tblMarca.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una marca de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String estadoActual = (String) tblMarca.getValueAt(filaSeleccionada, 4);
        boolean estaActivo = estadoActual.equals("Vigente");
        String accion = estaActivo ? "dar de baja" : "reactivar";

        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de que desea " + accion + " esta marca?", 
            "Confirmar Acción", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int idMarca = (int) tblMarca.getValueAt(filaSeleccionada, 0);
                
                // Llamamos al método de servicio correspondiente
                if (estaActivo) {
                    objMarca.darBajaMarca(idMarca);
                } else {
                    objMarca.reactivarMarca(idMarca);
                }
                
                actualizarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Marca actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al " + accion + " la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDardeBajaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
     String textoBusqueda = txtID.getText().trim();
     if (textoBusqueda.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
         txtID.requestFocus();
         return;
     }

     ResultSet rs = null; 
     try {
         int id = Integer.parseInt(textoBusqueda);
         rs = objMarca.buscarMarca(id);

         if (rs != null && rs.next()) {
             txtID.setText(String.valueOf(rs.getInt("idMarca")));
             txtNombre.setText(rs.getString("nombre"));
             txtDescripcion.setText(rs.getString("descripcion"));
             chkVigencia.setSelected(rs.getBoolean("estado"));

             String nombreLabBuscado = rs.getString("nombrelaboratorio");

             cmbLaboratorio.setSelectedItem(nombreLabBuscado);

             txtID.setEnabled(false); 
             txtNombre.setEnabled(true);
             txtDescripcion.setEnabled(true);
             cmbLaboratorio.setEnabled(true);
             chkVigencia.setEnabled(true);
             btnNuevo.setEnabled(false); 
             btnModificar.setText("Modificar"); 
             btnModificar.setEnabled(true);
             btnEliminar.setEnabled(true);
             btnDardeBaja.setEnabled(true);

         } else {
             JOptionPane.showMessageDialog(this, "No se encontró ninguna marca con el ID: " + id, "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
         }
     } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
     } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Ocurrió un error al buscar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
     }
    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDardeBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnNuevoLaboratorio;
    private javax.swing.JCheckBox chkVigencia;
    private javax.swing.JComboBox<clsLaboratorio> cmbLaboratorio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblMarca;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
