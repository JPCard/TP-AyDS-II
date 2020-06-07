package emisor.vista;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.SistemaEmisor;

import java.awt.BorderLayout;
import java.awt.Container;

import java.awt.Dimension;

import java.awt.Font;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class VentanaEnviando extends JDialog implements Observer
{
    public static final String RUTA_ICONO_CARGA = "enviando.gif";


    public VentanaEnviando()
    {
        super();
        ControladorEmisor.getInstance().addObserver(this);
        this.setUndecorated(true);
        this.setTitle("Guardando");
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setModal(true); //para que no puedan acceder a las otras ventanas mientras esta este activa
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel panelAux = new JPanel();
        panelAux.setLayout(new BorderLayout());

        panelAux.setPreferredSize(new Dimension(300, 230));
        panelAux.add("Center", new JLabel(new ImageIcon(RUTA_ICONO_CARGA), JLabel.CENTER));

        JLabel labelAux = new JLabel("Enviando...", JLabel.CENTER);
        labelAux.setVerticalAlignment(JLabel.BOTTOM);
        labelAux.setPreferredSize(new Dimension(40, 80));
        labelAux.setFont(new Font("Tahoma", Font.BOLD, 28));
        panelAux.add("North", labelAux);

        container.add("Center", panelAux);


        this.pack();
        this.setLocationRelativeTo(null); //centra el dialog
        this.setResizable(false);

        this.setVisible(true);
        this.repaint();
        
    }


    @Override
    public void update(Observable observable, Object object) {
        if(observable == ControladorEmisor.getInstance())
            if(object == null){
                this.dispose();
            }
        else
            throw new IllegalArgumentException();

    }

}
