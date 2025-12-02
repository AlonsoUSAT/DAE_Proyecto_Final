package Capa_Presentacion;

import Capa_Negocio.clsCliente;
import java.sql.*;
import Capa_Negocio.clsMedioPago;
import Capa_Negocio.clsSerieVenta;
import Capa_Negocio.clsVenta;
import javax.swing.table.DefaultTableModel;

public class mantComprobanteVenta extends javax.swing.JDialog {

    private int idCliente;
    private int idUsuario;
    private boolean esBoleta;
    private String totalVenta;
    private String subTotalVenta;
    private String igvVenta;

    public boolean ventaExitosa = false;
    private double totalNumerico = 0.0;
    private double saldoRestante = 0.0;

    clsVenta objVenta = new clsVenta();
    clsCliente objCliente = new clsCliente();
    clsMedioPago objMedioPago = new clsMedioPago();
    clsSerieVenta objSerie = new clsSerieVenta();
    private DefaultTableModel modeloProductosRecibido;

    public mantComprobanteVenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public mantComprobanteVenta(java.awt.Frame parent, boolean modal,
            int idCliente, int idUsuario, boolean esBoleta,
            String total, String subTotal, String igv, DefaultTableModel modeloTabla) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.esBoleta = esBoleta;
        this.totalVenta = total;
        this.subTotalVenta = subTotal;
        this.igvVenta = igv;

        txtTotal.setText(total);
        txtSubTotal.setText(subTotal);
        txtIGV.setText(igv);
        txtTotal.setEditable(false);
        txtSubTotal.setEditable(false);
        txtIGV.setEditable(false);
        txtNomCliente.setEditable(false);
        txtNumDoc.setEditable(false);
        txtDirec.setEditable(false);
        txtUsuario.setText(FrmLogin.getNomUser());
        txtFecha.setDate(new java.util.Date());
        tblPagos.setModel(modeloTabla);
        this.modeloProductosRecibido = modeloTabla;

