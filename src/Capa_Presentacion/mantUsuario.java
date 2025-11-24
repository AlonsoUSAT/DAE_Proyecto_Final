package Capa_Presentacion;

import Capa_Negocio.clsUsuario;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class mantUsuario extends javax.swing.JDialog {

    clsUsuario objUsuario = new clsUsuario();

    public mantUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cargarRoles();
        actualizarTabla();
        habilitarCampos(false);
        limpiarCampos();
        java.awt.event.KeyAdapter validador = new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bloquearNumeros(evt);
            }
        };

        txtNombre.addKeyListener(validador);
        txtApePaterno.addKeyListener(validador);
        txtApeMater.addKeyListener(validador);
        txtPregunta.addKeyListener(validador);
        txtRespuesta.addKeyListener(validador);
    }

    private void habilitarCampos(boolean estado) {
        txtNomUsuario.setEnabled(estado);
        txtClave.setEnabled(estado);
        txtNombre.setEnabled(estado);
        txtApePaterno.setEnabled(estado);
        txtApeMater.setEnabled(estado);
        txtCorreo.setEnabled(estado);
        txtPregunta.setEnabled(estado);
        txtRespuesta.setEnabled(estado);

        cbxRol.setEnabled(estado);
        cbxSexo.setEnabled(estado);
        chkVigencia.setEnabled(estado);
        txtID.setEnabled(false);
    }

    private void bloquearNumeros(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    private void cargarRoles() {
        try {
            ResultSet rs = objUsuario.listarRoles();
            cbxRol.removeAllItems();
            while (rs.next()) {
                cbxRol.addItem(rs.getString("nombre_rol"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar roles: " + e.getMessage());
        }
    }

    private void actualizarTabla() {
        // Tienes 11 títulos en total
        String[] titulos = {"ID", "Usuario", "Nombre", "Ap. Paterno", "Ap. Materno", "Correo", "Pregunta", "Sexo", "Respuesta", "Rol", "Estado"};

        DefaultTableModel model = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            ResultSet rs = objUsuario.listarUsuarios();

            while (rs.next()) {
                Object[] fila = new Object[11];

                fila[0] = rs.getInt("codusuario");
                fila[1] = rs.getString("nomusuario");
                fila[2] = rs.getString("nombres");
                fila[3] = rs.getString("apellidopaterno");
                fila[4] = rs.getString("apellidomaterno");
                fila[5] = rs.getString("correo");

                fila[6] = rs.getString("pregunta");
                String sexo;
                if (rs.getString("sexo").equalsIgnoreCase("M")) {
                    sexo = "Masculino";
                } else {
                    sexo = "Femenino";
                }
                fila[7] = sexo;
                fila[8] = rs.getString("respuesta");

                try {
                    fila[9] = rs.getString("nombre_rol");
                } catch (Exception ex) {
                    fila[9] = rs.getInt("id_rol"); // Si falla, pone el número
                }

                fila[10] = rs.getBoolean("estado") ? "Vigente" : "No Vigente";

                model.addRow(fila);
            }
            tblUsuario.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtID.setText("");
        txtNomUsuario.setText("");
        txtClave.setText("");
        txtNombre.setText("");
        txtApePaterno.setText("");
        txtApeMater.setText("");
        txtCorreo.setText("");
        cbxRol.setSelectedIndex(-1);
        cbxRol.setSelectedIndex(-1);
        chkVigencia.setSelected(true);

        txtID.setEnabled(true);
        btnBuscar.setEnabled(true);
        cbxRol.setEnabled(false);
        txtNomUsuario.setEnabled(false);
        txtClave.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApePaterno.setEnabled(false);
        txtApeMater.setEnabled(false);
        txtCorreo.setEnabled(false);
        cbxRol.setSelectedIndex(-1);
        chkVigencia.setEnabled(false);

        btnNuevo.setEnabled(true);
        btnNuevo.setText("Nuevo");
        btnModificar.setText("Modificar");
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnDardeBaja.setEnabled(false);

        tblUsuario.clearSelection();
        txtID.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuario = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnDardeBaja = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNomUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbxRol = new javax.swing.JComboBox<>();
        txtClave = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPregunta = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtRespuesta = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtApePaterno = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtApeMater = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        chkVigencia = new javax.swing.JCheckBox();
        cbxSexo = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuario);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(68, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDardeBaja, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(61, 61, 61))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnDardeBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnEliminar)
                .addGap(27, 27, 27)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("ID:");

        btnBuscar.setBackground(new java.awt.Color(204, 224, 250));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/buscar-usuario.png"))); // NOI18N
        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Nombre Usuario:");

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel7.setText("Rol:");

        cbxRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Almacenero", "Vendedor", " " }));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel8.setText("Clave:");

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel6.setText("Pregunta:");

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel12.setText("Respuesta:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel8)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(91, 91, 91)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel3)))
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar))
                    .addComponent(txtClave)
                    .addComponent(txtNomUsuario)
                    .addComponent(cbxRol, 0, 295, Short.MAX_VALUE)
                    .addComponent(txtRespuesta, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPregunta))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnBuscar))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNomUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(230, 230, 230))
        );

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Datos Personales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel1.setText("Nombre:");

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel9.setText("Apellido Paterno:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("Apellido Materno:");

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel10.setText("Correo:");

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel11.setText("Sexo (M/F):");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setText("Estado:");

        chkVigencia.setText("(Vigente)");
        chkVigencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVigenciaActionPerformed(evt);
            }
        });

        cbxSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Femenino" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(chkVigencia))
                    .addComponent(txtApePaterno, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtApeMater)
                    .addComponent(txtCorreo)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(cbxSexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApePaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtApeMater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbxSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkVigencia)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 723, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(19, 19, 19)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario? Esta acción es permanente.", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtID.getText());
                objUsuario.eliminarUsuario(id);

                JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
                limpiarCampos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error de Eliminación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // --- ESTADO 1: PREPARAR FORMULARIO (Cuando el botón dice "Nuevo") ---
        if (btnNuevo.getText().equals("Nuevo")) {
            limpiarCampos();
            habilitarCampos(true);

            // Generar ID
            try {
                txtID.setText(String.valueOf(objUsuario.generarCodigoUsuario()));
            } catch (Exception e) {
                txtID.setText("0");
            }

            // Cambiar el botón a modo "Guardar"
            btnNuevo.setText("Guardar");

            // Bloquear botones que estorban
            btnModificar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnDardeBaja.setEnabled(false);
            btnBuscar.setEnabled(false);
            btnCerrar.setEnabled(false);

            txtNomUsuario.requestFocus();

            // ¡IMPORTANTE! Detenemos la ejecución aquí para que el usuario pueda escribir.
            return;
        }

        if (btnNuevo.getText().equals("Guardar")) {

            // 1. Validaciones
            if (txtNomUsuario.getText().isEmpty() || txtNombre.getText().isEmpty()
                    || txtClave.getText().isEmpty() || cbxRol.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.");
                return;
            }

            try {
                int id = Integer.parseInt(txtID.getText());
                String nomUsuario = txtNomUsuario.getText();
                String clave = txtClave.getText();
                String nombre = txtNombre.getText();
                String apePaterno = txtApePaterno.getText();
                String apeMaterno = txtApeMater.getText();
                String correo = txtCorreo.getText();
                String pregunta = txtPregunta.getText();
                String respuesta = txtRespuesta.getText();
                boolean estado = chkVigencia.isSelected();

                // Sexo
                String sexo = "M";
                if (cbxSexo.getSelectedItem() != null) {
                    sexo = cbxSexo.getSelectedItem().toString().substring(0, 1);
                }

                // Rol (Con validación de seguridad para evitar el NullPointerException)
                String nombreRol = "";
                if (cbxRol.getSelectedItem() != null) {
                    nombreRol = cbxRol.getSelectedItem().toString();
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione un Rol válido.");
                    return;
                }

                Integer idRol = objUsuario.obtenerIdRol(nombreRol);

                // 3. Guardar en BD
                objUsuario.registrarUsuario(id, nombre, apePaterno, apeMaterno, correo, sexo, clave, estado, idRol, nomUsuario, pregunta, respuesta);

                JOptionPane.showMessageDialog(this, "Usuario Registrado con éxito.");

                // 4. Restaurar formulario
                actualizarTabla();
                limpiarCampos();

                // Volvemos el botón a su estado original "Nuevo"
                btnNuevo.setText("Nuevo");
                btnCerrar.setEnabled(true); // Rehabilitar cerrar

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // --- PASO 1: PREPARAR PARA BUSCAR EL USUARIO A MODIFICAR ---
        if (btnModificar.getText().equals("Modificar")) {
            limpiarCampos();
            habilitarCampos(false);

            txtID.setEnabled(true);
            btnBuscar.setEnabled(true);
            txtID.requestFocus();

            JOptionPane.showMessageDialog(this, "Ingrese el ID del usuario y presione BUSCAR.");
            return; // Terminamos aquí. El usuario debe buscar ahora.
        }

        if (txtNomUsuario.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtClave.getText().isEmpty() || cbxRol.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Complete los datos obligatorios.");
            return;
        }

        try {
            String sexo = "M";
            if (cbxSexo.getSelectedItem() != null) {
                sexo = cbxSexo.getSelectedItem().toString().substring(0, 1);
            }

            String nombreRol = cbxRol.getSelectedItem().toString();
            Integer idRol = objUsuario.obtenerIdRol(nombreRol);

            // 3. EJECUTAR SOLO LA MODIFICACIÓN
            objUsuario.modificarUsuario(Integer.parseInt(txtID.getText()), txtNombre.getText(), txtApePaterno.getText(), txtApeMater.getText(), txtCorreo.getText(), sexo, txtClave.getText(), chkVigencia.isSelected(), idRol, txtNomUsuario.getText(), txtPregunta.getText(), txtRespuesta.getText());

            JOptionPane.showMessageDialog(this, "¡Usuario MODIFICADO con éxito!");

            // 4. Limpieza final
            actualizarTabla();
            limpiarCampos();

            // Restauramos el botón a su estado original
            btnModificar.setText("Modificar");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }


    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        if (txtID.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID para buscar.");

            return;

        }

        try {
            int id = Integer.parseInt(txtID.getText());

            ResultSet rs = objUsuario.buscarUsuario(id);

            if (rs.next()) {
                txtNomUsuario.setText(rs.getString("nomusuario"));
                txtClave.setText(rs.getString("clave"));
                txtNombre.setText(rs.getString("nombres"));
                txtApePaterno.setText(rs.getString("apellidopaterno"));
                txtApeMater.setText(rs.getString("apellidomaterno"));
                txtCorreo.setText(rs.getString("correo"));
                txtPregunta.setText(rs.getString("pregunta"));
                txtRespuesta.setText(rs.getString("respuesta"));
                chkVigencia.setSelected(rs.getBoolean("estado"));

                String sexo = rs.getString("sexo");

                if (sexo != null && sexo.equals("M")) {

                    cbxSexo.setSelectedIndex(0);

                } else {

                    cbxSexo.setSelectedIndex(1);

                }

                try {

                    cbxRol.setSelectedItem(rs.getString("nombre_rol"));

                } catch (Exception ex) {

                }

                habilitarCampos(true);

                txtID.setEnabled(false);

                btnModificar.setText("Guardar");

                btnModificar.setEnabled(true);

                btnBuscar.setEnabled(false);

                btnNuevo.setEnabled(false);

            } else {

                JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con ese ID.");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());

        }


    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void tblUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuarioMouseClicked

        int fila = tblUsuario.getSelectedRow();
        if (fila != -1) {
            try {
                // Obtener el ID de la fila seleccionada y buscarlo
                int idUsuario = (int) tblUsuario.getValueAt(fila, 0);
                txtID.setText(String.valueOf(idUsuario));
                btnBuscar.doClick();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar el usuario: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_tblUsuarioMouseClicked

    private void btnDardeBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDardeBajaActionPerformed
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de dar de baja a este usuario?", "Confirmar Acción", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtID.getText());
                objUsuario.darBaja(id); // Usamos el nuevo método

                JOptionPane.showMessageDialog(this, "Usuario dado de baja con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
                limpiarCampos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDardeBajaActionPerformed

    private void chkVigenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVigenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkVigenciaActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnDardeBaja;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cbxRol;
    private javax.swing.JComboBox<String> cbxSexo;
    private javax.swing.JCheckBox chkVigencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuario;
    private javax.swing.JTextField txtApeMater;
    private javax.swing.JTextField txtApePaterno;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNomUsuario;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPregunta;
    private javax.swing.JTextField txtRespuesta;
    // End of variables declaration//GEN-END:variables
}
