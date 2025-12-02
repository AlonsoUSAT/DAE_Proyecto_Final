/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;

import Capa_Negocio.clsCategoria;
import Capa_Negocio.clsLaboratorio;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import Capa_Negocio.clsMarca;
import Capa_Negocio.clsProducto;
import javax.swing.DefaultComboBoxModel;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
/**
 *
 * @author USER
 */
public class jdBusquedaAvanzada extends javax.swing.JDialog {

    /**
     * Creates new form jdBusquedaAvanzada
     */
    
   clsMarca objMarca = new clsMarca();
    clsCategoria objCategoria = new clsCategoria();
    clsProducto objProducto = new clsProducto();
    clsLaboratorio objLaboratorio = new clsLaboratorio();
    boolean buscando = false;
    
    public jdBusquedaAvanzada(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
public int getCod() {
        try {
            return Integer.parseInt(String.valueOf(tblListado.getValueAt(tblListado.getSelectedRow(), 0)));
        } catch (Exception e) {
            return -1;
        }
    }

    public void setBuscando(boolean busc) {
        buscando = busc;
    }


    // --- ADAPTACIONES EN METODOS DE LLENADO ---
    
    private void llenarLaboratorio() {
        ResultSet rs = null;
        try {
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            rs = objLaboratorio.listarLaboratorio(); // Asumo que tienes este método en clsLaboratorio
            modelo.addElement("-Ninguno-");
            
            while (rs.next()) {
                // En tu BD la columna se llama 'nombrelaboratorio'
                modelo.addElement(rs.getString("nombrelaboratorio")); 
            }
            cmbLaboratorio.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al llenar Laboratorios: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ex) {}
        }
    }

