package Capa_Presentacion;

import Capa_Negocio.clsCliente;
import Capa_Negocio.clsProducto;
import Capa_Negocio.clsVenta;
import java.awt.Frame;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class jdVenta extends javax.swing.JDialog {

    int idProductoSeleccionado = 0;
    int idPresentacionSeleccionada = 0;
    double precioSeleccionado = 0.0;
    int stockSeleccionado = 0;
    boolean ventaBloqueada = false;

    public jdVenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        txtUsuario.setText(FrmLogin.nomUser);
        txtUsuario.setEditable(false);
        llenarTablaInicial();
        jDateChooser1.setDate(new java.util.Date());
        mostrarNumVenta();
    }

    clsVenta objVenta = new clsVenta();
    clsProducto objProducto = new clsProducto();
    clsCliente objCliente = new clsCliente();

    @SuppressWarnings("unchecked")
    private void mostrarNumVenta() {
        try {
            txtNumero.setText(String.valueOf(objVenta.generarIdVenta()));
        } catch (Exception e) {
            txtNumero.setText("1");
        }
    }

    private void limpiarProductoSeleccionado() {
        // Reseteamos variables
        idProductoSeleccionado = 0;
        idPresentacionSeleccionada = 0;
        precioSeleccionado = 0.0;
        stockSeleccionado = 0;
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        spnCantidad.setValue(0);
        txtCodigoProducto1.setText("");
        tblDetalle.clearSelection();
    }

    private void limpiarVentaCompleta() {
        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
        modelo.setRowCount(0);
        txtSubTotal.setText("");
        txtIGV.setText("");
        txtTotal.setText("");
        txtDniRuc.setText("");
        txtCodigoCliente.setText("");
        txtNombreCliente.setText("");
        txtDireccion.setText("");
        jComboBox1.setSelectedIndex(0);
        limpiarProductoSeleccionado();
        mostrarNumVenta();
        ventaBloqueada = false;  
        btnGuardar.setEnabled(true);
        btnAgregarProducto.setEnabled(true);
        btnQuitar1.setEnabled(true);
        txtDniRuc.requestFocus();
    }

    private void llenarTablaInicial() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacemos que la tabla no sea editable directamente
            }
        };
        modelo.addColumn("ID Prod");      // Columna 0
        modelo.addColumn("ID Pres");      // Columna 1 (Necesario para la BD)
        modelo.addColumn("Nombre");       // Columna 2
        modelo.addColumn("Precio Base");  // Columna 3
        modelo.addColumn("Cantidad");     // Columna 4
        modelo.addColumn("Desc %");       // Columna 5
        modelo.addColumn("Precio Final"); // Columna 6
        modelo.addColumn("Subtotal");     // Columna 7

        tblDetalle.setModel(modelo);

    }

    private void eliminarProducto() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
            int filaSeleccionada = tblDetalle.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Primero seleccione el producto que desea quitar de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Quitar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modelo.removeRow(filaSeleccionada);
                calcularTotal();
                limpiarProductoSeleccionado(); // Limpia las cajas de arriba
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error al eliminar: " + e.getMessage());
        }
    }

    private void agregarProducto(int idProducto, int idPresentacion, double precio, int cantidad, int descuento, String nombreProducto) {
        if (idProducto != 0 && cantidad != 0) {
            try {
                DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
                boolean existe = false;

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    int idProdTabla = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                    int idPresTabla = Integer.parseInt(modelo.getValueAt(i, 1).toString());

                    // Si el producto Y la presentación coinciden, actualizamos
                    if (idProdTabla == idProducto && idPresTabla == idPresentacion) {
                        int cantActual = Integer.parseInt(modelo.getValueAt(i, 4).toString());
                        int nuevaCantidad = cantActual + cantidad;
                        modelo.setValueAt(nuevaCantidad, i, 4); // Actualizar Cantidad

                        // Recalcular Importe
                        double precioFinal = Double.parseDouble(modelo.getValueAt(i, 6).toString().replace(",", "."));
                        double nuevoSubtotal = nuevaCantidad * precioFinal;
                        modelo.setValueAt(String.format("%.2f", nuevoSubtotal), i, 7);

                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    double precioConDescuento = precio - (precio * (descuento / 100.0));
                    double subtotal = cantidad * precioConDescuento;

                    modelo.addRow(new Object[]{
                        idProducto, // 0: ID Prod
                        idPresentacion, // 1: ID Pres
                        nombreProducto, // 2: Nombre
                        String.format("%.2f", precio).replace(",", "."),// 3: Precio Base
                        cantidad, // 4: Cantidad
                        descuento, // 5: Descuento
                        String.format("%.2f", precioConDescuento).replace(",", "."), // 6: Precio Final
                        String.format("%.2f", subtotal).replace(",", ".") // 7: Subtotal
                    });
                }

                tblDetalle.setModel(modelo);
                calcularTotal(); // Actualizar los totales generales

            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, "Error al agregar producto: " + e.getMessage());
            }
        }
    }

    private void calcularTotal() {
        try {
            double totalGeneral = 0.0;

            for (int i = 0; i < tblDetalle.getRowCount(); i++) {
                String valorFila = String.valueOf(tblDetalle.getValueAt(i, 7)).replace(",", ".");
                totalGeneral += Double.parseDouble(valorFila);
            }
            double subtotalBase = totalGeneral / 1.18;
            double montoIgv = totalGeneral - subtotalBase;

            // 3. Mostrar en las cajas de texto con 2 decimales
            // Usamos replace para asegurar el formato compatible con la BD
            txtSubTotal.setText(String.format("%.2f", subtotalBase).replace(",", "."));
            txtIGV.setText(String.format("%.2f", montoIgv).replace(",", "."));
            txtTotal.setText(String.format("%.2f", totalGeneral).replace(",", "."));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error calculando totales: " + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        btnBuscarVenta1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtDniRuc = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtCodigoCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        rdbBoleta = new javax.swing.JRadioButton();
        rdbFactura = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        txtNombreProducto = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        spnCantidad = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        txtCodigoProducto1 = new javax.swing.JTextField();
        btnQuitar1 = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        txtSubTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIGV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel12.setText("Vendedor:");

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel13.setText("Fecha:");

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel14.setText("Número:");

        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        btnBuscarVenta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar1.png"))); // NOI18N
        btnBuscarVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVenta1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Datos Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("DNI/RUC :");

        txtDniRuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDniRucKeyPressed(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar-usuario.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CLIENTE:");

        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyPressed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DIRECCION:");

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TIPO CLIENTE:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Natural", "Empresa" }));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("COMPROBANTE:");

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("TIPO DE");

        rdbBoleta.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        rdbBoleta.setForeground(new java.awt.Color(255, 255, 255));
        rdbBoleta.setText("Boleta");
        rdbBoleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbBoletaActionPerformed(evt);
            }
        });

        rdbFactura.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        rdbFactura.setForeground(new java.awt.Color(255, 255, 255));
        rdbFactura.setText("Factura");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtDniRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreCliente))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jLabel15))
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdbBoleta)
                        .addGap(18, 18, 18)
                        .addComponent(rdbFactura)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscar)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDniRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdbBoleta)
                            .addComponent(rdbFactura))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Detalle de venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("PRODUCTO:");

        btnBuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar1.png"))); // NOI18N
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("DESCUENTO:");

        btnQuitar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/quitar.png"))); // NOI18N
        btnQuitar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnQuitar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitar1ActionPerformed(evt);
            }
        });

        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar.png"))); // NOI18N
        btnAgregarProducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("CANTIDAD:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(55, 55, 55))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(23, 23, 23)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(spnCantidad)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 156, Short.MAX_VALUE)))
                        .addGap(22, 22, 22))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnQuitar1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addComponent(btnBuscarProducto, javax.swing.GroupLayout.Alignment.LEADING))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(txtCodigoProducto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuitar1)
                    .addComponent(btnAgregarProducto))
                .addGap(0, 0, 0))
        );

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDetalle);

        jLabel1.setText("Subtotal :");

        jLabel2.setText("IGV :");

        jLabel3.setText("TOTAL : ");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        btnAnular.setText("Anular");

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/salirMarca.png"))); // NOI18N
        btnSalir.setText("Salir");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscarVenta1)))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAnular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBuscarVenta1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAnular)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir)))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void btnBuscarVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVenta1ActionPerformed
        ResultSet rsVenta = null;
        ResultSet rsDetalle = null;

        try {
            // 1. Buscar la Cabecera
            int nroVenta = Integer.parseInt(txtNumero.getText());
            rsVenta = objVenta.buscarVenta(nroVenta);

            if (rsVenta.next()) {
                // Si encontramos la venta, limpiamos la tabla actual primero
                llenarTablaInicial(); 
                DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
                
                // Cargar datos del cliente
                txtDniRuc.setText(rsVenta.getString("nroDoc"));
                // Disparamos la búsqueda del cliente para llenar nombre y dirección
                btnBuscarActionPerformed(evt); 

                // Poner la fecha real de esa venta
                jDateChooser1.setDate(rsVenta.getDate("fecha"));
                
                // 2. Buscar y Llenar el Detalle
                rsDetalle = objVenta.listarDetalleVenta(nroVenta);

                while (rsDetalle.next()) {
                    int descuento = 0; // Si guardaste descuento en BD, recupéralo aquí
                    int idProducto = rsDetalle.getInt("idProducto");
                    // OJO: Si no guardaste idPresentacion en detalle_venta, esto podría fallar.
                    // Asumiremos que en tu BD 'detalle_venta' tiene esa columna o usas 1 por defecto.
                    int idPresentacion = rsDetalle.getInt("idPresentacion"); 
                    
                    double precioBase = rsDetalle.getDouble("precioUnitarioVenta");
                    int cantidad = rsDetalle.getInt("cantidad");
                    
                    // Cálculos para visualizar
                    double precioFinalUnitario = precioBase; 
                    double subtotal = precioFinalUnitario * cantidad;

                    modelo.addRow(new Object[]{
                        String.valueOf(idProducto),
                        String.valueOf(idPresentacion),
                        rsDetalle.getString("nombreProducto"),
                        String.format("%.2f", precioBase).replace(",", "."),
                        cantidad,
                        descuento + "", // Mostrar 0 si no hay columna descuento
                        String.format("%.2f", precioFinalUnitario).replace(",", "."),
                        String.format("%.2f", subtotal).replace(",", ".")
                    });
                }
                
                tblDetalle.setModel(modelo);

                // --- SOLUCIÓN 1: Cargar los Totales ---
                calcularTotal(); // <--- ESTA LÍNEA FALTABA PARA VER LOS MONTOS

                // --- SOLUCIÓN 2: Bloquear para no re-guardar ---
                ventaBloqueada = true;
                btnGuardar.setEnabled(false); // Desactivar botón guardar
                btnAgregarProducto.setEnabled(false); // No modificar venta vieja
                btnQuitar1.setEnabled(false);
                
                JOptionPane.showMessageDialog(this, "Venta N° " + nroVenta + " cargada (Solo Lectura).");

            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la venta N° " + nroVenta);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnBuscarVenta1ActionPerformed

    private void txtDniRucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniRucKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            if (txtDniRuc.getText().length() == 8 || txtDniRuc.getText().length() == 11 || txtDniRuc.getText().length() == 20) {
                btnBuscar.doClick();
                btnBuscarActionPerformed(null);
            } else {
                JOptionPane.showMessageDialog(this, "Ingresar DNI (8 digitos) / RUC (11 O 20 digitos)");
            }
        }
    }//GEN-LAST:event_txtDniRucKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        String nroDocumento = txtDniRuc.getText().trim();

        if (nroDocumento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de documento.");
            return;
        }

        try {
            boolean esDni = nroDocumento.length() == 8;
            ResultSet rsCliente = objCliente.buscarClienteDniRuc(nroDocumento, esDni);

            if (rsCliente.next()) {
                int idCliente = rsCliente.getInt("idcliente");
                String nombrePersona = rsCliente.getString("nombres");

                String apellidoPaterno = rsCliente.getString("apellidopaterno"); // Según tu BD

                String razonSocial = rsCliente.getString("razonsocial"); // Según tu BD

                String direccion = rsCliente.getString("direccion");

                if (razonSocial != null && !razonSocial.isEmpty()) {
                    txtNombreCliente.setText(razonSocial);
                    jComboBox1.setSelectedItem("Empresa");
                    rdbFactura.setSelected(true);
                    rdbBoleta.setSelected(false);

                } else {
                    txtNombreCliente.setText(nombrePersona + " " + apellidoPaterno);
                    jComboBox1.setSelectedItem("Natural");
                    rdbBoleta.setSelected(true);
                    rdbFactura.setSelected(false);
                }

                txtCodigoCliente.setText(String.valueOf(idCliente));

                txtDireccion.setText(direccion);

            } else {
                int rpta = JOptionPane.showConfirmDialog(this,
                        "El cliente con documento " + nroDocumento + " no existe.\n¿Desea registrarlo ahora?",
                        "Cliente No Encontrado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (rpta == JOptionPane.YES_OPTION) {

                    ManCliente objManCliente = new ManCliente((Frame) SwingUtilities.getWindowAncestor(this), true);

                    objManCliente.setLocationRelativeTo(this);

                    objManCliente.setVisible(true);
                    rsCliente = objCliente.buscarClienteDniRuc(nroDocumento, esDni);

                    if (rsCliente.next()) {

                        int idCliente = rsCliente.getInt("idcliente");

                        String nombre = rsCliente.getString("nombres");

                        String ape = rsCliente.getString("apellidopaterno");

                        String raz = rsCliente.getString("razonsocial");
                        if (raz != null) {
                            txtNombreCliente.setText(raz);
                            rdbFactura.setSelected(true);
                            rdbBoleta.setSelected(false);
                            rdbBoleta.setEnabled(true);
                            rdbFactura.setEnabled(true);
                        } else {
                            txtNombreCliente.setText(nombre + " " + ape);
                            rdbBoleta.setSelected(true);
                            rdbFactura.setSelected(false);
                            rdbBoleta.setEnabled(true);
                            rdbFactura.setEnabled(true);

                        }
                        txtCodigoCliente.setText(String.valueOf(idCliente));
                        txtDireccion.setText(rsCliente.getString("direccion"));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error al buscar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtNombreClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteKeyPressed

    private void rdbBoletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbBoletaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbBoletaActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        jdAñadirProducto objBusqueda = new jdAñadirProducto((Frame) SwingUtilities.getWindowAncestor(this), true);
        objBusqueda.setLocationRelativeTo(this);
        objBusqueda.setVisible(true);

        int idProd = objBusqueda.getCod();

        if (idProd > 0) { // Si seleccionó algo
            idProductoSeleccionado = idProd;
            idPresentacionSeleccionada = objBusqueda.getIdPresentacion();
            precioSeleccionado = objBusqueda.getPrecio();
            stockSeleccionado = objBusqueda.getStock();
            txtCodigoProducto.setText(String.valueOf(idProd));
            txtNombreProducto.setText(objBusqueda.getNombre());
            spnCantidad.setValue(1);
            txtCodigoProducto1.setText("0");
            spnCantidad.requestFocus();
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnQuitar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitar1ActionPerformed
        eliminarProducto();
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        spnCantidad.setValue(0);
    }//GEN-LAST:event_btnQuitar1ActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        if (idProductoSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Busque un producto primero.");
            return;
        }

        int cantidad = Integer.parseInt(spnCantidad.getValue().toString());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }

        if (cantidad > stockSeleccionado) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + stockSeleccionado);
            return;
        }

        int descuento = 0;
        try {
            descuento = Integer.parseInt(txtCodigoProducto1.getText());
        } catch (Exception e) {
            descuento = 0;
        }

        try {
            agregarProducto(idProductoSeleccionado, idPresentacionSeleccionada, precioSeleccionado, cantidad, descuento, txtNombreProducto.getText());
            limpiarProductoSeleccionado();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void tblDetalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalleMouseClicked
        int fila = tblDetalle.getSelectedRow();

        if (fila >= 0) {
            try {
                String idProd = tblDetalle.getValueAt(fila, 0).toString();
                String idPres = tblDetalle.getValueAt(fila, 1).toString();
                String nombre = tblDetalle.getValueAt(fila, 2).toString();
                String precioBase = tblDetalle.getValueAt(fila, 3).toString();
                String cantidad = tblDetalle.getValueAt(fila, 4).toString();
                String descuento = tblDetalle.getValueAt(fila, 5).toString();
                txtCodigoProducto.setText(idProd);
                txtNombreProducto.setText(nombre);
                spnCantidad.setValue(Integer.parseInt(cantidad));
                txtCodigoProducto1.setText(descuento);
                idProductoSeleccionado = Integer.parseInt(idProd);
                idPresentacionSeleccionada = Integer.parseInt(idPres);
                precioSeleccionado = Double.parseDouble(precioBase.replace(",", "."));
                try {
                    stockSeleccionado = objProducto.getStockPresentacion(idProductoSeleccionado, idPresentacionSeleccionada);
                } catch (Exception ex) {
                    stockSeleccionado = 9999;
                }

            } catch (Exception e) {
                System.out.println("Error al seleccionar fila: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_tblDetalleMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (ventaBloqueada) {
            JOptionPane.showMessageDialog(this, "Esta venta ya existe y no se puede modificar/guardar de nuevo.", "Acción Bloqueada", JOptionPane.WARNING_MESSAGE);
            return;
        } 
        
        if (tblDetalle.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "La lista de venta está vacía. Agregue productos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtCodigoCliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar y seleccionar un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            btnBuscar.requestFocus();
            return;
        }

        try {
            int idCliente = Integer.parseInt(txtCodigoCliente.getText());
            int idUsuario = FrmLogin.idUsuarioLogueado;
            if (idUsuario == 0) {
                idUsuario = 1;
            }

            boolean esBoleta = rdbBoleta.isSelected();
            DefaultTableModel modeloActual = (DefaultTableModel) tblDetalle.getModel();
            mantComprobanteVenta ventanaCobro = new mantComprobanteVenta(
                    (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                    true,
                    idCliente,
                    idUsuario,
                    esBoleta,
                    txtTotal.getText(), // Pasamos los totales como texto
                    txtSubTotal.getText(),
                    txtIGV.getText(),
                    modeloActual // Pasamos los productos
            );

            ventanaCobro.setVisible(true); 
            if (ventanaCobro.ventaExitosa) {
                limpiarVentaCompleta();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error al abrir caja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnBuscarVenta1;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitar1;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbBoleta;
    private javax.swing.JRadioButton rdbFactura;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtCodigoCliente;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCodigoProducto1;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDniRuc;
    private javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
