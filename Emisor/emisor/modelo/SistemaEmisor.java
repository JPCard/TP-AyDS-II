package emisor.modelo;

import directorio.modelo.DirectorioMain;

import emisor.controlador.ControladorEmisor;

import emisor.modelo.MensajeFactory.TipoMensaje;

import emisor.persistencia.IPersistenciaEmisor;
import emisor.persistencia.PersistenciaEmisor;

import emisor.red.TCPDestinatariosRegistrados;
import emisor.red.TCPdeEmisor;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.TreeSet;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public class SistemaEmisor {
    private Emisor emisor;
    private TCPdeEmisor tcpdeEmisor;
    
    private static SistemaEmisor instance;
    
    private HashMap<Integer,Mensaje> mensajesEnviados = new HashMap<Integer,Mensaje>();
    private HashMap<Integer,MensajeConComprobante> mensajesConComprobante = new HashMap<Integer,MensajeConComprobante>();
    
    private HashMap<Integer,ArrayList<Receptor>> listasReceptoresConfirmados = new HashMap<Integer,ArrayList<Receptor>>();
    private IPersistenciaEmisor persistencia = new PersistenciaEmisor();
    
    private SistemaEmisor() throws FileNotFoundException {
        super();
        emisor = persistencia.cargarEmisor();
        //viejo no va mas
        //emisor.setAgenda(persistencia.cargarAgenda());    
        
        this.tcpdeEmisor = new TCPdeEmisor();
       
    }
        
    public static void inicializar() throws FileNotFoundException {
        if(instance==null){
            instance = new SistemaEmisor();
            Thread hiloenviar = new Thread(instance.tcpdeEmisor);
            hiloenviar.start();//TODO CAMBIAR DONDE ESTA LA IP
            Thread hiloDestinatarios = new Thread(new TCPDestinatariosRegistrados(DirectorioMain.DIRECTORIO_IP,DirectorioMain.GETDESTINATARIOS_PORT));
            hiloDestinatarios.start();
        }
            
        
        
        
        
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
            ControladorEmisor.getInstance().agregarMensajeConComprobante((MensajeConComprobante)mensaje);
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

    public Iterator<Receptor> getReceptoresConfirmados(Mensaje mensaje) {
        
        return this.listasReceptoresConfirmados.get(mensaje.getId()).iterator();
    }
    
    public boolean hayReceptoresConfirmados(Mensaje mensaje){
        return this.listasReceptoresConfirmados.containsKey(mensaje.getId());
    }

    public int getPuerto() {
        return this.getEmisor().getPuerto();
    }

    public boolean isComprobado(Mensaje mensajeSeleccionado, Receptor receptor) {
        ArrayList<Receptor> receptoresConfirmados = this.listasReceptoresConfirmados.get(mensajeSeleccionado.getId());
        if(receptoresConfirmados== null)
            return false;
        else
            return receptoresConfirmados.contains(receptor);
    }

    public void setAgenda(Collection<Receptor> destinatariosRegistrados) {
        Agenda agenda = new Agenda();
        TreeSet<Receptor> contactosT = new TreeSet<Receptor>(destinatariosRegistrados);
        
        
        agenda.setContactos(contactosT);
        
        this.getEmisor().setAgenda(agenda);
    }
}
