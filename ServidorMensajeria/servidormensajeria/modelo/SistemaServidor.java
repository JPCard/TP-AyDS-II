package servidormensajeria.modelo;

import emisor.modelo.Emisor;
import emisor.modelo.IMensaje;

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
import servidormensajeria.red.TCPConsultaDirectorio;
import servidormensajeria.red.ComprobanteListener;
import servidormensajeria.red.MensajeListener;
import servidormensajeria.red.SolicitudComprobantesEmisoresListener;
import servidormensajeria.red.WatchDogDirectorio;

public class SistemaServidor {

    private static SistemaServidor instance;
    private IPersistenciaMensajesServidor persistenciaMensajes;
    private TCPConsultaDirectorio tcpParaDirectorio;

    //cosas para saber donde estna los usuarios
    private ArrayList<Receptor> receptores = new ArrayList<Receptor>(); //necesario para el synchronized
    private Long tiempoUltimaActualizacionReceptores = new Long(-1);

    private IPersistenciaParametrosServidor persistenciaParametros = new PersistenciaParametrosServidor();
    private Thread watchDogDirectorio;


    public static void main(String[] args) throws Exception {
        SistemaServidor sistema = getInstance();

        IPersistenciaParametrosServidor persistencia = sistema.persistenciaParametros;

        String metodoPersistencia = persistencia.cargarMetodoPersistenciaMsjs();
        sistema.persistenciaMensajes =
            PersistenciaMensajesFactory.getInstance().crearMetodoPersistenciaMensajes(metodoPersistencia);

        sistema.tcpParaDirectorio =
            new TCPConsultaDirectorio(persistencia.cargarIPDirectorio(),
                                  persistencia.cargarPuertoDirectorioTiempoUltModif(), persistencia.cargarPuertoDirectorioDestinatarios(),

                                  persistencia.cargarIPDirectorioSecundario(),
                                  persistencia.cargarPuertoDirectorioSecundarioTiempo(),
                                  persistencia.cargarPuertoDirectorioSecundarioDestinatarios());

        sistema.watchDogDirectorio =
            new Thread(new WatchDogDirectorio(persistencia.cargarIPDirectorio(),
                                              persistencia.cargarpuertoDirectorioPrincipalPushReceptores(),
                                              persistencia.cargarIPDirectorioSecundario(),
                                              persistencia.cargarPuertoDirectorioSecundarioPushReceptores()));


        new Thread(new MensajeListener()).start();
        //        System.out.println("hola");
        new Thread(new ComprobanteListener()).start();
        new Thread(new SolicitudComprobantesEmisoresListener()).start();
        sistema.watchDogDirectorio.start();
        
        
        sistema.tcpParaDirectorio.envioInicialMensajesAsincronicos();

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
        synchronized (this.tiempoUltimaActualizacionReceptores) {
            return tiempoUltimaActualizacionReceptores;
        }
    }

    public void setReceptores(ArrayList<Receptor> receptores) {
        synchronized (this.receptores) {
            this.receptores = receptores;
        }
    }

    public ArrayList<Receptor> getReceptores() {
        synchronized (this.receptores) {
            return receptores;
        }
    }

    public void setTiempoUltimaActualizacionReceptores(long tiempoUltimaActualizacionReceptores) {
        synchronized (this.tiempoUltimaActualizacionReceptores) {
            this.tiempoUltimaActualizacionReceptores = tiempoUltimaActualizacionReceptores;
        }
    }

    private SistemaServidor() {
        super();
    }

    public static SistemaServidor getInstance() {
        if (instance == null)
            instance = new SistemaServidor();
        return instance;
    }

    public void arriboMensaje(IMensaje mensaje) {
        new Thread(new MensajeHandler(mensaje, true)).start();
    }

    public IPersistenciaMensajesServidor getPersistencia() {
        return this.persistenciaMensajes;
    }

    public TCPConsultaDirectorio getDirectorio() {
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
    public Collection<IMensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception {
        return persistenciaMensajes.obtenerMsjsPendientesReceptor(receptor);
    }

    /**
     * @param emisor
     * @return null si no hay mensajes enviados de ese emisor, una coleccion de los mensajes enviados de ese emisor en caso contrario
     * @throws Exception
     */
    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(Emisor emisor) throws Exception {
        return persistenciaMensajes.obtenerMsjsComprobadosEmisor(emisor);
    }


    public void guardarMsj(IMensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception {
        persistenciaMensajes.guardarMsj(mensaje, usuarioReceptor, entregado);
    }

    public void guardarComp(Comprobante comprobante) throws Exception {
        persistenciaMensajes.guardarComp(comprobante);
    }


    public void marcarMensajeEnviado(IMensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception {
        persistenciaMensajes.marcarMensajeEnviado(mensaje, usuarioReceptor, primerIntento);
    }

    public void arriboComprobante(Comprobante comprobante) {
        new Thread(new ComprobanteHandler(comprobante)).start();
    }


    public int cargarPuertoRecepcionMensajes() throws Exception {
        return persistenciaParametros.cargarPuertoRecepcionMensajes();
    }

    public int cargarPuertoComprobantes() throws Exception {
        return persistenciaParametros.cargarPuertoComprobantes();
    }

    public int cargarPuertoDevolverMensajesEmisores() throws Exception {
        return persistenciaParametros.cargarPuertoDevolverMensajesEmisores();
    }


    public void eliminarComprobantesNoEnviados(Emisor emisor) {
        try {
            persistenciaMensajes.eliminarComprobantesNoEnviados(emisor);
        } catch (Exception e) {
        }
    }

    public void envioMensajesAsincronicos(Receptor receptor) {
        try {
            this.tcpParaDirectorio.envioMensajesAsincronicos(receptor);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("problemas de persistencia");
        }
    }
}
