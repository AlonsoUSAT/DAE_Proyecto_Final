
package Capa_Presentacion;

import Capa_Negocio.clsUsuario;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Mechan Vidaurre Mia
 */
public class FrmLogin extends javax.swing.JDialog {
    clsUsuario objUsuario = new clsUsuario();
    Byte numIntentos = 0;
    public String nombreUsuario = "";
    public String cargo = "";
    public String[] valores = new String[2];
    public static String nomUser = "";
    public static int idUsuarioLogueado = 0; // <--- AQUÍ SE GUARDARÁ EL ID PARA LA VENTA

    public static String nomCargo = "";

    public static String getNomCargo() {
        return nomCargo;
    }

    public static void setNomCargo(String nomCargo) {
        FrmLogin.nomCargo = nomCargo;
    }

    public static String getNomUser() {
        return nomUser;
    }

    public static void setNomUser(String nomUser) {
        FrmLogin.nomUser = nomUser;
    }

    public FrmLogin(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        generarCaptcha(this.txtPregunta);
        this.setLocationRelativeTo(null);
        this.setTitle("Inicio de Sesion");
    }

    private static String generarCaptcha(JTextField txtPregunta) {
        StringBuilder captcha = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            captcha.append(rand.nextInt(10));
        }

        for (int i = 0; i < 2; i++) {
            char letra = (char) ('A' + rand.nextInt(12));
            captcha.append(letra);
        }
        txtPregunta.setText(captcha.toString());
        return captcha.toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPregunta = new javax.swing.JTextField();
        txtRespuesta = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        btnCambiarCaptcha = new javax.swing.JButton();
        txtContraseña = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("Contraseña:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel2.setText("Usuario:");

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("Captcha:");

        txtPregunta.setEditable(false);

        btnCancelar.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        btnLogin.setText("Iniciar Sesion");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnCambiarCaptcha.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        btnCambiarCaptcha.setText("Cambiar");
        btnCambiarCaptcha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarCaptchaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/farmacia (1).png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCancelar)
                        .addGap(30, 30, 30)
                        .addComponent(btnLogin))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtRespuesta, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPregunta, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtContraseña, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCambiarCaptcha)
                .addGap(180, 180, 180))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPregunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCambiarCaptcha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnLogin))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowOpened

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
       try {
            // 1. VALIDAR CAPTCHA
            String captchaGenerado = txtPregunta.getText();
            String captchaIngresado = txtRespuesta.getText();

            if (!captchaGenerado.equalsIgnoreCase(captchaIngresado)) {
                JOptionPane.showMessageDialog(this, "Captcha incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                generarCaptcha(txtPregunta);
                txtRespuesta.setText("");
                return;
            }

            // 2. VALIDAR CREDENCIALES
            // Llamamos a tu método 'login' que ahora devuelve String[] (Arreglo)
            // datos[0] = Nombre, datos[1] = ID
            String[] datos = objUsuario.login(txtUsuario.getText(), String.valueOf(txtContraseña.getPassword()));

            if (datos == null) { // Si es null, es que no encontró al usuario
                numIntentos++;
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos. Intento " + numIntentos + " de 3.");

                generarCaptcha(txtPregunta);
                txtRespuesta.setText("");
                txtContraseña.setText("");

                if (numIntentos >= 3) {
                    JOptionPane.showMessageDialog(this, "Superó los intentos permitidos. El sistema se cerrará.");
                    System.exit(0);
                }
            } else {
                // --- LOGIN EXITOSO ---
                
                // Guardamos los datos en las variables globales
                nomUser = datos[0]; // Guardamos el Nombre
                
                try {
                    idUsuarioLogueado = Integer.parseInt(datos[1]); // Guardamos el ID (convirtiéndolo a número)
                } catch (NumberFormatException e) {
                    idUsuarioLogueado = 0;
                }

                JOptionPane.showMessageDialog(this, "¡Bienvenido al Sistema!\nUsuario: " + nomUser);
                this.dispose(); // Cerramos el login y damos paso al sistema
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }
    
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCambiarCaptchaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarCaptchaActionPerformed

        generarCaptcha(this.txtPregunta);
    }//GEN-LAST:event_btnCambiarCaptchaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarCaptcha;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtPregunta;
    private javax.swing.JTextField txtRespuesta;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
