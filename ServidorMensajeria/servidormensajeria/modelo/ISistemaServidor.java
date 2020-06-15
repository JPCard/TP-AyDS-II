package servidormensajeria.modelo;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;
import emisor.modelo.MensajeConComprobante;

import java.util.ArrayList;
import java.util.Collection;

import receptor.modelo.IComprobante;
import receptor.modelo.Receptor;

import servidormensajeria.persistencia.IPersistenciaMensajesServidor;

import servidormensajeria.red.TCPConsultaDirectorio;

public interface ISistemaServidor {
    /**
     * Pre: un id de mensaje fue entregado a un emisor
     */
    void avanzaProximoIdMensaje();

    int getProximoIdMensaje();

    Long getTiempoUltimaActualizacionReceptores();

    void setReceptores(ArrayList<Receptor> receptores);

    ArrayList<Receptor> getReceptores();

    void setTiempoUltimaActualizacionReceptores(long tiempoUltimaActualizacionReceptores);

    void arriboMensaje(IMensaje mensaje);

    IPersistenciaMensajesServidor getPersistencia();

    TCPConsultaDirectorio getDirectorio();

    /**
     * @param usuarioActual
     * @return null si el receptor no esta conectado o no hay conexion con el directorio, != null si el receptor esta conectado
     */
    Receptor getReceptor(String usuarioActual);

    /**
     * @param receptor
     * @return null si no hay mensajes para ese receptor, una coleccion de los mensajes para ese receptor en caso contrario
     * @throws Exception
     */
    Collection<IMensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception;

    /**
     * @param emisor
     * @return null si no hay mensajes enviados de ese emisor, una coleccion de los mensajes enviados de ese emisor en caso contrario
     * @throws Exception
     */
    Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(IDatosEmisor emisor) throws Exception;

    void guardarMsj(IMensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception;

    void guardarComp(IComprobante comprobante) throws Exception;

    void marcarMensajeEnviado(IMensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception;

    void arriboComprobante(IComprobante comprobante);

    int cargarPuertoRecepcionMensajes() throws Exception;

    int cargarPuertoComprobantes() throws Exception;

    int cargarPuertoDevolverMensajesEmisores() throws Exception;

    void eliminarComprobantesNoEnviados(IDatosEmisor emisor);

    void envioMensajesAsincronicos(Receptor receptor);
}
