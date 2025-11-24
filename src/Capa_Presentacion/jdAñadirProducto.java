package Capa_Presentacion;

import Capa_Negocio.clsProducto;
import java.awt.Frame;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Grupo 01
 */
public class jdAñadirProducto extends javax.swing.JDialog {

    clsProducto objProducto = new clsProducto();

    private int cod = 0;
    private int idPres = 0;
    private double precio = 0.0;

    public int getCod() {
        return cod;
    }

    public int getIdPresentacion() {
        return idPres;
    }

    public double getPrecio() {
        return precio;
    }

    public jdAñadirProducto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void listarProductos() {
        ResultSet rs = null;
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("CÓDIGO");        // 0
        modelo.addColumn("NOMBRE");        // 1
        modelo.addColumn("MARCA");         // 2
        modelo.addColumn("CATEGORÍA");     // 3
        modelo.addColumn("PRESENTACIÓN");  // 4 (Ej: Caja x 20)
        modelo.addColumn("PRECIO");        // 5
        modelo.addColumn("STOCK");         // 6
        modelo.addColumn("ID_PRES");       // 7 (Oculta, para lógica interna)

        try {
            rs = objProducto.listarParaVenta(txtNombreProducto.getText());

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getString("nombremarca"),
                    rs.getString("nombrecategoria"),
                    rs.getString("presentacion"), // Columna generada en el SQL
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("idpresentacion") // Dato oculto necesario
                });
            }
            tblProducto.setModel(modelo);

            tblProducto.getColumnModel().getColumn(7).setMinWidth(0);
            tblProducto.getColumnModel().getColumn(7).setMaxWidth(0);
            tblProducto.getColumnModel().getColumn(7).setWidth(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }

    private int stock = 0;
    private String nombre = "";

    public int getStock() { return stock; }
    public String getNombre() { return nombre; }

    private void pasarDatos(int pid, int prid, double pPrecio, int pStock, String pNombre) {
        this.cod = pid;
        this.idPres = prid;
        this.precio = pPrecio;
        this.stock = pStock; 
        this.nombre = pNombre;
        
        this.dispose(); // Cierra la ventana y devuelve el control a jdVenta1
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNombreProducto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        btnBusqueda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(153, 204, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        txtNombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreProductoKeyReleased(evt);
            }
        });

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto);

        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar1.png"))); // NOI18N
        btnBusqueda.setText("BÚSQUEDA AVANZADA");
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(btnBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        listarProductos();
    }//GEN-LAST:event_formWindowActivated

    private void txtNombreProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProductoKeyReleased
        listarProductos();
    }//GEN-LAST:event_txtNombreProductoKeyReleased

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        if (evt.getClickCount() == 2) { // Doble clic
            int fila = tblProducto.getSelectedRow();
            if (fila >= 0) {
                int pid = Integer.parseInt(tblProducto.getValueAt(fila, 0).toString());
                String pNombre = tblProducto.getValueAt(fila, 1).toString();
                double pPrecio = Double.parseDouble(tblProducto.getValueAt(fila, 5).toString());
                int pStock = Integer.parseInt(tblProducto.getValueAt(fila, 6).toString());
                int prid = Integer.parseInt(tblProducto.getValueAt(fila, 7).toString()); // ID Pres oculto
                pasarDatos(pid, prid, pPrecio, pStock, pNombre);
            }
        }
    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
        jdBusqAvanzada objConsulta = new jdBusqAvanzada((Frame) SwingUtilities.getWindowAncestor(this), true);
        objConsulta.setBuscando(true); 
        objConsulta.setVisible(true);

        int idProd = objConsulta.getCod();

        if (idProd > 0) {
            int idPres = objConsulta.getIdPresentacion();
            double prec = objConsulta.getPrecio();
            
            // Opcional: Buscar nombre y stock para mostrarlos mejor
            String nombreProd = "";
            int stockProd = 0;
            try {
                ResultSet rs = objProducto.buscarProducto(idProd);
                if(rs.next()) nombreProd = rs.getString("nombre");
                stockProd = objProducto.getStockPresentacion(idProd, idPres);
            } catch (Exception ex) {}
            pasarDatos(idProd, idPres, prec, stockProd, nombreProd);
        }
    }//GEN-LAST:event_btnBusquedaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtNombreProducto;
    // End of variables declaration//GEN-END:variables

}
