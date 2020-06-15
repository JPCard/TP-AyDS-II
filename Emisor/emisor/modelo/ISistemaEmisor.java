package emisor.modelo;

import emisor.red.RedComprobantes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import receptor.modelo.IComprobante;
import receptor.modelo.IDatosReceptor;

public interface ISistemaEmisor {
    IDatosEmisor getEmisor();

    RedComprobantes getTcpdeEmisor();

    boolean enviarMensaje(String asunto, String cuerpo, ArrayList<String> usuariosReceptores,
                          AbstractMensajeFactory.TipoMensaje tipoMensaje);

    void guardarMensaje(IMensaje mensajeSinEncriptar);

    Iterator<IDatosReceptor> consultarAgenda();

    Iterator<MensajeConComprobante> getMensajesConComprobanteIterator();

    void agregarComprobante(IComprobante comprobante);

    int getPuerto();

    boolean isComprobado(MensajeConComprobante mensajeSeleccionado, String usuarioReceptor);

    void setAgenda(Collection<IDatosReceptor> destinatariosRegistrados);

    void cargarComprobantesAsincronicos();

    Collection<IMensaje> getMensajesNoEnviados();

    /**
     * Marca los mensajes que estaban esperando ser enviados. Los marca como enviados.
     */
    void marcarMensajesPendientesComoEnviados(Collection<IMensaje> mensajesPendientes);

    boolean quedanMensajesPendientes();

    void actualizarIdMensaje(Integer viejaId, Integer nuevaId);

    String getNombreEmisor();
}
