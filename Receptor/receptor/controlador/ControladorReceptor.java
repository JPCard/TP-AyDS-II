package receptor.controlador;


import emisor.modelo.IDatosEmisor;
import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;


import emisor.vista.VentanaModalCarga;

import java.io.FileNotFoundException;

import java.security.PrivateKey;

import java.util.Observable;

import receptor.modelo.IComprobante;


import receptor.modelo.DesencriptarRSA;
import receptor.modelo.IDesencriptar;
import receptor.modelo.IDatosReceptor;
import receptor.modelo.ISistemaReceptor;
import receptor.modelo.SistemaReceptor;


import receptor.vista.IVistaReceptor;

public class ControladorReceptor extends Observable{
    public static final String RUTA_ICONO_INICIANDO = "iniciando.gif";
    
    private IVistaReceptor vistaReceptor;
    private static ControladorReceptor instance;
    private ISistemaReceptor sistemaReceptor;
    
    private ControladorReceptor(IVistaReceptor vista) {
        super();
        this.vistaReceptor = vista;
        new Thread(){

            @Override
            public void run() {
                super.run();
                try {
                    sistemaReceptor = new SistemaReceptor();
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
    
    public void enviarComprobante(IComprobante comprobante, IDatosEmisor emisor){
        sistemaReceptor.enviarComprobante(comprobante,emisor);
    }
    
    public void mostrarMensaje(IMensaje mensaje){
        this.vistaReceptor.agregarMensaje(mensaje);
    }
    
    public void activarAlerta(){
        this.vistaReceptor.activarAlerta();
    }

    public IDatosReceptor getReceptor() {
        return sistemaReceptor.getReceptor();
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
        return sistemaReceptor.getUsuarioReceptor();
    }
}