    private void llenarMarca() {
        ResultSet rs = null;
        try {
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            rs = objMarca.listarMarcas();
            modelo.addElement("-Ninguno-");
            
            // CORRECCIÓN: Asumo que listarMarcas() devuelve una columna llamada 'nombre'
            while (rs.next()) {
                modelo.addElement(rs.getString("nombre")); // Usar 'nombre' si es el nombre de la columna
            }
            cboMarca.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al llenar Marcas: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException ex) {}
        }
    }

    private void llenarCategoria() {
        ResultSet rs = null;
        try {
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            rs = objCategoria.listarCategoria();
            modelo.addElement("-Ninguno-");
            
            // CORRECCIÓN: Según el esquema, el nombre de la categoría es 'nombrecategoria'
            while (rs.next()) {
                modelo.addElement(rs.getString("nombrecategoria")); // Usar 'nombrecategoria'
            }
            cboCategoria.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al llenar Categorías: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException ex) {}
        }
    }


    // --- ADAPTACIONES EN EL METODO LISTAR (FILTRADO) ---

  private void listar() {
        ResultSet rsProductos = null;
        DefaultTableModel modelo = new DefaultTableModel();
        // Definimos las columnas
        modelo.addColumn("CÓDIGO");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("DESCRIPCIÓN");
        modelo.addColumn("PRECIO");
        modelo.addColumn("STOCK");
        modelo.addColumn("ESTADO"); 
        modelo.addColumn("MARCA");   
        modelo.addColumn("CATEGORÍA"); 
        modelo.addColumn("LABORATORIO"); // <--- Nueva columna visual

        try {
            // 1. Obtener valores de los Spinners (Double para evitar errores)
            double valorMaxSpinner = ((Number) spnMax.getValue()).doubleValue();
            
            if (valorMaxSpinner == 0) {
                double precioMaximoBD = objProducto.getPrecioMax(); 
                spnMax.setValue(precioMaximoBD);
                valorMaxSpinner = precioMaximoBD;
            }

            double min = ((Number) spnMin.getValue()).doubleValue();
            double max = valorMaxSpinner;
            
            if (min > max) { double temp = min; min = max; max = temp; }

            // 2. Obtener IDs de los ComboBoxes
            int idMarca = 0;
            if (!cboMarca.getSelectedItem().equals("-Ninguno-")) {
                idMarca = objMarca.getCodigo(cboMarca.getSelectedItem().toString());
            }

            int idCategoria = 0;
            if (!cboCategoria.getSelectedItem().equals("-Ninguno-")) {
                idCategoria = objCategoria.getCodigo(cboCategoria.getSelectedItem().toString());
            }

            // --- NUEVO: OBTENER ID LABORATORIO ---
            int idLaboratorio = 0;
            if (!cmbLaboratorio.getSelectedItem().equals("-Ninguno-")) {
                // Asegúrate de que objLaboratorio esté instanciado arriba
                idLaboratorio = objLaboratorio.getCodigo(cmbLaboratorio.getSelectedItem().toString());
            }

            // 3. LLAMADA AL NUEVO MÉTODO ÚNICO
            // Le pasamos los 3 IDs. Si alguno es 0, el método lo ignora.
            rsProductos = objProducto.filtrarAvanzadoLotes(
                    idMarca, 
                    idCategoria, 
                    idLaboratorio, 
                    txtNombre.getText(), 
                    min, max
            );
            
            // 4. Llenar Tabla
            while (rsProductos.next()) {
                modelo.addRow(new Object[]{
                    rsProductos.getInt("idproducto"),
                    rsProductos.getString("nombre"),
                    rsProductos.getString("descripcion"),
                    rsProductos.getDouble("precio"),
                    rsProductos.getInt("stock"),
                    rsProductos.getBoolean("estado") ? "SÍ" : "NO",
                    rsProductos.getString("nomMarca"),
                    rsProductos.getString("nomCategoria"),
                    rsProductos.getString("nombreLaboratorio") // <--- Mostramos el laboratorio
                });
            }

            tblListado.setModel(modelo);
            txtTotalProductos.setText(String.valueOf(tblListado.getRowCount()));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error en Listar: " + e.getMessage());
        } finally {
             try { if (rsProductos != null) rsProductos.close(); } catch (SQLException ex) {}
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

        spnMin = new javax.swing.JSpinner();
        spnMax = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListado = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboMarca = new javax.swing.JComboBox<>();
        txtTotalProductos = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        cboCategoria = new javax.swing.JComboBox<>();
        btnSalir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbLaboratorio = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        spnMin.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnMinStateChanged(evt);
            }
        });

        spnMax.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnMaxStateChanged(evt);
            }
        });

        tblListado.setModel(new javax.swing.table.DefaultTableModel(
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
        tblListado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListado);

        jLabel1.setText("Marca:");

        jLabel5.setText("Total de productos:");

        cboMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarcaActionPerformed(evt);
            }
        });

        txtTotalProductos.setText("jLabel6");

        jLabel2.setText("Categoria:");

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        cboCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriaActionPerformed(evt);
            }
        });

        btnSalir.setText("SALIR");

        jLabel3.setText("Nombre:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });

        jLabel4.setText("Precio:");

        jLabel6.setText("Laboratorio:");

        cmbLaboratorio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboMarca, 0, 308, Short.MAX_VALUE)
                            .addComponent(cboCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNombre)
                            .addComponent(cmbLaboratorio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(spnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(spnMax, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalProductos)
                        .addGap(98, 98, 98)
                        .addComponent(btnLimpiar)
                        .addGap(30, 30, 30)
                        .addComponent(btnSalir)))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cboMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(spnMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotalProductos)
                    .addComponent(btnLimpiar)
                    .addComponent(btnSalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
    cboMarca.setSelectedIndex(0);
        cboCategoria.setSelectedIndex(0);
        txtNombre.setText("");
        spnMin.setValue(0);
        spnMax.setValue(0);
        listar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cboMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarcaActionPerformed
        listar();
    }//GEN-LAST:event_cboMarcaActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
     llenarMarca(); // Llamar a llenarMarcar aquí
        llenarCategoria(); // Llamar a llenarCategoria aquí
        llenarLaboratorio();
        listar();
    }//GEN-LAST:event_formWindowActivated

    private void cboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriaActionPerformed
        listar();
    }//GEN-LAST:event_cboCategoriaActionPerformed

    private void spnMinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMinStateChanged
     listar();
    }//GEN-LAST:event_spnMinStateChanged

    private void spnMaxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMaxStateChanged
       listar();
    }//GEN-LAST:event_spnMaxStateChanged

    private void tblListadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListadoMouseClicked
       if (buscando) {
        dispose();
    }
    }//GEN-LAST:event_tblListadoMouseClicked

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        listar();
    }//GEN-LAST:event_txtNombreKeyReleased

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JComboBox<String> cboMarca;
    private javax.swing.JComboBox<String> cmbLaboratorio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnMax;
    private javax.swing.JSpinner spnMin;
    private javax.swing.JTable tblListado;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel txtTotalProductos;
    // End of variables declaration//GEN-END:variables
}
