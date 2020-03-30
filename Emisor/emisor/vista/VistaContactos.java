
package client;

/**
 *
 * @author Mau
 */
public class VistaContactos extends javax.swing.JFrame {

    /** Creates new form VistaContactos */
    public VistaContactos() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jPanelDestinatarios = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListDestinatarios = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Contactos");
        setMinimumSize(new java.awt.Dimension(200, 300));
        setPreferredSize(new java.awt.Dimension(200, 300));

        jPanelDestinatarios.setBorder(javax.swing.BorderFactory.createTitledBorder("Contactos"));
        jPanelDestinatarios.setMinimumSize(new java.awt.Dimension(600, 100));
        jPanelDestinatarios.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanelDestinatarios.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));

        jListDestinatarios.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListDestinatarios.setMaximumSize(new java.awt.Dimension(3300, 8000));
        jListDestinatarios.setMinimumSize(new java.awt.Dimension(300, 500));
        jListDestinatarios.setPreferredSize(new java.awt.Dimension(150, 1000));
        jScrollPane1.setViewportView(jListDestinatarios);

        jPanelDestinatarios.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelDestinatarios, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

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
                .getLogger(VistaContactos.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaContactos.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaContactos.class.getName())
                .log(java.util
                         .logging
                         .Level
                         .SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util
                .logging
                .Logger
                .getLogger(VistaContactos.class.getName())
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
                    new VistaContactos().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> jListDestinatarios;
    private javax.swing.JPanel jPanelDestinatarios;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
