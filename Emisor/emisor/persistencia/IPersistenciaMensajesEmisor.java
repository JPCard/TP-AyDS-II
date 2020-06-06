package emisor.persistencia;

import emisor.modelo.Emisor;
import emisor.modelo.Mensaje;
import emisor.modelo.MensajeConComprobante;

import java.util.Collection;

import java.util.Iterator;

import receptor.modelo.Comprobante;
import receptor.modelo.Receptor;

public interface IPersistenciaMensajesEmisor {
    public void guardarMensajeEncriptado(Mensaje mensaje);//si se guarda encriptado es por que no se mando
    
    public void guardarMensajeConComprobante(MensajeConComprobante mensaje);

    public void guardarComp(Comprobante comprobante) ;

    public Collection<MensajeConComprobante> obtenerMsjsComprobadosEmisor();
    
    public void marcarMensajesPendientesComoEnviados(Collection<Mensaje> mensajesPendientes);

    public boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor);

    public Collection<Mensaje> getMensajesNoEnviados();

    public boolean quedanMensajesPendientes();
    
    public int getNextIdNoEnviados();
    
    public void pasarASiguienteIdNoEnviados();

    public void actualizarIdMensaje(int viejaId, int nuevaId);
}
