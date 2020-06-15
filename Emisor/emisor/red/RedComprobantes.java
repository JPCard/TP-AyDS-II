package emisor.red;

import java.util.Collection;

import receptor.modelo.IComprobante;

public abstract class RedComprobantes implements Runnable {
    public abstract String getIpServidorMensajeria();

    public abstract int getPuertoServidorMensajeria();

    /**
     * Precondicion: El objeto que llega en XML es siempre un comprobante
     */
    @Override
    public final void run() {
        this.escucharComprobantes();

    }

    public abstract Collection<IComprobante> solicitarComprobantesAsincronicos();

    protected abstract void escucharComprobantes() ;
}
