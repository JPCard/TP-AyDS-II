package emisor.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;

import java.util.Collection;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IPersistenciaMensajesEmisor {
    public void guardarMsj(Mensaje mensaje, String usuarioReceptor, boolean entregado) throws Exception;

    public void guardarComp(Comprobante comprobante) throws Exception;

    public Collection<Mensaje> obtenerMsjsPendientesReceptor(Receptor receptor) throws Exception;

    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor(Emisor emisor) throws Exception;
    
    public void marcarMensajeEnviado(Mensaje mensaje, String usuarioReceptor, boolean primerIntento) throws Exception;

    public void avanzaProximoIdMensaje(); //una vez que le mandamos el id al emisor

    public int getProximoIdMensaje();
}
