package emisor.vista;


import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import receptor.modelo.IDatosReceptor;
import receptor.modelo.IDatosReceptor;

public class RendererContactos extends DefaultListCellRenderer {
    protected static DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    
    public RendererContactos() {
        super();
    }
    
    
    public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        IDatosReceptor receptor =  (IDatosReceptor) value;
          JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                  isSelected, cellHasFocus);
        renderer.setText("<html>"+receptor.descripcionCompleta().replaceAll("\n","<br>")+"</html>");
        renderer.setBorder(BorderFactory.createLineBorder(Color.black));
        
        return renderer;
      }
}