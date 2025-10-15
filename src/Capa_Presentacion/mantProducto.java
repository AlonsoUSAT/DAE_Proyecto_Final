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
 * @author Fernando Hernández
 */
public class mantProducto extends javax.swing.JDialog {
  
   
    private final ProductoDAO pDAO = new ProductoDAO();
    private final categoriaDAO cDAO = new categoriaDAO();
    private final MarcaDAO mDAO = new MarcaDAO();
    private final laboratorioDAO lDAO = new laboratorioDAO();
    
    
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
            
            this.listaProductos = pDAO.listar(); 
            
            
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
    cargarComboCategorias();
    cargarComboMarcas();
    cargarComboDistribuidores();
}

private void cargarComboCategorias() {
    try {
        DefaultComboBoxModel<clsCategoria> modelo = new DefaultComboBoxModel<>();
        cDAO.listarCategoriasActivas().forEach(modelo::addElement); 
        cmbCategoria.setModel(modelo);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void cargarComboMarcas() {
    try {
        DefaultComboBoxModel<clsMarca> modelo = new DefaultComboBoxModel<>();
        mDAO.listarMarcasActivas().forEach(modelo::addElement); 
        cmbMarca.setModel(modelo);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar marcas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void cargarComboDistribuidores() {
    try {
        DefaultComboBoxModel<clsLaboratorio> modelo = new DefaultComboBoxModel<>();
        lDAO.listarLaboratoriosActivos().forEach(modelo::addElement); 
        cmbDistribuidor.setModel(modelo);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar distribuidores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    

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
    
    txtID.setText("");
    txtNombre.setText("");
    txtDescripcion.setText("");
    chkVigencia.setSelected(true);
    cmbCategoria.setSelectedIndex(-1); 
    cmbMarca.setSelectedIndex(-1);
    cmbDistribuidor.setSelectedIndex(-1);

   
    txtID.setEnabled(true);
    
    jButton1.setEnabled(true); 

    
    txtNombre.setEnabled(false);
    txtDescripcion.setEnabled(false);
    chkVigencia.setEnabled(false);
    cmbCategoria.setEnabled(false);
    cmbMarca.setEnabled(false);
    cmbDistribuidor.setEnabled(false);

    
    btnNuevo.setText("Nuevo");
    btnNuevo.setEnabled(true);
    
    btnModificar.setText("Modificar");
    btnModificar.setEnabled(false);
    
    btnEliminar.setEnabled(false);
    btnDardeBaja.setEnabled(false);
    
    
    tblProducto.clearSelection();
    txtID.requestFocus(); 
}
    
    private void habilitarControles(boolean esNuevo) {
        txtID.setEnabled(esNuevo);
        txtNombre.setEnabled(true);
        txtDescripcion.setEnabled(true);
        chkVigencia.setEnabled(true);
        cmbCategoria.setEnabled(true);
        cmbMarca.setEnabled(true);
        cmbDistribuidor.setEnabled(true);
        
        btnNuevo.setEnabled(false); 
        btnModificar.setEnabled(true);
        btnDardeBaja.setEnabled(!esNuevo); 
        btnEliminar.setEnabled(!esNuevo);  
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
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel1.setText("Descripción:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("Categoría:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        btnNuevaMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        btnNuevaMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaMarcaActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel8.setText("Vigencia:");

        chkVigencia.setText("(Vigente)");

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel9.setText("Marca:");

        cmbMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMarcaActionPerformed(evt);
            }
        });

        btnNuevaCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        btnNuevaCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaCategoriaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel10.setText("Distribuidor:");

        cmbDistribuidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDistribuidorActionPerformed(evt);
            }
        });

        btnNuevoDistribuidor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/nuevo.png"))); // NOI18N
        btnNuevoDistribuidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoDistribuidorActionPerformed(evt);
            }
        });

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
        
    limpiarControles();
    habilitarControles(true); 

    try {
        Integer nuevoID = pDAO.generarCodigo();
        txtID.setText(String.valueOf(nuevoID));
        txtID.setEnabled(false); 
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al generar el código: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        estadoInicialControles();
        return;
    }

    
    btnModificar.setText("Guardar"); 
    
    txtNombre.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        
    if (txtNombre.getText().trim().isEmpty() || cmbCategoria.getSelectedIndex() == -1 || cmbMarca.getSelectedIndex() == -1 || cmbDistribuidor.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        
        clsProducto producto = new clsProducto();
        producto.setIdProducto(Integer.parseInt(txtID.getText()));
        producto.setNombre(txtNombre.getText());
        producto.setDescripcion(txtDescripcion.getText());
        producto.setEstado(chkVigencia.isSelected());
        producto.setCategoria((clsCategoria) cmbCategoria.getSelectedItem());
        producto.setMarca((clsMarca) cmbMarca.getSelectedItem());
        producto.setDistribuidor((clsLaboratorio) cmbDistribuidor.getSelectedItem());

       
        if (btnModificar.getText().equals("Guardar")) {
            
            pDAO.insertar(producto);
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            
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
         
    int filaSeleccionada = tblProducto.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un producto de la tabla.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    
    int confirmacion = JOptionPane.showConfirmDialog(
        this, 
        "¿Está seguro de que desea dar de baja este producto?\nEl producto no se eliminará, pero se marcará como 'No Vigente'.", // El mensaje para el usuario
        "Confirmar Acción", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE 
    );
    
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        try {
            
            int idProducto = (int) tblProducto.getValueAt(filaSeleccionada, 0);
            
            
            pDAO.darDeBaja(idProducto);
            
            
            listarProductos();
            estadoInicialControles();
            limpiarControles();
            
            
            JOptionPane.showMessageDialog(this, "Producto dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al dar de baja el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    }//GEN-LAST:event_btnDardeBajaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
        estadoInicialControles();
        
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnMostrarFormatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarFormatosActionPerformed
     
    int filaSeleccionada = tblProducto.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla para ver sus formatos.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        
        DefaultTableModel modelo = (DefaultTableModel) tblProducto.getModel();
        
       
        
        
        Object idObjeto = modelo.getValueAt(filaSeleccionada, 0);
        Object nombreObjeto = modelo.getValueAt(filaSeleccionada, 1);
        
        
        int idProducto = Integer.parseInt(idObjeto.toString());
        String nombreProducto = nombreObjeto.toString();
        
        
        ManPresPro dialogoFormatos = new ManPresPro(null, true, idProducto, nombreProducto);
        dialogoFormatos.setVisible(true);
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El ID del producto en la tabla no es un número válido.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir la ventana de formatos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_btnMostrarFormatosActionPerformed

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        int fila = tblProducto.getSelectedRow();
    if (fila >= 0) {
        try {
           
            int idProducto = (int) tblProducto.getValueAt(fila, 0);
            
           
            clsProducto productoSeleccionado = pDAO.buscarPorId(idProducto);

            if (productoSeleccionado != null) {
                
                txtID.setText(String.valueOf(productoSeleccionado.getIdProducto()));
                txtNombre.setText(productoSeleccionado.getNombre());
              
                txtDescripcion.setText(productoSeleccionado.getDescripcion()); 
                chkVigencia.setSelected(productoSeleccionado.isEstado());
                
                
                cmbCategoria.setSelectedItem(productoSeleccionado.getCategoria());
                cmbMarca.setSelectedItem(productoSeleccionado.getMarca());
                cmbDistribuidor.setSelectedItem(productoSeleccionado.getDistribuidor());

                
                habilitarControles(false);
                btnNuevo.setEnabled(true);
                btnModificar.setText("Modificar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al seleccionar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String textoBusqueda = txtID.getText().trim();

    if (textoBusqueda.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de producto para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int idProducto = Integer.parseInt(textoBusqueda);
        
        
        clsProducto productoEncontrado = pDAO.buscarPorId(idProducto);

        if (productoEncontrado != null) {
            
            txtNombre.setText(productoEncontrado.getNombre());
            txtDescripcion.setText(productoEncontrado.getDescripcion());
            chkVigencia.setSelected(productoEncontrado.isEstado());
            
            cmbCategoria.setSelectedItem(productoEncontrado.getCategoria());
            cmbMarca.setSelectedItem(productoEncontrado.getMarca());
            cmbDistribuidor.setSelectedItem(productoEncontrado.getDistribuidor());

            
            habilitarControles(false);
            btnNuevo.setEnabled(true);
            btnModificar.setText("Modificar");
            
            
            seleccionarFilaEnTabla(idProducto);

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún producto con el ID: " + idProducto, "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El ID del producto debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error al buscar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void seleccionarFilaEnTabla(int idProducto) {
    for (int i = 0; i < tblProducto.getRowCount(); i++) {
        if ((int) tblProducto.getValueAt(i, 0) == idProducto) {
            tblProducto.setRowSelectionInterval(i, i);
            tblProducto.scrollRectToVisible(tblProducto.getCellRect(i, 0, true));
            break;
        }
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnNuevaCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaCategoriaActionPerformed
     Object categoriaSeleccionada = cmbCategoria.getSelectedItem();
    mantCategoria frm = new mantCategoria(null, true);
    frm.setVisible(true);
    cargarComboCategorias();
    if (categoriaSeleccionada != null) {
        cmbCategoria.setSelectedItem(categoriaSeleccionada);
    }
    }//GEN-LAST:event_btnNuevaCategoriaActionPerformed

    private void btnNuevaMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaMarcaActionPerformed
     Object marcaSeleccionada = cmbMarca.getSelectedItem();
    mantMarca frm = new mantMarca(null, true);
    frm.setVisible(true);
    cargarComboMarcas();
    if (marcaSeleccionada != null) {
        cmbMarca.setSelectedItem(marcaSeleccionada);
    }
    }//GEN-LAST:event_btnNuevaMarcaActionPerformed

    private void btnNuevoDistribuidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoDistribuidorActionPerformed
    Object distribuidorSeleccionado = cmbDistribuidor.getSelectedItem();
    mantLaboratorio frm = new mantLaboratorio(null, true);
    frm.setVisible(true);
    cargarComboDistribuidores();
    if (distribuidorSeleccionado != null) {
        cmbDistribuidor.setSelectedItem(distribuidorSeleccionado);
    }
    }//GEN-LAST:event_btnNuevoDistribuidorActionPerformed

    private void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMarcaActionPerformed
     
    }//GEN-LAST:event_cmbMarcaActionPerformed

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void cmbDistribuidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDistribuidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDistribuidorActionPerformed

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
