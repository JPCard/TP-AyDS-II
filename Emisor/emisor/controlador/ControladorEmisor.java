package emisor.controlador;

import emisor.modelo.Emisor;

import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;
import emisor.modelo.MensajeFactory;

import emisor.modelo.SistemaEmisor;

import emisor.vista.IVistaComprobantes;
import emisor.vista.IVistaContactos;
import emisor.vista.IVistaEmisor;

import emisor.vista.VistaComprobantes;

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

    public IVistaComprobantes getVistaComprobantes() {
        return vistaComprobantes;
    }

    public void agregarComprobante(Comprobante comprobante) {
        
        SistemaEmisor.getInstance().agregarComprobante(comprobante);
        if(vistaComprobantes != null)
            vistaComprobantes.actualizarComprobanteRecibidos(comprobante);
    }

    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {
        return SistemaEmisor.getInstance().getMensajesConComprobanteIterator();
    }

    public Iterator<Receptor> getContactos() {
        return SistemaEmisor.getInstance().
                             consultarAgenda();
    }

    public void setVistaComprobantes(IVistaComprobantes vistaComprobantes) {
        this.vistaComprobantes = vistaComprobantes;
    }

    public Iterator<Receptor> getReceptoresConfirmados(Mensaje mensaje) throws Exception {
        if(SistemaEmisor.getInstance().hayReceptoresConfirmados(mensaje))
            return SistemaEmisor.getInstance().getReceptoresConfirmados(mensaje);
        else throw new Exception("Todavia no hay receptores confirmados");
    }

    public void agregarMensajeConComprobante(MensajeConComprobante mensaje) {
        if(vistaComprobantes != null)
            vistaComprobantes.agregarMensajeConComprobante(mensaje);
    }

    public boolean isComprobado(Mensaje mensajeSeleccionado, Receptor receptor) {
        return SistemaEmisor.getInstance().isComprobado(mensajeSeleccionado,receptor);
    }
}
