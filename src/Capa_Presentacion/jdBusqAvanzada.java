package Capa_Presentacion;

import Capa_Negocio.clsCategoria;
import Capa_Negocio.clsLaboratorio;
import Capa_Negocio.clsMarca;
import Capa_Negocio.clsProducto;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class jdBusqAvanzada extends javax.swing.JDialog {

    clsCategoria objCategoria = new clsCategoria();
    clsMarca objMarca = new clsMarca();
    clsLaboratorio objLaboratorio = new clsLaboratorio();
    clsProducto objProducto = new clsProducto();
    boolean buscando = false;

    private int idProductoSeleccionado = 0;
    private int idPresentacionSeleccionada = 0;
    private double precioUnitarioSeleccionado = 0.0;
    private int cantidadSeleccionada = 0;
    private int descuentoAplicado = 0;
    private int stockSeleccionado = 0;
    private String nombreSeleccionado = "";

    public int getCod() {
        return idProductoSeleccionado;
    }

    public int getIdPresentacion() {
        return idPresentacionSeleccionada;
    }

    public double getPrecio() {
        return precioUnitarioSeleccionado;
    }

    public int getCant() {
        return cantidadSeleccionada;
    } // Retornará 1 por defecto

    public int getDesc() {
        return descuentoAplicado;
    }

    public int getStock() {
        return stockSeleccionado;
    }

    public String getNombre() {
        return nombreSeleccionado;
    }

    public void setBuscando(boolean busc) {
        buscando = busc;
    }

    private void pasarDatos(int idProd, int idPres, double precio, int stock, String nombre) {
        this.idProductoSeleccionado = idProd;
        this.idPresentacionSeleccionada = idPres;
        this.precioUnitarioSeleccionado = precio;
        this.stockSeleccionado = stock;
        this.nombreSeleccionado = nombre;
        this.cantidadSeleccionada = 1; // Por defecto 1
        this.descuentoAplicado = 0;    // Por defecto 0

        this.dispose(); // Cierra la ventana
    }

    class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((String) value);
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    }

    private void listarProductos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacemos que no se pueda editar al dar click
            }
        };
        modelo.addColumn("CÓDIGO");       // 0
        modelo.addColumn("NOMBRE");       // 1
        modelo.addColumn("LABORATORIO");  // 2
        modelo.addColumn("PRESENTACIÓN"); // 3
        modelo.addColumn("PRECIO");       // 4
        modelo.addColumn("MARCA");        // 5
        modelo.addColumn("CATEGORÍA");    // 6
        modelo.addColumn("STOCK");        // 7
        modelo.addColumn("ID_PRES");

        try {
            String nombre = txtNombre.getText();
            String marcaSeleccionada = cbxMarca.getSelectedItem().toString();
            String labSeleccionado = cbxLaboratorio.getSelectedItem().toString();
            String catSeleccionada = cbxCategoria.getSelectedItem().toString();
            double min = Double.parseDouble(spnMin.getValue().toString());
            double max = Double.parseDouble(spnMax.getValue().toString());
            
            String filtroMarca = marcaSeleccionada.equals("--Ninguno--") ? "" : marcaSeleccionada;
            String filtroLaboratorio = labSeleccionado.equals("--Ninguno--") ? "" : labSeleccionado;
            String filtroCategoria = catSeleccionada.equals("--Ninguno--") ? "" : catSeleccionada;

            ResultSet rs = objProducto.filtrarAvanzado(nombre, filtroMarca, filtroLaboratorio, filtroCategoria, min, max);

            while (rs.next()) {
                // 2. AGREGAR LOS DATOS (Aquí también faltaba el último dato)
                modelo.addRow(new Object[]{
                    rs.getInt("idproducto"),
                    rs.getString("nombre"),
                    rs.getString("nombrelaboratorio"),
                    rs.getString("presentacion"),
                    rs.getDouble("precio"),
                    rs.getString("nombreMarca"),
                    rs.getString("nombreCategoria"),
                    rs.getInt("stock"),
                    rs.getInt("idpresentacion") // <--- ESTO ES CRUCIAL
                });
            }

            tblProducto1.setModel(modelo);

            tblProducto1.setDefaultRenderer(String.class, new TextAreaRenderer());
            tblProducto1.getColumnModel().getColumn(3).setCellRenderer(new TextAreaRenderer());

            // 3. OCULTAR LA COLUMNA 8 VISUALMENTE (Ancho 0)
            tblProducto1.getColumnModel().getColumn(8).setMinWidth(0);
            tblProducto1.getColumnModel().getColumn(8).setMaxWidth(0);
            tblProducto1.getColumnModel().getColumn(8).setWidth(0);

            // Ajuste de altura de filas
            for (int i = 0; i < tblProducto1.getRowCount(); i++) {
                int rowHeight = tblProducto1.getRowHeight(i);
                Component comp = tblProducto1.prepareRenderer(tblProducto1.getCellRenderer(i, 3), i, 3);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                tblProducto1.setRowHeight(i, rowHeight);
            }

            lblTotalProductos.setText(String.valueOf(modelo.getRowCount()));

        } catch (NumberFormatException nfe) {
             // Ignorar errores de tipeo en los spinners
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error listando productos: " + e.getMessage());
        }
    }

    private void cargarComboCategorias() {

        try {
            DefaultComboBoxModel modeloCat = new DefaultComboBoxModel();
            ResultSet rsCar = objCategoria.listarCategoriasActivas();
            modeloCat.addElement("--Ninguno--");
            while (rsCar.next()) {
                modeloCat.addElement(rsCar.getString("nombrecategoria"));
            }
            cbxCategoria.setModel(modeloCat);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar categoria en el combobox");
        }
    }

    private void cargarComboMarcas() {

        try {
            DefaultComboBoxModel modeloMar = new DefaultComboBoxModel();
            ResultSet rsMar = objMarca.listarMarcasActivas();
            modeloMar.addElement("--Ninguno--");
            while (rsMar.next()) {
                modeloMar.addElement(rsMar.getString("nombre"));
            }
            cbxMarca.setModel(modeloMar);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar marcas en el combobox");
        }
    }

    private void cargarComboLaboratorio() {

        try {
            DefaultComboBoxModel modeloLab = new DefaultComboBoxModel();
            ResultSet rsLab = objLaboratorio.listarLaboratoriosActivos();
            modeloLab.addElement("--Ninguno--");
            while (rsLab.next()) {
                modeloLab.addElement(rsLab.getString("nombrelaboratorio"));
            }
            cbxLaboratorio.setModel(modeloLab);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar laboratorio en el combobox");
        }
    }

    public jdBusqAvanzada(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        cargarComboLaboratorio();
        cargarComboMarcas();
        cargarComboCategorias();

        // Configurar Spinners (valores iniciales)
        spnMin.setValue(0.0); // Usamos 0.0 porque el precio es double/float
        spnMax.setValue(1000.0);
        listarProductos();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxMarca = new javax.swing.JComboBox<>();
        cbxLaboratorio = new javax.swing.JComboBox<>();
        cbxCategoria = new javax.swing.JComboBox<>();
        spnMin = new javax.swing.JSpinner();
        spnMax = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lblTotalProductos = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Marca:");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Laboratorio:");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Categoria:");

        cbxMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMarcaActionPerformed(evt);
            }
        });

        cbxLaboratorio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLaboratorioActionPerformed(evt);
            }
        });

        cbxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCategoriaActionPerformed(evt);
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

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nombre:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(spnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spnMax, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNombre)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbxLaboratorio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbxMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbxLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tblProducto1.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProducto1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProducto1MouseClicked(evt);
            }
        });
        tblProducto1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProducto1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto1);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("Total de productos:");

        lblTotalProductos.setBackground(new java.awt.Color(255, 255, 255));
        lblTotalProductos.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        lblTotalProductos.setText("N° Productos");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/cerrar-sesion (1).png"))); // NOI18N
        btnSalir.setText("Salir");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalProductos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalProductos)
                    .addComponent(btnSalir)
                    .addComponent(jButton2))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMarcaActionPerformed
        listarProductos();
    }//GEN-LAST:event_cbxMarcaActionPerformed

    private void cbxLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLaboratorioActionPerformed
        listarProductos();
    }//GEN-LAST:event_cbxLaboratorioActionPerformed

    private void cbxCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoriaActionPerformed
        listarProductos();
    }//GEN-LAST:event_cbxCategoriaActionPerformed

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        listarProductos();
    }//GEN-LAST:event_txtNombreKeyReleased

    private void spnMinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMinStateChanged
        listarProductos();
    }//GEN-LAST:event_spnMinStateChanged

    private void spnMaxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMaxStateChanged
        listarProductos();
    }//GEN-LAST:event_spnMaxStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cbxMarca.setSelectedIndex(0);
        cbxCategoria.setSelectedIndex(0);
        cbxLaboratorio.setSelectedIndex(0);
        txtNombre.setText("");
        spnMin.setValue(0);
        spnMax.setValue(0);
        listarProductos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        if (buscando) {
            dispose();
        }
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tblProducto1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProducto1KeyPressed

    }//GEN-LAST:event_tblProducto1KeyPressed

    private void tblProducto1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProducto1MouseClicked
        if (evt.getClickCount() == 2) { // Solo si es doble click
            int fila = tblProducto1.getSelectedRow();

            if (fila >= 0) {
                try {
                    // --- CORRECCIÓN IMPORTANTE ---
                    // Agregamos .getModel() antes de .getValueAt()
                    // Esto accede a los datos "crudos", ignorando si la columna está oculta o no.
                    
                    // Columna 0: ID PRODUCTO
                    int idProd = Integer.parseInt(tblProducto1.getModel().getValueAt(fila, 0).toString());
                    
                    // Columna 1: NOMBRE
                    String nom = tblProducto1.getModel().getValueAt(fila, 1).toString();
                    
                    // Columna 4: PRECIO
                    double prec = Double.parseDouble(tblProducto1.getModel().getValueAt(fila, 4).toString());
                    
                    // Columna 7: STOCK
                    int stk = Integer.parseInt(tblProducto1.getModel().getValueAt(fila, 7).toString());
                    
                    // Columna 8: ID PRESENTACION (La columna oculta que daba error)
                    int idPres = Integer.parseInt(tblProducto1.getModel().getValueAt(fila, 8).toString());

                    pasarDatos(idProd, idPres, prec, stk, nom);

                } catch (Exception e) {
                    System.out.println("Error al capturar datos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_tblProducto1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxCategoria;
    private javax.swing.JComboBox<String> cbxLaboratorio;
    private javax.swing.JComboBox<String> cbxMarca;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalProductos;
    private javax.swing.JSpinner spnMax;
    private javax.swing.JSpinner spnMin;
    private javax.swing.JTable tblProducto1;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
