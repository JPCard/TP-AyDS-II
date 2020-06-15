package servidormensajeria.persistencia;

import emisor.modelo.IDatosEmisor;
import emisor.modelo.IMensaje;

import emisor.modelo.MensajeConComprobante;

import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IPersistenciaMensajesServidor {

    public void guardarMsj(IMensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception;

    public void guardarComp(Comprobante comprobante) throws Exception;

    public Collection<IMensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception;

    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(IDatosEmisor emisor) throws Exception;
    
    public void marcarMensajeEnviado(IMensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception;

    public void avanzaProximoIdMensaje(); //una vez que le mandamos el id al emisor

    public int getProximoIdMensaje();
    
    public void guardarComprobanteNoEnviado(Comprobante comprobante) throws Exception;
    
    public Collection<Comprobante> getComprobantesNoEnviados(IDatosEmisor emisor);
    
    public void eliminarComprobantesNoEnviados(IDatosEmisor emisor) throws Exception;
}
    