package receptor.controlador;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.Mensaje;

import emisor.vista.IVistaEmisor;

import receptor.modelo.Comprobante;

import receptor.vista.IVistaReceptor;

public class ControladorReceptor {
    private IVistaReceptor vista;
    private static ControladorReceptor instance;
    
    private ControladorReceptor(IVistaReceptor vista) {
        super();
        this.vista = vista;
    }
    
    public static ControladorReceptor getInstance(IVistaReceptor vista){
        if(instance==null)
            instance=new ControladorReceptor(vista);
        return instance;
    }
    
    public static ControladorReceptor getInstance(){
        return instance;
    }
    
    public void enviarComprobante(Comprobante comprobante){
        
    }
    
    public void mostrarMensaje(Mensaje mensaje){
        this.vista.mostrarMensaje(mensaje);
        mensaje.onLlegada();
    }
}
