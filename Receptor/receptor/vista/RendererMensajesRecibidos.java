package receptor.vista;


import emisor.modelo.Mensaje;

import java.awt.Color;
import java.awt.Component;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;


public class RendererMensajesRecibidos extends DefaultListCellRenderer {
    protected static DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    
    public RendererMensajesRecibidos() {
        super();
    }
    
    
    public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
            Mensaje mensaje =  (Mensaje) value;
          JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                  isSelected, cellHasFocus);
        renderer.setText("<html>"+mensaje.toString().replaceAll("\n","<br>")+"</html>");
        renderer.setBorder(BorderFactory.createLineBorder(Color.black));
        renderer.setMaximumSize(new Dimension(99999,99999));
        
        return renderer;
      }
}