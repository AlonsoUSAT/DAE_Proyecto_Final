/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Capa_Presentacion;

import Capa_Negocio.clsLote;
import Capa_Negocio.clsPresentacion;
import Capa_Datos.ProductoDAO;
import Capa_Datos.LoteDAO;
import Capa_Negocio.clsProducto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class ManLote extends javax.swing.JDialog {

     private final LoteDAO objLote = new LoteDAO();
    private List<clsLote> listaLotes;
    private boolean esNuevo = true; // Variable para controlar si es un registro nuevo o una modificación

    // --- Datos recibidos de la ventana anterior ---
    private final int productoID;
    private final String productoNombre;
    private final int presentacionID;
    private final String presentacionDescripcion;
    /**      
     * CONSTRUCTOR MODIFICADO
     * Recibe todos los datos necesarios para operar.
     * @param parent
     * @param modal
     * @param idProducto El ID del producto seleccionado.
     * @param nombreProducto El nombre del producto.
     * @param idPresentacion El ID de la presentación seleccionada.
     * @param presentacionDesc La descripción del formato de venta.
     */
    public ManLote(java.awt.Frame parent, boolean modal, int idProducto, String nombreProducto, int idPresentacion, String presentacionDesc) {
        super(parent, modal);

        this.productoID = idProducto;
        this.productoNombre = nombreProducto;
        this.presentacionID = idPresentacion;
        this.presentacionDescripcion = presentacionDesc;

        initComponents(); // No olvides que esta línea es crucial

        configurarFormulario();
    }
 private void configurarFormulario() {
    this.setTitle("Gestión de Lotes para: " + this.productoNombre);
    this.setLocationRelativeTo(null);
    poblarInformacionLote();
    configurarTabla();
    listarLotesFiltrados();
    // ANTES: estadoInicialControles();
    gestionarEstadoControles("inicio"); // AHORA
}

    private void poblarInformacionLote() {
        txtProducto.setText(this.productoNombre);
        txtIDProducto.setText(String.valueOf(this.productoID));
        txtPresentacion.setText(this.presentacionDescripcion);
        txtIDPresentacion.setText(String.valueOf(this.presentacionID));

        txtProducto.setEditable(false);
        txtIDProducto.setEditable(false);
        txtPresentacion.setEditable(false);
        txtIDPresentacion.setEditable(false);
    }

    private void configurarTabla() {
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    modelo.addColumn("ID Lote");
    modelo.addColumn("ID Presentación");
    modelo.addColumn("Nro. Lote");
    modelo.addColumn("Fecha Fab.");
    modelo.addColumn("Fecha Venc.");
    modelo.addColumn("Stock");
    // --- LÍNEA NUEVA ---
    modelo.addColumn("Estado"); // Agregamos la columna para la vigencia del lote
    
    tblLote.setModel(modelo);
}
   private void listarLotesFiltrados() {
    DefaultTableModel modelo = (DefaultTableModel) tblLote.getModel();
    modelo.setRowCount(0);

    try {
        this.listaLotes = objLote.listarLotesPorPresentacion(this.productoID, this.presentacionID);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (clsLote lote : this.listaLotes) {
            // Se convierte el estado booleano a un texto descriptivo
            String estado = lote.isEstado() ? "Activo" : "Inactivo";

            modelo.addRow(new Object[]{
                lote.getIdLote(),
                lote.getIdPresentacion(),
                lote.getNroLote(),
                // Se valida que la fecha de fabricación no sea nula antes de formatear
                lote.getFechaFabricacion() != null ? sdf.format(lote.getFechaFabricacion()) : "",
                sdf.format(lote.getFechaVencimiento()),
                lote.getStockActual(),
                // --- DATO NUEVO AÑADIDO ---
                estado // Se muestra el estado en la nueva columna
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los lotes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   
   private void seleccionarFilaEnTabla(int idLote) {
    for (int i = 0; i < tblLote.getRowCount(); i++) {
        // Asumiendo que el ID del lote está en la primera columna (índice 0)
        if ((int) tblLote.getValueAt(i, 0) == idLote) {
            tblLote.setRowSelectionInterval(i, i);
            // Hacemos que la fila seleccionada sea visible si hay muchos registros
            tblLote.scrollRectToVisible(tblLote.getCellRect(i, 0, true));
            break;
        }
    }
}

    private void limpiarControles() {
        // ✅ CORREGIDO: Usando el nombre estandarizado 'txtIDLote'
        txtIDLote.setText("");
        txtNumeroLote.setText("");
        jdcFechaFabricacion.setDate(null);
        jdcFechaVencimiento.setDate(null);
        spnStock.setValue(0);
        txtExistencias.setText("");
        chkEstado.setSelected(true);
    }

    private void estadoInicialControles() {
    txtIDLote.setEnabled(false);
    txtNumeroLote.setEnabled(false);
    jdcFechaFabricacion.setEnabled(false);
    jdcFechaVencimiento.setEnabled(false);
    spnStock.setEnabled(false);
    txtExistencias.setEnabled(false);
    chkEstado.setEnabled(false);

    btnNuevo.setText("Nuevo"); // ✅ MUY IMPORTANTE: Esta línea resetea el texto del botón.
    btnNuevo.setEnabled(true);
    btnModificar.setEnabled(false); // El botón "Modificar" solo se debe activar cuando seleccionas una fila de la tabla.
    btnDarBaja.setEnabled(false);
    btnEliminar.setEnabled(false);
}

    private void habilitarControles() {
    txtIDLote.setEnabled(false); 
    txtNumeroLote.setEnabled(false);
    jdcFechaFabricacion.setEnabled(true);
    jdcFechaVencimiento.setEnabled(true);
    spnStock.setEnabled(true);
    txtExistencias.setEnabled(false);
    chkEstado.setEnabled(true);

    // --- CAMBIOS CLAVE AQUÍ ---
    btnNuevo.setEnabled(true);       // ✅ CORREGIDO: Mantenemos el botón Nuevo/Guardar HABILITADO.
    btnModificar.setEnabled(false);  // Deshabilitamos "Modificar" porque estamos en modo de creación.
    btnDarBaja.setEnabled(false);
    btnEliminar.setEnabled(false);
}

  private void cargarDatosDesdeTabla(int fila) {
    esNuevo = false; // Estamos modificando un registro existente
    clsLote loteSeleccionado = this.listaLotes.get(fila);

    txtIDLote.setText(String.valueOf(loteSeleccionado.getIdLote()));
    txtNumeroLote.setText(loteSeleccionado.getNroLote());
    jdcFechaFabricacion.setDate(loteSeleccionado.getFechaFabricacion());
    jdcFechaVencimiento.setDate(loteSeleccionado.getFechaVencimiento());
    spnStock.setValue(loteSeleccionado.getCantidadRecibida());
    txtExistencias.setText(String.valueOf(loteSeleccionado.getStockActual()));
    chkEstado.setSelected(loteSeleccionado.isEstado());

    // ANTES: habilitarControles(); y otras líneas
    gestionarEstadoControles("seleccionado"); // AHORA
}
    
   private void gestionarEstadoControles(String modo) {
    // Habilita o deshabilita los campos de edición
    boolean camposEditables = modo.equals("nuevo") || modo.equals("seleccionado");
    jdcFechaFabricacion.setEnabled(camposEditables);
    jdcFechaVencimiento.setEnabled(camposEditables);
    spnStock.setEnabled(camposEditables);
    chkEstado.setEnabled(camposEditables);
    
    // --- CAMBIO IMPORTANTE AQUÍ ---
    // Permitimos editar el ID solo en el estado inicial para poder buscar.
    txtIDLote.setEnabled(modo.equals("inicio")); 
    
    txtNumeroLote.setEnabled(false);
    txtExistencias.setEnabled(false);

    // Configura los botones según el modo
    switch (modo) {
        case "inicio":
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(true);
            btnModificar.setEnabled(false);
            btnDarBaja.setEnabled(false);
            btnEliminar.setEnabled(false);
            tblLote.clearSelection();
            limpiarControles();
            break;
            
        case "nuevo":
            btnNuevo.setText("Guardar");
            btnNuevo.setEnabled(true);
            btnModificar.setEnabled(false);
            btnDarBaja.setEnabled(false);
            btnEliminar.setEnabled(false);
            break;
            
        case "seleccionado":
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(false); 
            btnModificar.setEnabled(true);
            btnDarBaja.setEnabled(true);
            btnEliminar.setEnabled(true);
            break;
    }
}

   
   private Date getFechaSinHora(Date date) {
    if (date == null) {
        return null;
    }
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(date);
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    cal.set(java.util.Calendar.MILLISECOND, 0);
    return cal.getTime();
}
   
   private boolean validarFechas() {
    Date fechaHoy = getFechaSinHora(new Date());
    Date fechaFab = getFechaSinHora(jdcFechaFabricacion.getDate());
    Date fechaVen = getFechaSinHora(jdcFechaVencimiento.getDate());

    // Validación 1: La fecha de vencimiento es obligatoria.
    if (fechaVen == null) {
        JOptionPane.showMessageDialog(this, "La fecha de vencimiento es obligatoria.", "Validación de Fechas", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    // Validación 2: La fecha de fabricación no puede ser una fecha futura.
    if (fechaFab != null && fechaFab.after(fechaHoy)) {
        JOptionPane.showMessageDialog(this, "La fecha de fabricación no puede ser posterior a la fecha actual.", "Validación de Fechas", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    // Validación 3: La fecha de vencimiento no puede ser una fecha pasada.
    if (fechaVen.before(fechaHoy)) {
        JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede ser una fecha pasada.", "Validación de Fechas", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    // Validación 4: La fecha de vencimiento debe ser posterior (o igual) a la de fabricación.
    if (fechaFab != null && fechaVen.before(fechaFab)) {
        JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede ser anterior a la fecha de fabricación.", "Validación de Fechas", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    return true; // Si todas las validaciones pasan, devuelve true.
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
        jLabel1 = new javax.swing.JLabel();
        txtNumeroLote = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtExistencias = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        chkEstado = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        txtIDLote = new javax.swing.JTextField();
        jdcFechaFabricacion = new com.toedter.calendar.JDateChooser();
        jdcFechaVencimiento = new com.toedter.calendar.JDateChooser();
        spnStock = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtIDProducto = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPresentacion = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtIDPresentacion = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnDarBaja = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLote = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lote", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setText("Número de lote:");

        txtNumeroLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroLoteActionPerformed(evt);
            }
        });

        jLabel2.setText("Fecha fabricación:");

        jLabel3.setText("Fecha vencimiento:");

        jLabel4.setText("Stock:");

        jLabel5.setText("Existencias:");

        jLabel6.setText("Estado:");

        chkEstado.setText("Activo");
        chkEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEstadoActionPerformed(evt);
            }
        });

        jLabel9.setText("ID:");

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion del lote", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel10.setText("Producto:");

        jLabel11.setText("ID Producto:");

        txtIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDProductoActionPerformed(evt);
            }
        });

        jLabel12.setText("Presentación:");

        txtPresentacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPresentacionActionPerformed(evt);
            }
        });

        jLabel13.setText("ID Presentacion:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPresentacion)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtIDPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProducto))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 119, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtIDPresentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        btnBuscar.setBackground(new java.awt.Color(204, 224, 250));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar-usuario.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jdcFechaFabricacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jdcFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkEstado)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIDLote, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumeroLote, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar)))
                .addGap(30, 30, 30)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtIDLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(btnBuscar)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNumeroLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jdcFechaFabricacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jdcFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(spnStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtExistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(chkEstado)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        btnNuevo.setBackground(new java.awt.Color(204, 224, 250));
        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/registrarMarca.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(204, 224, 250));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/modificarMarca.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(204, 224, 250));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/eliminarMarca.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnDarBaja.setBackground(new java.awt.Color(204, 224, 250));
        btnDarBaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDarBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/darBajaMarca.png"))); // NOI18N
        btnDarBaja.setText("Dar baja");
        btnDarBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDarBajaActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(204, 224, 250));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/limpiarMarca.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(204, 224, 250));
        btnSalir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/salirMarca.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDarBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDarBaja)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblLote.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLote);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       // PRIMERA PARTE: Se ejecuta si el botón dice "Nuevo"
    if (btnNuevo.getText().equals("Nuevo")) {
        
        esNuevo = true;
        gestionarEstadoControles("nuevo"); // Esto prepara el formulario y cambia el botón a "Guardar"

        try {
            // Generamos y mostramos el nuevo ID para el lote
            txtIDLote.setText(String.valueOf(objLote.generarCodeLote()));
            txtNumeroLote.setText("<Se generará al guardar>");
            // Ponemos el foco en el campo de stock para que el usuario ingrese la cantidad
            spnStock.requestFocusInWindow();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar código de lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            gestionarEstadoControles("inicio"); // Si hay error, volvemos al estado inicial
        }

    // SEGUNDA PARTE: Se ejecuta si el botón dice "Guardar"
    } else {
        
        try {
            // --- VALIDACIONES ANTES DE GUARDAR ---
            
            // 1. Validación de stock (AHORA SÍ, EN EL LUGAR CORRECTO)
            if ((Integer) spnStock.getValue() <= 0) {
                JOptionPane.showMessageDialog(this, "El Stock debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                return; // Detiene el guardado
            }

            // 2. Validación de fechas
            if (!validarFechas()) {
                return; // Detiene el guardado si las fechas no son válidas
            }

            // --- Si todas las validaciones pasan, procedemos a guardar ---
            
            int idLote = Integer.parseInt(txtIDLote.getText());
            int cantidad = (Integer) spnStock.getValue();
            String nroLoteGenerado = objLote.generarNumeroLote(this.productoID, this.presentacionID, cantidad);
            
            objLote.registrarLote(idLote, nroLoteGenerado, jdcFechaFabricacion.getDate(), jdcFechaVencimiento.getDate(), cantidad, this.presentacionID, this.productoID);
            
            JOptionPane.showMessageDialog(this, "Lote registrado con éxito.\nNúmero de Lote: " + nroLoteGenerado, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Actualizamos la tabla y reseteamos el formulario al estado inicial
            listarLotesFiltrados();
            gestionarEstadoControles("inicio");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
     if (txtIDLote.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No ha seleccionado ningún lote para modificar.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea guardar los cambios en este lote?", "Confirmar Modificación", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        // ✅ LLAMADA AL MÉTODO DE VALIDACIÓN DE FECHAS
        if (!validarFechas()) {
            return; // Detiene el proceso si las fechas no son válidas
        }
        
        // Si las fechas son válidas, procedemos a modificar...
        int idLote = Integer.parseInt(txtIDLote.getText());
        int cantidad = (Integer) spnStock.getValue();
        boolean estado = chkEstado.isSelected();

        objLote.modificarLote(idLote, jdcFechaFabricacion.getDate(), jdcFechaVencimiento.getDate(), cantidad, estado);
        
        JOptionPane.showMessageDialog(this, "Lote modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        listarLotesFiltrados();
        gestionarEstadoControles("inicio");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al modificar el lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
     if (txtIDLote.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No ha seleccionado ningún lote para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // REGLA DE NEGOCIO: Solo permitir eliminar si no se ha vendido nada de este lote.
    int stockInicial = (Integer) spnStock.getValue();
    int stockActual = Integer.parseInt(txtExistencias.getText());

    if (stockActual < stockInicial) {
        JOptionPane.showMessageDialog(this, "No se puede eliminar este lote porque ya tiene movimientos de stock (ventas, etc.).\nConsidere 'Dar de Baja' en su lugar.", "Acción no Permitida", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Esta acción es IRREVERSIBLE y eliminará el lote permanentemente.\n¿Está seguro de que desea continuar?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        int idLote = Integer.parseInt(txtIDLote.getText());
        
        // Llamada al DAO (Asegúrate de tener este método en LoteDAO)
        objLote.eliminarLote(idLote);
        
        JOptionPane.showMessageDialog(this, "Lote eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        listarLotesFiltrados();
        gestionarEstadoControles("inicio");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar el lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnDarBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDarBajaActionPerformed
   if (txtIDLote.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No ha seleccionado ningún lote.", "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!chkEstado.isSelected()) {
        JOptionPane.showMessageDialog(this, "Este lote ya se encuentra inactivo.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea dar de baja este lote?", "Confirmar Acción", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        int idLote = Integer.parseInt(txtIDLote.getText());

        // Llamada al DAO (Asegúrate de tener este método en LoteDAO)
        objLote.darBajaLote(idLote);

        JOptionPane.showMessageDialog(this, "El lote ha sido dado de baja.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        listarLotesFiltrados();
        gestionarEstadoControles("inicio");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al dar de baja el lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_btnDarBajaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        gestionarEstadoControles("inicio");
        
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtIDProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDProductoActionPerformed

    private void txtNumeroLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroLoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroLoteActionPerformed

    private void chkEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkEstadoActionPerformed

    private void txtPresentacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPresentacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPresentacionActionPerformed

    private void tblLoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoteMouseClicked
       int fila = tblLote.getSelectedRow();
        if (fila >= 0) {
            cargarDatosDesdeTabla(fila);
        }
    }//GEN-LAST:event_tblLoteMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
       String textoBusqueda = txtIDLote.getText().trim();

    if (textoBusqueda.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de lote para buscar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int idLoteBuscado = Integer.parseInt(textoBusqueda);
        
        // Usamos el método 'buscarLote' de tu LoteDAO
        clsLote loteEncontrado = objLote.buscarLote(idLoteBuscado);

        // Verificamos si se encontró un lote y si pertenece al producto/presentación actual
        if (loteEncontrado != null && loteEncontrado.getIdProducto() == this.productoID && loteEncontrado.getIdPresentacion() == this.presentacionID) {
            
            // Llenamos los campos del formulario con los datos encontrados
            txtNumeroLote.setText(loteEncontrado.getNroLote());
            jdcFechaFabricacion.setDate(loteEncontrado.getFechaFabricacion());
            jdcFechaVencimiento.setDate(loteEncontrado.getFechaVencimiento());
            spnStock.setValue(loteEncontrado.getCantidadRecibida());
            txtExistencias.setText(String.valueOf(loteEncontrado.getStockActual()));
            chkEstado.setSelected(loteEncontrado.isEstado());
            
            // Ponemos el formulario en modo "seleccionado" para activar los botones
            gestionarEstadoControles("seleccionado");
            
            // Resaltamos la fila encontrada en la tabla
            seleccionarFilaEnTabla(idLoteBuscado);

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún lote con el ID " + idLoteBuscado + " para este producto.", "Búsqueda sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            gestionarEstadoControles("inicio"); // Reseteamos el formulario
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El ID del lote debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error al buscar el lote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnBuscarActionPerformed

    
    
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnDarBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JRadioButton chkEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFechaFabricacion;
    private com.toedter.calendar.JDateChooser jdcFechaVencimiento;
    private javax.swing.JSpinner spnStock;
    private javax.swing.JTable tblLote;
    private javax.swing.JTextField txtExistencias;
    private javax.swing.JTextField txtIDLote;
    private javax.swing.JTextField txtIDPresentacion;
    private javax.swing.JTextField txtIDProducto;
    private javax.swing.JTextField txtNumeroLote;
    private javax.swing.JTextField txtPresentacion;
    private javax.swing.JTextField txtProducto;
    // End of variables declaration//GEN-END:variables
}
