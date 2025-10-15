/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;

import Capa_Negocio.clsPresentacion;
import Capa_Negocio.clsTipoPresentacion;
import Capa_Negocio.clsUnidad;
import Capa_Datos.PresentacionDAO;
import Capa_Datos.PresentacionProductoDAO;
import Capa_Datos.TipoPresentacionDAO1;
import Capa_Datos.UnidadDAO;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class ManPresentacion extends javax.swing.JDialog {

    private final PresentacionDAO objPresentacion = new PresentacionDAO();
    private final TipoPresentacionDAO1 objTipoPresentacion = new TipoPresentacionDAO1();
    private final UnidadDAO objUnidad = new UnidadDAO();
    
    private final PresentacionProductoDAO objPresProdDAO = new PresentacionProductoDAO(); 
    private List<clsPresentacion> listaDePresentaciones;

    public ManPresentacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configurarComponentes();
    }

    
    private void configurarComponentes() {
        this.setTitle("Mantenimiento de Presentaciones");
        configurarSpinnerDecimal();
        configurarTabla();
        cargarDatosIniciales();
        this.setLocationRelativeTo(null);
        gestionarEstadoControles("inicio");
    }

    private void cargarDatosIniciales() {
        listarPresentaciones();
        listarTiposPresentacion();
        listarUnidades();
    }
    
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Tipo");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Unidad");
        modelo.addColumn("Estado");
        tblPresentacion.setModel(modelo);
    }
    
    private void listarPresentaciones() {
        DefaultTableModel modelo = (DefaultTableModel) tblPresentacion.getModel();
        modelo.setRowCount(0);
        try {
            this.listaDePresentaciones = objPresentacion.listarPresentaciones();
            for (clsPresentacion dto : this.listaDePresentaciones) {
                modelo.addRow(new Object[]{
                    dto.getId(),
                    dto.getNombreTipoPresentacion(),
                    dto.getCantidad(),
                    dto.getNombreUnidad(),
                    dto.isActivo() ? "Activo" : "Inactivo"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar presentaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarTiposPresentacion() {
        try {
            cmbTipoPresentacion.removeAllItems();
            List<clsTipoPresentacion> lista = objTipoPresentacion.listarTodos();
            for (clsTipoPresentacion dto : lista) {
                cmbTipoPresentacion.addItem(dto);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de presentación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarUnidades() {
        try {
            cmbUnidad.removeAllItems();
            List<clsUnidad> lista = objUnidad.listarActivas();
            for (clsUnidad dao : lista) {
                cmbUnidad.addItem(dao);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar unidades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarSpinnerDecimal() {
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spCantidad.getEditor();
        DecimalFormat format = editor.getFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(symbols);
    }

   
    
    private void cargarDatosEnFormulario(clsPresentacion presentacion) {
        if (presentacion == null) return;

        txtID.setText(String.valueOf(presentacion.getId()));
        spCantidad.setValue(presentacion.getCantidad());
        chkActivo.setSelected(presentacion.isActivo());

        String tipoNombre = presentacion.getNombreTipoPresentacion();
        for (int i = 0; i < cmbTipoPresentacion.getItemCount(); i++) {
            if (cmbTipoPresentacion.getItemAt(i).getNombre().equals(tipoNombre)) {
                cmbTipoPresentacion.setSelectedIndex(i);
                break;
            }
        }
        
        String unidadNombre = presentacion.getNombreUnidad();
        for (int i = 0; i < cmbUnidad.getItemCount(); i++) {
            if (cmbUnidad.getItemAt(i).getNombre().equals(unidadNombre)) {
                cmbUnidad.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void seleccionarFilaEnTabla(int id) {
        for (int i = 0; i < tblPresentacion.getRowCount(); i++) {
            if (Integer.parseInt(tblPresentacion.getValueAt(i, 0).toString()) == id) {
                tblPresentacion.setRowSelectionInterval(i, i);
                tblPresentacion.scrollRectToVisible(tblPresentacion.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void limpiarControles() {
        txtID.setText("");
        spCantidad.setValue(1.0f);
        chkActivo.setSelected(true);
        if (cmbTipoPresentacion.getItemCount() > 0) cmbTipoPresentacion.setSelectedIndex(0);
        if (cmbUnidad.getItemCount() > 0) cmbUnidad.setSelectedIndex(0);
        txtID.requestFocus();
        tblPresentacion.clearSelection();
    }
    
   private void gestionarEstadoControles(String modo) {
    boolean camposEditables = modo.equals("nuevo") || modo.equals("modificar");
    
    spCantidad.setEnabled(camposEditables);
    cmbTipoPresentacion.setEnabled(camposEditables);
    cmbUnidad.setEnabled(camposEditables);

    switch (modo) {
        case "inicio":
            txtID.setEnabled(true);
            btnNuevo.setEnabled(true);
            btnNuevo.setText("Nuevo");
            btnBuscar.setEnabled(true);
            btnModificar.setEnabled(false);
            btnDardeBaja.setEnabled(false);
            btnEliminar.setEnabled(false);
            chkActivo.setEnabled(false); 
            break;

        case "nuevo":
            txtID.setEnabled(false);
            btnNuevo.setEnabled(true);
            btnNuevo.setText("Guardar");
            btnBuscar.setEnabled(false);
            btnModificar.setEnabled(false);
            btnDardeBaja.setEnabled(false);
            btnEliminar.setEnabled(false);
          
            chkActivo.setSelected(true);
            chkActivo.setEnabled(false); 
            break;

        case "modificar":
            txtID.setEnabled(false);
            btnNuevo.setEnabled(false);
            btnBuscar.setEnabled(true);
            btnModificar.setEnabled(true);
            btnDardeBaja.setEnabled(true);
            btnEliminar.setEnabled(true);
          
            chkActivo.setEnabled(true); 
            break;
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        spCantidad = new javax.swing.JSpinner();
        cmbUnidad = new javax.swing.JComboBox<>();
        cmbTipoPresentacion = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        chkActivo = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnDardeBaja = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPresentacion = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Presentación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setText("ID:");

        jLabel2.setText("Tipo presentación:");

        jLabel3.setText("Cantidad:");

        jLabel4.setText("Unidad:");

        spCantidad.setModel(new javax.swing.SpinnerNumberModel(1.0f, 1.0f, null, 1.0f));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel5.setText("Estado:");

        chkActivo.setText("Activo");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spCantidad)
                    .addComponent(cmbUnidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTipoPresentacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkActivo)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addComponent(jButton1))))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(cmbTipoPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(cmbUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(chkActivo)))
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setName(""); // NOI18N

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDardeBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnDardeBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        tblPresentacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPresentacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPresentacionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPresentacion);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       try {
            if (btnNuevo.getText().equals("Nuevo")) {
                gestionarEstadoControles("nuevo");
                limpiarControles();
                txtID.setText(objPresentacion.generarCodePresentacion().toString());
                btnNuevo.setText("Guardar");
            } else {
                if (cmbTipoPresentacion.getSelectedIndex() == -1 || cmbUnidad.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo y una unidad.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                clsTipoPresentacion tipoSeleccionado = (clsTipoPresentacion) cmbTipoPresentacion.getSelectedItem();
                clsUnidad unidadSeleccionada = (clsUnidad) cmbUnidad.getSelectedItem();
                
                int id = Integer.parseInt(txtID.getText());
                float cantidad = ((Number) spCantidad.getValue()).floatValue();
              
                
                objPresentacion.registrarPresentacion(id, cantidad, unidadSeleccionada.getId(), tipoSeleccionado.getId(), true);

                JOptionPane.showMessageDialog(this, "Presentación registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                listarPresentaciones();
                gestionarEstadoControles("inicio");
                limpiarControles();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            btnNuevo.setText("Guardar");
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
           limpiarControles();
        gestionarEstadoControles("inicio");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnDardeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDardeBajaActionPerformed
    if (txtID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar o seleccionar una presentación para darla de baja.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!chkActivo.isSelected()) {
            JOptionPane.showMessageDialog(this, "Esta presentación ya se encuentra inactiva.", "Acción no Válida", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(txtID.getText());
            
            if (objPresProdDAO.presentacionEstaEnUso(id)) {
                JOptionPane.showMessageDialog(this,
                    "Esta presentación no se puede dar de baja porque está asignada a uno o más productos.",
                    "Acción Denegada",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int opt = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cambiar el estado a 'Inactivo'?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (opt == JOptionPane.YES_OPTION) {
                objPresentacion.darBajaPresentacion(id);
                
                listarPresentaciones();
                gestionarEstadoControles("inicio");
                limpiarControles();
                JOptionPane.showMessageDialog(this, "La presentación ha sido dada de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al dar de baja la presentación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDardeBajaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
          if (txtID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar o seleccionar una presentación para eliminar.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(txtID.getText());

            if (objPresProdDAO.presentacionEstaEnUso(id)) {
                JOptionPane.showMessageDialog(this,
                    "Esta presentación no se puede ELIMINAR porque está asignada a uno o más productos.",
                    "Acción Denegada",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int opt = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar permanentemente esta presentación?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (opt == JOptionPane.YES_OPTION) {
                objPresentacion.eliminarPresentacion(id);
                listarPresentaciones();
                gestionarEstadoControles("inicio");
                limpiarControles();
                JOptionPane.showMessageDialog(this, "Presentación eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la presentación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
           if (txtID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar o seleccionar una presentación para modificar.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(txtID.getText());
            float cantidad = ((Number) spCantidad.getValue()).floatValue();
            clsUnidad unidad = (clsUnidad) cmbUnidad.getSelectedItem();
            clsTipoPresentacion tipo = (clsTipoPresentacion) cmbTipoPresentacion.getSelectedItem();
            boolean nuevoEstado = chkActivo.isSelected();

            if (!nuevoEstado) {
                if (objPresProdDAO.presentacionEstaEnUso(id)) {
                    JOptionPane.showMessageDialog(this,
                        "No se puede desactivar esta presentación porque está asignada a uno o más productos.",
                        "Acción Denegada",
                        JOptionPane.ERROR_MESSAGE);
                    chkActivo.setSelected(true);
                    return;
                }
            }
            
            objPresentacion.modificarPresentacion(id, cantidad, unidad.getId(), tipo.getId(), nuevoEstado);

            listarPresentaciones();
            gestionarEstadoControles("inicio");
            limpiarControles();
            JOptionPane.showMessageDialog(this, "Presentación modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tblPresentacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPresentacionMouseClicked
      int fila = tblPresentacion.getSelectedRow();
        if (fila == -1) {
            return;
        }
        try {
            clsPresentacion presentacionSeleccionada = this.listaDePresentaciones.get(fila);
            cargarDatosEnFormulario(presentacionSeleccionada);
            gestionarEstadoControles("modificar");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de la fila: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tblPresentacionMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
         try {
            if (txtID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un código para buscar.");
                return;
            }
            int idABuscar = Integer.parseInt(txtID.getText());
            clsPresentacion presentacionEncontrada = objPresentacion.buscarPresentacion(idABuscar);

            if (presentacionEncontrada != null) {
                cargarDatosEnFormulario(presentacionEncontrada);
                gestionarEstadoControles("modificar");
                seleccionarFilaEnTabla(idABuscar);
            } else {
                JOptionPane.showMessageDialog(this, "El código de presentación no existe.");
                limpiarControles();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código numérico válido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error en la búsqueda: " + e.getMessage());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     ManTipoPresentacion manTP = new ManTipoPresentacion(null,true);
        manTP.setVisible(true);
        listarTiposPresentacion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
          ManUnidad mU = new ManUnidad(null,true);
        mU.setVisible(true);
        listarUnidades();
    }//GEN-LAST:event_jButton2ActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDardeBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JRadioButton chkActivo;
    private javax.swing.JComboBox<Capa_Negocio.clsTipoPresentacion> cmbTipoPresentacion;
    private javax.swing.JComboBox<Capa_Negocio.clsUnidad> cmbUnidad;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spCantidad;
    private javax.swing.JTable tblPresentacion;
    private javax.swing.JTextField txtID;
    // End of variables declaration//GEN-END:variables
}
