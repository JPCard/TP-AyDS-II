package receptor.vista;

import emisor.modelo.Mensaje;

import emisor.vista.RendererContactos;

import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;

import receptor.controlador.ControladorReceptor;

/**
 *
 * @author Mau
 */
public class VistaReceptor extends javax.swing.JFrame implements IVistaReceptor {
    DefaultListModel<Mensaje> listModel = new DefaultListModel<Mensaje>();

    /** Creates new form VistaReceptor */
    public VistaReceptor() {
        
        initComponents();
        ControladorReceptor.getInstance(this);
        this.jListMensajes.setCellRenderer(new RendererMensajesRecibidos());
        this.jListMensajes.setSelectionModel(new DefaultListSelectionModel(){

        @Override
        public void setSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(-1, -1);
        }
        @Override
        public void addSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(-1, -1);
        }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanelMensajes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListMensajes = new javax.swing.JList<>();
        jPanelAbajo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonSilenciar = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Recepcion de Mensajes");
        setMinimumSize(new java.awt.Dimension(600, 399));
        setPreferredSize(new java.awt.Dimension(600, 399));

        jPanelMensajes.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensajes"));
        jPanelMensajes.setMinimumSize(new java.awt.Dimension(600, 100));
        jPanelMensajes.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanelMensajes.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));

        jListMensajes.setModel(listModel);
        jListMensajes.setMaximumSize(new java.awt.Dimension(3300, 8000));
        jListMensajes.setMinimumSize(new java.awt.Dimension(300, 500));
        jListMensajes.setPreferredSize(new java.awt.Dimension(150, 1000));
        jScrollPane1.setViewportView(jListMensajes);

        jPanelMensajes.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Indicador de alerta:");

        jButtonSilenciar.setText("Silenciar");
        jButtonSilenciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSilenciarActionPerformed(evt);
            }
        });

        jProgressBar1.setForeground(new java.awt.Color(254, 0, 0));
        jProgressBar1.setMaximum(1);

        javax.swing.GroupLayout jPanelAbajoLayout = new javax.swing.GroupLayout(jPanelAbajo);
        jPanelAbajo.setLayout(jPanelAbajoLayout);
        jPanelAbajoLayout.setHorizontalGroup(
            jPanelAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAbajoLayout.createSequentialGroup()
                .addContainerGap(382, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButtonSilenciar)
                .addContainerGap())
        );
        jPanelAbajoLayout.setVerticalGroup(
            jPanelAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAbajoLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanelAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAbajoLayout.createSequentialGroup()
                        .addGroup(jPanelAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonSilenciar)
                            .addGroup(jPanelAbajoLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(2, 2, 2)))
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAbajoLayout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );

        jPanelMensajes.add(jPanelAbajo, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelMensajes, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void jButtonSilenciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSilenciarActionPerformed
        this.jProgressBar1.setValue(0);
    }//GEN-LAST:event_jButtonSilenciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing
                                                                   .UIManager
                                                                   .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing
                         .UIManager
                         .setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaReceptor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaReceptor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaReceptor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaReceptor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt
            .EventQueue
            .invokeLater(new Runnable() {
                public void run() {
                    new VistaReceptor().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSilenciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<Mensaje> jListMensajes;
    private javax.swing.JPanel jPanelAbajo;
    private javax.swing.JPanel jPanelMensajes;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void activarAlerta() {
        this.jProgressBar1.setValue(1);
        this.repaint();
    }

    @Override
    public void agregarMensaje(Mensaje mensaje) {
        this.listModel.add(0,mensaje);
        this.repaint();
    }

    @Override
    public void mostrarErrorNoReceptor() {
        JOptionPane.showMessageDialog(this, "Error: no se pudo encontrar el archivo con los datos del receptor", "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
