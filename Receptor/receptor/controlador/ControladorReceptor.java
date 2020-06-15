package receptor.controlador;


import emisor.modelo.Emisor;
import emisor.modelo.IMensaje;


import emisor.vista.VentanaModalCarga;

import java.io.FileNotFoundException;

import java.security.PrivateKey;

import java.util.Observable;

import receptor.modelo.Comprobante;


import receptor.modelo.DesencriptarRSA;
import receptor.modelo.IDesencriptar;
import receptor.modelo.Receptor;
import receptor.modelo.SistemaReceptor;


import receptor.vista.IVistaReceptor;

public class ControladorReceptor extends Observable{
    public static final String RUTA_ICONO_INICIANDO = "iniciando.gif";
    
    private IVistaReceptor vistaReceptor;
    private static ControladorReceptor instance;
    private IDesencriptar desencriptador;
    
    private ControladorReceptor(IVistaReceptor vista) {
        super();
        this.vistaReceptor = vista;
        this.desencriptador = new DesencriptarRSA();
        new Thread(){

            @Override
            public void run() {
                super.run();
                try {
                    SistemaReceptor.inicializar();
                    avisarFinIniciandoSistema();
                } catch (FileNotFoundException e) {
                    vistaReceptor.mostrarErrorNoReceptor();
                }
                
            }
        }.start();
        
        
        this.avisarIniciandoSistema();
        
        
        
        
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
    
    public void mostrarMensaje(IMensaje mensaje){
        //
        
        PrivateKey privateKey = SistemaReceptor.getInstance().getLlavePrivada();
        
        this.vistaReceptor.agregarMensaje(this.desencriptador.desencriptar(mensaje,privateKey ));
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
        new VentanaModalCarga(this, "Iniciando sistema...", RUTA_ICONO_INICIANDO);
    }

    private void avisarFinIniciandoSistema() {
        setChanged();
        notifyObservers();
    }

    public String getUsuarioReceptor() {
        return SistemaReceptor.getInstance().getUsuarioReceptor();
    }
}
