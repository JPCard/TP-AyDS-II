package emisor.modelo;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.MensajeFactory.TipoMensaje;

import emisor.red.TCPdeEmisor;

import java.beans.XMLDecoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class SistemaEmisor {
    private Emisor emisor;
    private TCPdeEmisor tcpdeEmisor;
    public static final String PARAMETROS_FILE_PATH = "ParametrosEmisor.xml";
    private static SistemaEmisor instance;
    
    private HashMap<Integer,Mensaje> mensajesEnviados = new HashMap<Integer,Mensaje>();
    private HashMap<Integer,MensajeConComprobante> mensajesConComprobante = new HashMap<Integer,MensajeConComprobante>();
    
    private HashMap<Integer,ArrayList<Receptor>> listasReceptoresConfirmados = new HashMap<Integer,ArrayList<Receptor>>();
    
    
    private SistemaEmisor() throws FileNotFoundException {
        super();
        XMLDecoder decoder;
        
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(PARAMETROS_FILE_PATH)));
            emisor = (Emisor) decoder.readObject();
            
        
        this.tcpdeEmisor = new TCPdeEmisor();
       
    }
        
    public static void inicializar() throws FileNotFoundException {
        if(instance==null)
            instance = new SistemaEmisor();
        
        Thread t = new Thread(instance.tcpdeEmisor);
        t.start();
    }
    
    public static SistemaEmisor getInstance(){
        return instance;
    }


    public Emisor getEmisor() {
        return emisor;
    }

    public TCPdeEmisor getTcpdeEmisor() {
        return tcpdeEmisor;
    }

    public void enviarMensaje(String asunto, String cuerpo, ArrayList<Receptor> receptores, TipoMensaje tipoMensaje){
        Mensaje mensaje = MensajeFactory.crearMensaje(this.emisor, asunto, cuerpo, tipoMensaje,receptores);
        this.guardarMensaje(mensaje);
        
        if(tipoMensaje == MensajeFactory.TipoMensaje.MSJ_CON_COMPROBANTE){
            mensajesConComprobante.put(mensaje.getId(),(MensajeConComprobante) mensaje);
        }
        
        this.getTcpdeEmisor().enviarMensaje(mensaje);
    }
    
    private void guardarMensaje(Mensaje mensaje){
        this.mensajesEnviados.put(mensaje.getId(),mensaje);
    }
    
    public Iterator<Receptor> consultarAgenda(){
        return this.emisor.consultarAgenda();
    }

    public Iterator<MensajeConComprobante> getMensajesConComprobanteIterator() {
        
        return this.mensajesConComprobante.values().iterator();
    }
//ODEU
    public void agregarComprobante(Comprobante comprobante) {
        int idMensaje = comprobante.getidMensaje();
        
        if(this.mensajesConComprobante.containsKey(idMensaje)){
                
            if(!listasReceptoresConfirmados.containsKey(idMensaje))
                listasReceptoresConfirmados.put(idMensaje,new ArrayList<Receptor>()); //si es el primer comprobante, crea el arraylist
           

            this.listasReceptoresConfirmados.get(idMensaje).add(comprobante.getReceptor());  
        }
        //else ////este mensaje no lo mandamos nosotros!
    }

    public Iterator<Receptor> getReceptoresConfirmados(int idMensaje) {
        
        return this.listasReceptoresConfirmados.get(idMensaje).iterator();
    }

    public int getPuerto() {
        return this.getEmisor().getPuerto();
    }
}
