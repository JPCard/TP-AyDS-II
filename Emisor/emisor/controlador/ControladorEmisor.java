package emisor.controlador;

import emisor.modelo.Emisor;

import emisor.modelo.MensajeFactory;

import emisor.vista.IVistaEmisor;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class ControladorEmisor {
    private IVistaEmisor vista;
    private static ControladorEmisor instance = null;
    
    
    
    private ControladorEmisor(IVistaEmisor vista) {
        super();
        this.vista = vista;

    }
    
    public static ControladorEmisor getInstance(IVistaEmisor vista){
        if(instance==null)
            instance=new ControladorEmisor(vista);
        return instance;
    }
    
    public static ControladorEmisor getInstance(){
        return instance;
    }
    
    public void enviarMensaje(String asunto, String cuerpo,MensajeFactory.TipoMensaje tipo, Receptor receptor){
        Emisor.getInstance().enviarMensaje(asunto,cuerpo,receptor);
    }

    
    public void consultarAgenda(){
        //TODO agregar contenido
    }

    public static void agregarComprobante(Comprobante comprobante) {
        vista.agregarComprobante(comprobante);
    }
}