        try {
            this.totalNumerico = Double.parseDouble(total.replace(",", "."));
            this.saldoRestante = this.totalNumerico;
        } catch (Exception e) {
            totalNumerico = 0.0;
            saldoRestante = 0.0;
        }
        inicializarTablaPagos();
        actualizarEtiquetaRestante();
        txtMonto.setText(String.format("%.2f", saldoRestante).replace(",", "."));
        cargarDatosCliente();
        cargarComboMedioPago();
        mostrarSerieCorrelativo();
    }

    private void inicializarTablaPagos() {
        // Configuramos el modelo de la tabla tblPagos
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Que no se pueda editar
            }
        };
        modelo.addColumn("ID");      // Col 0: ID del medio de pago (BD)
        modelo.addColumn("Medio");   // Col 1: Nombre (Efectivo, Yape...)
        modelo.addColumn("Monto");   // Col 2: Monto pagado

        tblPagos.setModel(modelo);

        // Opcional: Ocultar la columna ID visualmente (ancho 0)
        tblPagos.getColumnModel().getColumn(0).setMinWidth(0);
        tblPagos.getColumnModel().getColumn(0).setMaxWidth(0);
        tblPagos.getColumnModel().getColumn(0).setWidth(0);
    }

    private void actualizarEtiquetaRestante() {
        // Mostramos el restante con 2 decimales
        lblRestante.setText("RESTANTE: S/. " + String.format("%.2f", saldoRestante));

        // Cambiar color para alerta visual
        if (saldoRestante <= 0.01) {
            lblRestante.setForeground(new java.awt.Color(0, 153, 51)); // Verde
            lblRestante.setText("¡PAGO COMPLETADO!");

            // Habilitar botón guardar, bloquear ingresos
            jButton1.setEnabled(true);
            btnAgregarMetodoPago.setEnabled(false);
            txtMonto.setEnabled(false);
            txtMonto.setText("0.00");
        } else {
            lblRestante.setForeground(java.awt.Color.RED); // Rojo

            // Deshabilitar guardar, habilitar ingresos
            jButton1.setEnabled(false);
            btnAgregarMetodoPago.setEnabled(true);
            txtMonto.setEnabled(true);
        }
    }

    private void cargarDatosCliente() {
        try {
            ResultSet rs = objCliente.buscarClientePorId(idCliente);

            if (rs.next()) {
                String razonSocial = rs.getString("razonsocial");
                String nombres = rs.getString("nombres");
                String apellidoP = rs.getString("apellidopaterno");

                if (razonSocial != null && !razonSocial.trim().isEmpty()) {
                    txtNomCliente.setText(razonSocial);
                } else {
                    txtNomCliente.setText(nombres + " " + apellidoP);
                }

                txtNumDoc.setText(rs.getString("nrodoc"));
                txtDirec.setText(rs.getString("direccion"));
            }
        } catch (Exception e) {
            System.out.println("Error al cargar datos del cliente: " + e.getMessage());
        }
    }

    private void cargarComboMedioPago() {
        try {
            // 1. Limpiamos items anteriores por seguridad
            cbxMedioPago.removeAllItems();

            // 2. Traemos la lista de la BD
            ResultSet rs = objMedioPago.listarMediosPago();

            // 3. Recorremos y llenamos el combo
            while (rs.next()) {
                cbxMedioPago.addItem(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.out.println("Error al cargar combo de pagos: " + e.getMessage());
        }
    }

    private void mostrarSerieCorrelativo() {
        try {
            int idTipo = esBoleta ? 1 : 2;
            ResultSet rs = objSerie.obtenerSerieActiva(idTipo);

            if (rs.next()) {
                lblSerie.setText(rs.getString("serie"));
                int siguienteNumero = rs.getInt("correlativo");
                lblCorrelativo.setText(String.format("%08d", siguienteNumero));

            } else {
                lblSerie.setText("----");
                lblCorrelativo.setText("----");
            }
        } catch (Exception e) {
            lblSerie.setText("ERR");
            lblCorrelativo.setText("ERR");
            System.out.println("Error al mostrar serie: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFecha = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblSerie = new javax.swing.JLabel();
        lblCorrelativo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNomCliente = new javax.swing.JTextField();
        txtNumDoc = new javax.swing.JTextField();
        txtDirec = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        cbxMedioPago = new javax.swing.JComboBox<>();
        txtMonto = new javax.swing.JTextField();
        btnAgregarMetodoPago = new javax.swing.JButton();
        btnQuitarMetodoPago = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lblRestante = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtIGV = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Metodo de pago:");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/logo.png"))); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel3.setFont(new java.awt.Font("Yu Gothic", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Farmacia Sin Nombre");

        jLabel4.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Usuario:");

        lblSerie.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSerie.setForeground(new java.awt.Color(255, 255, 255));
        lblSerie.setText("jLabel5");

        lblCorrelativo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCorrelativo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorrelativo.setText("jLabel5");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(lblSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCorrelativo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCorrelativo)
                            .addComponent(lblSerie))
                        .addGap(5, 5, 5)
                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(201, 232, 229));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14)); // NOI18N
        jLabel2.setText("Comprobante de pago");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2))
        );

        jPanel2.setBackground(new java.awt.Color(232, 243, 255));

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Nro. Documento:");

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Cliente:");

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Direccion:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNumDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDirec, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNomCliente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtNomCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDirec)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtNumDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Medio Pago:");

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Monto:");

        tblPagos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPagos);

        txtMonto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoActionPerformed(evt);
            }
        });

        btnAgregarMetodoPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/agregar.png"))); // NOI18N
        btnAgregarMetodoPago.setText("Agregar Medio Pago");
        btnAgregarMetodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMetodoPagoActionPerformed(evt);
            }
        });

        btnQuitarMetodoPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/quitar.png"))); // NOI18N
        btnQuitarMetodoPago.setText("Quitar Medio Pago");
        btnQuitarMetodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarMetodoPagoActionPerformed(evt);
            }
        });

        jLabel5.setText("RESTANTE POR PAGAR:");

        lblRestante.setText("RESTANTE POR PAGAR:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxMedioPago, 0, 233, Short.MAX_VALUE)
                            .addComponent(txtMonto)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnAgregarMetodoPago)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitarMetodoPago))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(lblRestante))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cbxMedioPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuitarMetodoPago)
                    .addComponent(btnAgregarMetodoPago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRestante)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setText("Subtotal :");

        jLabel11.setText("IGV :");

        jLabel12.setText("TOTAL : ");

        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtIGV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (saldoRestante > 0.01) {
                javax.swing.JOptionPane.showMessageDialog(this, "Aún no se ha cubierto el total de la venta.");
                return;
            }

            int idVentaGenerado = objVenta.generarIdVenta();
            
            DefaultTableModel modeloPagos = (DefaultTableModel) tblPagos.getModel();
            javax.swing.JTable tablaProductosTemporal = new javax.swing.JTable(this.modeloProductosRecibido);

            objVenta.registrar(
                    idVentaGenerado, 
                    totalVenta, 
                    subTotalVenta, 
                    igvVenta, 
                    esBoleta, 
                    idCliente, 
                    idUsuario, 
                    modeloPagos,            // Tabla de Pagos (Yape, Efectivo, etc.)
                    tablaProductosTemporal  // <--- AQUÍ PASAMOS LOS PRODUCTOS DE JDVENTA
            );
            
            ventaExitosa = true; 
            javax.swing.JOptionPane.showMessageDialog(this, "¡Venta realizada con éxito!\nNro: " + idVentaGenerado);
            this.dispose(); 
            
            
        } catch (Exception e) {
            ventaExitosa = false;
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAgregarMetodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMetodoPagoActionPerformed
        try {
            double montoIngresado = Double.parseDouble(txtMonto.getText().replace(",", "."));

            if (montoIngresado <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }
            if (montoIngresado > saldoRestante + 0.01) {
                javax.swing.JOptionPane.showMessageDialog(this, "El monto excede lo que falta por pagar (S/. " + String.format("%.2f", saldoRestante) + ")");
                return;
            }

            String nombrePago = cbxMedioPago.getSelectedItem().toString();
            int idMedio = objMedioPago.obtenerIdPorNombre(nombrePago); // Tu método en clsMedioPago

            DefaultTableModel modelo = (DefaultTableModel) tblPagos.getModel();
            modelo.addRow(new Object[]{
                idMedio,
                nombrePago,
                String.format("%.2f", montoIngresado).replace(",", ".")
            });

            saldoRestante -= montoIngresado;
            if (saldoRestante < 0) {
                saldoRestante = 0; // Seguridad
            }
            actualizarEtiquetaRestante();

            txtMonto.setText(String.format("%.2f", saldoRestante).replace(",", "."));
            txtMonto.requestFocus();

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un monto válido (número).");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al agregar pago: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAgregarMetodoPagoActionPerformed

    private void btnQuitarMetodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarMetodoPagoActionPerformed
        int fila = tblPagos.getSelectedRow();

        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un pago de la tabla para quitarlo.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tblPagos.getModel();
        double montoRecuperado = Double.parseDouble(modelo.getValueAt(fila, 2).toString().replace(",", "."));

        modelo.removeRow(fila);
        saldoRestante += montoRecuperado;

        // 4. Actualizar interfaz
        actualizarEtiquetaRestante();
        txtMonto.setText(String.format("%.2f", saldoRestante).replace(",", "."));
    }//GEN-LAST:event_btnQuitarMetodoPagoActionPerformed

    private void txtMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarMetodoPago;
    private javax.swing.JButton btnQuitarMetodoPago;
    private javax.swing.JComboBox<String> cbxMedioPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCorrelativo;
    private javax.swing.JLabel lblRestante;
    private javax.swing.JLabel lblSerie;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextField txtDirec;
    private com.toedter.calendar.JDateChooser txtFecha;
    private javax.swing.JTextField txtIGV;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtNomCliente;
    private javax.swing.JTextField txtNumDoc;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
