/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;

/**
 *
 * @author Tiznado Leon
 */


import Capa_Datos.LoteDAO;
import Capa_Negocio.clsPresentacion;
import Capa_Negocio.clsPresentacionProducto;
import Capa_Datos.ProductoDAO;
import Capa_Datos.PresentacionDAO;
import Capa_Datos.PresentacionProductoDAO;
import Capa_Negocio.clsProducto;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ManPresPro extends javax.swing.JDialog {

 
    PresentacionProductoDAO objPresProd = new PresentacionProductoDAO();
    PresentacionDAO objPresentacion = new PresentacionDAO();
    LoteDAO objLoteDAO = new LoteDAO();
    
    
    private final int productoID;
    private final String productoNombre;
    private final boolean productoEstaVigente;

    
   public ManPresPro(java.awt.Frame parent, boolean modal, int idProducto, String nombreProducto, boolean productoEsVigente) {
       super(parent, modal);
        this.productoID = idProducto;
        this.productoNombre = nombreProducto;
        this.productoEstaVigente = productoEsVigente;
        initComponents();
        configurarComponentes(); 
    }
   
    private void configurarComponentes() {
    this.setLocationRelativeTo(null);
    this.setTitle("Asignar Presentaciones para: " + this.productoNombre);
    txtProducto.setText(this.productoNombre);
    txtProducto.setEditable(false);
    txtStock.setEditable(false);

    configurarTabla();
    
    
    lstPresentaciones.setModel(new DefaultListModel<clsPresentacion>());

    actualizarAmbasListas();
    
    gestionarEstadoControles("inicio");
    
    if (!this.productoEstaVigente) {
        btnNuevo.setEnabled(false);
        btnNuevo.setToolTipText("No se pueden asignar presentaciones a un producto no vigente.");
        btnNuevaPresentacion.setEnabled(false);
        btnNuevaPresentacion.setToolTipText("No se pueden crear presentaciones para un producto no vigente.");
    }
}

      private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID Pres.");
        modelo.addColumn("Formato de Venta");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Vigencia");
        tblPresentacionProducto.setModel(modelo);
    }
    
      private void limpiarControles() {
        txtPrecioVenta.setText("");
        txtStock.setText("0");
        chkVigencia.setSelected(true);
        tblPresentacionProducto.clearSelection();
        lstPresentaciones.clearSelection();
    }
      
        private void gestionarEstadoControles(String modo) {
        boolean camposEditables = modo.equals("nuevo") || modo.equals("modificar");
        
        txtPrecioVenta.setEnabled(camposEditables);
        
        chkVigencia.setEnabled(modo.equals("modificar"));
        
        txtStock.setEnabled(false);

        switch (modo) {
            case "inicio":
            lstPresentaciones.setEnabled(true);
            
            btnNuevo.setEnabled(this.productoEstaVigente); 
            btnModificar.setEnabled(false);
            btnDarDeBaja.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnNuevo.setText("Nuevo");
            break;
            
            case "nuevo":
                lstPresentaciones.setEnabled(true); 
                btnNuevo.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDarDeBaja.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnNuevo.setText("Guardar");
                break;

            case "modificar":
                lstPresentaciones.setEnabled(false);
                btnNuevo.setEnabled(false);
                btnModificar.setEnabled(true);
                btnDarDeBaja.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnNuevo.setText("Nuevo");
                break;
        }
    }
        
      private void actualizarAmbasListas() {
    DefaultTableModel tableModel = (DefaultTableModel) tblPresentacionProducto.getModel();
    tableModel.setRowCount(0);
    
    DefaultListModel<clsPresentacion> listModel = (DefaultListModel<clsPresentacion>) lstPresentaciones.getModel();
    listModel.clear();

    try {
      
        List<clsPresentacion> catalogoGlobal = objPresentacion.listarPresentaciones();
        for (clsPresentacion p : catalogoGlobal) {
            listModel.addElement(p); 
        }

      
        List<Object[]> asignadosData = objPresProd.listarFormatosParaTabla(this.productoID);
        for (Object[] fila : asignadosData) {
            int idPresentacion = (int) fila[0];
            int stockCalculado = objPresProd.obtenerStockTotalDeLotes(this.productoID, idPresentacion);
            objPresProd.actualizarStock(this.productoID, idPresentacion, stockCalculado);
            
            boolean vigente = (boolean) fila[5];

            tableModel.addRow(new Object[]{
                fila[0], 
                fila[2], 
                fila[3], 
                stockCalculado,
                vigente ? "Vigente" : "No Vigente"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar las listas: " + e.getMessage());
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
        jPanel3 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnDarDeBaja = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtProducto = new javax.swing.JTextField();
        btnNuevaPresentacion = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        chkVigencia = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstPresentaciones = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPresentacionProducto = new javax.swing.JTable();
        btnLotes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

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

        btnDarDeBaja.setBackground(new java.awt.Color(204, 224, 250));
        btnDarDeBaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDarDeBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBaja-usuario.png"))); // NOI18N
        btnDarDeBaja.setText("Dar de Baja");
        btnDarDeBaja.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDarDeBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarDeBajaActionPerformed(evt);
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
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDarDeBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnDarDeBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Presentacion producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setText("Producto:");

        jLabel2.setText("Presentaciones:");

        jLabel3.setText("Stock:");

        jLabel4.setText("Precio de venta:");

        txtProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductoActionPerformed(evt);
            }
        });

        btnNuevaPresentacion.setBackground(new java.awt.Color(204, 224, 250));
        btnNuevaPresentacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevaPresentacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar-usuario.png"))); // NOI18N
        btnNuevaPresentacion.setText("Nueva presentacion");
        btnNuevaPresentacion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNuevaPresentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaPresentacionActionPerformed(evt);
            }
        });

        jLabel5.setText("Vigencia:");

        chkVigencia.setText("Vigente");

        jScrollPane4.setViewportView(lstPresentaciones);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPrecioVenta)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkVigencia)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(btnNuevaPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(184, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                        .addGap(8, 8, 8)
                        .addComponent(btnNuevaPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(chkVigencia))
                .addGap(8, 8, 8))
        );

        tblPresentacionProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPresentacionProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPresentacionProductoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPresentacionProducto);

        btnLotes.setBackground(new java.awt.Color(204, 224, 250));
        btnLotes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar-usuario.png"))); // NOI18N
        btnLotes.setText("Mostrar Lotes");
        btnLotes.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLotesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLotes)
                        .addGap(32, 32, 32))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLotes))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
         if (btnNuevo.getText().equals("Nuevo")) {
        // MODO "NUEVO": Prepara el formulario para recibir datos.
        // Esta parte está correcta y no cambia.
        limpiarControles();
        tblPresentacionProducto.clearSelection();
        gestionarEstadoControles("nuevo");
        lstPresentaciones.requestFocusInWindow();
        
        JOptionPane.showMessageDialog(this, 
                "Modo de asignación activado.\n\n" +
                "1. Seleccione una presentación de la lista.\n" +
                "2. Ingrese el precio de venta.\n" +
                "3. Haga clic en 'Guardar'.",
                "Asignar Nueva Presentación", 
                JOptionPane.INFORMATION_MESSAGE);

    } else { 
      
        try {
            clsPresentacion presSeleccionada = lstPresentaciones.getSelectedValue();

           
            if (presSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una presentación de la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

           
            if (!presSeleccionada.isActivo()) {
                JOptionPane.showMessageDialog(this, "No se puede asignar una presentación inactiva.", "Acción no Válida", JOptionPane.WARNING_MESSAGE);
                return; 
            }
            
            
            int idSeleccionado = presSeleccionada.getId();
            DefaultTableModel modeloTabla = (DefaultTableModel) tblPresentacionProducto.getModel();
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int idEnTabla = (int) modeloTabla.getValueAt(i, 0); 
                if (idEnTabla == idSeleccionado) {
                    JOptionPane.showMessageDialog(this, "Esta presentación ya ha sido asignada al producto.", "Acción no Válida", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
            }
          
            float precio = Float.parseFloat(txtPrecioVenta.getText());
            
            
            
            
            objPresProd.registrar(productoID, presSeleccionada.getId(), precio, 0, true);
            
            
            JOptionPane.showMessageDialog(this, "Presentación asignada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarAmbasListas();
            limpiarControles();
            gestionarEstadoControles("inicio");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       limpiarControles();
        gestionarEstadoControles("inicio");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnDarDeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarDeBajaActionPerformed
   int filaSeleccionada = tblPresentacionProducto.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un formato de la tabla para dar de baja.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!chkVigencia.isSelected()) {
            JOptionPane.showMessageDialog(this, "Este formato ya se encuentra 'No Vigente'.", "Acción no Válida", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cambiar la vigencia de este formato a 'No Vigente'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
               
                int idPresentacion = (int) tblPresentacionProducto.getValueAt(filaSeleccionada, 0);
                objPresProd.darBaja(productoID, idPresentacion);
                float precio = Float.parseFloat(txtPrecioVenta.getText());
                int stockActual = Integer.parseInt(txtStock.getText());

               
                objPresProd.modificar(productoID, idPresentacion, precio, stockActual, false);
                
                JOptionPane.showMessageDialog(this, "El formato ha sido dado de baja.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

               actualizarAmbasListas();
                limpiarControles();
                gestionarEstadoControles("inicio");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDarDeBajaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
          int filaSeleccionada = tblPresentacionProducto.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un formato de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int idPresentacion = (int) tblPresentacionProducto.getValueAt(filaSeleccionada, 0);

       
        if (objLoteDAO.existenLotesParaPresentacion(this.productoID, idPresentacion)) {
            JOptionPane.showMessageDialog(this,
                "No se puede eliminar esta presentación porque tiene lotes registrados.\n\n",
                "Acción no Permitida",
                JOptionPane.ERROR_MESSAGE);
            return; 
        }
      

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Esta acción es irreversible y borrará la asignación del formato a este producto.\n¿Está seguro de que desea ELIMINAR este formato?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            objPresProd.eliminar(productoID, idPresentacion);

            JOptionPane.showMessageDialog(this, "Formato eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarAmbasListas();
            limpiarControles();
            gestionarEstadoControles("inicio");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int filaSeleccionada = tblPresentacionProducto.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un formato de la tabla para modificar.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        
        int idPresentacion = (int) tblPresentacionProducto.getValueAt(filaSeleccionada, 0);
        float precio = Float.parseFloat(txtPrecioVenta.getText());
        boolean vigente = chkVigencia.isSelected();
        
        int stockActual = (int) tblPresentacionProducto.getValueAt(filaSeleccionada, 3);

        
        objPresProd.modificar(productoID, idPresentacion, precio, stockActual, vigente);

        
        DefaultTableModel modelo = (DefaultTableModel) tblPresentacionProducto.getModel();
        
        
        modelo.setValueAt(precio, filaSeleccionada, 2);
        
        
        modelo.setValueAt(vigente ? "Vigente" : "No Vigente", filaSeleccionada, 4);
        
        

        
        limpiarControles();
        gestionarEstadoControles("inicio");
        JOptionPane.showMessageDialog(this, "Presentación modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void txtProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductoActionPerformed

    private void btnNuevaPresentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaPresentacionActionPerformed

        ManPresentacion mant = new ManPresentacion(null, true);
        mant.setVisible(true);
       
        actualizarAmbasListas();

    }//GEN-LAST:event_btnNuevaPresentacionActionPerformed

    private void tblPresentacionProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPresentacionProductoMouseClicked
          int fila = tblPresentacionProducto.getSelectedRow();
        if (fila == -1) return;

        try {
            lstPresentaciones.clearSelection(); 

            Object idPresObj = tblPresentacionProducto.getValueAt(fila, 0);
            Object precioObj = tblPresentacionProducto.getValueAt(fila, 2);
            Object stockObj = tblPresentacionProducto.getValueAt(fila, 3);
            Object vigenciaObj = tblPresentacionProducto.getValueAt(fila, 4);

            txtPrecioVenta.setText(precioObj.toString());
            txtStock.setText(stockObj.toString());
            chkVigencia.setSelected(vigenciaObj.toString().equals("Vigente"));
            
            gestionarEstadoControles("modificar");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tblPresentacionProductoMouseClicked

    private void btnLotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLotesActionPerformed
    int filaSeleccionada = tblPresentacionProducto.getSelectedRow();

    if (filaSeleccionada >= 0) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblPresentacionProducto.getModel();
            int idPres = (int) modelo.getValueAt(filaSeleccionada, 0);
            String presentacionDescripcion = modelo.getValueAt(filaSeleccionada, 1).toString();
            String vigencia = modelo.getValueAt(filaSeleccionada, 4).toString();

           
            if (vigencia.equals("Vigente")) {
                ManLote dialogoLote = new ManLote(null, true, this.productoID, this.productoNombre, idPres, presentacionDescripcion, true);
                dialogoLote.setVisible(true);
            } else {
                int stockActualDeLotes = objPresProd.obtenerStockTotalDeLotes(this.productoID, idPres);
                if (stockActualDeLotes > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Esta presentación no está vigente, pero tiene lotes con stock por gestionar.",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE);
                    ManLote dialogoLote = new ManLote(null, true, this.productoID, this.productoNombre, idPres, presentacionDescripcion, false);
                    dialogoLote.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Esta presentación no está vigente y no tiene lotes con stock.\nNo se pueden agregar nuevos lotes.",
                        "Acción no Permitida",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            
           
            actualizarAmbasListas();

           
            if (filaSeleccionada < tblPresentacionProducto.getRowCount()) {
                tblPresentacionProducto.setRowSelectionInterval(filaSeleccionada, filaSeleccionada); 
                Object nuevoStock = tblPresentacionProducto.getValueAt(filaSeleccionada, 3); 
                txtStock.setText(nuevoStock.toString());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la solicitud de lotes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione una presentación de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    }//GEN-LAST:event_btnLotesActionPerformed

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDarDeBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLotes;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevaPresentacion;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JRadioButton chkVigencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<Capa_Negocio.clsPresentacion> lstPresentaciones;
    private javax.swing.JTable tblPresentacionProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
