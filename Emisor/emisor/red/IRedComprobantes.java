package emisor.red;

import java.util.Collection;

import receptor.modelo.IComprobante;

public interface IRedComprobantes extends Runnable {
    String getIpServidorMensajeria();

    int getPuertoServidorMensajeria();

    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    void run();

    Collection<IComprobante> solicitarComprobantesAsincronicos();
}
