package emisor.vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import receptor.vista.IVistaReceptor;
import receptor.vista.VistaReceptor;

public class PanelEstadoConexion extends JPanel{
    private JLabel labelConectado;
    private JLabel labelDesconectado;
    private JLabel labelIncierto;
    
    public PanelEstadoConexion(boolean conectado) {
        super();
        //set layout
        ImageIcon iconoConectado = new ImageIcon("IconoConectado.png", "icono conectado");
        ImageIcon iconoDesconectado = new ImageIcon("IconoDesconectado.png", "icono desconectado");
        ImageIcon iconoIncierto = new ImageIcon("IconoIncierto.png", "icono desconectado");
        labelConectado = new JLabel(iconoConectado, JLabel.CENTER);
        labelDesconectado = new JLabel(iconoDesconectado, JLabel.CENTER);
        labelIncierto = new JLabel(iconoIncierto, JLabel.CENTER);
        this.setPreferredSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(50,50));
        this.setBackground(Color.white);
        
        this.updateConectado(conectado);
        
        this.setVisible(true);
        this.revalidate();
        this.repaint();
        
    }

    public PanelEstadoConexion() {//este es el caso de amarillo: incierto
        super();
        //set layout
        ImageIcon iconoConectado = new ImageIcon("IconoConectado.png", "icono conectado");
        ImageIcon iconoDesconectado = new ImageIcon("IconoDesconectado.png", "icono desconectado");
        ImageIcon iconoIncierto = new ImageIcon("IconoIncierto.png", "icono incierto");
        labelConectado = new JLabel(iconoConectado, JLabel.CENTER);
        labelDesconectado = new JLabel(iconoDesconectado, JLabel.CENTER);
        labelIncierto = new JLabel(iconoIncierto, JLabel.CENTER);
        this.setPreferredSize(new Dimension(50,50));
        this.setMinimumSize(new Dimension(50,50));
        this.setBackground(Color.white);
        
        this.updateConectado();
        
        this.setVisible(true);
        this.revalidate();
        this.repaint();
        
    }

    /**
     * Pre: esto se llama solo una vez en el constructor por lo que no es necesario sacarle los label de conexion anteriores
     * @param conectado
     */
    public void updateConectado(boolean conectado) {
        
        if(conectado)
            this.add(labelConectado);
        else
            this.add(labelDesconectado);
        
        repaint();
    }
    
    
    /**
     * Pre: esto se llama solo una vez en el constructor por lo que no es necesario sacarle los label de conexion anteriores
     * Este es el caso de el estado incierto (amarillo)
     */
    public void updateConectado() {
        
        this.add(labelIncierto);
//        System.out.println("meti label incierto");
        repaint();
    }


}
