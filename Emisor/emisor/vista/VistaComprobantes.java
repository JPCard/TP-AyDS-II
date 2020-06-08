
package emisor.vista;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;

import java.awt.Color;

import java.awt.Component;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Iterator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import javax.swing.JList;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import receptor.vista.RendererMensajesRecibidos;

/**
 *
 * @author Mau
 */
public class VistaComprobantes extends javax.swing.JFrame implements IVistaComprobantes {



    private class RedGreenCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            String usuarioReceptor = (String) value;
            VistaComprobantes v = (VistaComprobantes)ControladorEmisor.getInstance().getVistaComprobantes();
            MensajeConComprobante mensajeSeleccionado = v.jListMensajes.getSelectedValue();
            if (ControladorEmisor.getInstance().isComprobado(mensajeSeleccionado,usuarioReceptor)) {
                c.setBackground(Color.green);
            } else {
                c.setBackground(Color.red);
            }
            return c;
        }
       
        
        
    }


    DefaultListModel<MensajeConComprobante> listModelMensajes = new DefaultListModel<MensajeConComprobante>();
    DefaultListModel<String> listModelReceptores = new DefaultListModel<String>();

    /** Creates new form VistaComprobantes */
    public VistaComprobantes() {
        initComponents();
        
        this.addWindowListener(new WindowAdapter() {


            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ControladorEmisor.getInstance().setVistaComprobantes(null);
            }
        });
        
        jTextAreaAsunto.setEditable(false);
        jTextAreaCuerpo.setEditable(false);
        this.jListMensajes.setModel(listModelMensajes);

        this.iniciaMensajes();
        this.jListReceptores.setCellRenderer(new RedGreenCellRenderer());
        
        this.jListMensajes.setCellRenderer(new RendererMensajesRecibidos());
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanelPrincipal = new javax.swing.JPanel();
        jPanelMensajes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListMensajes = new javax.swing.JList<>();
        jPanelContenido = new javax.swing.JPanel();
        jPanelAsunto = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaAsunto = new javax.swing.JTextArea();
        jPanelCuerpo = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaCuerpo = new javax.swing.JTextArea();
        jPanelDestinatarios = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListReceptores = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mensajes con Comprobante");
        setMinimumSize(new java.awt.Dimension(1000, 450));
        setPreferredSize(new java.awt.Dimension(1000, 300));

        jPanelPrincipal.setLayout(new java.awt.BorderLayout(20, 20));

        jPanelMensajes.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensajes con Comprobante"));
        jPanelMensajes.setMinimumSize(new java.awt.Dimension(600, 300));
        jPanelMensajes.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(1000, 50));

        jListMensajes.setModel(listModelMensajes);
        jListMensajes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListMensajes.setMaximumSize(new java.awt.Dimension(3300, 8000));
        jListMensajes.setMinimumSize(new java.awt.Dimension(1000, 500));
        jListMensajes.setPreferredSize(new java.awt.Dimension(400, 1000));
        jListMensajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListMensajesMouseClicked(evt);
            }
        });
        jListMensajes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListMensajesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListMensajes);

        jPanelMensajes.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelPrincipal.add(jPanelMensajes, java.awt.BorderLayout.WEST);

        jPanelContenido.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensaje"));
        jPanelContenido.setMinimumSize(new java.awt.Dimension(200, 125));
        jPanelContenido.setPreferredSize(new java.awt.Dimension(200, 239));
        jPanelContenido.setLayout(new java.awt.BorderLayout(20, 20));

        jPanelAsunto.setBorder(javax.swing.BorderFactory.createTitledBorder("Asunto"));
        jPanelAsunto.setLayout(new java.awt.BorderLayout());

        jTextAreaAsunto.setColumns(20);
        jTextAreaAsunto.setLineWrap(true);
        jTextAreaAsunto.setRows(5);
        jScrollPane3.setViewportView(jTextAreaAsunto);

        jPanelAsunto.add(jScrollPane3, java.awt.BorderLayout.PAGE_START);

        jPanelContenido.add(jPanelAsunto, java.awt.BorderLayout.NORTH);

        jPanelCuerpo.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuerpo"));
        jPanelCuerpo.setLayout(new java.awt.BorderLayout());

        jTextAreaCuerpo.setColumns(20);
        jTextAreaCuerpo.setLineWrap(true);
        jTextAreaCuerpo.setRows(5);
        jScrollPane2.setViewportView(jTextAreaCuerpo);

        jPanelCuerpo.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanelContenido.add(jPanelCuerpo, java.awt.BorderLayout.CENTER);

        jPanelPrincipal.add(jPanelContenido, java.awt.BorderLayout.CENTER);

        jPanelDestinatarios.setMinimumSize(new java.awt.Dimension(250, 130));
        jPanelDestinatarios.setPreferredSize(new java.awt.Dimension(250, 300));
        jPanelDestinatarios.setLayout(new java.awt.GridLayout(3, 0));
        jPanelDestinatarios.add(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Destinatario"));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 71));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setMinimumSize(new java.awt.Dimension(200, 50));

        jListReceptores.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jListReceptores.setModel(listModelReceptores
        );
        jListReceptores.setMaximumSize(new java.awt.Dimension(3300, 8000));
        jListReceptores.setMinimumSize(new java.awt.Dimension(200, 500));
        jListReceptores.setPreferredSize(new java.awt.Dimension(200, 1000));
        jScrollPane4.setViewportView(jListReceptores);

        jPanel2.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanelDestinatarios.add(jPanel2);

        jPanelPrincipal.add(jPanelDestinatarios, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void jListMensajesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListMensajesValueChanged
        MensajeConComprobante elegido = this.jListMensajes.getSelectedValue();
        
        if(elegido!=null){
            this.jTextAreaAsunto.setText(elegido.getAsunto());
            this.jTextAreaCuerpo.setText(elegido.getCuerpo());

            this.listModelReceptores.clear();
            this.jListReceptores.clearSelection();

            /*Iterator<String> it = elegido.getReceptores();
            while (it.hasNext()) {
                listModelReceptores.addElement(it.next());
            }*/
            
            listModelReceptores.addElement(elegido.getReceptorObjetivo());

           

        }
        
        
    
    }//GEN-LAST:event_jListMensajesValueChanged

    private void jListMensajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMensajesMouseClicked

    }//GEN-LAST:event_jListMensajesMouseClicked

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
                .getLogger(VistaComprobantes.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaComprobantes.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaComprobantes.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaComprobantes.class.getName())
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
                    new VistaComprobantes().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<MensajeConComprobante> jListMensajes;
    private javax.swing.JList<String> jListReceptores;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelAsunto;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelCuerpo;
    private javax.swing.JPanel jPanelDestinatarios;
    private javax.swing.JPanel jPanelMensajes;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextAreaAsunto;
    private javax.swing.JTextArea jTextAreaCuerpo;
    // End of variables declaration//GEN-END:variables


    private void iniciaMensajes() {
        Iterator<MensajeConComprobante> it = ControladorEmisor.getInstance().getMensajesConComprobanteIterator();
        while (it.hasNext()) {
            listModelMensajes.add(0,it.next());
        }
        ControladorEmisor.getInstance().setVistaComprobantes(this);
        this.jListMensajes.setSelectedIndex(0);
        
    }

    @Override
    public void agregarMensajeConComprobante(MensajeConComprobante mensaje) {
        
        //este IF se agrego para evitar que cuando se cargan los con comprobante 2 veces no se vean 2 veces
        if(!listModelMensajes.contains(mensaje)){
            listModelMensajes.add(0, mensaje);
            if(listModelMensajes.size() == 1){
                this.jListMensajes.setSelectedIndex(0);
            }
        }
        
    }

    @Override
    public void actualizarComprobanteRecibidos(Comprobante comprobante) {
        repaint();//le avisa a lista que tiene que actualiza el estado de un receptor a traves de su cell renderer
    }
    
    
}


