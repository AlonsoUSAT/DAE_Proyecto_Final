/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;

/**
 *
 * @author USER
 */


import Capa_Datos.PresentacionDAO;
import Capa_Negocio.clsPresentacionProducto;
import Capa_Datos.ProductoDAO;
import Capa_Negocio.clsPresentacion;
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

    /**
     * Creates new form ManPresPro
     */
    
     private int idProductoActual;
    private String nombreProductoActual;
     // --- Objetos de Negocio ---
    PresentacionProductoDAO objPresProd = new PresentacionProductoDAO();
    clsPresentacion objPresentacion = new clsPresentacion();
    
    // --- Datos del producto que estamos gestionando ---
    private final int productoID;
    private final String productoNombre;
    
    public ManPresPro(java.awt.Frame parent, boolean modal, int idProducto, String nombreProducto) {
        super(parent, modal);
        
        // 1. Guardamos los datos del producto
        this.productoID = idProducto;
        this.productoNombre = nombreProducto;
        
        // 2. Inicializamos los componentes y configuramos el formulario
        initComponents();
        configurarFormulario();
        
        // Guardamos los datos del producto en nuestras nuevas variables
    this.idProductoActual = idProducto;
    this.nombreProductoActual = nombreProducto;
    }
    
     private void configurarFormulario() {
        this.setLocationRelativeTo(null);
        this.setTitle("Asignar Presentaciones para: " + this.productoNombre);
        txtProducto.setText(this.productoNombre);
        txtProducto.setEditable(false);
        
        configurarTabla(); // Prepara las columnas de la tabla
        
        // Carga los datos iniciales
        cargarListaPresentaciones();
        actualizarTablaFormatos();
        
        gestionarEstadoControles(false, "inicio");

        // --- LISTENERS (LA LÓGICA PRINCIPAL) ---

        // 1. Cuando se selecciona algo en la LISTA (para crear un NUEVO formato)
        lstPresentaciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lstPresentaciones.getSelectedIndex() != -1) {
                tblPresentacionProducto.clearSelection(); // Limpia la selección de la tabla
                prepararNuevoFormato();
            }
        });

        // 2. Cuando se hace clic en una fila de la TABLA (para MODIFICAR un formato existente)
        tblPresentacionProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (tblPresentacionProducto.getSelectedRow() != -1) {
                    lstPresentaciones.clearSelection(); // Limpia la selección de la lista
                    cargarDatosDesdeTabla();
                }
            }
        });
    }
     
      private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que la tabla no sea editable
            }
        };
        modelo.addColumn("ID Pres.");
        modelo.addColumn("Formato de Venta");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Vigente");
        tblPresentacionProducto.setModel(modelo);
    }
     
     
       private void cargarListaPresentaciones() {
        DefaultListModel<PresentacionDAO> modelo = new DefaultListModel<>();
        try {
            objPresentacion.listarPresentaciones().forEach(modelo::addElement);
            lstPresentaciones.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar presentaciones disponibles: " + e.getMessage());
        }
    }
     
      private void actualizarTablaFormatos() {
        DefaultTableModel modelo = (DefaultTableModel) tblPresentacionProducto.getModel();
        modelo.setRowCount(0); // Limpia la tabla
        try {
            // Usamos el método que ya tienes en tu capa de negocio
            List<Object[]> filas = objPresProd.listarFormatosParaTabla(this.productoID);
            for (Object[] fila : filas) {
                // Adaptamos los datos a las columnas de nuestra tabla
                modelo.addRow(new Object[]{
                    fila[0], // idPresentacion
                    fila[2], // Nombre del formato (String ya concatenado)
                    fila[3], // Precio
                    fila[4], // Stock
                    fila[5]  // Vigencia (Activo)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar formatos asignados: " + e.getMessage());
        }
    }

    private void prepararNuevoFormato() {
        limpiarCamposEdicion();
        chkVigencia.setSelected(true);
        gestionarEstadoControles(true, "nuevo");
    }

    private void cargarDatosDesdeTabla() {
        int filaSeleccionada = tblPresentacionProducto.getSelectedRow();
        if (filaSeleccionada == -1) return;
        
        DefaultTableModel modelo = (DefaultTableModel) tblPresentacionProducto.getModel();
        txtPrecioVenta.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
        txtStock.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
        chkVigencia.setSelected((Boolean) modelo.getValueAt(filaSeleccionada, 4));
        
        gestionarEstadoControles(true, "modificar");
    }

    private void gestionarEstadoControles(boolean habilitar, String modo) {
        txtPrecioVenta.setEnabled(habilitar);
        txtStock.setEnabled(habilitar);
        chkVigencia.setEnabled(habilitar);
        
        switch (modo) {
            case "nuevo":
                btnNuevo.setEnabled(true);
                btnModificar.setEnabled(false);
                btnDarDeBaja.setEnabled(false);
                btnEliminar.setEnabled(false);
                break;
            case "modificar":
                btnNuevo.setEnabled(false);
                btnModificar.setEnabled(true);
                btnDarDeBaja.setEnabled(true);
                btnEliminar.setEnabled(true);
                break;
            default: // "inicio" o cualquier otro caso
                btnNuevo.setEnabled(false);
                btnModificar.setEnabled(false);
                btnDarDeBaja.setEnabled(false);
                btnEliminar.setEnabled(false);
                break;
        }
    }
    
    private void limpiarCamposEdicion() {
        txtPrecioVenta.setText("");
        txtStock.setText("0");
    }
    
    private void limpiarFormularioCompleto() {
        lstPresentaciones.clearSelection();
        tblPresentacionProducto.clearSelection();
        limpiarCamposEdicion();
        chkVigencia.setSelected(false);
        gestionarEstadoControles(false, "inicio");
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
                .addContainerGap(15, Short.MAX_VALUE))
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btnLotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
        PresentacionDAO presSeleccionada = lstPresentaciones.getSelectedValue();
        if (presSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una presentación de la lista de la izquierda.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            float precio = Float.parseFloat(txtPrecioVenta.getText());
            int stock = Integer.parseInt(txtStock.getText());
            boolean vigente = chkVigencia.isSelected();

            objPresProd.registrar(productoID, presSeleccionada.getId(), precio, stock, vigente);
            JOptionPane.showMessageDialog(this, "Presentación asignada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablaFormatos();
            limpiarFormularioCompleto();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormularioCompleto();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnDarDeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarDeBajaActionPerformed

    }//GEN-LAST:event_btnDarDeBajaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tblPresentacionProducto.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un formato de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea ELIMINAR este formato del producto?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idPresentacion = (int) tblPresentacionProducto.getValueAt(filaSeleccionada, 0);
                objPresProd.eliminar(productoID, idPresentacion);
                actualizarTablaFormatos();
                limpiarFormularioCompleto();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            int stock = Integer.parseInt(txtStock.getText());
            boolean vigente = chkVigencia.isSelected();

            objPresProd.modificar(productoID, idPresentacion, precio, stock, vigente);
            JOptionPane.showMessageDialog(this, "Presentación modificada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablaFormatos();
            limpiarFormularioCompleto();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
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
        cargarListaPresentaciones();

    }//GEN-LAST:event_btnNuevaPresentacionActionPerformed

    private void tblPresentacionProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPresentacionProductoMouseClicked
        // Verifica si realmente se ha seleccionado una fila
        if (tblPresentacionProducto.getSelectedRow() != -1) {

            // 1. IMPORTANTE: Limpia la selección de la lista de la izquierda
            // para que el programa sepa que estás en "Modo Edición" y no en "Modo Nuevo".
            lstPresentaciones.clearSelection();

            // 2. Llama al método que hace el trabajo de cargar los datos.
            cargarDatosDesdeTabla();
        }
    }//GEN-LAST:event_tblPresentacionProductoMouseClicked

    private void btnLotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLotesActionPerformed
         int filaSeleccionada = tblPresentacionProducto.getSelectedRow();

    if (filaSeleccionada >= 0) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblPresentacionProducto.getModel();

            // --- DATOS A ENVIAR ---
            // 1. ID de la Presentación (Columna 0)
            int idPres = (int) modelo.getValueAt(filaSeleccionada, 0);
            
            // 2. Descripción de la Presentación (Columna 1: "Formato de Venta") - ¡NUEVO!
            String presentacionDescripcion = modelo.getValueAt(filaSeleccionada, 1).toString();

            // 3. El idProducto y nombreProducto ya los tienes guardados en la clase
            //    (idProductoActual y nombreProductoActual)

            // ✅ CAMBIO: Llamamos al nuevo constructor de ManLote con los 4 parámetros
            ManLote dialogoLote = new ManLote(
                null, 
                true, 
                this.idProductoActual, 
                this.nombreProductoActual, 
                idPres, 
                presentacionDescripcion // <-- El nuevo dato que enviamos
            );
            dialogoLote.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos de la fila: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    private javax.swing.JList<PresentacionDAO> lstPresentaciones;
    private javax.swing.JTable tblPresentacionProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
