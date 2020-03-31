
package emisor.vista;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Emisor;
import emisor.modelo.MensajeFactory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import java.beans.XMLEncoder;

import java.io.BufferedOutputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.ListModel;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

/**
 *
 * @author Mau
 */
public class VistaEmisor extends javax.swing.JFrame implements IVistaEmisor {

    DefaultListModel<Receptor> listModel = new DefaultListModel<Receptor>();


    /** Creates new form VistaEmisor */
    public VistaEmisor() {
        
        initComponents();
        this.jListDestinatarios.setModel(listModel);
        ControladorEmisor.getInstance(this);
        
        this.cargarContactos();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanelBotonesArriba = new javax.swing.JPanel();
        jPanelListaContactos = new javax.swing.JPanel();
        jButtonListaContactos = new javax.swing.JButton();
        jPanelVerComprobante = new javax.swing.JPanel();
        jButtonVerComprobante = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanelPrincipal = new javax.swing.JPanel();
        jPanelDestinatarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListDestinatarios = new javax.swing.JList<>();
        jPanelMensaje = new javax.swing.JPanel();
        jPanelAsunto = new javax.swing.JPanel();
        jTextFieldAsunto = new javax.swing.JTextField();
        jPanelCuerpo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanelBotonesAbajo = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanelBotonSimple = new javax.swing.JPanel();
        jButtonEnviarSimple = new javax.swing.JButton();
        jPanelBotonAviso = new javax.swing.JPanel();
        jButtonEnviarAviso = new javax.swing.JButton();
        jPanelBotonComprobante = new javax.swing.JPanel();
        jButtonEnviarConComprobante = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Envio de Mensajes");
        setMinimumSize(new java.awt.Dimension(850, 500));

        jPanelBotonesArriba.setLayout(new java.awt.GridLayout(1, 3));

        jButtonListaContactos.setText("Ver Lista De Contactos Completa");
        jButtonListaContactos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListaContactosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelListaContactosLayout = new javax.swing.GroupLayout(jPanelListaContactos);
        jPanelListaContactos.setLayout(jPanelListaContactosLayout);
        jPanelListaContactosLayout.setHorizontalGroup(
            jPanelListaContactosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListaContactosLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jButtonListaContactos)
                .addContainerGap(109, Short.MAX_VALUE))
        );
        jPanelListaContactosLayout.setVerticalGroup(
            jPanelListaContactosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListaContactosLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButtonListaContactos)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanelBotonesArriba.add(jPanelListaContactos);

        jButtonVerComprobante.setText("Ver Mensajes con Comprobante");
        jButtonVerComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerComprobanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelVerComprobanteLayout = new javax.swing.GroupLayout(jPanelVerComprobante);
        jPanelVerComprobante.setLayout(jPanelVerComprobanteLayout);
        jPanelVerComprobanteLayout.setHorizontalGroup(
            jPanelVerComprobanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelVerComprobanteLayout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addComponent(jButtonVerComprobante)
                .addGap(83, 83, 83))
        );
        jPanelVerComprobanteLayout.setVerticalGroup(
            jPanelVerComprobanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelVerComprobanteLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jButtonVerComprobante)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanelBotonesArriba.add(jPanelVerComprobante);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanelBotonesArriba.add(jPanel3);

        getContentPane().add(jPanelBotonesArriba, java.awt.BorderLayout.NORTH);

        jPanelPrincipal.setLayout(new java.awt.BorderLayout(20, 0));

        jPanelDestinatarios.setBorder(javax.swing.BorderFactory.createTitledBorder("Destinatarios"));
        jPanelDestinatarios.setMinimumSize(new java.awt.Dimension(600, 300));
        jPanelDestinatarios.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));

        jListDestinatarios.setModel(listModel);
        jListDestinatarios.setMaximumSize(new java.awt.Dimension(3300, 8000));
        jListDestinatarios.setMinimumSize(new java.awt.Dimension(300, 500));
        jListDestinatarios.setPreferredSize(new java.awt.Dimension(150, 1000));
        jScrollPane1.setViewportView(jListDestinatarios);

        jPanelDestinatarios.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelPrincipal.add(jPanelDestinatarios, java.awt.BorderLayout.WEST);

        jPanelMensaje.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensaje"));
        jPanelMensaje.setLayout(new java.awt.BorderLayout(20, 20));

        jPanelAsunto.setBorder(javax.swing.BorderFactory.createTitledBorder("Asunto"));
        jPanelAsunto.setLayout(new java.awt.BorderLayout());

        jTextFieldAsunto.setToolTipText("Escriba aqui el asunto");
        jPanelAsunto.add(jTextFieldAsunto, java.awt.BorderLayout.NORTH);

        jPanelMensaje.add(jPanelAsunto, java.awt.BorderLayout.NORTH);

        jPanelCuerpo.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuerpo"));
        jPanelCuerpo.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(jEditorPane1);

        jPanelCuerpo.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanelMensaje.add(jPanelCuerpo, java.awt.BorderLayout.CENTER);

        jPanelPrincipal.add(jPanelMensaje, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        jPanelBotonesAbajo.setPreferredSize(new java.awt.Dimension(500, 100));
        jPanelBotonesAbajo.setLayout(new java.awt.GridLayout(1, 4, 20, 20));

        jPanel8.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanelBotonesAbajo.add(jPanel8);

        jPanelBotonSimple.setPreferredSize(new java.awt.Dimension(100, 100));

        jButtonEnviarSimple.setText("Enviar mensaje Simple");
        jButtonEnviarSimple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarSimpleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotonSimpleLayout = new javax.swing.GroupLayout(jPanelBotonSimple);
        jPanelBotonSimple.setLayout(jPanelBotonSimpleLayout);
        jPanelBotonSimpleLayout.setHorizontalGroup(
            jPanelBotonSimpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonSimpleLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jButtonEnviarSimple)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanelBotonSimpleLayout.setVerticalGroup(
            jPanelBotonSimpleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonSimpleLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButtonEnviarSimple)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanelBotonesAbajo.add(jPanelBotonSimple);

        jPanelBotonAviso.setPreferredSize(new java.awt.Dimension(100, 100));

        jButtonEnviarAviso.setText("Enviar mensaje con aviso");
        jButtonEnviarAviso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarAvisoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotonAvisoLayout = new javax.swing.GroupLayout(jPanelBotonAviso);
        jPanelBotonAviso.setLayout(jPanelBotonAvisoLayout);
        jPanelBotonAvisoLayout.setHorizontalGroup(
            jPanelBotonAvisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBotonAvisoLayout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(jButtonEnviarAviso)
                .addGap(56, 56, 56))
        );
        jPanelBotonAvisoLayout.setVerticalGroup(
            jPanelBotonAvisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonAvisoLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButtonEnviarAviso)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanelBotonesAbajo.add(jPanelBotonAviso);

        jPanelBotonComprobante.setPreferredSize(new java.awt.Dimension(100, 100));

        jButtonEnviarConComprobante.setText("Enviar mensaje con comprobante");
        jButtonEnviarConComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarConComprobanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotonComprobanteLayout = new javax.swing.GroupLayout(jPanelBotonComprobante);
        jPanelBotonComprobante.setLayout(jPanelBotonComprobanteLayout);
        jPanelBotonComprobanteLayout.setHorizontalGroup(
            jPanelBotonComprobanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBotonComprobanteLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jButtonEnviarConComprobante)
                .addGap(37, 37, 37))
        );
        jPanelBotonComprobanteLayout.setVerticalGroup(
            jPanelBotonComprobanteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonComprobanteLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jButtonEnviarConComprobante)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanelBotonesAbajo.add(jPanelBotonComprobante);

        getContentPane().add(jPanelBotonesAbajo, java.awt.BorderLayout.PAGE_END);

        pack();
    }//GEN-END:initComponents

    private void jButtonListaContactosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListaContactosActionPerformed
        jButtonListaContactos.setEnabled(false);
        JFrame vistaContactos = new VistaContactos();
        vistaContactos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        vistaContactos.setVisible(true);

        vistaContactos.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(WindowEvent windowEvent) {
                jButtonListaContactos.setEnabled(true);
            }
        });
        
    }//GEN-LAST:event_jButtonListaContactosActionPerformed

    private void jButtonVerComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerComprobanteActionPerformed
        jButtonVerComprobante.setEnabled(false);
        JFrame vistaComprobantes = new VistaComprobantes();
        vistaComprobantes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        vistaComprobantes.setVisible(true);


        vistaComprobantes.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(WindowEvent windowEvent) {
                jButtonVerComprobante.setEnabled(true);
            }
        });
    }//GEN-LAST:event_jButtonVerComprobanteActionPerformed

    private void jButtonEnviarConComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnviarConComprobanteActionPerformed
        ControladorEmisor.getInstance()
            .enviarMensaje(getAsunto(), getCuerpo(), MensajeFactory.TipoMensaje.MSJ_CON_COMPROBANTE,
                           this.getDestinatarios());
        this.envioExitoso();
    }//GEN-LAST:event_jButtonEnviarConComprobanteActionPerformed

    private void jButtonEnviarSimpleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnviarSimpleActionPerformed
        ControladorEmisor.getInstance()
            .enviarMensaje(getAsunto(), getCuerpo(), MensajeFactory.TipoMensaje.MSJ_NORMAL, this.getDestinatarios());
        this.envioExitoso();
    }//GEN-LAST:event_jButtonEnviarSimpleActionPerformed

    private void jButtonEnviarAvisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnviarAvisoActionPerformed
        ControladorEmisor.getInstance()
            .enviarMensaje(getAsunto(), getCuerpo(), MensajeFactory.TipoMensaje.MSJ_CON_ALERTA,
                           this.getDestinatarios());
        this.envioExitoso();
    }//GEN-LAST:event_jButtonEnviarAvisoActionPerformed


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
                .getLogger(VistaEmisor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaEmisor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaEmisor.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaEmisor.class.getName())
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
                    new VistaEmisor().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEnviarAviso;
    private javax.swing.JButton jButtonEnviarConComprobante;
    private javax.swing.JButton jButtonEnviarSimple;
    private javax.swing.JButton jButtonListaContactos;
    private javax.swing.JButton jButtonVerComprobante;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JList<Receptor> jListDestinatarios;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelAsunto;
    private javax.swing.JPanel jPanelBotonAviso;
    private javax.swing.JPanel jPanelBotonComprobante;
    private javax.swing.JPanel jPanelBotonSimple;
    private javax.swing.JPanel jPanelBotonesAbajo;
    private javax.swing.JPanel jPanelBotonesArriba;
    private javax.swing.JPanel jPanelCuerpo;
    private javax.swing.JPanel jPanelDestinatarios;
    private javax.swing.JPanel jPanelListaContactos;
    private javax.swing.JPanel jPanelMensaje;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JPanel jPanelVerComprobante;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldAsunto;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getAsunto() {
        return this.jTextFieldAsunto.getText();
    }

    @Override
    public String getCuerpo() {
        return this.jEditorPane1.getText();
    }

    @Override
    public ArrayList<Receptor> getDestinatarios() {
        return new ArrayList<Receptor>(this.jListDestinatarios.getSelectedValuesList());
    }

    @Override
    public void mostrarErrorNoEmisor() {
        JOptionPane.showMessageDialog(this, "Error: no se pudo encontrar el archivo con los datos del emisor", "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
        JOptionPane jop = new JOptionPane("chau");
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
    }

    public void envioExitoso() {
        this.jTextFieldAsunto.setText("");
        this.jEditorPane1.setText("");
        this.jListDestinatarios.clearSelection();
        JOptionPane.showConfirmDialog(this, "Mensaje enviado correctamente", "Exito", JOptionPane.PLAIN_MESSAGE);
    }
    
    public void cargarContactos(){
        System.out.println("eeee");
        Iterator<Receptor> it = ControladorEmisor.getInstance().getContactos();
        System.out.println(it);
        while(it.hasNext()){
            System.out.println("aaaa");
            this.listModel.addElement(it.next());
        }
        
        this.repaint();
    }
    
}


