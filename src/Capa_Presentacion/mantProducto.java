/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;
import Capa_Datos.*;
import Capa_Negocio.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Usuario
 */
public class mantProducto extends javax.swing.JDialog {
  
   // --- DAOs para acceso a datos (Nombres de variable consistentes) ---
    private final ProductoDAO pDAO = new ProductoDAO();
    private final categoriaDAO cDAO = new categoriaDAO();
    private final MarcaDAO mDAO = new MarcaDAO();
    private final laboratorioDAO lDAO = new laboratorioDAO();
    
    // --- Lista para almacenar los productos de la tabla ---
    private List<clsProducto> listaProductos;

    /**
     * Creates new form mantCategoria
     */
    public mantProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         this.setTitle("Mantenimiento de Productos");
        this.setLocationRelativeTo(null);
        configurarTabla();
        cargarDatosIniciales();
        estadoInicialControles();
    }

    // --- MÉTODOS DE CONFIGURACIÓN Y CARGA DE DATOS ---

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Categoría");
        modelo.addColumn("Marca");
        modelo.addColumn("Distribuidor");
        modelo.addColumn("Estado");
        tblProducto.setModel(modelo);
    }

    private void cargarDatosIniciales() {
        cargarCombos();
        listarProductos();
    }

    private void listarProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tblProducto.getModel();
        modelo.setRowCount(0);

        try {
            // CORRECCIÓN: Se usó el nombre correcto de la variable "listaProductos" y "pDAO".
            this.listaProductos = pDAO.listar(); 
            
            // CORRECCIÓN: Se iteró sobre la variable correcta "this.listaProductos".
            for (clsProducto producto : this.listaProductos) {
                modelo.addRow(new Object[]{
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getCategoria().getNombreCategoria(),
                    producto.getMarca().getNombre(),
                    producto.getDistribuidor().getNombreLaboratorio(),
                    producto.isEstado() ? "Vigente" : "No Vigente"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCombos() {
        try {
            // Cargar Categorías
            DefaultComboBoxModel<clsCategoria> modeloCategoria = new DefaultComboBoxModel<>();
            // CORRECCIÓN: Se usó la variable correcta "cDAO".
            cDAO.listarCategorias().forEach(modeloCategoria::addElement);
            cmbCategoria.setModel(modeloCategoria);

            // Cargar Marcas
            DefaultComboBoxModel<clsMarca> modeloMarca = new DefaultComboBoxModel<>();
            // CORRECCIÓN: Se usó la variable correcta "mDAO".
            mDAO.listarMarcas().forEach(modeloMarca::addElement);
            cmbMarca.setModel(modeloMarca);

            // Cargar Distribuidores (Laboratorios)
            DefaultComboBoxModel<clsLaboratorio> modeloDistribuidor = new DefaultComboBoxModel<>();
            // CORRECCIÓN: Se usó la variable correcta "lDAO".
            lDAO.listarLaboratorios().forEach(modeloDistribuidor::addElement);
            cmbDistribuidor.setModel(modeloDistribuidor);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de combos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- GESTIÓN DE ESTADO DE CONTROLES DEL FORMULARIO ---

    private void limpiarControles() {
        txtID.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        chkVigencia.setSelected(true);
        cmbCategoria.setSelectedIndex(-1);
        cmbMarca.setSelectedIndex(-1);
        cmbDistribuidor.setSelectedIndex(-1);
        txtID.requestFocus();
    }

    private void estadoInicialControles() {
        txtID.setEnabled(false);
        txtNombre.setEnabled(false);
        txtDescripcion.setEnabled(false);
        chkVigencia.setEnabled(false);
        cmbCategoria.setEnabled(false);
        cmbMarca.setEnabled(false);
        cmbDistribuidor.setEnabled(false);
        
        btnNuevo.setText("Nuevo");
        btnNuevo.setEnabled(true);
        btnModificar.setEnabled(false);
        btnDardeBaja.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
    
    private void habilitarControles(boolean esNuevo) {
        txtID.setEnabled(esNuevo);
        txtNombre.setEnabled(true);
        txtDescripcion.setEnabled(true);
        chkVigencia.setEnabled(true);
        cmbCategoria.setEnabled(true);
        cmbMarca.setEnabled(true);
        cmbDistribuidor.setEnabled(true);
        
        btnNuevo.setEnabled(false); // Deshabilitar "Nuevo" mientras se edita/crea
        btnModificar.setEnabled(true);
        btnDardeBaja.setEnabled(!esNuevo); // Solo habilitar si no es nuevo
        btnEliminar.setEnabled(!esNuevo);  // Solo habilitar si no es nuevo
    }
    
  private void seleccionarItemPorNombre(JComboBox<?> combo, String nombre) {
    for (int i = 0; i < combo.getItemCount(); i++) {
        Object item = combo.getItemAt(i);
        
        // El método toString() se encarga de devolver el nombre para la comparación
        if (item.toString().equals(nombre)) {
            combo.setSelectedIndex(i);
            return; // Termina el bucle una vez que encuentra la coincidencia
        }
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
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnDardeBaja = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        cmbCategoria = new javax.swing.JComboBox<>();
        btnNuevaMarca = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        chkVigencia = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        cmbMarca = new javax.swing.JComboBox<>();
        btnNuevaCategoria = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cmbDistribuidor = new javax.swing.JComboBox<>();
        btnNuevoDistribuidor = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        btnMostrarFormatos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDardeBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnDardeBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Producto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("ID:");

        jButton1.setBackground(new java.awt.Color(204, 224, 250));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar-usuario.png"))); // NOI18N
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel1.setText("Descripción:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("Categoría:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        btnNuevaMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel8.setText("Vigencia:");

        chkVigencia.setText("(Vigente)");

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel9.setText("Marca:");

        btnNuevaCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel10.setText("Distribuidor:");

        btnNuevoDistribuidor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombre)
                                    .addComponent(txtID, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(16, 16, 16))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCategoria, 0, 331, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(chkVigencia)
                                .addGap(161, 161, 161))
                            .addComponent(cmbMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbDistribuidor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnNuevaMarca)
                                .addComponent(btnNuevaCategoria, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(btnNuevoDistribuidor, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(chkVigencia))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnNuevaCategoria))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNuevaMarca)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cmbDistribuidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnNuevoDistribuidor)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Vigencia", "Categoria", "Marca", "Distribuidor"
            }
        ));
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProducto);

        btnMostrarFormatos.setBackground(new java.awt.Color(204, 224, 250));
        btnMostrarFormatos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMostrarFormatos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar-usuario.png"))); // NOI18N
        btnMostrarFormatos.setText("Mostrar Formatos");
        btnMostrarFormatos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMostrarFormatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarFormatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMostrarFormatos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarFormatos))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
         if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pDAO.eliminar(Integer.parseInt(txtID.getText()));
                listarProductos();
                estadoInicialControles();
                limpiarControles();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // Si el botón dice "Nuevo", preparamos el formulario para un nuevo registro.
    if (btnNuevo.getText().equals("Nuevo")) {
        limpiarControles();
        habilitarControles(true); // Habilita todos los campos necesarios

        try {
            // --- AQUÍ SE USA EL GENERADOR DE CÓDIGO ---
            // 1. Llama al DAO para obtener el siguiente código disponible.
            Integer nuevoID = pDAO.generarCodigo();
            
            // 2. Coloca el código en el campo de texto.
            txtID.setText(String.valueOf(nuevoID));
            
            // 3. Deshabilita el campo ID para que el usuario NO pueda cambiarlo.
            txtID.setEnabled(false); 

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar el código: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            estadoInicialControles(); // Regresar al estado inicial si hay un error
            return; // Detener la ejecución
        }

        // 4. Cambia el texto del botón a "Guardar" y pone el foco en el nombre.
        btnNuevo.setText("Guardar");
        txtNombre.requestFocus();

    } else { 
        // Si el botón dice "Guardar", se procede a insertar el nuevo registro.
        try {
            // --- Validación de campos obligatorios ---
            if (txtNombre.getText().trim().isEmpty() || cmbCategoria.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debe completar el nombre y seleccionar una categoría.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return; // No continuar si faltan datos
            }
            
            // --- Recopilar datos del formulario ---
            clsProducto producto = new clsProducto();
            producto.setIdProducto(Integer.parseInt(txtID.getText()));
            producto.setNombre(txtNombre.getText());
            producto.setDescripcion(txtDescripcion.getText());
            producto.setEstado(chkVigencia.isSelected());
            producto.setCategoria((clsCategoria) cmbCategoria.getSelectedItem());
            producto.setMarca((clsMarca) cmbMarca.getSelectedItem());
            producto.setDistribuidor((clsLaboratorio) cmbDistribuidor.getSelectedItem());
            
            // --- Enviar a la base de datos ---
            pDAO.insertar(producto);
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // --- Actualizar y limpiar el formulario ---
            listarProductos();
            estadoInicialControles();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
         // Este botón sirve para Guardar un nuevo registro o Modificar uno existente
        try {
            if (txtNombre.getText().trim().isEmpty() || cmbCategoria.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debe completar los campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            clsProducto producto = new clsProducto();
            producto.setNombre(txtNombre.getText());
            producto.setDescripcion(txtDescripcion.getText());
            producto.setEstado(chkVigencia.isSelected());
            producto.setCategoria((clsCategoria) cmbCategoria.getSelectedItem());
            producto.setMarca((clsMarca) cmbMarca.getSelectedItem());
            producto.setDistribuidor((clsLaboratorio) cmbDistribuidor.getSelectedItem());

            if (btnNuevo.getText().equals("Guardar")) { // Lógica para insertar
                producto.setIdProducto(Integer.parseInt(txtID.getText()));
                pDAO.insertar(producto);
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else { // Lógica para modificar
                producto.setIdProducto(Integer.parseInt(txtID.getText()));
                pDAO.modificar(producto);
                JOptionPane.showMessageDialog(this, "Producto modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            listarProductos();
            estadoInicialControles();
            limpiarControles();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnDardeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDardeBajaActionPerformed
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            pDAO.darDeBaja(Integer.parseInt(txtID.getText()));
            listarProductos();
            estadoInicialControles();
            limpiarControles();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al dar de baja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDardeBajaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarControles();
        estadoInicialControles();
        btnModificar.setText("Modificar");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnMostrarFormatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarFormatosActionPerformed
       int fila = tblProducto.getSelectedRow();
    
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Primero debe seleccionar un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtenemos los datos que necesitamos ENVIAR
    int productoID = (int) tblProducto.getValueAt(fila, 0);
    String productoNombre = tblProducto.getValueAt(fila, 1).toString();
    
    // Creamos el formulario USANDO EL CONSTRUCTOR que RECIBE los datos
    ManPresPro form = new ManPresPro(null, true, productoID, productoNombre);
    form.setVisible(true);
    
    }//GEN-LAST:event_btnMostrarFormatosActionPerformed

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        int fila = tblProducto.getSelectedRow();
    if (fila >= 0) {
        // Obtener datos directamente del modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblProducto.getModel();
        
        // Columna 0: ID
        String id = modelo.getValueAt(fila, 0).toString(); 
        // Columna 1: Nombre
        String nombre = modelo.getValueAt(fila, 1).toString();
        // Columna 2: Nombre de la Categoría
        String nombreCategoria = modelo.getValueAt(fila, 2).toString();
        // Columna 3: Nombre de la Marca
        String nombreMarca = modelo.getValueAt(fila, 3).toString();
        // Columna 4: Nombre del Distribuidor
        String nombreDistribuidor = modelo.getValueAt(fila, 4).toString();
        // Columna 5: Estado (leído como String)
        boolean estado = modelo.getValueAt(fila, 5).toString().equals("Vigente");

        // Llenar los campos de texto
        txtID.setText(id);
        txtNombre.setText(nombre);
        // La descripción no está en la tabla, así que no se podría cargar así.
        // txtDescripcion.setText(...); 
        chkVigencia.setSelected(estado);
        
        // Para los combos, necesitas un buscador por nombre
        seleccionarItemPorNombre(cmbCategoria, nombreCategoria);
        seleccionarItemPorNombre(cmbMarca, nombreMarca);
        seleccionarItemPorNombre(cmbDistribuidor, nombreDistribuidor);

        habilitarControles(false);
        btnNuevo.setText("Nuevo");
        btnNuevo.setEnabled(true);
    }
    }//GEN-LAST:event_tblProductoMouseClicked

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDardeBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrarFormatos;
    private javax.swing.JButton btnNuevaCategoria;
    private javax.swing.JButton btnNuevaMarca;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnNuevoDistribuidor;
    private javax.swing.JCheckBox chkVigencia;
    private javax.swing.JComboBox<clsCategoria> cmbCategoria;
    private javax.swing.JComboBox<clsLaboratorio> cmbDistribuidor;
    private javax.swing.JComboBox<clsMarca> cmbMarca;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
