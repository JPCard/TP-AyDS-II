package emisor.controlador;

import emisor.modelo.AbstractMensajeFactory;
import emisor.modelo.IMensaje;
import emisor.modelo.MensajeConComprobante;
import emisor.modelo.MensajeFactory;

import emisor.modelo.SistemaEmisor;

import emisor.vista.IVistaComprobantes;
import emisor.vista.IVistaContactos;
import emisor.vista.IVistaEmisor;


import emisor.vista.VentanaModalCarga;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.Observable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.json.simple.parser.ParseException;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class ControladorEmisor extends Observable {
    public static final String RUTA_ICONO_CARGA = "enviando.gif";
    
    private IVistaEmisor vistaPrincipal;
    private IVistaContactos vistaContactos;
    private IVistaComprobantes vistaComprobantes;
    private static ControladorEmisor instance = null;
    private boolean directorioConectado =false;


    private ControladorEmisor(IVistaEmisor vista) {
        super();
        this.vistaPrincipal = vista;
        try {
            SistemaEmisor.inicializar();
            this.vistaPrincipal.setNombreUsuario(SistemaEmisor.getInstance().getNombreEmisor());
            
            
            
        } catch (IOException e) {
            vistaPrincipal.mostrarErrorEmisorContactos();
        }
        catch(ParseException e){
            vistaPrincipal.mostrarErrorEmisorContactos();
        }
        catch(Exception e){
            //error inesperado
            //e.printStackTrace();
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
    
    public void enviarMensajeConComprobante(String asunto, String cuerpo, ArrayList<Receptor> receptores) {
        this.enviarMensaje(asunto, cuerpo, receptores, AbstractMensajeFactory.TipoMensaje.MSJ_CON_COMPROBANTE);
    }
    
    public void enviarMensajeNormal(String asunto, String cuerpo, ArrayList<Receptor> receptores) {
        this.enviarMensaje(asunto, cuerpo, receptores, AbstractMensajeFactory.TipoMensaje.MSJ_NORMAL);
    }
    
    public void enviarMensajeConAlerta(String asunto, String cuerpo, ArrayList<Receptor> receptores) {
        this.enviarMensaje(asunto, cuerpo, receptores, AbstractMensajeFactory.TipoMensaje.MSJ_CON_ALERTA);
    }
    
    public void enviarMensaje(String asunto, String cuerpo, ArrayList<Receptor> receptores, MensajeFactory.TipoMensaje tipo){
        ArrayList<String> usuariosReceptores = new ArrayList<String>();
        for(Receptor receptor : receptores){
            usuariosReceptores.add(receptor.getUsuario());
        }
        
        new Thread(new Runnable(){

            @Override
            public void run() {
                if(!SistemaEmisor.getInstance().enviarMensaje(asunto,cuerpo,usuariosReceptores,tipo)) {
                    ControladorEmisor.getInstance().cerrarEnviando();
                    ControladorEmisor.getInstance().vistaPrincipal.mostrarErrorServidorNoDisponible();   
                }
                else{
                    ControladorEmisor.getInstance().cerrarEnviando();
                    ControladorEmisor.getInstance().vistaPrincipal.envioExitoso();
                }
                System.out.println("hilo chiquito llega  a su fin =====================================================================================");
            }
            
        }).start();
        
        
        ControladorEmisor.getInstance().mostrarEnviando();
        
       
        
        
        
        
        
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



    public void mostrarMensajeConComprobante(MensajeConComprobante mensaje) {
        if(vistaComprobantes != null)
            vistaComprobantes.agregarMensajeConComprobante(mensaje);
    }

    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor) {
        return SistemaEmisor.getInstance().isComprobado(mensajeSeleccionado,usuarioReceptor);
    }

    public void setAgenda(Collection<Receptor> destinatariosRegistrados) {
        SistemaEmisor.getInstance().setAgenda(destinatariosRegistrados);
        this.vistaPrincipal.cargarContactos(destinatariosRegistrados);
    }

    public void updateConectado(boolean estado) {
        this.directorioConectado=estado;
        this.vistaPrincipal.updateConectado(estado);
    }

    public boolean isDirectorioConectado() {
        return directorioConectado;
    }



    private void mostrarEnviando() {
        new VentanaModalCarga(this, "Enviando...", RUTA_ICONO_CARGA);
    }

    private void cerrarEnviando() {
        this.setChanged();
        this.notifyObservers();
    }
}
