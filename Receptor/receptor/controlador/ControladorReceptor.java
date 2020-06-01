package receptor.controlador;


import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;


import java.io.FileNotFoundException;

import receptor.modelo.Comprobante;


import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;


import receptor.vista.IVistaReceptor;

public class ControladorReceptor {
    private IVistaReceptor vistaReceptor;
    private static ControladorReceptor instance;
    
    private ControladorReceptor(IVistaReceptor vista) {
        super();
        this.vistaReceptor = vista;
        
        try {
            this.avisarIniciandoSistema();
            SistemaReceptor.inicializar();
            this.avisarFinIniciandoSistema();
        } catch (FileNotFoundException e) {
            vistaReceptor.mostrarErrorNoReceptor();
        }
        
        
        
    }
    
    public static ControladorReceptor getInstance(IVistaReceptor vista){
        if(instance==null)
            instance=new ControladorReceptor(vista);
        return instance;
    }
    
    public static ControladorReceptor getInstance(){
        return instance;
    }
    
    public void enviarComprobante(Comprobante comprobante,Emisor emisor){
        SistemaReceptor.getInstance().getTcpdeReceptor().enviarComprobante(comprobante,emisor);
    }
    
    public void mostrarMensaje(Mensaje mensaje){
        
        this.vistaReceptor.agregarMensaje(mensaje);
        mensaje.onLlegada();
        
    }
    
    public void activarAlerta(){
        this.vistaReceptor.activarAlerta();
    }

    public Receptor getReceptor() {
        return SistemaReceptor.getInstance().getReceptor();
    }

    public void updateConectado(boolean estado) {
        this.vistaReceptor.updateConectado(estado);
    }

    private void avisarIniciandoSistema() {
        //TODO
    }

    private void avisarFinIniciandoSistema() {
        //TODO
    }
}
