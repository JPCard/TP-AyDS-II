package emisor.controlador;

import emisor.modelo.Emisor;

import emisor.modelo.MensajeConComprobante;
import emisor.modelo.MensajeFactory;

import emisor.modelo.SistemaEmisor;

import emisor.vista.IVistaComprobantes;
import emisor.vista.IVistaContactos;
import emisor.vista.IVistaEmisor;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JList;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import receptor.vista.IVistaReceptor;

public class ControladorEmisor {
    private IVistaEmisor vistaPrincipal;
    private IVistaContactos vistaContactos;
    private IVistaComprobantes vistaComprobantes;
    private static ControladorEmisor instance = null;
    
    
    
    private ControladorEmisor(IVistaEmisor vista) {
        super();
        this.vistaPrincipal = vista;
        try {
            SistemaEmisor.inicializar();
            
            
        } catch (FileNotFoundException e) {
            vistaPrincipal.mostrarErrorNoEmisor();
        }
    }
    
    public static ControladorEmisor getInstance(IVistaEmisor vista){
        if(instance==null)
            instance=new ControladorEmisor(vista);
        return instance;
    }
    
    public static ControladorEmisor getInstance(){
        return instance;
    }
    
    public void enviarMensaje(String asunto, String cuerpo,MensajeFactory.TipoMensaje tipo, ArrayList<Receptor> receptores){
        SistemaEmisor.getInstance().enviarMensaje(asunto,cuerpo,receptores,tipo);
        
    }

    
    public void consultarAgenda(){
        Iterator<Receptor> it = SistemaEmisor.getInstance().getEmisor().consultarAgenda();
        while(it.hasNext()){
            vistaContactos.agregarContacto(it.next());
        }
    }

    public static void agregarComprobante(Comprobante comprobante) {
        
        instance.vistaComprobantes.actualizarComprobanteRecibidos(comprobante);
    }

    public static Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {
        return SistemaEmisor.getInstance().getMensajesConComprobanteIterator();
    }

    public Iterator<Receptor> getContactos() {
        return SistemaEmisor.getInstance().
                             consultarAgenda();
    }
}
