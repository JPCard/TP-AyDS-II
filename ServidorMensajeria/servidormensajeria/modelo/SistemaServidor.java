package servidormensajeria.modelo;

import emisor.modelo.Mensaje;

import java.util.ArrayList;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

import servidormensajeria.persistencia.IPersistenciaMensajesServidor;

import servidormensajeria.persistencia.IPersistenciaParametrosServidor;

import servidormensajeria.persistencia.PersistenciaParametrosServidor;

import servidormensajeria.red.TCPParaDirectorio;
import servidormensajeria.red.ComprobanteListener;
import servidormensajeria.red.MensajeListener;

public class SistemaServidor {
    public final static int PUERTO_MENSAJES = 27446;
    public final static int PUERTO_COMPROBANTES = 27447; //todo estos supongo tendrian q salir de algun archivo
    
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
        //la linea anterior hay que cargar del archivo el metodo de persistencia
        //String id_metodo = sistema.persistenciaParametros.cargarMetodoPersistenciaMsjs();
        //metodoPersistenciaMsjsFactory.instance(id_metodo); TODO
        
        sistema.ipDirectorio = sistema.persistenciaParametros.cargarIPDirectorio();
        sistema.puertoDirectorioDest = sistema.persistenciaParametros.cargarPuertoDirectorioDestinatarios();
        sistema.puertoDirectorioTiempo = sistema.persistenciaParametros.cargarPuertoDirectorioTiempoUltModif();
        sistema.tcpParaDirectorio = new TCPParaDirectorio(sistema.ipDirectorio,sistema.puertoDirectorioDest,sistema.puertoDirectorioTiempo);
        new Thread(new MensajeListener()).start();
        System.out.println("hola");
        new Thread(new ComprobanteListener()).start();
        
    }


    public long getTiempoUltimaActualizacionReceptores() {
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
            new Thread(new MensajeHandler(mensaje)).start();
    }

    public IPersistenciaMensajesServidor getPersistencia() {
        return this.persistenciaMensajes;
    }

    public TCPParaDirectorio getDirectorio() {
        return this.tcpParaDirectorio;
    }

    /**
     * @param usuarioActual
     * @return null si el receptor no esta conectado, != null si el receptor esta conectado
     */
    Receptor getReceptor(String usuarioActual) {
        return getDirectorio().getReceptor(usuarioActual);
    }

    public void arriboComprobante(Comprobante comprobante) {
        new Thread(new ComprobanteHandler(comprobante)).start();
    }
}
