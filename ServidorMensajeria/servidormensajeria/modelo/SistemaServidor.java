package servidormensajeria.modelo;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;

import emisor.modelo.MensajeConComprobante;

import java.util.ArrayList;

import java.util.Collection;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import servidormensajeria.persistencia.IPersistenciaMensajesServidor;

import servidormensajeria.persistencia.IPersistenciaParametrosServidor;

import servidormensajeria.persistencia.PersistenciaMensajesFactory;
import servidormensajeria.persistencia.PersistenciaParametrosServidor;

import servidormensajeria.red.ComprobanteHandler;
import servidormensajeria.red.MensajeHandler;
import servidormensajeria.red.TCPParaDirectorio;
import servidormensajeria.red.ComprobanteListener;
import servidormensajeria.red.MensajeListener;
import servidormensajeria.red.SolicitudMensajesEmisoresListener;

public class SistemaServidor {
    public final static int PUERTO_RECEPCION_MENSAJES = 27446;
    public final static int PUERTO_COMPROBANTES = 27447;
    public static final int PUERTO_DEVOLVER_MENSAJES_A_EMISORES = 27448;
    public static final int PUERTO_INFO_DIRECTORIO = 41149; //todo estos supongo tendrian q salir de algun archivo
    
    private static SistemaServidor instance;
    private IPersistenciaMensajesServidor persistenciaMensajes;
    private TCPParaDirectorio tcpParaDirectorio;
    
    //cosas para saber donde estna los usuarios
    private ArrayList<Receptor> receptores = new ArrayList<Receptor>(); //necesario para el synchronized
    private Long tiempoUltimaActualizacionReceptores = new Long(-1);
    
    private IPersistenciaParametrosServidor persistenciaParametros = new PersistenciaParametrosServidor();
    private String ipDirectorio;
    private int puertoDirectorioDest;
    private int puertoDirectorioTiempo;
    

    public static void main(String[] args) throws Exception {
        SistemaServidor sistema = getInstance();
        
        IPersistenciaParametrosServidor persistencia = sistema.persistenciaParametros;
        
        String metodoPersistencia = persistencia.cargarMetodoPersistenciaMsjs();
        sistema.persistenciaMensajes = PersistenciaMensajesFactory.getInstance().crearMetodoPersistenciaMensajes(metodoPersistencia);
        
        sistema.ipDirectorio = persistencia.cargarIPDirectorio();
        sistema.puertoDirectorioDest = persistencia.cargarPuertoDirectorioDestinatarios();
        sistema.puertoDirectorioTiempo = persistencia.cargarPuertoDirectorioTiempoUltModif();
        sistema.tcpParaDirectorio = new TCPParaDirectorio(sistema.ipDirectorio,sistema.puertoDirectorioDest,sistema.puertoDirectorioTiempo);
        new Thread(new MensajeListener()).start();
        System.out.println("hola");
        new Thread(new ComprobanteListener()).start();
        
        new Thread(new SolicitudMensajesEmisoresListener()).start();
        
        new Thread(instance.tcpParaDirectorio).start();
        
    }


    /**
     *Pre: un id de mensaje fue entregado a un emisor
     */
    public void avanzaProximoIdMensaje() {
        this.persistenciaMensajes.avanzaProximoIdMensaje();
    }


    public int getProximoIdMensaje() {
        return persistenciaMensajes.getProximoIdMensaje();
    }

    public Long getTiempoUltimaActualizacionReceptores() {
        synchronized(this.tiempoUltimaActualizacionReceptores){
            return tiempoUltimaActualizacionReceptores;
        }
    }

    public void setReceptores(ArrayList<Receptor> receptores) {
        synchronized(this.receptores){
            this.receptores = receptores;
        }
    }

    public ArrayList<Receptor> getReceptores() {
        synchronized(this.receptores){
            return receptores;
        }
    }

    public void setTiempoUltimaActualizacionReceptores(long tiempoUltimaActualizacionReceptores) {
        synchronized(this.tiempoUltimaActualizacionReceptores){
            this.tiempoUltimaActualizacionReceptores = tiempoUltimaActualizacionReceptores;
        }
    }

    private SistemaServidor() {
        super();
    }

    public static SistemaServidor getInstance(){
        if(instance == null)
            instance = new SistemaServidor();
        return instance;
    }
    
    public void arriboMensaje(Mensaje mensaje){
            new Thread(new MensajeHandler(mensaje,true)).start();
    }

    public IPersistenciaMensajesServidor getPersistencia() {
        return this.persistenciaMensajes;
    }

    public TCPParaDirectorio getDirectorio() {
        return this.tcpParaDirectorio;
    }

    /**
     * @param usuarioActual
     * @return null si el receptor no esta conectado o no hay conexion con el directorio, != null si el receptor esta conectado
     */
    public Receptor getReceptor(String usuarioActual) {
        return getDirectorio().getReceptor(usuarioActual);
    }

    /**
     * @param receptor
     * @return null si no hay mensajes para ese receptor, una coleccion de los mensajes para ese receptor en caso contrario
     * @throws Exception
     */
    public Collection<Mensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception{
        return persistenciaMensajes.obtenerMsjsPendientesReceptor(receptor);
    }

    /**
     * @param emisor
     * @return null si no hay mensajes enviados de ese emisor, una coleccion de los mensajes enviados de ese emisor en caso contrario
     * @throws Exception
     */
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(Emisor emisor) throws Exception{
        return persistenciaMensajes.obtenerMsjsComprobadosEmisor(emisor);
    }
    
    
    public void guardarMsj(Mensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception{
        persistenciaMensajes.guardarMsj(mensaje, usuarioReceptor, entregado);
    }

    public void guardarComp(Comprobante comprobante) throws Exception{ 
        persistenciaMensajes.guardarComp(comprobante);
    }

    
    public void marcarMensajeEnviado(Mensaje mensaje,String usuarioReceptor, boolean primerIntento) throws Exception{
        persistenciaMensajes.marcarMensajeEnviado(mensaje, usuarioReceptor, primerIntento);
    }

    public void arriboComprobante(Comprobante comprobante) {
        new Thread(new ComprobanteHandler(comprobante)).start();
    }

    
}
